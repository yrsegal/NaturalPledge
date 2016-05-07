package shadowfox.botanicaladdons.common.crafting


import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.CraftingManager
import net.minecraft.item.crafting.IRecipe
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.oredict.RecipeSorter
import net.minecraftforge.oredict.ShapedOreRecipe
import net.minecraftforge.oredict.ShapelessOreRecipe
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.api.lib.LibOreDict
import shadowfox.botanicaladdons.common.block.ModBlocks
import shadowfox.botanicaladdons.common.block.colored.BlockFrozenStar
import shadowfox.botanicaladdons.common.core.helper.RainbowItemHelper
import shadowfox.botanicaladdons.common.crafting.recipe.RecipeDynamicDye
import shadowfox.botanicaladdons.common.items.ModItems
import shadowfox.botanicaladdons.common.items.bauble.faith.*
import vazkii.botania.common.block.ModBlocks as BotaniaBlocks
import vazkii.botania.common.item.ModItems as BotaniaItems
import vazkii.botania.common.lib.LibOreDict as BotaniaOreDict

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

    val recipeMortalStone: IRecipe

    val recipesStar: Array<IRecipe>

    val recipeToolbelt: IRecipe
    val recipeTravelStone: IRecipe
    val recipePrismRod: IRecipe

    //    val recipeSoulSuffuser: IRecipe

    init {

        RecipeSorter.register("${LibMisc.MOD_ID}:dynamicDye", RecipeDynamicDye::class.java, RecipeSorter.Category.SHAPELESS, "")
        GameRegistry.addRecipe(RecipeDynamicDye(ModItems.lightPlacer, true))

        recipeSymbol = addOreDictRecipe(ItemStack(ModItems.symbol),
                "S S",
                "Q Q",
                " G ",
                'S', BotaniaOreDict.MANA_STRING,
                'Q', "gemQuartz",
                'G', "ingotGold")

        recipeNjordEmblem = addOreDictRecipe(ItemFaithBauble.emblemOf(PriestlyEmblemNjord::class.java) ?: ItemStack(ModItems.emblem, 1, 32767),
                " P ",
                "WSA",
                " C ",
                'P', BotaniaOreDict.PIXIE_DUST,
                'W', BotaniaOreDict.RUNE[0], // Water
                'S', "holySymbol",
                'A', BotaniaOreDict.RUNE[3], // Air
                'C', ItemStack(BotaniaItems.waterRing))

        recipeIdunnEmblem = addOreDictRecipe(ItemFaithBauble.emblemOf(PriestlyEmblemIdunn::class.java) ?: ItemStack(ModItems.emblem, 1, 32767),
                " T ",
                "RSU",
                " B ",
                'T', BotaniaOreDict.TERRA_STEEL,
                'R', BotaniaOreDict.RUNE[4], // Spring
                'S', "holySymbol",
                'U', BotaniaOreDict.RUNE[5], // Summer
                'B', ItemStack(BotaniaItems.knockbackBelt))

        recipeThorEmblem = addOreDictRecipe(ItemFaithBauble.emblemOf(PriestlyEmblemThor::class.java) ?: ItemStack(ModItems.emblem, 1, 32767),
                " G ",
                "WSA",
                " B ",
                'G', BotaniaOreDict.LIFE_ESSENCE,
                'W', BotaniaOreDict.RUNE[13], // Wrath
                'S', "holySymbol",
                'A', BotaniaOreDict.RUNE[3], // Air
                'B', ItemStack(BotaniaItems.travelBelt))

        recipeHeimdallEmblem = addOreDictRecipe(ItemFaithBauble.emblemOf(PriestlyEmblemHeimdall::class.java) ?: ItemStack(ModItems.emblem, 1, 32767),
                " G ",
                "PSF",
                " B ",
                'G', ItemStack(BotaniaBlocks.bifrostPerm),
                'P', BotaniaOreDict.RUNE[15], // Pride
                'S', "holySymbol",
                'F', BotaniaOreDict.RUNE[1], // Fire
                'B', ItemStack(BotaniaItems.pixieRing))

        recipeDivineCore = addOreDictRecipe(ItemStack(ModBlocks.awakenerCore),
                " D ",
                "DGD",
                " D ",
                'D', BotaniaOreDict.MANA_DIAMOND,
                'G', BotaniaOreDict.GAIA_INGOT)

        recipeTerrestrialFocus = addOreDictRecipe(ItemStack(ModItems.spellFocus),
                " D ",
                "DPD",
                "ODO",
                'D', BotaniaOreDict.MANA_DIAMOND,
                'P', BotaniaOreDict.MANA_PEARL,
                'O', ItemStack(Blocks.obsidian))

        recipeMortalStone = addOreDictRecipe(ItemStack(ModItems.mortalStone),
                "PSP",
                "SMS",
                "PSP",
                'P', BotaniaOreDict.MANA_POWDER,
                'S', "stone",
                'M', BotaniaOreDict.RUNE[8]) // Mana

        recipesStar = Array(LibOreDict.DYES.size, {
            addOreDictRecipe(RainbowItemHelper.forColor(it, ModBlocks.star),
                    " E ",
                    "GDG",
                    " G ",
                    'E', BotaniaOreDict.ENDER_AIR_BOTTLE,
                    'G', "dustGlowstone",
                    'D', LibOreDict.IRIS_DYES[it])
        })

        recipeToolbelt = addOreDictRecipe(ItemStack(ModItems.toolbelt),
                "CL ",
                "L L",
                "PLD",
                'P', BotaniaOreDict.PIXIE_DUST,
                'L', ItemStack(Items.leather),
                'C', ItemStack(Blocks.chest),
                'D', LibOreDict.IRIS_DYE)

        recipeTravelStone = addOreDictRecipe(ItemStack(ModItems.travelStone),
                "DSD",
                "SCS",
                "DSD",
                'S', "stone",
                'C', ItemStack(Items.compass),
                'D', LibOreDict.IRIS_DYE)

        recipePrismRod = addOreDictRecipe(ItemStack(ModItems.lightPlacer),
                " BG",
                " DB",
                "D  ",
                'G', "glowstone",
                'B', LibOreDict.IRIS_DYES[16],
                'D', BotaniaOreDict.DREAMWOOD_TWIG)

        //        recipeSoulSuffuser = addOreDictRecipe(ItemStack(ModBlocks.suffuser),
        //                "LDL",
        //                " L ",
        //                "lll",
        //                'L', BotaniaOreDict.LIVING_ROCK,
        //                'D', BotaniaOreDict.MANA_DIAMOND,
        //                'l', ItemStack(ModFluffBlocks.livingrockSlab))
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
