package shadowfox.botanicaladdons.common.potions

import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import shadowfox.botanicaladdons.common.lib.LibNames

/**
 * @author WireSegal
 * Created at 2:47 PM on 4/15/16.
 */
class PotionRooted(iconIndex: Int) : PotionMod(LibNames.ROOTED, true, 0x634D05, iconIndex) {
    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent
    fun onLivingTick(e: LivingEvent.LivingUpdateEvent) {
        if (this.hasEffect(e.entityLiving) && e.entityLiving.onGround) {
            e.entityLiving.setPosition(e.entityLiving.prevPosX, e.entityLiving.posY, e.entityLiving.prevPosZ)
        }
    }

    @SubscribeEvent
    fun onJump(e: LivingEvent.LivingJumpEvent) {
        if (this.hasEffect(e.entityLiving)) {
            e.entityLiving.motionY = 0.0
        }
    }
}
