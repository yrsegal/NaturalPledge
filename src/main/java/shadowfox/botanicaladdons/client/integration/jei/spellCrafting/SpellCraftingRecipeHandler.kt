package shadowfox.botanicaladdons.client.integration.jei.spellcrafting

import mezz.jei.api.recipe.IRecipeHandler
import mezz.jei.api.recipe.IRecipeWrapper
import shadowfox.botanicaladdons.api.lib.LibMisc

object SpellCraftingRecipeHandler : IRecipeHandler<SpellCraftingRecipeJEI> {
    override fun getRecipeClass(): Class<SpellCraftingRecipeJEI> {
        return SpellCraftingRecipeJEI::class.java
    }

    override fun getRecipeCategoryUid(): String {
        return "${LibMisc.MOD_ID}:spellCrafting"
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
