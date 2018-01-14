package shadowfox.botanicaladdons.common.potions

import com.teamwizardry.librarianlib.features.base.PotionMod
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingHurtEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import shadowfox.botanicaladdons.common.lib.LibNames

/**
 * @author WireSegal
 * Created at 10:22 AM on 4/26/16.
 */
class PotionImmortality : PotionMod(LibNames.IMMORTALITY, false, 0xE2BD16) {

    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent
    fun onCreatureOw(e: LivingHurtEvent) {
        val creature = e.entityLiving
        val source = e.source
        if (hasEffect(creature) && !source.canHarmInCreative()) {
            if (source.immediateSource == null && creature.health <= 1f && e.amount > 1f) {
                e.amount = 1f
            } else if (e.amount > 2f) {
                e.amount = 2f
            }
        }
    }
}
