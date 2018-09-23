package com.wiresegal.naturalpledge.client.integration.jei.spellcrafting

import com.wiresegal.naturalpledge.api.SpellRegistry

object SpellCraftingRecipeMaker {
    val recipes: List<SpellCraftingRecipeJEI>
        get() = SpellRegistry.getSpellRecipeRegistry().map(::SpellCraftingRecipeJEI)
}
