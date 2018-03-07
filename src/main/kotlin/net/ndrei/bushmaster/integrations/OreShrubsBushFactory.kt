package net.ndrei.bushmaster.integrations

import lhykos.oreshrubs.api.OreShrubsAPI
import net.minecraft.block.state.IBlockState
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.ndrei.bushmaster.api.IHarvestable
import net.ndrei.bushmaster.api.IHarvestableFactory
import net.ndrei.bushmaster.couldBe

class OreShrubsBushFactory : IHarvestableFactory {
    override fun getHarvestable(world: World, pos: BlockPos, state: IBlockState) =
        when {
            OreShrubsBushWrapper.canHarvest(world, pos, state) -> OreShrubsBushWrapper
            else -> null
        }

    object OreShrubsBushWrapper : IHarvestable {
        fun canHarvest(world: World, pos: BlockPos, state: IBlockState) =
            state.block.javaClass.couldBe("lhykos.oreshrubs.common.block.BlockOreShrub")

        override fun canBeHarvested(worldIn: World, pos: BlockPos, state: IBlockState)
            = OreShrubsAPI.shrubHelper.hasBerriesToHarvest(state)

        override fun harvest(loot: MutableList<ItemStack>, worldIn: World, pos: BlockPos, state: IBlockState, simulate: Boolean) {
            loot.addAll(OreShrubsAPI.shrubHelper.harvestBerries(worldIn, pos) ?: return)
        }
    }
}
