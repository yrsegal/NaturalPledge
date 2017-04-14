package shadowfox.botanicaladdons.common.potions

import com.teamwizardry.librarianlib.features.base.PotionMod
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.effect.EntityLightningBolt
import net.minecraft.item.ItemStack
import shadowfox.botanicaladdons.common.lib.LibNames

/**
 * @author WireSegal
 * Created at 11:10 AM on 4/18/16.
 */
class PotionOvercharge : PotionMod(LibNames.OVERCHARGED, true, 0x00E4D7) {
    override fun isReady(ticks: Int, amplifier: Int) = ticks % 40 == 0

    val RANGE = 6

    override fun performEffect(entity: EntityLivingBase, amp: Int) {
        if (entity.isDead) return
        val x = (Math.random() - 0.5) * RANGE * 2 + entity.posX
        val y = (Math.random() - 0.5) * RANGE * 2 + entity.posY
        val z = (Math.random() - 0.5) * RANGE * 2 + entity.posZ
        entity.world.addWeatherEffect(EntityLightningBolt(entity.world, x, y, z, false))
    }

    override fun getCurativeItems(): MutableList<ItemStack> = mutableListOf()
}
