package net.ndrei.bushmaster.integrations

import net.minecraft.block.state.IBlockState
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.WorldServer
import net.ndrei.bushmaster.BushMasterCore
import net.ndrei.bushmaster.api.IHarvestable
import net.ndrei.bushmaster.api.IHarvestableFactory
import net.ndrei.bushmaster.collect
import net.ndrei.bushmaster.couldBe
import net.ndrei.bushmaster.isIntPropertyMax

class ExtraUtils2BushFactory : IHarvestableFactory {
    override fun getHarvestable(world: World, pos: BlockPos, state: IBlockState) =
        when {
            EnderLillyWrapper.canHarvest(state) -> EnderLillyWrapper
            EnderRedOrchid.canHarvest(state) -> EnderRedOrchid
            else -> null
        }

    object EnderLillyWrapper : IHarvestable {
        fun canHarvest(state: IBlockState) =
            state.block.javaClass.couldBe("com.rwtema.extrautils2.blocks.BlockEnderLilly")

        override fun canBeHarvested(worldIn: World, pos: BlockPos, state: IBlockState) =
            this.canHarvest(state) && state.isIntPropertyMax("growth")

        override fun harvest(loot: MutableList<ItemStack>, worldIn: World, pos: BlockPos, state: IBlockState, simulate: Boolean) {
            if (!simulate && (worldIn is WorldServer)) {
                val fake = BushMasterCore.getFakePlayer(worldIn)
                fake.setPosition(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())
                state.block.onBlockActivated(worldIn, pos, state, fake, EnumHand.MAIN_HAND, EnumFacing.UP, .5f, .5f, .5f)
                pos.collect(loot, worldIn)
            }
        }
    }

    object EnderRedOrchid : IHarvestable {
        fun canHarvest(state: IBlockState) =
            state.block.javaClass.couldBe("com.rwtema.extrautils2.blocks.BlockRedOrchid")

        override fun canBeHarvested(worldIn: World, pos: BlockPos, state: IBlockState) =
            this.canHarvest(state) && state.isIntPropertyMax("growth")

        override fun harvest(loot: MutableList<ItemStack>, worldIn: World, pos: BlockPos, state: IBlockState, simulate: Boolean) {
            if (!simulate && (worldIn is WorldServer)) {
                val fake = BushMasterCore.getFakePlayer(worldIn)
                fake.setPosition(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())
                state.block.onBlockActivated(worldIn, pos, state, fake, EnumHand.MAIN_HAND, EnumFacing.UP, .5f, .5f, .5f)
                pos.collect(loot, worldIn)
            }
        }
    }
}
