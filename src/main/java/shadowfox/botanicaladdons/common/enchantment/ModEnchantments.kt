package shadowfox.botanicaladdons.common.enchantment

import net.minecraftforge.common.util.EnumHelper
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.common.lib.LibNames

/**
 * @author WireSegal
 * Created at 9:43 AM on 5/19/16.
 */
object ModEnchantments {

    val WEIGHTY = EnumHelper.addEnchantmentType("${LibMisc.MOD_ID}:WEIGHTY")

    val heavy: EnchantmentWeight
    val lightweight: EnchantmentWeight

    init {

        heavy = EnchantmentWeight(LibNames.HEAVY, true)
        lightweight = EnchantmentWeight(LibNames.LIGHT, false)
    }
}
