package com.wiresegal.naturalpledge.client.integration.jei.treegrowing

import mezz.jei.api.recipe.IRecipeHandler
import mezz.jei.api.recipe.IRecipeWrapper
import com.wiresegal.naturalpledge.api.lib.LibMisc

object TreeGrowingRecipeHandler : IRecipeHandler<TreeGrowingRecipeJEI> {
    override fun getRecipeClass(): Class<TreeGrowingRecipeJEI> {
        return TreeGrowingRecipeJEI::class.java
    }

    override fun getRecipeCategoryUid(recipe: TreeGrowingRecipeJEI): String {
        return "${LibMisc.MOD_ID}:tree_growing"
    }

    override fun getRecipeWrapper(recipe: TreeGrowingRecipeJEI): IRecipeWrapper {
        return recipe
    }

    override fun isRecipeValid(recipe: TreeGrowingRecipeJEI): Boolean {
        return true
    }
}
