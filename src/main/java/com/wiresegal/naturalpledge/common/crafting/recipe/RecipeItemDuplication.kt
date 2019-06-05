package com.wiresegal.naturalpledge.common.crafting.recipe

import com.teamwizardry.librarianlib.features.kotlin.isNotEmpty
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.item.crafting.ShapelessRecipes
import net.minecraft.util.NonNullList
import net.minecraftforge.common.ForgeHooks

/**
 * @author WireSegal
 * Created at 1:45 PM on 5/23/16.
 */
class RecipeItemDuplication(group : String? ,val output : ItemStack ,ingredients :  NonNullList<Ingredient> ) : ShapelessRecipes(group, output,ingredients) {
    override fun getRemainingItems(inv: InventoryCrafting): NonNullList<ItemStack> {
        val ret = NonNullList.withSize(inv.sizeInventory, ItemStack.EMPTY)
        for (i in ret.indices) {
            val stack = inv.getStackInSlot(i)
            if (stack.isNotEmpty && stack.item == output.item && stack.itemDamage == output.itemDamage) {
                val newStack = stack.copy()
                newStack.count = 1
                ret[i] = newStack
            } else
                ret[i] = ForgeHooks.getContainerItem(stack)
        }
        return ret
    }
}
