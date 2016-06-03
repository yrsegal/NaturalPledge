package shadowfox.botanicaladdons.client.integration.jei.treegrowing

import net.minecraft.item.ItemStack
import shadowfox.botanicaladdons.api.SaplingVariantRegistry
import shadowfox.botanicaladdons.api.sapling.IStackConvertible
import shadowfox.botanicaladdons.common.block.ModBlocks

object TreeGrowingRecipeMaker {
    val recipes: List<TreeGrowingRecipeJEI>
        get() {
            val out = mutableListOf<TreeGrowingRecipeJEI>()
            val variants = SaplingVariantRegistry.getVariantRegistry().values
            for (variant in variants) for (state in variant.displaySoilBlockstates) {
                val leaves = variant.getLeaves(state)
                val wood = variant.getWood(state)
                val soilstack = if (state.block is IStackConvertible)
                    (state.block as IStackConvertible).itemStackFromState(state) ?: continue
                else
                    ItemStack(state.block, 1, state.block.getMetaFromState(state))
                val leavesstack = if (leaves.block is IStackConvertible)
                    (leaves.block as IStackConvertible).itemStackFromState(leaves) ?: continue
                else
                    ItemStack(leaves.block, 1, leaves.block.getMetaFromState(leaves))
                val woodstack = if (wood.block is IStackConvertible)
                    (wood.block as IStackConvertible).itemStackFromState(wood) ?: continue
                else
                    ItemStack(wood.block, 1, wood.block.getMetaFromState(wood))

                if (soilstack.item == null || leavesstack.item == null || woodstack.item == null) continue

                out.add(TreeGrowingRecipeJEI(ItemStack(ModBlocks.irisSapling), soilstack, woodstack, leavesstack))
            }

            for (recipe in SaplingVariantRegistry.getSaplingRecipeRegistry())
                out.add(TreeGrowingRecipeJEI(recipe.sapling, recipe.soil, recipe.wood, recipe.leaves))

            return out
        }
}
