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
import net.ndrei.bushmaster.testPropertyAsString

class BopBushFactory : IHarvestableFactory {
    override fun getHarvestable(world: World, pos: BlockPos, state: IBlockState) =
        when {
            BopBushWrapper.isBerryBush(state) -> BopBushWrapper
            else -> null
        }

    object BopBushWrapper: IHarvestable {
        fun isBerryBush(state: IBlockState) =
            (state.block.registryName?.toString() == "biomesoplenty:plant_0") &&
                (state.testPropertyAsString("variant", "berrybush") ||
                state.testPropertyAsString("variant", "bush"))

        override fun canBeHarvested(worldIn: World, pos: BlockPos, state: IBlockState) =
            this.isBerryBush(state) && state.testPropertyAsString("variant", "berrybush")

        override fun canFakeHarvest(worldIn: World, pos: BlockPos, state: IBlockState) = false

        override fun harvest(loot: MutableList<ItemStack>, worldIn: World, pos: BlockPos, state: IBlockState, doHarvest: Boolean) {
            if (doHarvest && (worldIn is WorldServer)) {
                val fake = BushMasterCore.getFakePlayer(worldIn)
                fake.setPosition(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())
                state.block.onBlockActivated(worldIn, pos, state, fake, EnumHand.MAIN_HAND, EnumFacing.UP, .5f, .5f, .5f)
                pos.collect(loot, worldIn)
            }
        }
    }
}