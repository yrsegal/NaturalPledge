package com.wiresegal.naturalpledge.common.crafting.recipe

import com.wiresegal.naturalpledge.common.core.helper.RainbowItemHelper
import com.wiresegal.naturalpledge.common.lib.LibOreDict
import net.minecraft.block.material.MapColor
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.util.NonNullList
import net.minecraft.world.World
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.oredict.OreDictionary
import net.minecraftforge.registries.IForgeRegistryEntry
import java.awt.Color

/**
 * @author WireSegal
 * Created at 12:36 PM on 3/20/16.
 */
class RecipeDynamicDye(val dyable: Item, val iris: Boolean = true) : IForgeRegistryEntry.Impl<IRecipe>(), IRecipe {

    override fun canFit(width: Int, height: Int): Boolean {
        return true
    }


    override fun isDynamic(): Boolean {
        return true
    }

    override fun matches(inv: InventoryCrafting, worldIn: World?): Boolean {
        var ink: ItemStack = ItemStack.EMPTY
        var foundDye = false
        var foundRainbow = false

        (0 until inv.sizeInventory)
                .asSequence()
                .mapNotNull { inv.getStackInSlot(it) }
                .filterNot { it.isEmpty }
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

        return !ink.isEmpty && foundDye
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    override fun getCraftingResult(inv: InventoryCrafting): ItemStack {
        var ink: ItemStack = ItemStack.EMPTY
        var colors = 0
        var r = 0
        var g = 0
        var b = 0
        var rainbow = false

        (0..inv.sizeInventory - 1)
                .mapNotNull { inv.getStackInSlot(it) }
                .forEach {
                    if (it.item == dyable) {
                        ink = it

                        val newstack = it.copy()
                        newstack.count = 1

                        if (RainbowItemHelper.getColor(it) != -1) {
                            val color = Color(RainbowItemHelper.getColor(it))
                            r += color.red
                            g += color.green
                            b += color.blue
                            colors++
                        }
                    } else {
                        if (!checkStack(it, if (iris) LibOreDict.IRIS_DYES else LibOreDict.DYES)) {
                            return ItemStack.EMPTY
                        }

                        val colorInt = getColorFromDye(it, if (iris) LibOreDict.IRIS_DYES else LibOreDict.DYES)
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

        if (ink.isEmpty || colors == 0) {
            return ItemStack.EMPTY
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
        for ((index, value) in dyes.withIndex()) {
            if (checkStack(stack, value)) {
                if (index == 16) return -1
                return MapColor.getBlockColor(EnumDyeColor.byMetadata(index)).colorValue
            }
        }
        return 0xFFFFFF
    }

    override fun getRecipeOutput(): ItemStack {
        return ItemStack.EMPTY
    }

    override fun getRemainingItems(inv: InventoryCrafting?): NonNullList<ItemStack> {
        return ForgeHooks.defaultRecipeGetRemainingItems(inv)
    }
}
