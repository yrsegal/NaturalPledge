package com.wiresegal.naturalpledge.common.core.helper

import net.minecraft.item.Item
import net.minecraft.util.CooldownTracker

/**
 * @author WireSegal
 * Created at 4:23 PM on 4/28/16.
 */
object CooldownHelper {
    fun setCooldown(cooldownTracker: CooldownTracker, item: Item, startTime: Int, expireTime: Int) {
        val ticks = NPMethodHandles.getCooldownTicks(cooldownTracker)
        if (expireTime <= ticks || startTime > ticks)
            return
        NPMethodHandles.addNewCooldown(cooldownTracker, item, startTime, expireTime)
    }

    data class CooldownWrapper(val createTicks: Int, val expireTicks: Int)

    fun getCooldown(cooldownTracker: CooldownTracker, item: Item): CooldownWrapper? {
        val cooldowns = NPMethodHandles.getCooldowns(cooldownTracker)
        val cooldown = cooldowns[item] ?: return null
        return CooldownWrapper(NPMethodHandles.getCreateTicks(cooldown), NPMethodHandles.getExpireTicks(cooldown))
    }
}
