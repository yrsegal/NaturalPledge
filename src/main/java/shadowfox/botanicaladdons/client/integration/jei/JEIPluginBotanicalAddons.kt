package shadowfox.botanicaladdons.client.integration.jei

import mezz.jei.api.*
import net.minecraft.item.ItemStack
import shadowfox.botanicaladdons.client.integration.jei.spellcrafting.SpellCraftingCategory
import shadowfox.botanicaladdons.client.integration.jei.spellcrafting.SpellCraftingRecipeHandler
import shadowfox.botanicaladdons.client.integration.jei.spellcrafting.SpellCraftingRecipeMaker
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

        registry.addRecipeHandlers(SpellCraftingRecipeHandler)

        registry.addRecipeCategories(SpellCraftingCategory)

        registry.addRecipes(SpellCraftingRecipeMaker.recipes)

        registry.addRecipeCategoryCraftingItem(ItemStack(ModItems.spellFocus), SpellCraftingCategory.uid)
    }

    override fun onRuntimeAvailable(jeiRuntime: IJeiRuntime) {
        // NO-OP
    }
}
