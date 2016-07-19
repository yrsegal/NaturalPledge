package shadowfox.botanicaladdons.client.integration.jei.treegrowing

import net.minecraft.block.state.IBlockState
import net.minecraft.item.ItemStack
import shadowfox.botanicaladdons.api.SaplingVariantRegistry
import shadowfox.botanicaladdons.api.sapling.IStackConvertible
import shadowfox.botanicaladdons.common.BotanicalAddons
import shadowfox.botanicaladdons.common.block.ModBlocks

object TreeGrowingRecipeMaker {
    val recipes: List<TreeGrowingRecipeJEI>
        get() {
            val out = mutableListOf<TreeGrowingRecipeJEI>()
            val variants = SaplingVariantRegistry.getVariantRegistry().values
            for (variant in variants) for (state in variant.displaySoilBlockstates) {
                val leaves = variant.getLeaves(state)
                val wood = variant.getWood(state)

                val soilStack = convertState(state)
                val leavesStack = convertState(leaves)
                val woodStack = convertState(wood)

                if (soilStack == null || leavesStack == null || woodStack == null) {
                    BotanicalAddons.LOGGER.warn("Sapling variant {$variant} has a null stack, skipping. Report this to the modmaker of that mod")
                    continue
                }
                if (soilStack.item == null || leavesStack.item == null || woodStack.item == null) {
                    BotanicalAddons.LOGGER.warn("Sapling variant {$variant} has a block without an item, skipping. Report this to the modmaker of that mod")
                    continue
                }

                out.add(TreeGrowingRecipeJEI(ItemStack(ModBlocks.irisSapling), soilStack, woodStack, leavesStack))
            }

            for (recipe in SaplingVariantRegistry.getSaplingRecipeRegistry())
                out.add(TreeGrowingRecipeJEI(recipe.sapling, recipe.soil, recipe.wood, recipe.leaves))

            return out
        }

    private fun convertState(state: IBlockState): ItemStack? {
        return if (state.block is IStackConvertible)
            (state.block as IStackConvertible).itemStackFromState(state)
        else
            ItemStack(state.block, 1, state.block.getMetaFromState(state))
    }
}
