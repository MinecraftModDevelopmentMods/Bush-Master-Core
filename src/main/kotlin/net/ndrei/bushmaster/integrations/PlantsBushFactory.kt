package net.ndrei.bushmaster.integrations

import net.minecraft.block.state.IBlockState
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.WorldServer
import net.ndrei.bushmaster.api.IHarvestable
import net.ndrei.bushmaster.api.IHarvestableFactory
import net.ndrei.bushmaster.couldBe
import net.ndrei.bushmaster.loot
import net.ndrei.bushmaster.testBoolProperty
import net.ndrei.teslacorelib.BushMasterCore

class PlantsBushFactory : IHarvestableFactory {
    override fun getHarvestable(world: World, pos: BlockPos, state: IBlockState) =
        if (PlantsBushWrapper.canHarvest(world, pos, state)) {
            PlantsBushWrapper()
        } else null

    // TODO: consider making this an object
    class PlantsBushWrapper: IHarvestable {
        override fun canBeHarvested(worldIn: World, pos: BlockPos, state: IBlockState) =
            PlantsBushWrapper.canHarvest(worldIn, pos, state) && state.testBoolProperty("fruit")

        override fun canFakeHarvest(worldIn: World, pos: BlockPos, state: IBlockState) = false

        override fun harvest(loot: MutableList<ItemStack>, worldIn: World, pos: BlockPos, state: IBlockState, doHarvest: Boolean) {
            val block = state.block
            if (doHarvest && (worldIn is WorldServer)) {
                val fake = BushMasterCore.getFakePlayer(worldIn)
                fake.inventory.clear()
                block.onBlockActivated(worldIn, pos, state, fake, EnumHand.MAIN_HAND, EnumFacing.UP, .5f, .5f, .5f)
                fake.loot(loot)
                // pos.harvest(loot, worldIn, 1) // @shadows said it's fine :)
            }
//            else if (!doHarvest && (block is IShearable)) {
//                val shears = ItemStack(Items.SHEARS)
//                if (block.isShearable(shears, worldIn, pos)) {
//                    loot.addAll(block.onSheared(shears, worldIn, pos, 1))
//                }
//                else {
//                    loot.addAll(NonNullList.create<ItemStack>().also {
//                        block.getDrops(it, worldIn, pos, state, 1)
//                    })
//                }
//            }
        }

        companion object {
            fun canHarvest(worldIn: World, pos: BlockPos, state: IBlockState) =
                state.block.javaClass.couldBe("shadows.plants2.block.forgotten.BlockBushLeaves")
//                state.block.javaClass.couldBe("shadows.plants2.block.BlockEnumHarvestBush")
        }
    }
}
