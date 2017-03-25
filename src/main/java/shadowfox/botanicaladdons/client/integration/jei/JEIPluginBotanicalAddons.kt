package shadowfox.botanicaladdons.client.integration.jei

import mezz.jei.api.*
import mezz.jei.api.ingredients.IModIngredientRegistration
import net.minecraft.item.ItemStack
import shadowfox.botanicaladdons.client.integration.jei.spellcrafting.SpellCraftingCategory
import shadowfox.botanicaladdons.client.integration.jei.spellcrafting.SpellCraftingRecipeHandler
import shadowfox.botanicaladdons.client.integration.jei.spellcrafting.SpellCraftingRecipeMaker
import shadowfox.botanicaladdons.client.integration.jei.treegrowing.TreeGrowingCategory
import shadowfox.botanicaladdons.client.integration.jei.treegrowing.TreeGrowingRecipeHandler
import shadowfox.botanicaladdons.client.integration.jei.treegrowing.TreeGrowingRecipeMaker
import shadowfox.botanicaladdons.common.block.ModBlocks
import shadowfox.botanicaladdons.common.core.helper.RainbowItemHelper
import shadowfox.botanicaladdons.common.items.ModItems

/**
 * @author WireSegal
 * Created at 9:16 AM on 5/22/16.
 */
@JEIPlugin
class JEIPluginBotanicalAddons : IModPlugin {

    companion object {
        lateinit var helpers: IJeiHelpers
    }

    override fun register(registry: IModRegistry) {
        helpers = registry.jeiHelpers

        registry.addRecipeCategories(SpellCraftingCategory, TreeGrowingCategory)
        registry.addRecipeHandlers(SpellCraftingRecipeHandler, TreeGrowingRecipeHandler)

        registry.addRecipes(SpellCraftingRecipeMaker.recipes)
        registry.addRecipes(TreeGrowingRecipeMaker.recipes)

        registry.addRecipeCategoryCraftingItem(ItemStack(ModItems.spellFocus), SpellCraftingCategory.uid)
        registry.addRecipeCategoryCraftingItem(ItemStack(ModBlocks.irisSapling), TreeGrowingCategory.uid)
    }

    override fun onRuntimeAvailable(jeiRuntime: IJeiRuntime) {
        // NO-OP
    }

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
