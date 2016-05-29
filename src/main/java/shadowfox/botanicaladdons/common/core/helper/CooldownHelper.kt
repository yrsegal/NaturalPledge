package shadowfox.botanicaladdons.common.core.helper

import net.minecraft.item.Item
import net.minecraft.util.CooldownTracker

/**
 * @author WireSegal
 * Created at 4:23 PM on 4/28/16.
 */
object CooldownHelper {
    fun setCooldown(cooldownTracker: CooldownTracker, item: Item, startTime: Int, expireTime: Int) {
        val cooldowns = cooldownTracker.cooldowns
        if (expireTime <= cooldownTracker.ticks || startTime > cooldownTracker.ticks)
            return
        var cooldown = cooldowns[item]
        while (cooldown == null) {
            cooldownTracker.setCooldown(item, 0)
            cooldown = cooldowns[item]
        }
        cooldown.createTicks = startTime
        cooldown.expireTicks = expireTime
    }

    fun getCooldown(cooldownTracker: CooldownTracker, item: Item): CooldownTracker.Cooldown? {
        return cooldownTracker.cooldowns[item]
    }
}
