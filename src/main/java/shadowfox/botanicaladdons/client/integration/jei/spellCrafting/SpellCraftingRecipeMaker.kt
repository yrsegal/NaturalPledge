package shadowfox.botanicaladdons.client.integration.jei.spellcrafting

import shadowfox.botanicaladdons.api.SpellRegistry

object SpellCraftingRecipeMaker {
    val recipes: List<SpellCraftingRecipeJEI> by lazy {
        SpellRegistry.getSpellRecipeRegistry().mapNotNull { SpellCraftingRecipeJEI(it) }
    }
}
