package shadowfox.botanicaladdons.common.block.tile

import com.teamwizardry.librarianlib.common.util.nonnullListOf
import net.minecraft.block.BlockChest
import net.minecraft.block.BlockHopper
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.item.EntityItemFrame
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.ISidedInventory
import net.minecraft.inventory.ItemStackHelper
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.tileentity.IHopper
import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntityChest
import net.minecraft.tileentity.TileEntityHopper
import net.minecraft.tileentity.TileEntityHopper.putStackInInventoryAllSlots
import net.minecraft.util.EntitySelectors
import net.minecraft.util.EnumFacing
import net.minecraft.util.NonNullList
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.world.World
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.items.*
import net.minecraftforge.items.VanillaInventoryCodeHooks.getItemHandler
import net.minecraftforge.items.wrapper.InvWrapper
import org.apache.commons.lang3.tuple.Pair
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.common.block.BlockFunnel

/**
 * @author L0neKitsune
 * Created on 3/20/16.
 */
class TileLivingwoodFunnel : TileModTickable(), IHopper {

    override fun getDisplayName(): ITextComponent {
        return if (this.hasCustomName()) TextComponentString(this.name) else TextComponentTranslation(this.name)
    }

    override fun removeStackFromSlot(index: Int): ItemStack? {
        return ItemStackHelper.getAndRemove(this.inventory, index)
    }

    override fun getField(id: Int): Int {
        return 0
    }

    override fun setField(id: Int, value: Int) {
        //NO-OP
    }

    override fun getFieldCount(): Int {
        return 0
    }

    override fun clear() {
        for (i in this.inventory.indices) {
            this.inventory[i] = null
        }
    }

    override fun getName(): String {
        return "${LibMisc.MOD_ID}:container.funnel"
    }

    private var inventory: NonNullList<ItemStack> = NonNullList.withSize(1, ItemStack.EMPTY)
    private var transferCooldown = -1

    override fun getSizeInventory(): Int = 1
    override fun getStackInSlot(par1: Int): ItemStack = inventory[par1]

    override fun updateEntity() {
        if (world != null && !world.isRemote) {
            --transferCooldown

            if (transferCooldown <= 0) {
                transferCooldown = 0
                if (world != null && !world.isRemote) {
                    if (transferCooldown <= 0 && BlockFunnel.getActiveStateFromMetadata(blockMetadata)) {
                        var flag = false

                        if (!isEmpty()) {
                            flag = pushToAttachedInventory()
                        }

                        if (!isFull()) {
                            flag = canHopperPull(this) || flag
                        }

                        if (flag) {
                            transferCooldown = 8
                            markDirty()
                        }
                    }
                }
            }
        }
    }


    fun getInventoryAt(world: World, x: Double, y: Double, z: Double): IInventory? {
        var iinventory: Any? = null
        val i = MathHelper.floor(x)
        val j = MathHelper.floor(y)
        val k = MathHelper.floor(z)
        val blockpos = BlockPos(i, j, k)
        val block = world.getBlockState(blockpos).block
        if (block.hasTileEntity(world.getBlockState(blockpos))) {
            val list = world.getTileEntity(blockpos)
            if (list is IInventory) {
                iinventory = list
                if (iinventory is TileEntityChest && block is BlockChest) {
                    iinventory = (block).getLockableContainer(world, blockpos)
                }
            }
        }

        if (iinventory == null) {
            val list1 = world.getEntitiesInAABBexcluding(null as Entity?, AxisAlignedBB(x - 0.5, y - 0.5, z - 0.5, x + 0.5, y + 0.5, z + 0.5), EntitySelectors.HAS_INVENTORY)
            if (!list1.isEmpty()) {
                iinventory = list1[world.rand.nextInt(list1.size)] as IInventory
            }
        }

        return iinventory as IInventory?
    }

