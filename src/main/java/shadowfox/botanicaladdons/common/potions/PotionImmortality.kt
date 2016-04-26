package shadowfox.botanicaladdons.common.potions

import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingAttackEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import shadowfox.botanicaladdons.common.lib.LibNames
import shadowfox.botanicaladdons.common.potions.base.PotionMod

/**
 * @author WireSegal
 * Created at 10:22 AM on 4/26/16.
 */
class PotionImmortality(iconIndex: Int) : PotionMod(LibNames.IMMORTALITY, false, 0xE2BD16, iconIndex, false) {

    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent
    fun onCreatureOw(e: LivingAttackEvent) {
        val creature = e.entityLiving
        val source = e.source
        if (hasEffect(creature) && !source.canHarmInCreative()) {
            if (source.entity != null)
                e.isCanceled = true
            else if (e.amount > 4f) {
                e.isCanceled = true
                creature.attackEntityFrom(e.source, 4f)
            }
        }
    }
}
