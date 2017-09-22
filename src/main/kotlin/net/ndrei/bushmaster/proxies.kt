package net.ndrei.bushmaster

import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.crafting.IRecipe
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

abstract class CommonProxy(val side: Side) {
    open fun preInit(ev: FMLPreInitializationEvent) {
        MinecraftForge.EVENT_BUS.register(this)
        HarvestableFactory.preInit()
    }

    open fun init(ev: FMLInitializationEvent) {
    }

    open fun postInit(ev: FMLPostInitializationEvent) {
    }

    @SubscribeEvent
    fun registerBlocks(ev: RegistryEvent.Register<Block>) {
    }

    @SubscribeEvent
    fun registerItems(ev: RegistryEvent.Register<Item>) {
    }

    @SubscribeEvent
    fun registerRecipes(ev: RegistryEvent.Register<IRecipe>) {
    }
}

@Suppress("unused")
@SideOnly(Side.CLIENT)
class ClientProxy: CommonProxy(Side.CLIENT) {
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    fun registerModel(ev: ModelRegistryEvent) {
    }
}

@Suppress("unused")
@SideOnly(Side.SERVER)
class ServerProxy: CommonProxy(Side.SERVER) {
    override fun preInit(ev: FMLPreInitializationEvent) {
        super.preInit(ev)
    }
}
