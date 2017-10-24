package net.ndrei.bushmaster.api

import net.minecraft.block.state.IBlockState
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

/**
 * Interface for easier handling of bushes and other plant like things that can be harvested without being destroyed.
 */
interface IHarvestable {
    /**
     * Specifies if the target block is a harvestable block.
     */
    fun canBeHarvested(worldIn: World, pos: BlockPos, state: IBlockState): Boolean

    /**
     * Specifies if the target block can provide the list of dropped items without actually dropping them.
     * This will usually be 'true' if implemented in a mod, and 'false' if it comes from a wrapper.
     */
    fun canSimulateHarvest(worldIn: World, pos: BlockPos, state: IBlockState) = false

    /**
     * Harvests the block into [loot]. If [canSimulateHarvest] is 'false' then it should do nothing if [simulate] is true.
     */
    fun harvest(loot: MutableList<ItemStack>, worldIn: World, pos: BlockPos, state: IBlockState, simulate: Boolean)

    /**
     * Harvests the block. If [canSimulateHarvest] is 'false' then it should do nothing if [simulate] is true.
     */
    fun harvest(worldIn: World, pos: BlockPos, state: IBlockState, simulate: Boolean) =
        mutableListOf<ItemStack>().also { this.harvest(it, worldIn, pos, state, simulate) }.toList()
}