    fun canHopperPull(funnel: IHopper): Boolean {
        val iinventory = getInventoryAbove(funnel)
        if (iinventory != null) {
            if (inventoryEmpty(iinventory, EnumFacing.DOWN)) return false

            if (iinventory is ISidedInventory) {
                for (slot in iinventory.getSlotsForFace(EnumFacing.DOWN)) {
                    if (pullItemIn(funnel, iinventory, slot, EnumFacing.DOWN)) return true
                }
            } else {
                for (slot in 0..iinventory.sizeInventory - 1) {
                    if (pullItemIn(funnel, iinventory, slot, EnumFacing.DOWN)) return true
                }
            }
        } else {
            val entityitem = entitiesOnFunnel(funnel.world, funnel.xPos, funnel.yPos + 1.0, funnel.zPos)

            if (entityitem != null) {
                return pullEntityFromWorld(funnel, entityitem)
            }
        }

        return false
    }


    fun getInventoryAbove(hopper: IHopper): IInventory? {
        return getInventoryAt(hopper.world, hopper.xPos, hopper.yPos + 1.0, hopper.zPos)
    }


    private fun isFull(): Boolean {
        val aitemstack = inventory
        val i = aitemstack.size

        for (j in 0..i - 1) {
            val itemstack = aitemstack[j]

            if (itemstack.isEmpty || itemstack.count != itemstack.maxStackSize) {
                return false
            }
        }

        return true
    }

    override fun isEmpty(): Boolean {
        val aitemstack = inventory
        val i = aitemstack.size

        for (j in 0..i - 1) {
            val itemstack = aitemstack[j]

            if (!itemstack.isEmpty) {
                return false
            }
        }

        return true
    }

    private fun isFull(inventory: IInventory, side: EnumFacing): Boolean {
        if (inventory is ISidedInventory) {
            val aint = inventory.getSlotsForFace(side)

            for (l in aint.indices) {
                val itemstack1 = inventory.getStackInSlot(aint[l])

                if (itemstack1 == null || itemstack1.count != itemstack1.maxStackSize) {
                    return false
                }
            }
        } else {
            val j = inventory.sizeInventory

            for (k in 0..j - 1) {
                val itemstack = inventory.getStackInSlot(k)

                if (itemstack == null || itemstack.count != itemstack.maxStackSize) {
                    return false
                }
            }
        }

        return true
    }

    private fun getItemHandler(hopper: IHopper, hopperFacing: EnumFacing): Pair<IItemHandler, Any>? {
        val x = hopper.xPos + hopperFacing.frontOffsetX.toDouble()
        val y = hopper.yPos + hopperFacing.frontOffsetY.toDouble()
        val z = hopper.zPos + hopperFacing.frontOffsetZ.toDouble()
        return getItemHandler(hopper.world, x, y, z, hopperFacing.opposite)
    }

    private fun isFull(itemHandler: IItemHandler): Boolean {
        for (slot in 0..itemHandler.slots - 1) {
            val stackInSlot = itemHandler.getStackInSlot(slot)
            if (stackInSlot.isEmpty || stackInSlot.count != stackInSlot.maxStackSize) {
                return false
            }
        }
        return true
    }

    private fun isEmpty(itemHandler: IItemHandler): Boolean {
        for (slot in 0..itemHandler.slots - 1) {
            val stackInSlot = itemHandler.getStackInSlot(slot)
            if (stackInSlot.count > 0) {
                return false
            }
        }
        return true
    }

    private fun insertStack(source: TileEntity, destination: Any, destInventory: IItemHandler, stack: ItemStack, slot: Int): ItemStack {
        var stack = stack
        val itemstack = destInventory.getStackInSlot(slot)

        if (destInventory.insertItem(slot, stack, true).isEmpty) {
            var insertedItem = false
            val inventoryWasEmpty = isEmpty(destInventory)

            if (itemstack.isEmpty) {
                destInventory.insertItem(slot, stack, false)
                stack = ItemStack.EMPTY
                insertedItem = true
            } else if (ItemHandlerHelper.canItemStacksStack(itemstack, stack)) {
                val originalSize = stack.count
                stack = destInventory.insertItem(slot, stack, false)
                insertedItem = originalSize < stack.count
            }

            if (insertedItem) {
                if (inventoryWasEmpty && destination is TileEntityHopper) {
                    val destinationHopper = destination

                    if (!destinationHopper.mayTransfer()) {
                        var k = 0

                        if (source is TileLivingwoodFunnel) {
                            k = 1
                        }

                        destinationHopper.setTransferCooldown(8 - k)
                    }
                }
            }
        }

        return stack
    }

