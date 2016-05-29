package shadowfox.botanicaladdons.client.integration.jei.treegrowing

import mezz.jei.api.recipe.BlankRecipeWrapper
import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.OreDictionary
import shadowfox.botanicaladdons.api.priest.IFocusSpell
import shadowfox.botanicaladdons.api.priest.SpellRecipe
import shadowfox.botanicaladdons.api.sapling.IIridescentSaplingVariant
import shadowfox.botanicaladdons.common.items.ItemTerrestrialFocus
import shadowfox.botanicaladdons.common.items.ModItems

class TreeGrowingRecipeJEI(val sapling: ItemStack, val soil: ItemStack, val wood: ItemStack, val leaves: ItemStack) : BlankRecipeWrapper() {

    override fun getInputs(): List<Any> {
        return listOf(sapling, soil)
    }

    override fun getOutputs(): List<Any> {
        return listOf(wood, leaves)
    }
}
