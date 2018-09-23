package com.wiresegal.naturalpledge.common.potions

import com.teamwizardry.librarianlib.features.base.PotionMod
import net.minecraft.item.ItemStack
import com.wiresegal.naturalpledge.common.block.trap.BlockBaseTrap
import com.wiresegal.naturalpledge.common.lib.LibNames

/**
 * @author WireSegal
 * Created at 11:10 AM on 4/18/16.
 */
class PotionTrapSeer : PotionMod(LibNames.TRAP_SEER, false, BlockBaseTrap.COLOR) {
    override fun getCurativeItems(): MutableList<ItemStack> = mutableListOf()
}
