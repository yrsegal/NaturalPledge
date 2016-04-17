package shadowfox.botanicaladdons.common.crafting

import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.CraftingManager
import net.minecraft.item.crafting.IRecipe
import net.minecraftforge.oredict.ShapedOreRecipe
import net.minecraftforge.oredict.ShapelessOreRecipe
import shadowfox.botanicaladdons.common.items.ModItems
import shadowfox.botanicaladdons.common.items.bauble.faith.ItemFaithBauble
import shadowfox.botanicaladdons.common.items.bauble.faith.PriestlyEmblemIdunn
import shadowfox.botanicaladdons.common.items.bauble.faith.PriestlyEmblemNjord
import shadowfox.botanicaladdons.common.items.bauble.faith.PriestlyEmblemThor
import vazkii.botania.common.lib.LibOreDict

import vazkii.botania.common.item.ModItems as BotaniaItems

/**
 * @author WireSegal
 * Created at 9:03 PM on 4/16/16.
 */
object ModRecipes {

    val recipeSymbol: IRecipe
    val recipeNjordEmblem: IRecipe
    val recipeIdunnEmblem: IRecipe
    val recipeThorEmblem: IRecipe

    init {

        recipeSymbol = addOreDictRecipe(ItemStack(ModItems.symbol),
                "S S",
                "Q Q",
                " G ",
                'S', LibOreDict.MANA_STRING,
                'Q', "gemQuartz",
                'G', "ingotGold")

        recipeNjordEmblem = addOreDictRecipe(ItemFaithBauble.emblemOf(PriestlyEmblemNjord::class.java) ?: ItemStack(ModItems.emblem, 1, 32767),
                " P ",
                "WSA",
                " C ",
                'P', LibOreDict.PIXIE_DUST,
                'W', LibOreDict.RUNE[0], // Water
                'S', "holySymbol",
                'A', LibOreDict.RUNE[3], // Air
                'C', ItemStack(BotaniaItems.waterRing))

        recipeIdunnEmblem = addOreDictRecipe(ItemFaithBauble.emblemOf(PriestlyEmblemIdunn::class.java) ?: ItemStack(ModItems.emblem, 1, 32767),
                " T ",
                "RSU",
                " B ",
                'T', LibOreDict.TERRA_STEEL,
                'R', LibOreDict.RUNE[4], // Spring
                'S', "holySymbol",
                'U', LibOreDict.RUNE[5], // Summer
                'B', ItemStack(BotaniaItems.knockbackBelt))

        recipeThorEmblem = addOreDictRecipe(ItemFaithBauble.emblemOf(PriestlyEmblemThor::class.java)?: ItemStack(ModItems.emblem, 1, 32767),
                " G ",
                "WSA",
                " B ",
                'G', LibOreDict.LIFE_ESSENCE,
                'W', LibOreDict.RUNE[13], // Wrath
                'S', "holySymbol",
                'A', LibOreDict.RUNE[3], // Air
                'B', ItemStack(BotaniaItems.travelBelt))
    }

    fun addOreDictRecipe(output: ItemStack, vararg recipe: Any): IRecipe {
        val obj = object : ShapedOreRecipe(output, *recipe) {
            override fun toString(): String {
                return recipeToString(this)
            }
        }
        CraftingManager.getInstance().recipeList.add(obj)
        return obj
    }

    fun addShapelessOreDictRecipe(output: ItemStack, vararg recipe: Any): IRecipe {
        val obj = object : ShapelessOreRecipe(output, *recipe) {
            override fun toString(): String {
                return recipeToString(this)
            }
        }
        CraftingManager.getInstance().recipeList.add(obj)
        return obj
    }

    private fun recipeToString(recipe: IRecipe): String {
        if (recipe is ShapedOreRecipe)
            return "${joinArr(recipe.input)} -> ${recipe.recipeOutput}"
        if (recipe is ShapelessOreRecipe)
            return "${joinArr(recipe.input)} -> ${recipe.recipeOutput}"
        return recipe.toString()
    }

    private fun joinArr(arr: Array<*>): String {
        var out = "["
        for (item in arr) {
            out += if (out.equals("[")) "" else ", " + item.toString()
        }
        return out + "]"
    }

    private fun joinArr(arr: List<*>): String {
        var out = "["
        for (item in arr) {
            out += if (out.equals("[")) "" else ", " + item.toString()
        }
        return out + "]"
    }
}
