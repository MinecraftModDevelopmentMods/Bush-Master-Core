package net.ndrei.bushmaster.api

import net.minecraft.block.state.IBlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

interface IHarvestableFactory {
    fun getHarvestable(world: World, pos: BlockPos): IHarvestable? =
        getHarvestable(world, pos, world.getBlockState(pos))

    fun getHarvestable(world: World, pos: BlockPos, state: IBlockState): IHarvestable?
}