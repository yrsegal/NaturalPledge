package com.wiresegal.naturalpledge.common.enchantment

import net.minecraft.enchantment.EnumEnchantmentType
import net.minecraftforge.common.util.EnumHelper
import com.wiresegal.naturalpledge.api.item.IWeightEnchantable
import com.wiresegal.naturalpledge.api.lib.LibMisc
import com.wiresegal.naturalpledge.common.lib.LibNames

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
