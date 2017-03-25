package shadowfox.botanicaladdons.client.integration.jei.treegrowing

import com.teamwizardry.librarianlib.client.util.TooltipHelper
import mezz.jei.api.gui.IDrawable
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.IRecipeCategory
import net.minecraft.client.Minecraft
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.client.integration.jei.JEIPluginBotanicalAddons

object TreeGrowingCategory : IRecipeCategory<TreeGrowingRecipeJEI> {

    private val background = JEIPluginBotanicalAddons.helpers.guiHelper.createDrawable(ResourceLocation(LibMisc.MOD_ID, "textures/gui/jei/tree.png"), 0, 0, 87, 37)

    override fun setRecipe(recipeLayout: IRecipeLayout, recipeWrapper: TreeGrowingRecipeJEI, ingredients: IIngredients?) {
        recipeLayout.itemStacks.init(SAPLING_SLOT, true, 6, 1)
        recipeLayout.itemStacks.init(SOIL_SLOT, true, 6, 18)
        recipeLayout.itemStacks.init(LEAVES_SLOT, false, 60, 1)
        recipeLayout.itemStacks.init(WOOD_SLOT, false, 60, 18)

        recipeLayout.itemStacks.set(SAPLING_SLOT, recipeWrapper.sapling)
        if (recipeWrapper.soil.isEmpty)
            recipeLayout.itemStacks.set(SOIL_SLOT, defaultSoil)
        else
            recipeLayout.itemStacks.set(SOIL_SLOT, recipeWrapper.soil)
        recipeLayout.itemStacks.set(LEAVES_SLOT, recipeWrapper.leaves)
        recipeLayout.itemStacks.set(WOOD_SLOT, recipeWrapper.wood)
    }

    override fun getTooltipStrings(mouseX: Int, mouseY: Int): List<String> {
        return emptyList()
    }

    override fun getIcon() = null

    override fun getUid(): String {
        return "${LibMisc.MOD_ID}:treeGrowing"
    }

    override fun getTitle(): String {
        return TooltipHelper.local("jei.${LibMisc.MOD_ID}.recipe.treeGrowing")
    }

    override fun getBackground(): IDrawable {
        return background
    }

    override fun drawExtras(minecraft: Minecraft) {
        // NO-OP
    }

    val defaultSoil = listOf(ItemStack(Blocks.DIRT), ItemStack(Blocks.GRASS))

    private val SAPLING_SLOT = 0
    private val SOIL_SLOT = 1
    private val LEAVES_SLOT = 2
    private val WOOD_SLOT = 3
}
