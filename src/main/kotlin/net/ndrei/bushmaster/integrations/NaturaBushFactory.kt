package net.ndrei.bushmaster.integrations

import net.minecraft.block.state.IBlockState
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.WorldServer
import net.ndrei.bushmaster.api.IHarvestable
import net.ndrei.bushmaster.api.IHarvestableFactory
import net.ndrei.bushmaster.collect
import net.ndrei.bushmaster.couldBe
import net.ndrei.bushmaster.isIntPropertyMax
import net.ndrei.teslacorelib.BushMasterCore

class NaturaBushFactory : IHarvestableFactory {
    override fun getHarvestable(world: World, pos: BlockPos, state: IBlockState) =
        if (NaturaBushWrapper.canHarvest(state)) {
            NaturaBushWrapper()
        } else null

    // TODO: consider making this an object
    class NaturaBushWrapper: IHarvestable {
        override fun canBeHarvested(worldIn: World, pos: BlockPos, state: IBlockState) =
            NaturaBushWrapper.canHarvest(state) && state.isIntPropertyMax("age")

        override fun canFakeHarvest(worldIn: World, pos: BlockPos, state: IBlockState) = false

        override fun harvest(loot: MutableList<ItemStack>, worldIn: World, pos: BlockPos, state: IBlockState, doHarvest: Boolean) {
            if (doHarvest && (worldIn is WorldServer)) {
                val fake = BushMasterCore.getFakePlayer(worldIn)
                fake.setPosition(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())
                state.block.onBlockClicked(worldIn, pos, fake)
                pos.collect(loot, worldIn)
            }
        }

        companion object {
            fun canHarvest(state: IBlockState) =
                state.block.javaClass.couldBe("com.progwml6.natura.common.block.BlockEnumBerryBush")
        }
    }
}
