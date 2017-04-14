package shadowfox.botanicaladdons.common.items.colored

import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import shadowfox.botanicaladdons.common.items.base.ItemRainbow
import vazkii.botania.api.mana.IManaItem
import vazkii.botania.api.mana.IManaTooltipDisplay

/**
 * @author WireSegal
 * Created at 9:26 AM on 4/29/16.
 */
class ItemManaDye(name: String) : ItemRainbow(name, true), IManaItem, IManaTooltipDisplay {

    val TAG_MANA = "mana"
    val COST_PER_USE = 100
    val USES = 150
    val MAX_MANA = USES * COST_PER_USE

    init {
        setMaxStackSize(1)
    }

    override fun addMana(stack: ItemStack, mana: Int) = ItemNBTHelper.setInt(stack, TAG_MANA, Math.max(0, Math.min(mana + getMana(stack), getMaxMana(stack))))
    override fun canExportManaToItem(stack: ItemStack, p1: ItemStack): Boolean = false
    override fun canExportManaToPool(stack: ItemStack, p1: TileEntity?): Boolean = false
    override fun canReceiveManaFromItem(stack: ItemStack, p1: ItemStack) = false
    override fun canReceiveManaFromPool(stack: ItemStack, p1: TileEntity?) = true
    override fun getMana(stack: ItemStack) = ItemNBTHelper.getInt(stack, TAG_MANA, getMaxMana(stack))
    override fun getMaxMana(stack: ItemStack) = MAX_MANA
    override fun isNoExport(stack: ItemStack) = true

    override fun getEntityLifespan(stack: ItemStack, world: World?) = Int.MAX_VALUE

    override fun showDurabilityBar(stack: ItemStack) = getMana(stack) != getMaxMana(stack)
    override fun getDurabilityForDisplay(stack: ItemStack) = 1 - (getMana(stack) / getMaxMana(stack).toDouble())

    override fun getManaFractionForDisplay(stack: ItemStack) = getMana(stack) / getMaxMana(stack).toFloat()

    override fun hasContainerItem(stack: ItemStack) = true
    override fun getContainerItem(stackIn: ItemStack): ItemStack {
        val stack = stackIn.copy()
        val mana = getMana(stack)
        if (mana >= COST_PER_USE) {
            addMana(stack, -COST_PER_USE)
            return stack
        }
        return ItemStack.EMPTY
    }

    override fun mapOreDict(keys: Array<String>): ItemManaDye {
        super.mapOreDict(keys)
        return this
    }

    override fun mapOreKey(key: String): ItemManaDye {
        super.mapOreKey(key)
        return this
    }
}
