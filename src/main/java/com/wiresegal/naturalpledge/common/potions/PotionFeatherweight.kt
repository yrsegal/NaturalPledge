package com.wiresegal.naturalpledge.common.potions

import com.teamwizardry.librarianlib.features.base.PotionMod
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import com.wiresegal.naturalpledge.common.lib.LibNames


/**
 * @author WireSegal
 * Created at 11:32 AM on 4/18/16.
 */
class PotionFeatherweight : PotionMod(LibNames.FEATHERWEIGHT, true, 0x87CEEB) {

    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    override fun isReady(ticks: Int, amplifier: Int) = ticks % 10 == 0

    override fun performEffect(entity: EntityLivingBase, amp: Int) {
        entity.motionX = (Math.random() - 0.5) * 0.5
        entity.motionZ = (Math.random() - 0.5) * 0.5
    }

    @SubscribeEvent
    fun onTick(e: LivingEvent.LivingUpdateEvent) {
        if (hasEffect(e.entityLiving) && !e.entityLiving.isDead) {
            e.entityLiving.motionY = 0.025
            e.entityLiving.fallDistance = 0f
        }
    }

    override fun getCurativeItems(): MutableList<ItemStack> = mutableListOf()
}
