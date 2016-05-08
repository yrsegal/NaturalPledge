package shadowfox.botanicaladdons.common.block.tile

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.IItemHandlerModifiable
import net.minecraftforge.items.ItemStackHandler

/**
 * NaturalPledge
 * created on 5/7/16
 */
abstract class TileSimpleInventory() : TileMod() {
    val itemHandler : SimpleItemStackHandler by lazy {
        createItemHandler()
    }

    abstract val sizeInventory: Int

    override fun readCustomNBT(cmp: NBTTagCompound) {
        this.itemHandler.deserializeNBT(cmp)
    }

    override fun writeCustomNBT(cmp: NBTTagCompound) {
        cmp.merge(this.itemHandler.serializeNBT())
    }

    protected open fun createItemHandler(): TileSimpleInventory.SimpleItemStackHandler {
        return TileSimpleInventory.SimpleItemStackHandler(this, true)
    }

    fun getItemHandler(): IItemHandlerModifiable {
        return this.itemHandler
    }

    override fun hasCapability(cap: Capability<*>, side: EnumFacing): Boolean {
        return cap === CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(cap, side)
    }

    override fun <T> getCapability(cap: Capability<T>, side: EnumFacing): T {
        return if (cap === CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) this.itemHandler as T else super.getCapability(cap, side)
    }

    open class SimpleItemStackHandler(private val tile: TileSimpleInventory, private val allowWrite: Boolean) : ItemStackHandler(tile.sizeInventory) {

        override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
            return if (this.allowWrite) super.insertItem(slot, stack, simulate) else stack
        }

        override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack? {
            return if (this.allowWrite) super.extractItem(slot, amount, simulate) else null
        }

        public override fun onContentsChanged(slot: Int) {
            this.tile.markDirty()
        }
    }
}
