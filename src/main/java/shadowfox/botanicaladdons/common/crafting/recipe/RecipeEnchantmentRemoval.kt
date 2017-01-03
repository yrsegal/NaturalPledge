package shadowfox.botanicaladdons.common.crafting.recipe

import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.init.Items
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.world.World
import net.minecraftforge.common.ForgeHooks
import shadowfox.botanicaladdons.common.items.ModItems
import shadowfox.botanicaladdons.common.items.resetSeed
import shadowfox.botanicaladdons.common.items.xp
import shadowfox.botanicaladdons.common.items.xpSeed
import java.util.*


/**
 * @author WireSegal
 * Created at 9:35 AM on 1/3/17.
 */
class RecipeEnchantmentRemoval : IRecipe {
    override fun getRemainingItems(inv: InventoryCrafting): Array<ItemStack?> {
        var tome: ItemStack? = null
        var enchanted: ItemStack? = null
        (0 until inv.sizeInventory)
                .asSequence()
                .mapNotNull { inv.getStackInSlot(it) }
                .forEach {
                    if (it.item == ModItems.xpTome)
                        tome = it
                    else if (EnchantmentHelper.getEnchantments(it).isNotEmpty())
                        enchanted = it
                }

        val finalTome = tome!!.copy()
        val finalEnchanted = enchanted!!

        val index = getEnchantmentIndex(finalTome, finalEnchanted)
        val enchantmentTag = finalEnchanted.enchantmentTagList.getCompoundTagAt(index)
        val enchantment = Enchantment.getEnchantmentByID(enchantmentTag.getShort("id").toInt())
                ?: return ForgeHooks.defaultRecipeGetRemainingItems(inv)
        val level = enchantmentTag.getShort("lvl").toInt()

        val enchantabilityCalc = ((finalEnchanted.item.itemEnchantability / 2) shr 1) * 2 + 1
        val rarityXpCalc = 20f / enchantment.rarity.weight
        val k = Math.max(1.0f,
                ((enchantment.getMinEnchantability(level).toFloat() - 0.5f)
                        * 0.869f * rarityXpCalc)
                - enchantabilityCalc).toInt()

        return Array(inv.sizeInventory) {
            val stack = inv.getStackInSlot(it)
            if (stack == null || stack == finalEnchanted) null
            else if (stack.item == ModItems.xpTome) {
                val final = stack.copy()
                final.xp += k
                final.resetSeed = true
                final
            } else ForgeHooks.getContainerItem(inv.getStackInSlot(it))
        }
    }

    override fun getRecipeOutput(): ItemStack? {
        return null
    }

    override fun getRecipeSize(): Int {
        return 10
    }

    override fun getCraftingResult(inv: InventoryCrafting): ItemStack? {
        var tome: ItemStack? = null
        var enchanted: ItemStack? = null
        (0 until inv.sizeInventory)
                .asSequence()
                .mapNotNull { inv.getStackInSlot(it) }
                .forEach {
                    if (it.item == ModItems.xpTome)
                        tome = it
                    else if (EnchantmentHelper.getEnchantments(it).isNotEmpty())
                        enchanted = it
                }

        if (tome == null || enchanted == null) return null
        val finalTome = tome!!
        val finalEnchanted = enchanted!!.copy()
        val index = getEnchantmentIndex(finalTome, finalEnchanted)
        val list = (if (finalEnchanted.item === Items.ENCHANTED_BOOK)
            Items.ENCHANTED_BOOK.getEnchantments(finalEnchanted) else finalEnchanted.enchantmentTagList) ?: return null
        list.removeTag(index)
        if (list.tagCount() == 0)
            finalEnchanted.tagCompound?.removeTag("ench")
        if ((finalEnchanted.tagCompound?.size ?: 0) == 0)
            finalEnchanted.tagCompound = null

        if (finalEnchanted.item === Items.ENCHANTED_BOOK && list.tagCount() == 0) {
            val newStack = ItemStack(Items.BOOK, finalEnchanted.stackSize)
            newStack.tagCompound = finalEnchanted.tagCompound
            return newStack
        }

        return finalEnchanted
    }

    override fun matches(inv: InventoryCrafting, worldIn: World?): Boolean {
        var foundTome = false
        var foundEnchanted = false
        (0 until inv.sizeInventory)
                .asSequence()
                .mapNotNull { inv.getStackInSlot(it) }
                .forEach {
                    if (it.item == ModItems.xpTome) {
                        if (foundTome) return false
                        else foundTome = true
                    } else if (EnchantmentHelper.getEnchantments(it).isNotEmpty()) {
                        if (foundEnchanted) return false
                        else foundEnchanted = true
                    } else return false
                }
        return foundTome && foundEnchanted
    }

    private val rand = Random()

    fun getEnchantmentIndex(tome: ItemStack, enchanted: ItemStack): Int {
        val seed = tome.xpSeed
        val total = EnchantmentHelper.getEnchantments(enchanted).size
        rand.setSeed(seed.toLong())
        return rand.nextInt(total)
    }
}
