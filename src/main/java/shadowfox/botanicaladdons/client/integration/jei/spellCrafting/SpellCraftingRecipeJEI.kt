package shadowfox.botanicaladdons.client.integration.jei.spellcrafting

import mezz.jei.api.recipe.BlankRecipeWrapper
import net.minecraft.item.ItemStack
import shadowfox.botanicaladdons.api.priest.IFocusSpell
import shadowfox.botanicaladdons.api.priest.SpellRecipe
import shadowfox.botanicaladdons.common.items.ItemTerrestrialFocus
import shadowfox.botanicaladdons.common.items.ModItems

class SpellCraftingRecipeJEI(val recipe: SpellRecipe) : BlankRecipeWrapper() {

    companion object {
        fun getFocusStack(spell: IFocusSpell): ItemStack {
            val stack = ItemStack(ModItems.spellFocus)
            ItemTerrestrialFocus.setSpell(stack, spell)
            return stack
        }
    }

    override fun getInputs(): List<Any> {
        return listOf(recipe.input, getFocusStack(recipe.spell), recipe.spell.iconStack)
    }

    override fun getOutputs(): List<Any> {
        return listOf(recipe.output)
    }
}
