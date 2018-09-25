package com.wiresegal.naturalpledge.common.achievements

import com.wiresegal.naturalpledge.api.lib.LibMisc
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.util.ResourceLocation

object AchievementHandler {

    val triggers = mutableListOf<NPAchievementTrigger<*>>()

    fun registerTrigger(trigger: NPAchievementTrigger<*>) {
        triggers.add(trigger)
        CriteriaTriggers.register(trigger)
    }

    fun getAdvancement(s: String): ResourceLocation {
        return ResourceLocation(LibMisc.MOD_ID, "naturalpledge/$s")
    }

}