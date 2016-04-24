package shadowfox.botanicaladdons.common.potions

import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import shadowfox.botanicaladdons.common.lib.LibNames
import shadowfox.botanicaladdons.common.potions.base.PotionMod

/**
 * @author WireSegal
 * Created at 2:47 PM on 4/15/16.
 */
class PotionRooted(iconIndex: Int) : PotionMod(LibNames.ROOTED, true, 0x634D05, iconIndex, true) {
    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent
    fun onLivingTick(e: LivingEvent.LivingUpdateEvent) {
        val entity = e.entityLiving
        if (this.hasEffect(entity)) {
            if (entity.onGround)
                entity.setPosition(entity.prevPosX, entity.prevPosY, entity.prevPosZ)
            if (entity is EntityPlayer && entity.capabilities.isFlying && !entity.isCreative) {
                entity.capabilities.isFlying = false
            }
        }
    }

    @SubscribeEvent
    fun onJump(e: LivingEvent.LivingJumpEvent) {
        val entity = e.entityLiving
        if (this.hasEffect(entity)) {
            entity.motionY = -1.0
            entity.setPosition(entity.prevPosX, entity.prevPosY, entity.prevPosZ)
        }
    }
}
