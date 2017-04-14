package shadowfox.botanicaladdons.common.core.tab

import com.teamwizardry.librarianlib.features.base.ModCreativeTab
import net.minecraft.enchantment.EnumEnchantmentType
import net.minecraft.item.ItemStack
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.common.enchantment.ModEnchantments
import shadowfox.botanicaladdons.common.items.ModItems

/**
 * @author WireSegal
 * Created at 1:05 PM on 5/17/16.
 */
object ModTab : ModCreativeTab("divine") {
    init {
        registerDefaultTab()
        setNoTitle()
        backgroundImageName = "${LibMisc.MOD_ID}/divine.png"
    }

    override fun hasSearchBar(): Boolean {
        return true
    }

    override val iconStack by lazy { ItemStack(ModItems.symbol) }

    override fun getRelevantEnchantmentTypes(): Array<out EnumEnchantmentType>? {
        return arrayOf(ModEnchantments.WEIGHTY)
    }
}
