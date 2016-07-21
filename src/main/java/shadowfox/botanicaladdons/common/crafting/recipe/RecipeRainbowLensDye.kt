package shadowfox.botanicaladdons.common.crafting.recipe

import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.world.World
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.oredict.OreDictionary
import shadowfox.botanicaladdons.common.lib.LibOreDict
import vazkii.botania.api.mana.ILens
import vazkii.botania.common.item.lens.ItemLens

/**
 * @author WireSegal
 * Created at 8:26 PM on 2/21/16.
 */
class RecipeRainbowLensDye : IRecipe {

    val ores: MutableList<ItemStack> by lazy {
        OreDictionary.getOres(LibOreDict.DYES[16])
    }

    override fun matches(var1: InventoryCrafting, var2: World?): Boolean {
        var foundLens = false
        var foundDye = false

        for (i in 0..var1.sizeInventory - 1) {
            val stack = var1.getStackInSlot(i)
            if (stack != null) {
                if (stack.item is ILens && !foundLens) {
                    foundLens = true
                } else {
                    if (foundDye) {
                        return false
                    }

                    if (!isRainbow(stack))
                        return false

                    foundDye = true
                }
            }
        }

        return foundLens && foundDye
    }

    fun isRainbow(stack: ItemStack): Boolean {
        for (ore in ores)
            if (OreDictionary.itemMatches(ore, stack, false))
                return true
        return false
    }

    override fun getCraftingResult(var1: InventoryCrafting): ItemStack? {
        var lens: ItemStack? = null

        for (lensCopy in 0..var1.sizeInventory - 1) {
            val stack = var1.getStackInSlot(lensCopy)
            if (stack != null) {
                if (stack.item is ILens && lens == null) {
                    lens = stack
                }
            }
        }

        if (lens!!.item is ILens) {
            lens.item
            val var6 = lens.copy()
            ItemLens.setLensColor(var6, 16)
            return var6
        } else {
            return null
        }
    }

    override fun getRecipeSize(): Int {
        return 10
    }

    override fun getRecipeOutput(): ItemStack? {
        return null
    }

    override fun getRemainingItems(inv: InventoryCrafting?): Array<out ItemStack>? {
        return ForgeHooks.defaultRecipeGetRemainingItems(inv)
    }
}
