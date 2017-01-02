package shadowfox.botanicaladdons.common.core.tab

import com.teamwizardry.librarianlib.common.base.ModCreativeTab
import net.minecraft.enchantment.EnumEnchantmentType
import net.minecraft.item.ItemStack
import shadowfox.botanicaladdons.common.enchantment.ModEnchantments
import shadowfox.botanicaladdons.common.items.ModItems

/**
 * @author WireSegal
 * Created at 1:05 PM on 5/17/16.
 */
object ModTab : ModCreativeTab("divine") {
    init {
        registerDefaultTab()
    }

    override fun getIconItemStack(): ItemStack {
        return ItemStack(ModItems.symbol)
    }

    override fun getRelevantEnchantmentTypes(): Array<out EnumEnchantmentType>? {
        return arrayOf(ModEnchantments.WEIGHTY)
    }
}
