package shadowfox.botanicaladdons.common.crafting.recipe

import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.util.NonNullList
import net.minecraft.world.World
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.oredict.OreDictionary
import net.minecraftforge.registries.IForgeRegistryEntry
import shadowfox.botanicaladdons.common.lib.LibOreDict
import vazkii.botania.api.mana.ILens
import vazkii.botania.common.item.lens.ItemLens

/**
 * @author WireSegal
 * Created at 8:26 PM on 2/21/16.
 */
class RecipeRainbowLensDye : IForgeRegistryEntry.Impl<IRecipe>(), IRecipe {

    override fun canFit(width: Int, height: Int): Boolean {
        return true
    }

    val ores: MutableList<ItemStack> by lazy {
        OreDictionary.getOres(LibOreDict.DYES[16])
    }

    override fun matches(var1: InventoryCrafting, var2: World?): Boolean {
        var foundLens = false
        var foundDye = false

        (0 until var1.sizeInventory)
                .asSequence()
                .mapNotNull { var1.getStackInSlot(it) }
                .filterNot { it.isEmpty }
                .forEach {
                    if (it.item is ILens && !foundLens) {
                        foundLens = true
                    } else {
                        if (foundDye) {
                            return false
                        }

                        if (!isRainbow(it))
                            return false

                        foundDye = true
                    }
                }

        return foundLens && foundDye
    }

    fun isRainbow(stack: ItemStack): Boolean {
        return ores.any { OreDictionary.itemMatches(it, stack, false) }
    }

    override fun getCraftingResult(var1: InventoryCrafting): ItemStack {
        var lens: ItemStack = ItemStack.EMPTY

        (0..var1.sizeInventory - 1)
                .map { var1.getStackInSlot(it) }
                .filter { it != null && it.item is ILens && lens.isEmpty }
                .forEach { lens = it }

        if (lens.item is ILens) {
            lens.item
            val var6 = lens.copy()
            ItemLens.setLensColor(var6, 16)
            return var6
        } else {
            return ItemStack.EMPTY
        }
    }

    override fun getRecipeOutput(): ItemStack {
        return ItemStack.EMPTY
    }

    override fun getRemainingItems(inv: InventoryCrafting?): NonNullList<ItemStack> {
        return ForgeHooks.defaultRecipeGetRemainingItems(inv)
    }
}
