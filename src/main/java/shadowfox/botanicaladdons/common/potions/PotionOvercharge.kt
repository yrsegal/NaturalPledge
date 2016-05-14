package shadowfox.botanicaladdons.common.potions

import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.effect.EntityLightningBolt
import shadowfox.botanicaladdons.common.lib.LibNames
import shadowfox.botanicaladdons.common.potions.base.PotionMod

/**
 * @author WireSegal
 * Created at 11:10 AM on 4/18/16.
 */
class PotionOvercharge(iconIndex: Int) : PotionMod(LibNames.OVERCHARGED, true, 0x00E4D7, iconIndex, true) {
    override fun isReady(ticks: Int, amplifier: Int) = ticks % 40 == 0

    val RANGE = 6

    override fun performEffect(entity: EntityLivingBase, amp: Int) {
        val x = (Math.random() - 0.5) * RANGE * 2 + entity.posX
        val y = (Math.random() - 0.5) * RANGE * 2 + entity.posY
        val z = (Math.random() - 0.5) * RANGE * 2 + entity.posZ
        entity.worldObj.addWeatherEffect(EntityLightningBolt(entity.worldObj, x, y, z, false))
    }
}
