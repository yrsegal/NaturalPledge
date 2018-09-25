package com.wiresegal.naturalpledge.common.achievements

import net.minecraft.advancements.ICriterionTrigger
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.entity.player.EntityPlayerMP
import java.util.ArrayList
import net.minecraft.advancements.PlayerAdvancements
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World

abstract class NPAchievementTrigger<T: NPCriterion>(val achievementId: String) : ICriterionTrigger<T> {
    private val playerTrackers = mutableMapOf<PlayerAdvancements, IPlayerTracker<T>>()

    override fun getId(): ResourceLocation {
        return AchievementHandler.getAdvancement(achievementId)
    }

    abstract val newTrackerFunc: (player: PlayerAdvancements) -> IPlayerTracker<T>

    override fun addListener(player: PlayerAdvancements, listener: ICriterionTrigger.Listener<T>) {
        playerTrackers.getOrPut(player) {
            newTrackerFunc(player)
        }
    }

    override fun removeListener(player: PlayerAdvancements, listener: ICriterionTrigger.Listener<T>) {
        val tracker = playerTrackers[player]
        tracker?.let {
            it.listeners.remove(listener)

            if (it.listeners.isEmpty()) {
                playerTrackers.remove(player)
            }
        }
    }

    override fun removeAllListeners(playerAdvancementsIn: PlayerAdvancements) {
        playerTrackers.remove(playerAdvancementsIn)
    }

    /*
    override fun deserializeInstance(json: JsonObject, context: JsonDeserializationContext): T {
        return FaithItemCriteria(ItemPredicate.deserialize(json.get("relic")))
    }
    */

    abstract class IPlayerTracker<T : NPCriterion>(private val playerAdvancements: PlayerAdvancements) {
        val listeners: MutableSet<ICriterionTrigger.Listener<T>> = mutableSetOf()

        fun trigger(player: EntityPlayer?, world: World?, stack: ItemStack?) {
            val testPassed = listeners.filter { it.criterionInstance.test(player, world, stack) }
            testPassed.map { it.grantCriterion(playerAdvancements) }
        }
    }


    fun trigger(player: EntityPlayerMP, world: World?, stack: ItemStack?) {
        playerTrackers[player.advancements]?.trigger(player, world, stack)
    }

}