    private fun putStackInInventoryAllSlots(source: TileEntity, destination: Any, destInventory: IItemHandler, stack: ItemStack): ItemStack {
        var stack = stack
        var slot = 0
        while (slot < destInventory.slots && !stack.isEmpty) {
            stack = insertStack(source, destination, destInventory, stack, slot)
            slot++
        }
        return stack
    }

    fun insertHook(hopper: IHopper, facing: EnumFacing): Boolean {
        val destinationResult = getItemHandler(hopper, facing)
        if (destinationResult == null) {
            return false
        } else {
            val itemHandler = destinationResult.key
            val destination = destinationResult.value as IInventory
            if (isFull(itemHandler)) {
                return false
            } else {
                for (i in 0..hopper.sizeInventory - 1) {
                    if (!hopper.getStackInSlot(i).isEmpty) {
                        val originalSlotContents = hopper.getStackInSlot(i).copy()
                        val insertStack = hopper.decrStackSize(i, 1)
                        val remainder = putStackInInventoryAllSlots(this, destination, itemHandler, insertStack)

                        if (remainder.isEmpty) {
                            return true
                        }

                        hopper.setInventorySlotContents(i, originalSlotContents)
                    }
                }

                return false
            }
        }
    }

    private fun pushToAttachedInventory(): Boolean {
        if (insertHook(this, BlockFunnel.getFacing(this.blockMetadata))) {
            return true
        } else {
            val iinventory = this.getFacingInventory()
            if (iinventory == null) {
                return false
            } else {
                val enumfacing = BlockFunnel.getFacing(this.blockMetadata).opposite
                if (isFull(iinventory, enumfacing)) {
                    return false
                } else {
                    for (i in 0..this.sizeInventory - 1) {
                        if (this.getStackInSlot(i) != null) {
                            val itemstack = this.getStackInSlot(i).copy()
                            val itemstack1 = iinventory.addItemToSide(this.decrStackSize(i, 1), enumfacing)
                            if (itemstack1 == null || itemstack1.count == 0) {
                                iinventory.markDirty()
                                return true
                            }

                            this.setInventorySlotContents(i, itemstack)
                        }
                    }

                    return false
                }
            }
        }
    }

    private fun getFacingInventory(): IInventory? {
        val i = BlockFunnel.getFacing(blockMetadata)
        return getInventoryAt(world, (xPos + i.frontOffsetX.toDouble()), (yPos + i.frontOffsetY).toDouble(), (zPos + i.frontOffsetZ).toDouble())
    }

    fun IInventory.addItemToSide(item: ItemStack?, side: EnumFacing?): ItemStack? {
        var stack = item
        if (inventory is ISidedInventory && side != null) {
            val aint = (this as ISidedInventory).getSlotsForFace(side)

            var l = 0
            while (l < aint.size && stack != null && stack.count > 0) {
                stack = pushToInventory(this, stack, aint[l], side)
                ++l
            }
        } else {
            val j = this.sizeInventory

            var k = 0
            while (k < j && stack != null && stack.count > 0) {
                stack = pushToInventory(this, stack, k, side)
                ++k
            }
        }

        if (stack != null && stack.count == 0) {
            stack = null
        }

        return stack
    }

    private fun canInsertItem(inventory: IInventory, stack: ItemStack, slot: Int, side: EnumFacing?): Boolean {
        return if (!inventory.isItemValidForSlot(slot, stack)) false else inventory !is ISidedInventory || inventory.canInsertItem(slot, stack, side)
    }


