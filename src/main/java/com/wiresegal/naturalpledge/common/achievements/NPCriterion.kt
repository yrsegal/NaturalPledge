package com.wiresegal.naturalpledge.common.achievements

import net.minecraft.advancements.ICriterionInstance
import net.minecraft.advancements.ICriterionTrigger
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World

interface NPCriterion : ICriterionInstance {

    val predicate: ItemPredicate

    fun test(player: EntityPlayer?, world: World?, stack: ItemStack?): Boolean {
        return predicate.test(stack!!)
    }

    val trigger: NPAchievementTrigger<*>

    override fun getId(): ResourceLocation {
        return AchievementHandler.getAdvancement(trigger.achievementId)
    }


}