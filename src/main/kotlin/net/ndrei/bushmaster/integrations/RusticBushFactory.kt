package net.ndrei.bushmaster.integrations

import net.minecraft.block.state.IBlockState
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.WorldServer
import net.ndrei.bushmaster.*
import net.ndrei.bushmaster.api.IHarvestable
import net.ndrei.bushmaster.api.IHarvestableFactory
import net.ndrei.teslacorelib.BushMasterCore

class RusticBushFactory : IHarvestableFactory {
    override fun getHarvestable(world: World, pos: BlockPos, state: IBlockState): IHarvestable? =
        when {
            RusticBushWrapper.isBush(state) -> RusticBushWrapper
            RusticStakeCropWrapper.isStakeCrop(world, pos, state) -> RusticStakeCropWrapper
            RusticGrapesCropWrapper.isGrapeCrop(state) -> RusticGrapesCropWrapper
            else -> null
        }

    object RusticBushWrapper : IHarvestable {
        fun isBush(state: IBlockState) =
            state.block.javaClass.couldBe("rustic.common.blocks.crops.BlockBerryBush")

        override fun canBeHarvested(worldIn: World, pos: BlockPos, state: IBlockState) =
            RusticBushWrapper.isBush(state) && state.testBoolProperty("berries")

        override fun canFakeHarvest(worldIn: World, pos: BlockPos, state: IBlockState) = false

        override fun harvest(loot: MutableList<ItemStack>, worldIn: World, pos: BlockPos, state: IBlockState, doHarvest: Boolean) {
            val block = state.block
            if (doHarvest && (worldIn is WorldServer)) {
                val fake = BushMasterCore.getFakePlayer(worldIn)
                block.onBlockActivated(worldIn, pos, state, fake, EnumHand.MAIN_HAND, EnumFacing.UP, .5f, .5f, .5f)
                pos.up().collect(loot, worldIn)
            }
        }
    }

    object RusticStakeCropWrapper : IHarvestable {
        fun isStakeCrop(world: World, pos: BlockPos, state: IBlockState) =
            state.block.javaClass.couldBe("rustic.common.blocks.crops.BlockStakeCrop")
                && ((pos.y == 0) || (world.getBlockState(pos.down()).block != state.block))

        override fun canBeHarvested(worldIn: World, pos: BlockPos, state: IBlockState) =
            RusticStakeCropWrapper.isStakeCrop(worldIn, pos, state) &&
                (state.isIntPropertyMax("age") || (1..2).any {
                    val p = pos.up(it)
                    if (p.y < worldIn.height) {
                        val s = worldIn.getBlockState(p)
                        if ((s.block == state.block) && s.isIntPropertyMax("age"))
                            return true
                    }
                    return false
                })

        override fun canFakeHarvest(worldIn: World, pos: BlockPos, state: IBlockState) = false

        override fun harvest(loot: MutableList<ItemStack>, worldIn: World, pos: BlockPos, state: IBlockState, doHarvest: Boolean) {
            val block = state.block
            if (doHarvest && (worldIn is WorldServer)) {
                val fake = BushMasterCore.getFakePlayer(worldIn)
                var currentPos = pos
                var currentState = state
                while ((currentPos.y < worldIn.height) && (currentState.block === state.block)) {
                    if (currentState.isIntPropertyMax("age")) {
                        block.onBlockActivated(worldIn, currentPos, currentState, fake, EnumHand.MAIN_HAND, EnumFacing.UP, .5f, .5f, .5f)
                        currentPos.up().collect(loot, worldIn)
                    }
                    currentPos = currentPos.up()
                    currentState = worldIn.getBlockState(currentPos)
                }
            }
        }
    }

    object RusticGrapesCropWrapper : IHarvestable {
        fun isGrapeCrop(state: IBlockState) =
            state.block.javaClass.couldBe("rustic.common.blocks.crops.BlockGrapeStem")

        private fun process(world: World, pos: BlockPos, processor: (IBlockState, BlockPos) -> Boolean) {
            val upper = world.getBlockState(pos.up())
            if (upper.block.javaClass.couldBe("rustic.common.blocks.crops.BlockGrapeLeaves")) {
                val axis = upper.getEnumProperty<EnumFacing.Axis>("axis")
                if (axis != null) {
                    val leftPos = pos.up().offset(EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.NEGATIVE, axis))
                    val leftState = world.getBlockState(leftPos)
                    if ((leftState.block == upper.block) && leftState.testBoolProperty("grapes")) {
                        if (processor(leftState, leftPos)) { return }
                    }
                    val rightPos = pos.up().offset(EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE, axis))
                    val rightState = world.getBlockState(rightPos)
                    if ((rightState.block == upper.block) && rightState.testBoolProperty("grapes")) {
                        processor(rightState, rightPos)
                    }
                }
            }
        }

        override fun canBeHarvested(worldIn: World, pos: BlockPos, state: IBlockState): Boolean {
            if (!this.isGrapeCrop(state)) {
                return false
            }

            var result = false
            this.process(worldIn, pos, { _, _ -> result = true; true })
            return result
        }

        override fun canFakeHarvest(worldIn: World, pos: BlockPos, state: IBlockState) = false

        override fun harvest(loot: MutableList<ItemStack>, worldIn: World, pos: BlockPos, state: IBlockState, doHarvest: Boolean) {
            if (doHarvest && (worldIn is WorldServer) && this.isGrapeCrop(state)) {
                val fake = BushMasterCore.getFakePlayer(worldIn)
                this.process(worldIn, pos, { s, p ->
                    s.block.onBlockActivated(worldIn, p, s, fake, EnumHand.MAIN_HAND, EnumFacing.UP, .5f, .5f, .5f)
                    p.up().collect(loot, worldIn)
                    false
                })
            }
        }
    }
}
