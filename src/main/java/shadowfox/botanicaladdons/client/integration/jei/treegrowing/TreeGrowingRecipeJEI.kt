package shadowfox.botanicaladdons.client.integration.jei.treegrowing

import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.BlankRecipeWrapper
import net.minecraft.item.ItemStack
import shadowfox.botanicaladdons.client.integration.jei.JEIPluginBotanicalAddons

class TreeGrowingRecipeJEI(val sapling: ItemStack, val soil: ItemStack?, val wood: ItemStack, val leaves: ItemStack) : BlankRecipeWrapper() {

    override fun getInputs(): List<Any> {
        return if (soil == null) listOf(sapling, *TreeGrowingCategory.defaultSoil.toTypedArray()) else listOf(sapling, soil)
    }

    override fun getOutputs(): List<Any> {
        return listOf(wood, leaves)
    }

    override fun getIngredients(ingredients: IIngredients) {
        val stackHelper = JEIPluginBotanicalAddons.helpers.stackHelper

        val inputs = stackHelper.expandRecipeItemStackInputs(listOf(sapling, soil))
        ingredients.setInputLists(ItemStack::class.java, inputs)

        ingredients.setOutput(ItemStack::class.java, wood)
    }
}
