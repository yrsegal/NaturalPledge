package shadowfox.botanicaladdons.common.items.base

import com.teamwizardry.librarianlib.features.base.item.ItemModBauble
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fml.common.Optional
import vazkii.botania.api.item.ICosmeticAttachable
import vazkii.botania.api.item.IPhantomInkable


/**
 * @author WireSegal
 * Created at 12:03 PM on 4/14/17.
 */
@Optional.Interface(modid = "Thaumcraft", iface = "thaumcraft.api.items.IRunicArmor")
abstract class ItemBaseBauble(name: String, vararg variants: String) : ItemModBauble(name, *variants), ICosmeticAttachable, IPhantomInkable {

    private val TAG_COSMETIC_ITEM = "cosmeticItem"
    private val TAG_PHANTOM_INK = "phantomInk"

    override fun getCosmeticItem(stack: ItemStack): ItemStack {
        val cmp = ItemNBTHelper.getCompound(stack, TAG_COSMETIC_ITEM) ?: return ItemStack.EMPTY
        return ItemStack(cmp)
    }

    override fun setCosmeticItem(stack: ItemStack, cosmetic: ItemStack) {
        var cmp = NBTTagCompound()
        if (!cosmetic.isEmpty)
            cmp = cosmetic.writeToNBT(cmp)
        ItemNBTHelper.setCompound(stack, TAG_COSMETIC_ITEM, cmp)
    }

    override fun hasContainerItem(stack: ItemStack?): Boolean {
        return !getContainerItem(stack!!).isEmpty
    }

    override fun getContainerItem(itemStack: ItemStack): ItemStack {
        return getCosmeticItem(itemStack)
    }

    override fun hasPhantomInk(stack: ItemStack): Boolean {
        return ItemNBTHelper.getBoolean(stack, TAG_PHANTOM_INK, false)
    }

    override fun setPhantomInk(stack: ItemStack, ink: Boolean) {
        ItemNBTHelper.setBoolean(stack, TAG_PHANTOM_INK, ink)
    }
}
