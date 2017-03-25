package shadowfox.botanicaladdons.client.integration.jei.spellcrafting

import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.BlankRecipeWrapper
import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.OreDictionary
import shadowfox.botanicaladdons.api.priest.IFocusSpell
import shadowfox.botanicaladdons.api.priest.SpellRecipe
import shadowfox.botanicaladdons.client.integration.jei.JEIPluginBotanicalAddons
import shadowfox.botanicaladdons.common.items.ItemTerrestrialFocus
import shadowfox.botanicaladdons.common.items.ModItems

class SpellCraftingRecipeJEI(val recipe: SpellRecipe) : BlankRecipeWrapper() {

    val inputs by lazy {
        val outputs = getOutputsTyped()
        OreDictionary.getOres(recipe.input).filter { stack ->
            outputs.find { stack.item == it.item && stack.itemDamage == it.itemDamage } == null
        }
    }

    companion object {
        fun getFocusStack(spell: IFocusSpell): ItemStack {
            val stack = ItemStack(ModItems.spellFocus)
            ItemTerrestrialFocus.setSpell(stack, spell)
            return stack
        }
    }

    override fun getIngredients(ingredients: IIngredients) {
        val stackHelper = JEIPluginBotanicalAddons.helpers.stackHelper

        val inputs = stackHelper.toItemStackList(recipe.input)
        ingredients.setInputLists(ItemStack::class.java, listOf(inputs))
        ingredients.setOutputs(ItemStack::class.java, recipe.output.toList())
    }

    fun getOutputsTyped() = recipe.output.toList()
    fun getInputsTyped() = inputs
    fun getFocusTyped() = getFocusStack(recipe.spell)
    fun getIconTyped() = recipe.spell.iconStack
}
