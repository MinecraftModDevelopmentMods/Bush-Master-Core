package net.ndrei.bushmaster

import net.minecraft.block.properties.IProperty
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.properties.PropertyInteger
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.ItemStack
import net.minecraft.util.IStringSerializable
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.util.FakePlayer

fun Class<*>.couldBe(thing: String): Boolean =
    if (this.name == thing) { true }
    else this.superclass?.couldBe(thing) ?: false

fun IBlockState.isIntPropertyMax(propertyName: String): Boolean {
    val property = this.propertyKeys
        .filterIsInstance<PropertyInteger>()
        .firstOrNull { it.name == propertyName } ?: return false

    return (this.getValue(property) == property.allowedValues.max())
}

fun IBlockState.testIntProperty(propertyName: String, value: Int): Boolean {
    val property = this.propertyKeys
        .filterIsInstance<PropertyInteger>()
        .firstOrNull { it.name == propertyName } ?: return false

    return (this.getValue(property) == value)
}

fun IBlockState.testBoolProperty(propertyName: String): Boolean {
    val property = this.propertyKeys
        .filterIsInstance<PropertyBool>()
        .firstOrNull { it.name == propertyName } ?: return false

    return this.getValue(property)
}

fun<TE> IBlockState.getEnumProperty(propertyName: String): TE?
    where TE: Enum<TE>, TE: IStringSerializable {
    val property = this.propertyKeys
        .filterIsInstance<PropertyEnum<TE>>()
        .firstOrNull { it.name == propertyName } ?: return null

    return this.getValue(property)
}

fun IBlockState.getPropertyAsString(propertyName: String): String? {
    val property = this.propertyKeys
        .filterIsInstance<IProperty<*>>()
        .firstOrNull { it.getName() == propertyName } ?: return null

    return this.properties[property]?.toString()
}

fun IBlockState.testPropertyAsString(propertyName: String, value: String) =
    this.getPropertyAsString(propertyName) == value

fun BlockPos.collect(loot: MutableList<ItemStack>, world: World, radius: Int = 1) {
    val aabb = AxisAlignedBB(this.west(radius).north(radius).down(radius), this.east(radius).south(radius).up(radius))

    world.getEntitiesWithinAABB(EntityItem::class.java, aabb)
        .filter { !it.isDead }
        .mapTo(loot) {
            world.removeEntity(it)
            it.item
        }
}

fun FakePlayer.loot(loot: MutableList<ItemStack>) {
    for(index in 0..this.inventory.sizeInventory) {
        val stack = this.inventory.getStackInSlot(index)
        if (!stack.isEmpty) {
            loot.add(stack)
        }
    }
    this.inventory.clear()
}
