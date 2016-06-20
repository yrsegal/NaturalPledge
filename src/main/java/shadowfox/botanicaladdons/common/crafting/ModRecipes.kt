package shadowfox.botanicaladdons.common.crafting


import net.minecraft.block.Block
import net.minecraft.block.BlockPlanks
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.CraftingManager
import net.minecraft.item.crafting.IRecipe
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.oredict.OreDictionary
import net.minecraftforge.oredict.RecipeSorter
import net.minecraftforge.oredict.ShapedOreRecipe
import net.minecraftforge.oredict.ShapelessOreRecipe
import shadowfox.botanicaladdons.api.SaplingVariantRegistry
import shadowfox.botanicaladdons.api.SpellRegistry
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.common.block.BlockStorage.Variants
import shadowfox.botanicaladdons.common.block.ModBlocks
import shadowfox.botanicaladdons.common.core.helper.RainbowItemHelper
import shadowfox.botanicaladdons.common.crafting.recipe.RecipeDynamicDye
import shadowfox.botanicaladdons.common.crafting.recipe.RecipeItemDuplication
import shadowfox.botanicaladdons.common.crafting.recipe.RecipePureDaisyExclusion
import shadowfox.botanicaladdons.common.crafting.recipe.RecipeRainbowLensDye
import shadowfox.botanicaladdons.common.items.ItemResource.Companion.of
import shadowfox.botanicaladdons.common.items.ItemResource.Variants.*
import shadowfox.botanicaladdons.common.items.ModItems
import shadowfox.botanicaladdons.common.items.bauble.faith.*
import shadowfox.botanicaladdons.common.lib.LibNames
import shadowfox.botanicaladdons.common.lib.LibOreDict
import shadowfox.botanicaladdons.common.potions.brew.ModBrews
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.recipe.RecipeBrew
import vazkii.botania.api.recipe.RecipePureDaisy
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

    val recipesDivineCore: Array<IRecipe>

    val recipeTerrestrialFocus: IRecipe

    val recipeMortalStone: IRecipe

    val recipesStar: Array<IRecipe>

    val recipeToolbelt: IRecipe
    val recipeTravelStone: IRecipe
    val recipePrismRod: IRecipe

    val recipesDirt: Array<IRecipe>
    val recipeDirtDeconversion: RecipePureDaisy
    val recipesIrisPlanks: Array<IRecipe>
    val recipesAltPlanks: Array<IRecipe>

    val immortalBrew: RecipeBrew
    val drabBrew: RecipeBrew

    val recipeMjolnir: IRecipe
    val recipeThunderFists: IRecipe

    val recipeFateHorn: IRecipe
    val recipeImmortalApple: IRecipe

    val recipesIridescentShards: Array<IRecipe>

    val recipeFindStone: IRecipe

    val recipeFoodBelt: IRecipe

    val recipeAscensionArrow: IRecipe
    val recipeAscensionDupe: IRecipe

    val recipeAquaBricks: IRecipe
    val recipeThunderBlock: IRecipe
    val recipeAquaDeconversion: IRecipe
    val recipeThunderDeconversion: IRecipe

    val recipeSealSapling: IRecipe
    val recipeSealPlanks: IRecipe

    val recipeAmp: IRecipe

    val recipeThunderSapling: IRecipe
    val recipeThunderPlanks: IRecipe

    val recipeCircuitSapling: IRecipe
    val recipeCircuitPlanks: IRecipe

    val recipeCalicoSapling: IRecipe
    val recipeCalicoPlanks: IRecipe

    val recipeIrisSapling: RecipePureDaisy
    val recipeIrisLamp: IRecipe

    val recipeFunnel: IRecipe

    val recipeAquaGlass: IRecipe
    val recipeAquaPane: IRecipe

    val recipeDeathStone: IRecipe

    init {

        RecipeSorter.register("${LibMisc.MOD_ID}:rainbowLens", RecipeRainbowLensDye::class.java, RecipeSorter.Category.SHAPELESS, "")
        RecipeSorter.register("${LibMisc.MOD_ID}:itemDuplicate", RecipeItemDuplication::class.java, RecipeSorter.Category.SHAPELESS, "")
        RecipeSorter.register("${LibMisc.MOD_ID}:dynamicDye", RecipeDynamicDye::class.java, RecipeSorter.Category.SHAPELESS, "")
        GameRegistry.addRecipe(RecipeDynamicDye(ModItems.lightPlacer, true))
        GameRegistry.addRecipe(RecipeRainbowLensDye())

        recipeSymbol = addOreDictRecipe(ModItems.symbol,
                "S S",
                "Q Q",
                " G ",
                'S', BotaniaOreDict.MANA_STRING,
                'Q', "gemQuartz",
                'G', "ingotGold")

        recipeNjordEmblem = addOreDictRecipe(ItemFaithBauble.emblemOf(PriestlyEmblemNjord::class.java) ?: ItemStack(ModItems.emblem, 1, OreDictionary.WILDCARD_VALUE),
                " P ",
                "WSA",
                " C ",
                'P', BotaniaOreDict.PIXIE_DUST,
                'W', BotaniaOreDict.RUNE[0], // Water
                'S', "holySymbol",
                'A', BotaniaOreDict.RUNE[3], // Air
                'C', BotaniaItems.waterRing)

        recipeIdunnEmblem = addOreDictRecipe(ItemFaithBauble.emblemOf(PriestlyEmblemIdunn::class.java) ?: ItemStack(ModItems.emblem, 1, OreDictionary.WILDCARD_VALUE),
                " T ",
                "RSU",
                " B ",
                'T', BotaniaOreDict.TERRA_STEEL,
                'R', BotaniaOreDict.RUNE[4], // Spring
                'S', "holySymbol",
                'U', BotaniaOreDict.RUNE[5], // Summer
                'B', BotaniaItems.knockbackBelt)

        recipeThorEmblem = addOreDictRecipe(ItemFaithBauble.emblemOf(PriestlyEmblemThor::class.java) ?: ItemStack(ModItems.emblem, 1, OreDictionary.WILDCARD_VALUE),
                " G ",
                "WSA",
                " B ",
                'G', BotaniaOreDict.LIFE_ESSENCE,
                'W', BotaniaOreDict.RUNE[13], // Wrath
                'S', "holySymbol",
                'A', BotaniaOreDict.RUNE[3], // Air
                'B', BotaniaItems.travelBelt)

        recipeHeimdallEmblem = addOreDictRecipe(ItemFaithBauble.emblemOf(PriestlyEmblemHeimdall::class.java) ?: ItemStack(ModItems.emblem, 1, OreDictionary.WILDCARD_VALUE),
                " G ",
                "PSF",
                " B ",
                'G', ItemStack(BotaniaBlocks.bifrostPerm),
                'P', BotaniaOreDict.RUNE[15], // Pride
                'S', "holySymbol",
                'F', BotaniaOreDict.RUNE[1], // Fire
                'B', BotaniaItems.pixieRing)

        recipesDivineCore = Array(LibOreDict.HOLY_MATERIALS.size) {addOreDictRecipe(ModBlocks.awakenerCore,
                " I ",
                "DGD",
                " D ",
                'I', LibOreDict.HOLY_MATERIALS[it],
                'D', BotaniaOreDict.MANA_DIAMOND,
                'G', BotaniaOreDict.GAIA_INGOT)
        }

        recipeTerrestrialFocus = addOreDictRecipe(ModItems.spellFocus,
                " D ",
                "DPD",
                "ODO",
                'D', BotaniaOreDict.MANA_DIAMOND,
                'P', BotaniaOreDict.MANA_PEARL,
                'O', Blocks.OBSIDIAN)

        recipeMortalStone = addOreDictRecipe(ModItems.mortalStone,
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

        recipeToolbelt = addOreDictRecipe(ModItems.toolbelt,
                "CL ",
                "L L",
                "PLD",
                'P', BotaniaOreDict.PIXIE_DUST,
                'L', Items.LEATHER,
                'C', Blocks.CHEST,
                'D', LibOreDict.IRIS_DYE)

        recipeTravelStone = addOreDictRecipe(ModItems.travelStone,
                "DSD",
                "SAS",
                "DSD",
                'S', "stone",
                'A', BotaniaOreDict.RUNE[3], // Air
                'D', LibOreDict.IRIS_DYE)

        recipePrismRod = addOreDictRecipe(ModItems.lightPlacer,
                " BG",
                " DB",
                "D  ",
                'G', "glowstone",
                'B', LibOreDict.IRIS_DYES[16],
                'D', BotaniaOreDict.DREAMWOOD_TWIG)

        recipesDirt = Array(LibOreDict.DYES.size, {
            addOreDictRecipe(ItemStack(if (it == 16) ModBlocks.rainbowDirt else ModBlocks.irisDirt, 10, it % 16),
                    "DDD",
                    "DID",
                    "DDD",
                    'D', Blocks.DIRT,
                    'I', LibOreDict.IRIS_DYES[it])
        })

        recipeDirtDeconversion = BotaniaAPI.registerPureDaisyRecipe(LibOreDict.IRIS_DIRT, Blocks.DIRT.defaultState)

        recipesIrisPlanks = Array(LibOreDict.DYES.size, {
            addShapelessOreDictRecipe(ItemStack(if (it == 16) ModBlocks.rainbowPlanks else ModBlocks.irisPlanks, 4, it % 16),
                    ItemStack(if (it == 16) ModBlocks.rainbowLog else ModBlocks.irisLogs[it / 4], 1, it % 4))
        })

        recipesAltPlanks = Array(6, {
            addShapelessOreDictRecipe(ItemStack(ModBlocks.altPlanks, 4, it),
                    ItemStack(ModBlocks.altLogs[it / 4], 1, it % 4))
        })

        immortalBrew = BotaniaAPI.registerBrewRecipe(ModBrews.immortality, ItemStack(Items.NETHER_WART), BotaniaOreDict.PIXIE_DUST, ItemStack(ModItems.apple))
        drabBrew = BotaniaAPI.registerBrewRecipe(ModBrews.drained, ItemStack(Items.NETHER_WART), LibOreDict.IRIS_DYES[7], ItemStack(Items.CLAY_BALL)) // Gray

        recipeMjolnir = addOreDictRecipe(ModItems.mjolnir,
                "TTT",
                "TLT",
                " L ",
                'T', LibOreDict.THUNDERSTEEL_AWAKENED,
                'L', BotaniaOreDict.LIVINGWOOD_TWIG)

        recipeThunderFists = addOreDictRecipe(ModItems.fists,
                "TDT",
                "TT ",
                'T', LibOreDict.THUNDERSTEEL,
                'D', BotaniaOreDict.MANA_DIAMOND)

        recipeFateHorn = addOreDictRecipe(ModItems.fateHorn,
                " G ",
                "GIG",
                "GG ",
                'G', "ingotGold",
                'I', LibOreDict.IRIS_DYE_AWAKENED)

        recipeImmortalApple = addOreDictRecipe(ModItems.apple,
                " R ",
                "GAG",
                " G ",
                'R', LibOreDict.LIFE_ROOT_AWAKENED,
                'G', "ingotGold",
                'A', Items.APPLE)

        recipesIridescentShards = Array(LibOreDict.DYES.size, {
            addOreDictRecipe(ItemStack(ModItems.manaDye, 1, it),
                    " I ",
                    "EPE",
                    " M ",
                    'I', LibOreDict.IRIS_DYES[it],
                    'E', BotaniaOreDict.ELEMENTIUM,
                    'P', "gemPrismarine",
                    'M', BotaniaOreDict.MANA_POWDER)
        })

        recipeFindStone = addOreDictRecipe(ModItems.finder,
                "DSD",
                "SCS",
                "DSD",
                'S', "stone",
                'C', Items.COMPASS,
                'D', LibOreDict.IRIS_DYE)

        recipeFoodBelt = addOreDictRecipe(ModItems.foodbelt,
                "CL ",
                "L L",
                "PLD",
                'P', BotaniaOreDict.PIXIE_DUST,
                'L', Items.LEATHER,
                'C', Items.CAKE,
                'D', LibOreDict.IRIS_DYE)

        recipeAscensionArrow = addOreDictRecipe(ModItems.sealArrow,
                "A",
                "S",
                "R",
                'A', LibOreDict.AQUAMARINE_AWAKENED,
                'S', BotaniaOreDict.DREAMWOOD_TWIG,
                'R', BotaniaOreDict.RUNE[3]) // Air
        recipeAscensionDupe = RecipeItemDuplication(LibOreDict.AQUAMARINE, ModItems.sealArrow)
        GameRegistry.addRecipe(recipeAscensionDupe)

        recipeAquaBricks = addOreDictRecipe(ItemStack(ModBlocks.storage, 1, Variants.AQUAMARINE.ordinal),
                "AAA", "AAA", "AAA", 'A', //my recipe seems to want to scream a bit
                LibOreDict.AQUAMARINE)

        recipeThunderBlock = addOreDictRecipe(ItemStack(ModBlocks.storage, 1, Variants.THUNDERSTEEL.ordinal),
                "TTT", "TTT", "TTT",
                'T', LibOreDict.THUNDERSTEEL)

        recipeAquaDeconversion = addShapelessOreDictRecipe(ItemStack(ModItems.resource, 9, AQUAMARINE.ordinal),
                LibOreDict.BLOCK_AQUAMARINE)

        recipeThunderDeconversion = addShapelessOreDictRecipe(ItemStack(ModItems.resource, 9, THUNDER_STEEL.ordinal),
                LibOreDict.BLOCK_THUNDERSTEEL)


        recipeSealSapling = addOreDictRecipe(ModBlocks.sealSapling,
                "W W",
                " S ",
                "R R",
                'W', Blocks.WOOL,
                'S', ModBlocks.irisSapling,
                'R', LibOreDict.LIFE_ROOT)

        recipeSealPlanks = addShapelessOreDictRecipe(ItemStack(ModBlocks.sealPlanks, 4),
                ModBlocks.sealLog)

        recipeAmp = addOreDictRecipe(ModBlocks.amp,
                " N ",
                "NRN",
                " N ",
                'N', Blocks.NOTEBLOCK,
                'R', ModBlocks.sealPlanks)

        recipeThunderSapling = addOreDictRecipe(ModBlocks.thunderSapling,
                "W I",
                " S ",
                "R R",
                'W', BotaniaOreDict.RUNE[13], // Wrath
                'I', "ingotIron",
                'S', ModBlocks.irisSapling,
                'R', LibOreDict.LIFE_ROOT)

        recipeThunderPlanks = addShapelessOreDictRecipe(ItemStack(ModBlocks.thunderPlanks, 4),
                ModBlocks.thunderLog)

        recipeCircuitSapling = addOreDictRecipe(ModBlocks.circuitSapling,
                "D C",
                " S ",
                "R R",
                'D', "dustRedstone",
                'C', Items.COMPARATOR,
                'S', ModBlocks.irisSapling,
                'R', LibOreDict.LIFE_ROOT)

        recipeCircuitPlanks = addShapelessOreDictRecipe(ItemStack(ModBlocks.circuitPlanks, 4),
                ModBlocks.circuitLog)

        recipeCalicoSapling = addOreDictRecipe(ModBlocks.calicoSapling,
                "W B",
                " S ",
                "R R",
                'W', LibOreDict.DYES[0], // White
                'B', LibOreDict.DYES[15], // Black
                'S', ModBlocks.irisSapling,
                'R', LibOreDict.LIFE_ROOT)

        recipeCalicoPlanks = addShapelessOreDictRecipe(ItemStack(ModBlocks.calicoPlanks, 4),
                ModBlocks.calicoLog)

        recipeIrisSapling = RecipePureDaisyExclusion("treeSapling", ModBlocks.irisSapling.defaultState)
        BotaniaAPI.pureDaisyRecipes.add(recipeIrisSapling)

        recipeIrisLamp = addOreDictRecipe(ModBlocks.irisLamp,
                " D ",
                "DLD",
                " D ",
                'D', LibOreDict.IRIS_DYES[16],
                'L', Blocks.REDSTONE_LAMP)

        recipeFunnel = addOreDictRecipe(ModBlocks.funnel,
                "L L",
                "LPL",
                " L ",
                'L', BotaniaOreDict.LIVING_WOOD,
                'P', LibOreDict.CIRCUIT_PLANKS)

        recipeAquaGlass = addOreDictRecipe(ModBlocks.aquaGlass,
                "AAA",
                "AGA",
                "AAA",
                'A', LibOreDict.AQUAMARINE,
                'G', BotaniaBlocks.manaGlass)

        recipeAquaPane = addOreDictRecipe(ItemStack(ModBlocks.aquaPane, 16),
                "GGG", "GGG", //GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG
                'G', ModBlocks.aquaGlass)

        recipeDeathStone = addShapelessOreDictRecipe(ModItems.deathFinder,
                ModItems.finder, "bone")


        var spell = SpellRegistry.getSpell(LibNames.SPELL_NJORD_INFUSION)
        if (spell != null) {
            SpellRegistry.registerSpellRecipe("gemPrismarine", spell, of(AQUAMARINE), of(AQUAMARINE, true))
            SpellRegistry.registerSpellRecipe("blockPrismarineBrick", spell, ItemStack(ModBlocks.storage, 1, Variants.AQUAMARINE.ordinal))
        }
        spell = SpellRegistry.getSpell(LibNames.SPELL_THOR_INFUSION)
        if (spell != null) {
            SpellRegistry.registerSpellRecipe("ingotIron", spell, of(THUNDER_STEEL), of(THUNDER_STEEL, true))
            SpellRegistry.registerSpellRecipe("blockIron", spell, ItemStack(ModBlocks.storage, 1, Variants.THUNDERSTEEL.ordinal))
        }

        spell = SpellRegistry.getSpell(LibNames.SPELL_IDUNN_INFUSION)
        if (spell != null) SpellRegistry.registerSpellRecipe(BotaniaOreDict.LIVING_WOOD, spell, of(LIFE_ROOT), of(LIFE_ROOT, true))

        spell = SpellRegistry.getSpell(LibNames.SPELL_RAINBOW)
        if (spell != null)
            for (i in LibOreDict.DYES.withIndex()) SpellRegistry.registerSpellRecipe(i.value, spell, ItemStack(ModItems.iridescentDye, 1, i.index), ItemStack(ModItems.awakenedDye, 1, i.index))


        SaplingVariantRegistry.registerRecipe(ItemStack(ModBlocks.sealSapling), null, ItemStack(ModBlocks.sealLog), ItemStack(ModBlocks.sealLeaves))
        SaplingVariantRegistry.registerRecipe(ItemStack(ModBlocks.thunderSapling), null, ItemStack(ModBlocks.thunderLog), ItemStack(ModBlocks.thunderLeaves))
        SaplingVariantRegistry.registerRecipe(ItemStack(ModBlocks.circuitSapling), null, ItemStack(ModBlocks.circuitLog), ItemStack(ModBlocks.circuitLeaves))
        SaplingVariantRegistry.registerRecipe(ItemStack(ModBlocks.calicoSapling), null, ItemStack(ModBlocks.calicoLog), ItemStack(ModBlocks.calicoLeaves))

        SaplingVariantRegistry.registerRecipe(ItemStack(Blocks.SAPLING, 1, BlockPlanks.EnumType.OAK.metadata), null,
                ItemStack(Blocks.LOG, 1, BlockPlanks.EnumType.OAK.metadata), ItemStack(Blocks.LEAVES, 1, BlockPlanks.EnumType.OAK.metadata))
        SaplingVariantRegistry.registerRecipe(ItemStack(Blocks.SAPLING, 1, BlockPlanks.EnumType.SPRUCE.metadata), null,
                ItemStack(Blocks.LOG, 1, BlockPlanks.EnumType.SPRUCE.metadata), ItemStack(Blocks.LEAVES, 1, BlockPlanks.EnumType.SPRUCE.metadata))
        SaplingVariantRegistry.registerRecipe(ItemStack(Blocks.SAPLING, 1, BlockPlanks.EnumType.BIRCH.metadata), null,
                ItemStack(Blocks.LOG, 1, BlockPlanks.EnumType.BIRCH.metadata), ItemStack(Blocks.LEAVES, 1, BlockPlanks.EnumType.BIRCH.metadata))
        SaplingVariantRegistry.registerRecipe(ItemStack(Blocks.SAPLING, 1, BlockPlanks.EnumType.JUNGLE.metadata), null,
                ItemStack(Blocks.LOG, 1, BlockPlanks.EnumType.JUNGLE.metadata), ItemStack(Blocks.LEAVES, 1, BlockPlanks.EnumType.JUNGLE.metadata))

        SaplingVariantRegistry.registerRecipe(ItemStack(Blocks.SAPLING, 1, BlockPlanks.EnumType.ACACIA.metadata), null,
                ItemStack(Blocks.LOG2, 1, BlockPlanks.EnumType.ACACIA.metadata - 4), ItemStack(Blocks.LEAVES2, 1, BlockPlanks.EnumType.ACACIA.metadata - 4))
        SaplingVariantRegistry.registerRecipe(ItemStack(Blocks.SAPLING, 1, BlockPlanks.EnumType.DARK_OAK.metadata), null,
                ItemStack(Blocks.LOG2, 1, BlockPlanks.EnumType.DARK_OAK.metadata - 4), ItemStack(Blocks.LEAVES2, 1, BlockPlanks.EnumType.DARK_OAK.metadata - 4))
    }

    fun addOreDictRecipe(output: Item, vararg recipe: Any) = addOreDictRecipe(ItemStack(output), *recipe)
    fun addOreDictRecipe(output: Block, vararg recipe: Any) = addOreDictRecipe(ItemStack(output), *recipe)
    fun addOreDictRecipe(output: ItemStack, vararg recipe: Any): IRecipe {
        val obj = ShapedOreRecipe(output, *recipe)
        CraftingManager.getInstance().recipeList.add(obj)
        return obj
    }

    fun addShapelessOreDictRecipe(output: Item, vararg recipe: Any) = addShapelessOreDictRecipe(ItemStack(output), *recipe)
    fun addShapelessOreDictRecipe(output: Block, vararg recipe: Any) = addShapelessOreDictRecipe(ItemStack(output), *recipe)
    fun addShapelessOreDictRecipe(output: ItemStack, vararg recipe: Any): IRecipe {
        val obj = ShapelessOreRecipe(output, *recipe)
        CraftingManager.getInstance().recipeList.add(obj)
        return obj
    }
}
