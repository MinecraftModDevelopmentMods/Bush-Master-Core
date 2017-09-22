package net.ndrei.bushmaster

import com.mojang.authlib.GameProfile
import net.minecraft.world.WorldServer
import net.minecraftforge.common.util.FakePlayerFactory
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.*
import net.ndrei.bushmaster.api.BushMasterApi
import net.ndrei.bushmaster.cmd.HarvestableCommand
import org.apache.logging.log4j.Logger
import java.util.*

/**
 * Created by CF on 2017-06-28.
 */
@Mod(modid = MOD_ID, version = MOD_VERSION, name = MOD_NAME,
        acceptedMinecraftVersions = MOD_MC_VERSION,
        dependencies = MOD_DEPENDENCIES,
        useMetadata = true, modLanguage = "kotlin", modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter")
object BushMasterCore {
    @SidedProxy(clientSide = "net.ndrei.bushmaster.ClientProxy", serverSide = "net.ndrei.bushmaster.ServerProxy")
    lateinit var proxy: CommonProxy
    lateinit var logger: Logger

    @Suppress("unused")
    val isClientSide
        get() = net.minecraftforge.fml.common.FMLCommonHandler.instance().effectiveSide?.isClient ?: false

    private val gameProfile by lazy { GameProfile(UUID.fromString("5198ed9d-5108-46c6-a684-50bb29e011e6"), "_BushMaster_") }
    fun getFakePlayer(world: WorldServer) = FakePlayerFactory.get(world, gameProfile).also { it.inventory.clear() }

    @Mod.EventHandler
    fun construct(event: FMLConstructionEvent) {
        BushMasterApi.harvestableFactory = HarvestableFactory
    }

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        logger = event.modLog
        proxy.preInit(event)
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        proxy.init(event)
    }

    @Mod.EventHandler
    fun postInit(event: FMLPostInitializationEvent) {
        proxy.postInit(event)
    }

    @Mod.EventHandler
    fun onServerStarting(event: FMLServerStartingEvent) {
        event.registerServerCommand(HarvestableCommand)
    }
}
