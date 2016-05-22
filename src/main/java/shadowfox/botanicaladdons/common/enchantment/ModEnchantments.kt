package shadowfox.botanicaladdons.common.enchantment

import net.minecraft.enchantment.Enchantment
import shadowfox.botanicaladdons.common.lib.LibNames

/**
 * @author WireSegal
 * Created at 9:43 AM on 5/19/16.
 */
object ModEnchantments {

    val heavy: EnchantmentMod
    val lightweight: EnchantmentMod

    init {
        heavy = EnchantmentWeight(LibNames.HEAVY, true)
        lightweight = EnchantmentWeight(LibNames.LIGHT, false)
    }
}
