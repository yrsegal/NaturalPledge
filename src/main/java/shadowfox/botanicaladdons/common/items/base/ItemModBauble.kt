package shadowfox.botanicaladdons.common.items.base

import baubles.api.BaublesApi
import baubles.api.IBauble
import com.teamwizardry.librarianlib.client.util.TooltipHelper
import com.teamwizardry.librarianlib.common.base.item.ItemMod
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.world.World
import shadowfox.botanicaladdons.api.lib.LibMisc
import vazkii.botania.api.item.ICosmeticAttachable
import vazkii.botania.api.item.IPhantomInkable
import vazkii.botania.api.sound.BotaniaSoundEvents
import vazkii.botania.client.core.helper.RenderHelper
import vazkii.botania.common.achievement.ModAchievements
import com.teamwizardry.librarianlib.common.util.ItemNBTHelper
import vazkii.botania.common.entity.EntityDoppleganger
import java.util.*

/**
 * @author WireSegal
 * Created at 1:51 PM on 4/13/16.
 */

/*@Optional.Interface(modid = "Thaumcraft", iface = "thaumcraft.api.items.IRunicArmor")*/
abstract class ItemModBauble(name: String, vararg variants: String) : ItemMod(name, *variants), IBauble, ICosmeticAttachable, IPhantomInkable/*, IRunicArmor*/ {

    companion object {
        val TAG_HASHCODE = "playerHashcode"
        val TAG_COSMETIC = "cosmeticItem"
        val TAG_BAUBLE_UUID_MOST = "baubleUUIDMost"
        val TAG_BAUBLE_UUID_LEAST = "baubleUUIDLeast"
        val TAG_PHANTOM_INK = "phantomInk"

        fun getLastPlayerHashcode(stack: ItemStack) = ItemNBTHelper.getInt(stack, TAG_HASHCODE, 0)
        fun setLastPlayerHashcode(stack: ItemStack, hash: Int) = ItemNBTHelper.setInt(stack, TAG_HASHCODE, hash)

        fun getBaubleUUID(stack: ItemStack): UUID {
            val most = ItemNBTHelper.getLong(stack, TAG_BAUBLE_UUID_MOST, 0L)
            if (most == 0L) {
                val least1 = UUID.randomUUID()
                ItemNBTHelper.setLong(stack, TAG_BAUBLE_UUID_MOST, least1.mostSignificantBits)
                ItemNBTHelper.setLong(stack, TAG_BAUBLE_UUID_LEAST, least1.leastSignificantBits)
                return getBaubleUUID(stack)
            } else {
                val least = ItemNBTHelper.getLong(stack, TAG_BAUBLE_UUID_LEAST, 0L)
                return UUID(most, least)
            }
        }
    }

    init {
        maxStackSize = 1
    }

    override fun onItemRightClick(par1ItemStack: ItemStack, par2World: World, par3EntityPlayer: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        if (!EntityDoppleganger.isTruePlayer(par3EntityPlayer))
            return ActionResult.newResult(EnumActionResult.FAIL, par1ItemStack)

        if (canEquip(par1ItemStack, par3EntityPlayer)) {
            val baubles = BaublesApi.getBaublesHandler(par3EntityPlayer)
            for (i in 0..baubles.slots - 1) {
                if (baubles.isItemValidForSlot(i, par1ItemStack, par3EntityPlayer)) {
                    val stackInSlot = baubles.getStackInSlot(i)
                    if (stackInSlot == null || (stackInSlot.item as IBauble).canUnequip(stackInSlot, par3EntityPlayer)) {
                        if (!par2World.isRemote) {
                            baubles.setStackInSlot(i, par1ItemStack.copy())
                            if (!par3EntityPlayer.capabilities.isCreativeMode)
                                par3EntityPlayer.inventory.setInventorySlotContents(par3EntityPlayer.inventory.currentItem, null)
                        }

                        if (stackInSlot != null) {
                            (stackInSlot.item as IBauble).onUnequipped(stackInSlot, par3EntityPlayer)
                            return ActionResult.newResult(EnumActionResult.SUCCESS, stackInSlot.copy())
                        }
                        break
                    }
                }
            }
        }

        return ActionResult.newResult(EnumActionResult.PASS, par1ItemStack)
    }

    open fun onEquippedOrLoadedIntoWorld(stack: ItemStack, player: EntityLivingBase) {
        //NO-OP
    }

    override fun onEquipped(stack: ItemStack, player: EntityLivingBase?) {
        if (player != null) {
            if (!player.worldObj.isRemote)
                player.worldObj.playSound(null, player.posX, player.posY, player.posZ, BotaniaSoundEvents.equipBauble, SoundCategory.PLAYERS, 0.1F, 1.3F);

            if (player is EntityPlayer)
                player.addStat(ModAchievements.baubleWear, 1);

            onEquippedOrLoadedIntoWorld(stack, player);
            setLastPlayerHashcode(stack, player.hashCode());
        }

    }

    override fun onWornTick(stack: ItemStack, player: EntityLivingBase) {
        if (getLastPlayerHashcode(stack) != player.hashCode()) {
            onEquippedOrLoadedIntoWorld(stack, player);
            setLastPlayerHashcode(stack, player.hashCode());
        }
    }

    override fun addInformation(stack: ItemStack, player: EntityPlayer?, tooltip: MutableList<String>, advanced: Boolean) {
        TooltipHelper.tooltipIfShift(tooltip) {
            addHiddenTooltip(stack, player, tooltip, advanced)
        }
    }

    override fun canUnequip(stack: ItemStack, player: EntityLivingBase) = true
    override fun canEquip(stack: ItemStack, player: EntityLivingBase) = true

    override fun hasPhantomInk(stack: ItemStack) = ItemNBTHelper.getBoolean(stack, TAG_PHANTOM_INK, false)
    override fun setPhantomInk(stack: ItemStack, ink: Boolean) = ItemNBTHelper.setBoolean(stack, TAG_PHANTOM_INK, ink)

    override fun hasContainerItem(stack: ItemStack): Boolean = this.getContainerItem(stack) != null
    override fun getContainerItem(itemStack: ItemStack): ItemStack? = this.getCosmeticItem(itemStack)

    override fun getCosmeticItem(stack: ItemStack): ItemStack? {
        val cmp = ItemNBTHelper.getCompound(stack, TAG_COSMETIC, true)
        return if (cmp == null) null else ItemStack.loadItemStackFromNBT(cmp)
    }

    override fun setCosmeticItem(stack: ItemStack, cosmetic: ItemStack?) {
        if (cosmetic != null) {
            val cmp = NBTTagCompound()
            cosmetic.writeToNBT(cmp)

            ItemNBTHelper.setCompound(stack, TAG_COSMETIC, cmp)
        } else {
            ItemNBTHelper.removeEntry(stack, TAG_COSMETIC)
        }
    }

    open fun addHiddenTooltip(stack: ItemStack, player: EntityPlayer?, tooltip: MutableList<String>, advanced: Boolean) {
        val key = RenderHelper.getKeyDisplayString("Baubles Inventory")
        if (key != null) {
            TooltipHelper.addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.baubletooltip", key)
        }

        val cosmetic = this.getCosmeticItem(stack)
        if (cosmetic != null) {
            TooltipHelper.addToTooltip(tooltip, "botaniamisc.hasCosmetic", cosmetic.displayName)
        }

        if (this.hasPhantomInk(stack)) {
            TooltipHelper.addToTooltip(tooltip, "botaniamisc.hasPhantomInk")
        }
    }

    /*@Optional.Method(modid = "Thaumcraft")
    override fun getRunicCharge(stack: ItemStack) = 0*/
}
