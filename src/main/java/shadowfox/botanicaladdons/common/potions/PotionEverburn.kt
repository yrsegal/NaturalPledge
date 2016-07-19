package shadowfox.botanicaladdons.common.potions

import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.effect.EntityLightningBolt
import net.minecraft.util.DamageSource
import net.minecraft.util.math.BlockPos
import shadowfox.botanicaladdons.common.lib.LibNames
import shadowfox.botanicaladdons.common.potions.base.PotionMod

/**
 * @author WireSegal
 * Created at 11:10 AM on 4/18/16.
 */
class PotionEverburn(iconIndex: Int) : PotionMod(LibNames.EVERBURN, true, 0xDD581F, iconIndex, true) {
    override fun isReady(ticks: Int, amplifier: Int) = true

    override fun performEffect(entity: EntityLivingBase, amp: Int) {
        if (entity.health <= 1f)
            entity.extinguish()
        else if (!entity.isBurning) {
            entity.attackEntityFrom(DamageSource.inFire, 1f)
            entity.setFire(5)
        }
    }
}
