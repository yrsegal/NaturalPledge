package com.wiresegal.naturalpledge.common.block.tile

import com.teamwizardry.librarianlib.features.kotlin.forCap
import com.teamwizardry.librarianlib.features.saving.Save
import com.teamwizardry.librarianlib.features.saving.SaveMethodGetter
import com.teamwizardry.librarianlib.features.saving.SaveMethodSetter
import net.minecraft.entity.item.EntityItem
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.AxisAlignedBB
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.ItemHandlerHelper
import net.minecraftforge.items.ItemStackHandler
import com.wiresegal.naturalpledge.common.block.BlockFunnel

/**
 * @author L0neKitsune
 * Created on 3/20/16.
 */
class TileLivingwoodFunnel : TileModTickable() {

    val inventory = object : ItemStackHandler(1) {
        override fun getSlotLimit(slot: Int): Int {
            return 1
        }

        override fun onContentsChanged(slot: Int) {
            markDirty()
        }
    }

    @Save
    var transferCooldown = 0

    internal var inventoryCompound: NBTTagCompound
        @SaveMethodGetter("inventory") get() = inventory.serializeNBT()
        @SaveMethodSetter("inventory") set(value) = inventory.deserializeNBT(value)

    override fun updateEntity() {
        if (!world.isRemote) {

            val items = world.getEntitiesWithinAABB(EntityItem::class.java, AxisAlignedBB(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), pos.x + 1.0, pos.y + 1.5, pos.z + 1.0))
            for (item in items) {
                val stack = item.item
                val result = inventory.insertItem(0, stack, false)
                if (result.isEmpty) item.setDead()
            }

            if (transferCooldown == 0) {
                val state = world.getBlockState(pos)
                if (state.block is BlockFunnel && state.getValue(BlockFunnel.ENABLED)) {
                    val facing = state.getValue(BlockFunnel.FACING)
                    world.getTileEntity(pos.offset(facing))?.forCap(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.opposite) {
                        val inInv = inventory.extractItem(0, 64, false)
                        val remainder = ItemHandlerHelper.insertItem(it, inInv, false)
                        inventory.insertItem(0, remainder, false)
                        if (remainder.isEmpty) transferCooldown = 8
                    }
                    if (inventory.getStackInSlot(0).isEmpty) {
                        world.getTileEntity(pos.up())?.forCap(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN) {
                            for (i in 0 until it.slots) {
                                val stack = it.getStackInSlot(i)
                                if (!stack.isEmpty) {
                                    val extracted = it.extractItem(i, 1, false)
                                    inventory.insertItem(0, extracted, false)
                                    transferCooldown = 8
                                    break
                                }
                            }
                        }
                    }
                }
            } else transferCooldown--
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T: Any> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return inventory as T
        return super.getCapability(capability, facing)
    }

    override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean {
        return capability === CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing)
    }
}
