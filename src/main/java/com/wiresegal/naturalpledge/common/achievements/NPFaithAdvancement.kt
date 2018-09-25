package com.wiresegal.naturalpledge.common.achievements

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonObject
import com.wiresegal.naturalpledge.api.lib.LibMisc
import net.minecraft.advancements.PlayerAdvancements
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.util.ResourceLocation

object NPFaithAdvancement : NPAchievementTrigger<NPFaithAdvancement.FaithItemCriteria>("focus") {

    override val newTrackerFunc: (player: PlayerAdvancements) -> IPlayerTracker<FaithItemCriteria> = { player -> NPFaithItemTracker(player) }

    class NPFaithItemTracker(advancements: PlayerAdvancements) : IPlayerTracker<NPFaithAdvancement.FaithItemCriteria>(advancements) {

    }

    override fun deserializeInstance(json: JsonObject, context: JsonDeserializationContext): FaithItemCriteria {
        println("Getting ach inst")
        return FaithItemCriteria(ItemPredicate.deserialize(json.get("relic")))
    }

    class FaithItemCriteria(override val predicate: ItemPredicate) : NPCriterion {
        override val trigger = NPFaithAdvancement
    }

}