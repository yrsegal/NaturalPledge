package shadowfox.botanicaladdons.common.enchantment

import net.minecraft.enchantment.EnumEnchantmentType
import net.minecraftforge.common.util.EnumHelper
import shadowfox.botanicaladdons.api.item.IWeightEnchantable
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.common.lib.LibNames

/**
 * @author WireSegal
 * Created at 9:43 AM on 5/19/16.
 */
object ModEnchantments {

    val WEIGHTY: EnumEnchantmentType = EnumHelper.addEnchantmentType("${LibMisc.MOD_ID}:WEIGHTY") {
        it is IWeightEnchantable
    }!!

    val heavy: EnchantmentWeight = EnchantmentWeight(LibNames.HEAVY, true)
    val lightweight: EnchantmentWeight = EnchantmentWeight(LibNames.LIGHT, false)

}
