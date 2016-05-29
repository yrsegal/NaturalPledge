package shadowfox.botanicaladdons.client.integration.jei.spellcrafting

import shadowfox.botanicaladdons.api.SpellRegistry
import shadowfox.botanicaladdons.client.integration.jei.treegrowing.TreeGrowingRecipeJEI

object SpellCraftingRecipeMaker {
    val recipes: List<SpellCraftingRecipeJEI>
        get() = SpellRegistry.getSpellRecipeRegistry().mapNotNull { SpellCraftingRecipeJEI(it) }
}
