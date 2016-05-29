package shadowfox.botanicaladdons.common.core.helper

import net.minecraft.item.Item
import net.minecraft.util.CooldownTracker

/**
 * @author WireSegal
 * Created at 4:23 PM on 4/28/16.
 */
object CooldownHelper {
    fun setCooldown(cooldownTracker: CooldownTracker, item: Item, startTime: Int, expireTime: Int) {
        val ticks = BAMethodHandles.getCooldownTicks(cooldownTracker)
        if (expireTime <= ticks || startTime > ticks)
            return
        BAMethodHandles.addNewCooldown(cooldownTracker, item, startTime, expireTime)
    }

    data class CooldownWrapper(val createTicks: Int, val expireTicks: Int)

    fun getCooldown(cooldownTracker: CooldownTracker, item: Item): CooldownWrapper? {
        val cooldowns = BAMethodHandles.getCooldowns(cooldownTracker)
        val cooldown = cooldowns[item] ?: return null
        return CooldownWrapper(BAMethodHandles.getCreateTicks(cooldown), BAMethodHandles.getExpireTicks(cooldown))
    }
}
