package com.wiresegal.naturalpledge.common.achievements

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonObject
import net.minecraft.advancements.PlayerAdvancements
import net.minecraft.advancements.critereon.ItemPredicate

object AdvancementBeginRagnarok : NPAchievementTrigger<AdvancementBeginRagnarok.FaithItemCriteria>("focus") {

    override val newTrackerFunc: (player: PlayerAdvancements) -> IPlayerTracker<FaithItemCriteria> = { player -> BeginRagnarokTracker(player) }

    class BeginRagnarokTracker(advancements: PlayerAdvancements) : IPlayerTracker<AdvancementBeginRagnarok.FaithItemCriteria>(advancements)

    override fun deserializeInstance(json: JsonObject, context: JsonDeserializationContext): FaithItemCriteria {
        return FaithItemCriteria(ItemPredicate.deserialize(json.get("relic")))
    }

    class FaithItemCriteria(override val predicate: ItemPredicate) : NPCriterion {
        override val trigger = AdvancementBeginRagnarok
    }

}
