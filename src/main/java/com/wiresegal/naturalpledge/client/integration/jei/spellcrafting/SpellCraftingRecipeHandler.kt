package com.wiresegal.naturalpledge.client.integration.jei.spellcrafting

import mezz.jei.api.recipe.IRecipeHandler
import mezz.jei.api.recipe.IRecipeWrapper
import com.wiresegal.naturalpledge.api.lib.LibMisc

object SpellCraftingRecipeHandler : IRecipeHandler<SpellCraftingRecipeJEI> {
    override fun getRecipeClass(): Class<SpellCraftingRecipeJEI> {
        return SpellCraftingRecipeJEI::class.java
    }

    override fun getRecipeCategoryUid(recipe: SpellCraftingRecipeJEI): String {
        return "${LibMisc.MOD_ID}:spell_crafting"
    }

    override fun getRecipeWrapper(recipe: SpellCraftingRecipeJEI): IRecipeWrapper {
        return recipe
    }

    override fun isRecipeValid(recipe: SpellCraftingRecipeJEI): Boolean {
        try {
            recipe.getInputsTyped()
            recipe.getFocusTyped()
            recipe.getIconTyped()
            recipe.getOutputsTyped()
        } catch (t: Throwable) {
            return false
        }
        return recipe.getInputsTyped().isNotEmpty() && recipe.getOutputsTyped().isNotEmpty()
    }
}