    private fun pushToInventory(inventory: IInventory, item: ItemStack?, slot: Int, side: EnumFacing?): ItemStack? {
        var stack = item
        val itemstack1 = inventory.getStackInSlot(slot)

        if (stack != null && canInsertItem(inventory, stack, slot, side)) {
            var flag = false

            if (itemstack1 == null) {
                val max = Math.min(stack.maxStackSize, inventory.inventoryStackLimit)
                if (max >= stack.count) {
                    inventory.setInventorySlotContents(slot, stack)
                    stack = null
                } else {
                    inventory.setInventorySlotContents(slot, stack.splitStack(max))
                }
                flag = true
            } else if (canAddToStack(itemstack1, stack)) {
                val max = Math.min(stack.maxStackSize, inventory.inventoryStackLimit)
                if (max > itemstack1.count) {
                    val l = Math.min(stack.count, max - itemstack1.count)
                    stack.count -= l
                    itemstack1.count += l
                    flag = l > 0
                }
            }

            if (flag) {
                if (inventory is TileLivingwoodFunnel) {
                    inventory.transferCooldown = 8
                    inventory.markDirty()
                }

                inventory.markDirty()
            }
        }

        return stack
    }

    private fun canAddToStack(stack: ItemStack, mainStack: ItemStack): Boolean {
        return if (stack.item !== mainStack.item) false else (if (stack.itemDamage != mainStack.itemDamage) false else (if (stack.count > stack.maxStackSize) false else ItemStack.areItemStackTagsEqual(stack, mainStack)))
    }

    fun pullEntityFromWorld(inventory: IInventory, item: EntityItem?): Boolean {
        var flag = false

        if (item == null) {
            return false
        } else {
            val itemstack = item.entityItem
            if (itemstack.itemInFrames()) {
                val itemstack1 = inventory.addItemToSide(itemstack, null)

                if (itemstack1 != null && itemstack1.count != 0) {
                    item.setEntityItemStack(itemstack1)
                } else {
                    flag = true
                    item.setDead()
                }
            }

            return flag
        }
    }

    fun entitiesOnFunnel(world: World, x: Double, y: Double, z: Double): EntityItem? {
        val list = world.getEntitiesWithinAABB(EntityItem::class.java, AxisAlignedBB(x, y - 0.5, z, x + 1.0, y + 1.0, z + 1.0), EntitySelectors.IS_ALIVE)
        return if (list.size > 0) list[0] else null
    }

    private fun pullItemIn(hopper: IHopper, inventory: IInventory, slot: Int, side: EnumFacing): Boolean {
        val itemstack = inventory.getStackInSlot(slot)

        if (itemstack != null && canPullItem(inventory, itemstack, slot, side)) {
            if (itemstack.itemInFrames()) {

                val itemstack1 = itemstack.copy()
                val itemstack2 = hopper.addItemToSide(inventory.decrStackSize(slot, 1), null)

                if (itemstack2 == null || itemstack2.count == 0) {
                    inventory.markDirty()
                    return true
                }

                inventory.setInventorySlotContents(slot, itemstack1)
            }
        }

        return false
    }

    private fun ItemStack.itemInFrames(): Boolean {
        val frameItems: MutableList<ItemStack> = arrayListOf()
        for (i in EnumFacing.HORIZONTALS) {
            val var21 = AxisAlignedBB((xPos + i.frontOffsetX).toDouble(), (yPos + i.frontOffsetY).toDouble(), (zPos + i.frontOffsetZ).toDouble(), (xPos + i.frontOffsetX + 1).toDouble(), (yPos + i.frontOffsetY + 1).toDouble(), (zPos + i.frontOffsetZ + 1).toDouble())
            val frames = world.getEntitiesWithinAABB(EntityItemFrame::class.java, var21)
            for (frame in frames) {
                if (frame is EntityItemFrame)
                    if (frame.displayedItem != null)
                        frameItems.add(frame.displayedItem!!)
            }
        }
        if (frameItems.isEmpty()) return true
        for (i in frameItems) {
            if (this.item === i.item && this.itemDamage == i.itemDamage) return true
        }

        return false
    }

