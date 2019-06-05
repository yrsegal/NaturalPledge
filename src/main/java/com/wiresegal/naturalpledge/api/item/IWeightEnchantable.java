package com.wiresegal.naturalpledge.api.item;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * @author WireSegal
 *         Created at 9:48 AM on 5/19/16.
 */
public interface IWeightEnchantable {
    boolean canApplyWeightEnchantment(@Nonnull ItemStack stack, @Nonnull Enchantment ench);
}
