package shadowfox.botanicaladdons.client.integration.jei.treegrowing

import mezz.jei.api.recipe.BlankRecipeWrapper
import net.minecraft.item.ItemStack

class TreeGrowingRecipeJEI(val sapling: ItemStack, val soil: ItemStack, val wood: ItemStack, val leaves: ItemStack) : BlankRecipeWrapper() {

    override fun getInputs(): List<Any> {
        return listOf(sapling, soil)
    }

    override fun getOutputs(): List<Any> {
        return listOf(wood, leaves)
    }
}
