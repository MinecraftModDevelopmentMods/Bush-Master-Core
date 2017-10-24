package net.ndrei.bushmaster

import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.common.Loader
import net.ndrei.bushmaster.api.IHarvestable
import net.ndrei.bushmaster.api.IHarvestableFactory
import net.ndrei.bushmaster.integrations.BopBushFactory
import net.ndrei.bushmaster.integrations.NaturaBushFactory
import net.ndrei.bushmaster.integrations.PlantsBushFactory
import net.ndrei.bushmaster.integrations.OreberriesBushFactory
import net.ndrei.bushmaster.integrations.RusticBushFactory

object HarvestableFactory : IHarvestableFactory {
    private val factories: MutableMap<String, IHarvestableFactory> = mutableMapOf()

    private fun MutableMap<String, IHarvestableFactory>.addModFactory(modId: String, builder: () -> IHarvestableFactory) {
        if (Loader.isModLoaded(modId)) {
            this[modId] = builder()
        }
    }

    fun preInit() {
        this.factories.addModFactory("natura") { NaturaBushFactory() }
        this.factories.addModFactory("plants2") { PlantsBushFactory() }
        this.factories.addModFactory("rustic") { RusticBushFactory() }
        this.factories.addModFactory("biomesoplenty") { BopBushFactory() }
        this.factories.addModFactory("oreberries") { OreberriesBushFactory() }
    }

    override fun getHarvestable(world: World, pos: BlockPos, state: IBlockState): IHarvestable? {
        if (state.block == Blocks.AIR) {
            return null
        }

        if (state.block is IHarvestable) {
            return state.block as IHarvestable
        }

        val modIdFilter = state.block?.registryName?.resourceDomain
        for ((key, factory) in this.factories) {
            if (modIdFilter.isNullOrBlank() || (key == modIdFilter)) {
                val harvestable = factory.getHarvestable(world, pos, state)
                if (harvestable != null)
                    return harvestable
            }
        }
        return null
    }
}