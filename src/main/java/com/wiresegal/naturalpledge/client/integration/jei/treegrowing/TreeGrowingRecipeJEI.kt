package com.wiresegal.naturalpledge.client.integration.jei.treegrowing

import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.IRecipeWrapper
import net.minecraft.item.ItemStack
import com.wiresegal.naturalpledge.client.integration.jei.JEIPluginBotanicalAddons

class TreeGrowingRecipeJEI(val sapling: ItemStack, val soil: ItemStack, val wood: ItemStack, val leaves: ItemStack) : IRecipeWrapper {

    override fun getIngredients(ingredients: IIngredients) {
        val stackHelper = JEIPluginBotanicalAddons.helpers.stackHelper

        val inputs = stackHelper.expandRecipeItemStackInputs(listOf(sapling, soil))
        ingredients.setInputLists(ItemStack::class.java, inputs)

        ingredients.setOutputs(ItemStack::class.java, listOf(wood, leaves))
    }
}