    private fun canPullItem(inventory: IInventory, stack: ItemStack, slot: Int, side: EnumFacing): Boolean {
        return inventory !is ISidedInventory || inventory.canExtractItem(slot, stack, side)
    }

    private fun inventoryEmpty(inventory: IInventory, side: EnumFacing): Boolean {
        if (inventory is ISidedInventory) {
            inventory.getSlotsForFace(side)
                    .filter { inventory.getStackInSlot(it) != null }
                    .forEach { return false }
        } else {
            (0..inventory.sizeInventory - 1)
                    .filter { inventory.getStackInSlot(it) != null }
                    .forEach { return false }
        }

        return true
    }


    override fun decrStackSize(slot: Int, par2: Int): ItemStack? {
        if (!inventory[slot].isEmpty) {

            val itemstack: ItemStack

            if (inventory[slot]!!.count <= par2) {
                itemstack = inventory[slot]!!
                inventory[slot] = null
                markDirty()
                return itemstack
            } else {
                itemstack = inventory[slot]!!.splitStack(par2)
                if (inventory[slot]!!.count == 0) {
                    inventory[slot] = null
                }

                this.markDirty()
                return itemstack
            }
        } else {
            return null
        }
    }

    override fun setInventorySlotContents(par1: Int, par2ItemStack: ItemStack) {
        inventory[par1] = par2ItemStack
        if (par2ItemStack != null && par2ItemStack.count > inventoryStackLimit) {
            par2ItemStack.count = inventoryStackLimit
        }

        markDirty()
    }

    override fun isUsableByPlayer(player: EntityPlayer?) = true
    override fun hasCustomName(): Boolean = false

    override fun readCustomNBT(cmp: NBTTagCompound) {
        val nbttaglist = cmp.getTagList("Items", 10)
        inventory = NonNullList.withSize(1, ItemStack.EMPTY)

        for (i in 0..nbttaglist.tagCount() - 1) {
            val nbttagcompound1 = nbttaglist.getCompoundTagAt(i)

            val b0: Int = (nbttagcompound1.getByte("Slot")).toInt()

            if (b0 >= 0 && b0 < inventory.size) {
                inventory[b0] = ItemStack(nbttagcompound1)
            }
        }

    }

    override fun writeCustomNBT(cmp: NBTTagCompound, sync: Boolean) {
        val nbttaglist = NBTTagList()

        for (i in inventory.indices) {
            if (!inventory[i].isEmpty) {
                val nbttagcompound1 = NBTTagCompound()
                nbttagcompound1.setByte("Slot", i.toByte())
                inventory[i].writeToNBT(nbttagcompound1)
                nbttaglist.appendTag(nbttagcompound1)
            }
        }

        cmp.setTag("Items", nbttaglist)
    }

    override fun getXPos(): Double = pos.x.toDouble()
    override fun getYPos(): Double = pos.y.toDouble()
    override fun getZPos(): Double = pos.z.toDouble()

    override fun openInventory(p0: EntityPlayer?) {
        //NO-OP
    }

    override fun closeInventory(p0: EntityPlayer?) {
        //NO-OP
    }

    override fun getInventoryStackLimit(): Int = 1

    override fun isItemValidForSlot(par1: Int, par2ItemStack: ItemStack): Boolean = true

    val unsidedHandler: IItemHandlerModifiable by lazy {
        FunnelWrapper(this)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T: Any> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return unsidedHandler as T
        return super.getCapability(capability, facing)
    }

    override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean {
        return capability === CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing)
    }

    class FunnelWrapper(val hopper: TileLivingwoodFunnel) : InvWrapper(hopper) {
        override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
            if (stack.isEmpty) return stack
            if (simulate || hopper.transferCooldown > 1)
                return super.insertItem(slot, stack, simulate)

            val curStackSize = stack.count
            val itemStack = super.insertItem(slot, stack, false)
            if (itemStack.isEmpty || curStackSize != itemStack.count) {
                hopper.transferCooldown = 8
            }
            return itemStack
        }
    }
}
