package shadowfox.botanicaladdons.common.potions

import com.teamwizardry.librarianlib.common.base.PotionMod
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.DamageSource
import shadowfox.botanicaladdons.common.lib.LibNames

/**
 * @author WireSegal
 * Created at 11:10 AM on 4/18/16.
 */
class PotionEverburn : PotionMod(LibNames.EVERBURN, true, 0xDD581F) {
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
