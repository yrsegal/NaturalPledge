package shadowfox.botanicaladdons.common.potions

import com.teamwizardry.librarianlib.features.base.PotionMod
import com.teamwizardry.librarianlib.features.base.PotionMod.Companion.hasEffect
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingAttackEvent
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

    private var no = false

    @SubscribeEvent
    fun onCreatureOw(e: LivingAttackEvent) {
        if (no) return
        val creature = e.entityLiving
        val source = e.source
        if (hasEffect(creature) && !source.canHarmInCreative()) {
            if (source.entity == null && (e.amount > 1f || creature.health <= 1f)) {
                e.isCanceled = true
                if (creature.health > 1f) {
                    no = true
                    creature.attackEntityFrom(e.source, 1f)
                    no = false
                }
            } else if (e.amount > 2f) {
                e.isCanceled = true
                no = true
                creature.attackEntityFrom(e.source, 2f)
                no = false
            }
        }
    }
}
