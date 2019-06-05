package com.wiresegal.naturalpledge.common.potions

import com.teamwizardry.librarianlib.features.base.PotionMod
import net.minecraft.item.ItemStack
import com.wiresegal.naturalpledge.common.lib.LibNames

/**
 * @author WireSegal
 * Created at 9:25 AM on 4/15/16.
 */
class PotionFaithlessness : PotionMod(LibNames.FAITHLESSNESS, true, 0x916464) {
    override fun getCurativeItems(): MutableList<ItemStack> = mutableListOf()
}
