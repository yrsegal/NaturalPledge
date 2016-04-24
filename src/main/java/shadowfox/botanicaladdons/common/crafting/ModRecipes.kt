package shadowfox.botanicaladdons.common.crafting

import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.CraftingManager
import net.minecraft.item.crafting.IRecipe
import net.minecraftforge.oredict.ShapedOreRecipe
import net.minecraftforge.oredict.ShapelessOreRecipe
import shadowfox.botanicaladdons.common.block.ModBlocks
import shadowfox.botanicaladdons.common.items.ModItems
import shadowfox.botanicaladdons.common.items.bauble.faith.*
import vazkii.botania.common.lib.LibOreDict

import vazkii.botania.common.block.ModBlocks as BotaniaBlocks
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
    val recipeHeimdallEmblem: IRecipe

    val recipeDivineCore: IRecipe

    val recipeTerrestrialFocus: IRecipe

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

        recipeThorEmblem = addOreDictRecipe(ItemFaithBauble.emblemOf(PriestlyEmblemThor::class.java) ?: ItemStack(ModItems.emblem, 1, 32767),
                " G ",
                "WSA",
                " B ",
                'G', LibOreDict.LIFE_ESSENCE,
                'W', LibOreDict.RUNE[13], // Wrath
                'S', "holySymbol",
                'A', LibOreDict.RUNE[3], // Air
                'B', ItemStack(BotaniaItems.travelBelt))

        recipeHeimdallEmblem = addOreDictRecipe(ItemFaithBauble.emblemOf(PriestlyEmblemHeimdall::class.java) ?: ItemStack(ModItems.emblem, 1, 32767),
                " G ",
                "PSF",
                " B ",
                'G', ItemStack(BotaniaBlocks.bifrostPerm),
                'P', LibOreDict.RUNE[15], // Pride
                'S', "holySymbol",
                'F', LibOreDict.RUNE[1], // Fire
                'B', ItemStack(BotaniaItems.pixieRing))

        recipeDivineCore = addOreDictRecipe(ItemStack(ModBlocks.awakenerCore),
                " D ",
                "DGD",
                " D ",
                'D', LibOreDict.MANA_DIAMOND,
                'G', LibOreDict.GAIA_INGOT)

        recipeTerrestrialFocus = addOreDictRecipe(ItemStack(ModItems.spellFocus),
                " D ",
                "DPD",
                "ODO",
                'D', LibOreDict.MANA_DIAMOND,
                'P', LibOreDict.MANA_PEARL,
                'O', ItemStack(Blocks.obsidian))
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
