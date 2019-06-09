package com.wiresegal.naturalpledge.client.integration.jei.treegrowing

import com.wiresegal.naturalpledge.client.integration.jei.JEIPluginBotanicalAddons
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.ingredients.VanillaTypes
import mezz.jei.api.recipe.IRecipeWrapper
import net.minecraft.item.ItemStack

class TreeGrowingRecipeJEI(val sapling: ItemStack, val soil: ItemStack, val wood: ItemStack, val leaves: ItemStack) : IRecipeWrapper {

    override fun getIngredients(ingredients: IIngredients) {
        val stackHelper = JEIPluginBotanicalAddons.helpers.stackHelper

        val inputs = stackHelper.expandRecipeItemStackInputs(listOf(sapling, soil))
        ingredients.setInputLists(VanillaTypes.ITEM, inputs)

        ingredients.setOutputs(VanillaTypes.ITEM, listOf(wood, leaves))
    }
}
