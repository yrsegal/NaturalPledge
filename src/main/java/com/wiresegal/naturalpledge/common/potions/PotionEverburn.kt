package com.wiresegal.naturalpledge.common.potions

import com.teamwizardry.librarianlib.features.base.PotionMod
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.DamageSource
import com.wiresegal.naturalpledge.common.lib.LibNames

/**
 * @author WireSegal
 * Created at 11:10 AM on 4/18/16.
 */
class PotionEverburn : PotionMod(LibNames.EVERBURN, true, 0xDD581F) {
    override fun isReady(ticks: Int, amplifier: Int) = true

    override fun performEffect(entity: EntityLivingBase, amp: Int) {
        if (entity is EntityPlayer && entity.health <= 1f)
            entity.extinguish()
        else {
            if (!entity.isBurning && !entity.isImmuneToFire)
                entity.attackEntityFrom(DamageSource.IN_FIRE, 1f)
            entity.setFire(5)
        }
    }

    override fun getCurativeItems(): MutableList<ItemStack> = mutableListOf()
}
