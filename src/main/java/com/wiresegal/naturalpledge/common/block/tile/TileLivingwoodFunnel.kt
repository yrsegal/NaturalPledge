package com.wiresegal.naturalpledge.common.block.tile

import com.teamwizardry.librarianlib.features.autoregister.TileRegister
import com.teamwizardry.librarianlib.features.base.block.tile.module.ModuleInventory
import com.teamwizardry.librarianlib.features.kotlin.forCap
import com.teamwizardry.librarianlib.features.saving.Module
import com.teamwizardry.librarianlib.features.saving.Save
import com.wiresegal.naturalpledge.common.block.BlockFunnel
import com.wiresegal.naturalpledge.common.lib.LibNames
import net.minecraft.entity.item.EntityItem
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.AxisAlignedBB
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.ItemHandlerHelper
import net.minecraftforge.items.ItemStackHandler

/**
 * @author L0neKitsune
 * Created on 3/20/16.
 */
@TileRegister(LibNames.FUNNEL)
class TileLivingwoodFunnel : TileModTickable() {

    @Module
    val inventory = ModuleInventory(object : ItemStackHandler(1) {
        override fun getSlotLimit(slot: Int): Int {
            return 1
        }

        override fun onContentsChanged(slot: Int) {
            markDirty()
        }
    })

    @Save
    var transferCooldown = 0

    override fun updateEntity() {
        if (!world.isRemote) {

            val items = world.getEntitiesWithinAABB(EntityItem::class.java, AxisAlignedBB(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), pos.x + 1.0, pos.y + 1.5, pos.z + 1.0))
            for (item in items) {
                val stack = item.item
                val result = inventory.handler.insertItem(0, stack, false)
                if (result.isEmpty) item.setDead()
            }

            if (transferCooldown == 0) {
                val state = world.getBlockState(pos)
                if (state.block is BlockFunnel && state.getValue(BlockFunnel.ENABLED)) {
                    val facing = state.getValue(BlockFunnel.FACING)
                    world.getTileEntity(pos.offset(facing))?.forCap(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.opposite) {
                        val inInv = inventory.handler.extractItem(0, 64, false)
                        val remainder = ItemHandlerHelper.insertItem(it, inInv, false)
                        inventory.handler.insertItem(0, remainder, false)
                        if (remainder.isEmpty) transferCooldown = 8
                    }
                    if (inventory.handler.getStackInSlot(0).isEmpty) {
                        world.getTileEntity(pos.up())?.forCap(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN) {
                            for (i in 0 until it.slots) {
                                val stack = it.getStackInSlot(i)
                                if (!stack.isEmpty) {
                                    val extracted = it.extractItem(i, 1, false)
                                    inventory.handler.insertItem(0, extracted, false)
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
}
