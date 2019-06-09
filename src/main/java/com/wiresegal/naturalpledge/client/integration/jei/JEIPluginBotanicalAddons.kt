package com.wiresegal.naturalpledge.client.integration.jei

import com.wiresegal.naturalpledge.api.SpellRegistry
import com.wiresegal.naturalpledge.api.priest.SpellRecipe
import com.wiresegal.naturalpledge.client.integration.jei.spellcrafting.SpellCraftingCategory
import com.wiresegal.naturalpledge.client.integration.jei.spellcrafting.SpellCraftingRecipeJEI
import com.wiresegal.naturalpledge.client.integration.jei.treegrowing.TreeGrowingCategory
import com.wiresegal.naturalpledge.client.integration.jei.treegrowing.TreeGrowingRecipeJEI
import com.wiresegal.naturalpledge.client.integration.jei.treegrowing.TreeGrowingRecipeMaker
import com.wiresegal.naturalpledge.common.block.ModBlocks
import com.wiresegal.naturalpledge.common.core.helper.RainbowItemHelper
import com.wiresegal.naturalpledge.common.items.ModItems
import mezz.jei.api.*
import mezz.jei.api.ingredients.IModIngredientRegistration
import mezz.jei.api.recipe.IRecipeCategoryRegistration
import net.minecraft.item.ItemStack
import vazkii.botania.common.item.ModItems as BotaniaItems

/**
 * @author WireSegal
 * Created at 9:16 AM on 5/22/16.
 */
@JEIPlugin
class JEIPluginBotanicalAddons : IModPlugin {

    companion object {
        var initialized = false
        lateinit var helpers: IJeiHelpers
        lateinit var runtime: IJeiRuntime
    }

    override fun registerCategories(registry: IRecipeCategoryRegistration) {
        helpers = registry.jeiHelpers
        registry.addRecipeCategories(SpellCraftingCategory(), TreeGrowingCategory())
    }

    override fun register(registry: IModRegistry) {
        initialized = true
        helpers = registry.jeiHelpers

        registry.handleRecipes(SpellRecipe::class.java, ::SpellCraftingRecipeJEI, SpellCraftingCategory.uid)
        registry.handleRecipes(TreeGrowingRecipeJEI::class.java, { recipe -> recipe }, TreeGrowingCategory.uid)

        registry.addRecipes(SpellRegistry.getSpellRecipeRegistry(), SpellCraftingCategory.uid)
        registry.addRecipes(TreeGrowingRecipeMaker.recipes, TreeGrowingCategory.uid)

        registry.addRecipeCatalyst(ItemStack(ModItems.spellFocus), SpellCraftingCategory.uid)
        registry.addRecipeCatalyst(ItemStack(ModBlocks.irisSapling), TreeGrowingCategory.uid)

        // Botania

        registry.jeiHelpers.ingredientBlacklist.addIngredientToBlacklist(ItemStack(BotaniaItems.manaResource, 1, 10)) // Prismarine
    }

    override fun onRuntimeAvailable(jeiRuntime: IJeiRuntime) {
        runtime = jeiRuntime
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun registerItemSubtypes(subtypeRegistry: ISubtypeRegistry) {
        subtypeRegistry.registerSubtypeInterpreter(ModBlocks.star.itemForm) {
            RainbowItemHelper.getColor(it).toString()
        }

        subtypeRegistry.registerSubtypeInterpreter(ModBlocks.cracklingStar.itemForm) {
            RainbowItemHelper.getColor(it).toString()
        }
    }

    override fun registerIngredients(registry: IModIngredientRegistration) {
        // NO-OP
    }
}
