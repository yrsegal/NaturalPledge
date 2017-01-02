package shadowfox.botanicaladdons.common.crafting.recipe

import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.world.World
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.oredict.OreDictionary
import shadowfox.botanicaladdons.common.core.helper.RainbowItemHelper
import shadowfox.botanicaladdons.common.lib.LibOreDict
import java.awt.Color

/**
 * @author WireSegal
 * Created at 12:36 PM on 3/20/16.
 */
class RecipeDynamicDye(val dyable: Item, val iris: Boolean = true) : IRecipe {

    override fun matches(inv: InventoryCrafting, worldIn: World?): Boolean {
        var ink: ItemStack? = null
        var foundDye = false
        var foundRainbow = false

        (0 until inv.sizeInventory)
                .asSequence()
                .mapNotNull { inv.getStackInSlot(it) }
                .forEach {
                    if (it.item == dyable) {
                        ink = it
                    } else {
                        if (!checkStack(it, if (iris) LibOreDict.IRIS_DYES else LibOreDict.DYES)) {
                            return false
                        }

                        if (foundRainbow) return false

                        if (checkStack(it, (if (iris) LibOreDict.IRIS_DYES else LibOreDict.DYES)[16])) {
                            if (foundDye) return false
                            foundRainbow = true
                        }

                        foundDye = true
                    }
                }

        return ink != null && foundDye
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    override fun getCraftingResult(inv: InventoryCrafting): ItemStack? {
        var ink: ItemStack? = null
        var colors = 0
        var r = 0
        var g = 0
        var b = 0
        var rainbow = false

        for (k in 0..inv.sizeInventory - 1) {
            val stack = inv.getStackInSlot(k)

            if (stack != null) {
                if (stack.item == dyable) {
                    ink = stack

                    val newstack = stack.copy()
                    newstack.stackSize = 1

                    if (RainbowItemHelper.getColor(stack) != -1) {
                        val color = Color(RainbowItemHelper.getColor(stack))
                        r += color.red
                        g += color.green
                        b += color.blue
                        colors++
                    }
                } else {
                    if (!checkStack(stack, if (iris) LibOreDict.IRIS_DYES else LibOreDict.DYES)) {
                        return null
                    }

                    val colorInt = getColorFromDye(stack, if (iris) LibOreDict.IRIS_DYES else LibOreDict.DYES)
                    if (colorInt == -1) rainbow = true
                    else {
                        val color = Color(colorInt)
                        r += color.red
                        g += color.green
                        b += color.blue
                        colors++
                    }
                }
            }
        }

        if (ink == null || colors == 0) {
            return null
        }

        r /= colors
        g /= colors
        b /= colors

        val newink = ink.copy()

        val colorInt = (r and 0xFF shl 16) or (g and 0xFF shl 8) or (b and 0xFF)
        RainbowItemHelper.setColor(newink, if (rainbow) -1 else colorInt)
        return newink
    }

    val oreSets = hashMapOf<String, List<ItemStack>>() // Caching

    fun getOres(key: String): List<ItemStack> = oreSets.getOrPut(key) { OreDictionary.getOres(key, false) }

    fun checkStack(stack: ItemStack, keys: Array<String>): Boolean {
        return keys.any { checkStack(stack, it) }
    }

    fun checkStack(stack: ItemStack, key: String): Boolean {
        val ores = getOres(key)
        return ores.any { OreDictionary.itemMatches(stack, it, false) }
    }

    fun getColorFromDye(stack: ItemStack, dyes: Array<String>): Int {
        for (i in dyes.withIndex()) {
            if (checkStack(stack, i.value)) {
                if (i.index == 16) return -1
                return EnumDyeColor.byMetadata(i.index).mapColor.colorValue
            }
        }
        return 0xFFFFFF
    }

    /**
     * Returns the size of the recipe area
     */
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
