package shadowfox.botanicaladdons.common.crafting

import com.teamwizardry.librarianlib.core.common.RecipeGeneratorHandler
import com.teamwizardry.librarianlib.core.common.RegistrationHandler
import com.teamwizardry.librarianlib.features.helpers.currentModId
import com.teamwizardry.librarianlib.features.kotlin.toRl
import net.minecraft.block.Block
import net.minecraft.block.BlockPlanks
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.util.ResourceLocation
import net.minecraftforge.oredict.OreDictionary
import net.minecraftforge.oredict.ShapedOreRecipe
import shadowfox.botanicaladdons.api.SaplingVariantRegistry
import shadowfox.botanicaladdons.api.SpellRegistry
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.common.block.BlockStorage.Variants
import shadowfox.botanicaladdons.common.block.ModBlocks
import shadowfox.botanicaladdons.common.core.helper.RainbowItemHelper
import shadowfox.botanicaladdons.common.crafting.recipe.*
import shadowfox.botanicaladdons.common.crafting.recipe.factory.RecipeItemDuplicationFactory
import shadowfox.botanicaladdons.common.items.ItemResource
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
import vazkii.botania.common.crafting.recipe.ArmorUpgradeRecipe
import vazkii.botania.common.block.ModBlocks as BotaniaBlocks
import vazkii.botania.common.item.ModItems as BotaniaItems
import vazkii.botania.common.lib.LibOreDict as BotaniaOreDict

/**
 * @author WireSegal
 * Created at 9:03 PM on 4/16/16.
 */
object ModRecipes {

    val recipeSymbol: ResourceLocation
    val recipeNjordEmblem: ResourceLocation
    val recipeIdunnEmblem: ResourceLocation
    val recipeThorEmblem: ResourceLocation
    val recipeHeimdallEmblem: ResourceLocation
    val recipeLokiEmblem: ResourceLocation

    val recipesDivineCore: Array<ResourceLocation>

    val recipeTerrestrialFocus: ResourceLocation

    val recipeMortalStone: ResourceLocation

    val recipesStar: Array<ResourceLocation>

    val recipeToolbelt: ResourceLocation
    val recipeTravelStone: ResourceLocation
    val recipePrismRod: ResourceLocation

    val recipesDirt: Array<ResourceLocation>
    val recipeDirtDeconversion: RecipePureDaisy
    val recipesIrisPlanks: Array<ResourceLocation>
    val recipesAltPlanks: Array<ResourceLocation>

    val recipeAuroraDirt: ResourceLocation
    val recipeAuroraPlanks: ResourceLocation

    val immortalBrew: RecipeBrew
    val drabBrew: RecipeBrew

    val recipeMjolnir: ResourceLocation
    val recipeThunderFists: ResourceLocation

    val recipeFateHorn: ResourceLocation
    val recipeImmortalApple: ResourceLocation

    val recipesIridescentShards: Array<ResourceLocation>

    val recipeFindStone: ResourceLocation

    val recipeFoodBelt: ResourceLocation

    val recipeAscensionArrow: ResourceLocation
    val recipeAscensionDupe: ResourceLocation

    val recipeAquaBricks: ResourceLocation
    val recipeThunderBlock: ResourceLocation
    val recipeThunderNugget: ResourceLocation
    val recipeAquaDeconversion: ResourceLocation
    val recipeThunderDeconversion: ResourceLocation
    val recipeThunderReconversion: ResourceLocation

    val recipeSealSapling: ResourceLocation
    val recipeSealPlanks: ResourceLocation

    val recipeAmp: ResourceLocation

    val recipeThunderSapling: ResourceLocation
    val recipeThunderPlanks: ResourceLocation

    val recipeCircuitSapling: ResourceLocation
    val recipeCircuitPlanks: ResourceLocation

    val recipeCalicoSapling: ResourceLocation
    val recipeCalicoPlanks: ResourceLocation

    val recipeIrisSapling: RecipePureDaisy
    val recipeIrisLamp: ResourceLocation

    val recipeFunnel: ResourceLocation

    val recipeAquaGlass: ResourceLocation
    val recipeAquaPane: ResourceLocation

    val recipeDeathStone: ResourceLocation
    val recipesCrackleStar: Array<ResourceLocation>

    val recipeNjordCloak: ResourceLocation
    val recipeIdunnCloak: ResourceLocation
    val recipeThorCloak: ResourceLocation
    val recipeHeimdallCloak: ResourceLocation
    val recipeLokiCloak: ResourceLocation

    val recipeIronBelt: ResourceLocation

    val recipeXpTome: ResourceLocation

    val recipeCorporeaResonator: ResourceLocation
    val recipeRecallStone: ResourceLocation
    val recipeEnderActuator: ResourceLocation

    val recipeSleepStone: ResourceLocation
    val recipeNetherStone: ResourceLocation
    val recipePolyStone: ResourceLocation

    val recipeDisorientationTrap: ResourceLocation
    val recipeInfernoTrap: ResourceLocation
    val recipeLaunchTrap: ResourceLocation
    val recipeRootTrap: ResourceLocation
    val recipeSandTrap: ResourceLocation
    val recipeSignalTrap: ResourceLocation
    val recipeWrathTrap: ResourceLocation

    val recipePerditionFist: ResourceLocation

    val recipeEclipseHelm: ResourceLocation
    val recipeEclipseChest: ResourceLocation
    val recipeEclipseLegs: ResourceLocation
    val recipeEclipseBoots: ResourceLocation
    val recipeEclipseWeapon: ResourceLocation

    val recipeSunmakerHelm: ResourceLocation
    val recipeSunmakerChest: ResourceLocation
    val recipeSunmakerLegs: ResourceLocation
    val recipeSunmakerBoots: ResourceLocation
    val recipeSunmakerWeapon: ResourceLocation

    val recipeFenrisHelm: ResourceLocation
    val recipeFenrisChest: ResourceLocation
    val recipeFenrisLegs: ResourceLocation
    val recipeFenrisBoots: ResourceLocation
    val recipeFenrisWeapon: ResourceLocation

    init {
        recipeAscensionDupe = "${LibMisc.MOD_ID}:ascensiondupe".toRl()
        RecipeItemDuplicationFactory().make(recipeAscensionDupe,null, ModItems.sealArrow,ItemResource.of(ItemResource.Variants.AQUAMARINE))
        RegistrationHandler.register(RecipeDynamicDye(ModItems.lightPlacer, true).setRegistryName("${LibMisc.MOD_ID}:dynamicprism"))
        RegistrationHandler.register(RecipeRainbowLensDye().setRegistryName("${LibMisc.MOD_ID}:rainbowlens"))
        RegistrationHandler.register(RecipeEnchantmentRemoval.setRegistryName("${LibMisc.MOD_ID}:enchantmentremove"))

        recipeSymbol = addOreDictRecipe("symbol", ModItems.symbol,
                "S S",
                "Q Q",
                " G ",
                'S', BotaniaOreDict.MANA_STRING,
                'Q', "gemQuartz",
                'G', "ingotGold")

        recipeNjordEmblem = addOreDictRecipe("njord", ItemFaithBauble.emblemOf(PriestlyEmblemNjord::class.java),
                " P ",
                "WSA",
                " C ",
                'P', BotaniaOreDict.MANA_DIAMOND,
                'W', BotaniaOreDict.RUNE[0], // Water
                'S', "holySymbol",
                'A', BotaniaOreDict.RUNE[3], // Air
                'C', BotaniaItems.waterRing)

        recipeIdunnEmblem = addOreDictRecipe("idunn", ItemFaithBauble.emblemOf(PriestlyEmblemIdunn::class.java),
                " T ",
                "ESE",
                " B ",
                'T', BotaniaOreDict.LIVING_WOOD,
                'E', BotaniaOreDict.RUNE[2], // Earth
                'S', "holySymbol",
                'B', BotaniaItems.knockbackBelt)

        recipeThorEmblem = addOreDictRecipe("thor", ItemFaithBauble.emblemOf(PriestlyEmblemThor::class.java),
                " G ",
                "WSA",
                " B ",
                'G', BotaniaOreDict.MANA_PEARL,
                'W', BotaniaOreDict.RUNE[1], // Fire
                'S', "holySymbol",
                'A', BotaniaOreDict.RUNE[3], // Air
                'B', BotaniaItems.travelBelt)

        recipeHeimdallEmblem = addOreDictRecipe("heimdall", ItemFaithBauble.emblemOf(PriestlyEmblemHeimdall::class.java),
                " G ",
                "PSF",
                " B ",
                'G', "dye",
                'P', BotaniaOreDict.RUNE[15], // Pride
                'S', "holySymbol",
                'F', BotaniaOreDict.RUNE[1], // Fire
                'B', BotaniaItems.pixieRing)

        recipeLokiEmblem = addOreDictRecipe("loki", ItemFaithBauble.emblemOf(PriestlyEmblemLoki::class.java),
                " G ",
                "PSF",
                " B ",
                'G', "powderBlaze",
                'P', BotaniaOreDict.RUNE[15], // Pride
                'S', "holySymbol",
                'F', BotaniaOreDict.RUNE[13], // Wrath
                'B', BotaniaItems.lavaPendant)

        recipesDivineCore = Array(LibOreDict.HOLY_MATERIALS.size) {
            addOreDictRecipe("awakener$it", ModBlocks.awakenerCore,
                    " I ",
                    "DGD",
                    " D ",
                    'I', LibOreDict.HOLY_MATERIALS[it],
                    'D', BotaniaOreDict.MANA_DIAMOND,
                    'G', BotaniaOreDict.GAIA_INGOT)
        }

        recipeTerrestrialFocus = addOreDictRecipe("focus", ModItems.spellFocus,
                " D ",
                "DPD",
                "ODO",
                'D', BotaniaOreDict.MANA_DIAMOND,
                'P', BotaniaOreDict.MANA_PEARL,
                'O', "obsidian")

        recipeMortalStone = addOreDictRecipe("mortalstone", ModItems.mortalStone,
                "PSP",
                "SMS",
                "PSP",
                'P', BotaniaOreDict.MANA_POWDER,
                'S', "stone",
                'M', BotaniaOreDict.RUNE[8]) // Mana

        recipesStar = Array(LibOreDict.DYES.size, {
            addOreDictRecipe("star$it", RainbowItemHelper.forColor(it, ItemStack(ModBlocks.star, 5)),
                    " E ",
                    "GDG",
                    " G ",
                    'E', BotaniaOreDict.ENDER_AIR_BOTTLE,
                    'G', "dustGlowstone",
                    'D', LibOreDict.IRIS_DYES[it])
        })

        recipeToolbelt = addOreDictRecipe("toolbelt", ModItems.toolbelt,
                "CL ",
                "L L",
                "PLD",
                'P', BotaniaOreDict.RUNE[12], // Sloth
                'L', "leather",
                'C', "chest",
                'D', LibOreDict.IRIS_DYE)

        recipeTravelStone = addOreDictRecipe("travelstone", ModItems.travelStone,
                "DSD",
                "SAS",
                "DSD",
                'S', "stone",
                'A', BotaniaOreDict.RUNE[3], // Air
                'D', LibOreDict.IRIS_DYE)

        recipePrismRod = addOreDictRecipe("prism", ModItems.lightPlacer,
                " BG",
                " DB",
                "D  ",
                'G', "glowstone",
                'B', LibOreDict.IRIS_DYES[16],
                'D', BotaniaOreDict.DREAMWOOD_TWIG)

        recipesDirt = Array(LibOreDict.DYES.size, {
            addOreDictRecipe("dirt$it", ItemStack(if (it == 16) ModBlocks.rainbowDirt else ModBlocks.irisDirt, 9, it % 16),
                    "DDD",
                    "DID",
                    "DDD",
                    'D', "dirt",
                    'I', LibOreDict.IRIS_DYES[it])
        })

        recipeAuroraDirt = addOreDictRecipe("auroradirt", ItemStack(ModBlocks.auroraDirt, 9),
                "DDD",
                "DMD",
                "DDD",
                'D', "dirt",
                'M', BotaniaOreDict.MANA_PEARL)

        recipeAuroraPlanks = addOreDictRecipe("auroraplank", ItemStack(ModBlocks.auroraPlanks, 4), "W", 'W', ModBlocks.auroraLog)

        recipeDirtDeconversion = BotaniaAPI.registerPureDaisyRecipe(LibOreDict.IRIS_DIRT, Blocks.DIRT.defaultState)

        recipesIrisPlanks = Array(LibOreDict.DYES.size, {
            addShapelessOreDictRecipe("irisplanks$it", ItemStack(if (it == 16) ModBlocks.rainbowPlanks else ModBlocks.irisPlanks, 4, it % 16),
                    ItemStack(if (it == 16) ModBlocks.rainbowLog else ModBlocks.irisLogs[it / 4], 1, it % 4))
        })

        recipesAltPlanks = Array(6, {
            addShapelessOreDictRecipe("altplanks$it", ItemStack(ModBlocks.altPlanks, 4, it),
                    ItemStack(ModBlocks.altLogs[it / 4], 1, it % 4))
        })

        immortalBrew = BotaniaAPI.registerBrewRecipe(ModBrews.immortality, ItemStack(Items.NETHER_WART), BotaniaOreDict.PIXIE_DUST, ItemStack(ModItems.apple))
        drabBrew = BotaniaAPI.registerBrewRecipe(ModBrews.drained, ItemStack(Items.NETHER_WART), LibOreDict.IRIS_DYES[7], ItemStack(Items.CLAY_BALL)) // Gray

        recipeMjolnir = addOreDictRecipe("mjolnir", ModItems.mjolnir,
                "TTT",
                "TLT",
                " L ",
                'T', LibOreDict.THUNDERSTEEL_AWAKENED,
                'L', BotaniaOreDict.LIVINGWOOD_TWIG)

        recipeThunderFists = addOreDictRecipe("fist", ModItems.fists,
                "TDT",
                "TT ",
                'T', LibOreDict.THUNDERSTEEL,
                'D', BotaniaOreDict.MANA_DIAMOND)

        recipeFateHorn = addOreDictRecipe("fatehorn", ModItems.fateHorn,
                " G ",
                "GIG",
                "GG ",
                'G', "ingotGold",
                'I', LibOreDict.IRIS_DYE_AWAKENED)

        recipeImmortalApple = addOreDictRecipe("apple", ModItems.apple,
                " R ",
                "GAG",
                " G ",
                'R', LibOreDict.LIFE_ROOT_AWAKENED,
                'G', "ingotGold",
                'A', Items.APPLE)

        recipesIridescentShards = Array(LibOreDict.DYES.size, {
            addOreDictRecipe("irisshard$it", ItemStack(ModItems.manaDye, 1, it),
                    " I ",
                    "EPE",
                    " M ",
                    'I', LibOreDict.IRIS_DYES[it],
                    'E', BotaniaOreDict.ELEMENTIUM,
                    'P', "gemPrismarine",
                    'M', BotaniaOreDict.MANA_POWDER)
        })

        recipeFindStone = addOreDictRecipe("finder", ModItems.finder,
                "DSD",
                "SCS",
                "DSD",
                'S', "stone",
                'C', Items.COMPASS,
                'D', LibOreDict.IRIS_DYE)

        recipeFoodBelt = addOreDictRecipe("foodbelt", ModItems.foodbelt,
                "CL ",
                "L L",
                "PLD",
                'P', BotaniaOreDict.PIXIE_DUST,
                'L', "leather",
                'C', Items.CAKE,
                'D', LibOreDict.IRIS_DYE)

        recipeAscensionArrow = addOreDictRecipe("ascension", ModItems.sealArrow,
                "A",
                "S",
                "R",
                'A', LibOreDict.AQUAMARINE_AWAKENED,
                'S', BotaniaOreDict.DREAMWOOD_TWIG,
                'R', BotaniaOreDict.RUNE[3]) // Air
        recipeAquaBricks = addOreDictRecipe("aquabrick", ItemStack(ModBlocks.storage, 1, Variants.AQUAMARINE.ordinal),
                "AAA", "AAA", "AAA", 'A', //my recipe seems to want to scream a bit
                LibOreDict.AQUAMARINE)

        recipeThunderBlock = addOreDictRecipe("thunderblock", ItemStack(ModBlocks.storage, 1, Variants.THUNDERSTEEL.ordinal),
                "TTT", "TTT", "TTT",
                'T', LibOreDict.THUNDERSTEEL)

        recipeThunderNugget = addShapelessOreDictRecipe("thundernugget", of(THUNDERNUGGET, false, 9),
                LibOreDict.THUNDERSTEEL)

        recipeAquaDeconversion = addShapelessOreDictRecipe("aquadeconvert", of(AQUAMARINE, false, 9),
                LibOreDict.BLOCK_AQUAMARINE)

        recipeThunderDeconversion = addShapelessOreDictRecipe("thunderdeconvert", of(THUNDER_STEEL, false, 9),
                LibOreDict.BLOCK_THUNDERSTEEL)

        recipeThunderReconversion = addOreDictRecipe("thunderreconvert", of(THUNDER_STEEL),
                "NNN", "NNN", "NNN",
                'N', LibOreDict.THUNDERSTEEL_NUGGET)


        recipeSealSapling = addOreDictRecipe("sealsap", ModBlocks.sealSapling,
                "W W",
                " S ",
                "R R",
                'W', Blocks.WOOL,
                'S', ModBlocks.irisSapling,
                'R', LibOreDict.LIFE_ROOT)

        recipeSealPlanks = addShapelessOreDictRecipe("sealplanks", ItemStack(ModBlocks.sealPlanks, 4),
                ModBlocks.sealLog)

        recipeAmp = addOreDictRecipe("amp", ModBlocks.amp,
                " N ",
                "NRN",
                " N ",
                'N', Blocks.NOTEBLOCK,
                'R', ModBlocks.sealPlanks)

        recipeThunderSapling = addOreDictRecipe("thundersap", ModBlocks.thunderSapling,
                "W I",
                " S ",
                "R R",
                'W', BotaniaOreDict.RUNE[13], // Wrath
                'I', "ingotIron",
                'S', ModBlocks.irisSapling,
                'R', LibOreDict.LIFE_ROOT)

        recipeThunderPlanks = addShapelessOreDictRecipe("thunderplanks", ItemStack(ModBlocks.thunderPlanks, 4),
                ModBlocks.thunderLog)

        recipeCircuitSapling = addOreDictRecipe("circuitsap", ModBlocks.circuitSapling,
                "D C",
                " S ",
                "R R",
                'D', "dustRedstone",
                'C', Items.COMPARATOR,
                'S', ModBlocks.irisSapling,
                'R', LibOreDict.LIFE_ROOT)

        recipeCircuitPlanks = addShapelessOreDictRecipe("circuitplanks", ItemStack(ModBlocks.circuitPlanks, 4),
                ModBlocks.circuitLog)

        recipeCalicoSapling = addOreDictRecipe("calicosap", ModBlocks.calicoSapling,
                "W B",
                " S ",
                "R R",
                'W', LibOreDict.DYES[0], // White
                'B', LibOreDict.DYES[15], // Black
                'S', ModBlocks.irisSapling,
                'R', LibOreDict.LIFE_ROOT)

        recipeCalicoPlanks = addShapelessOreDictRecipe("calicoplanks", ItemStack(ModBlocks.calicoPlanks, 4),
                ModBlocks.calicoLog)

        recipeIrisSapling = RecipePureDaisyExclusion("treeSapling", ModBlocks.irisSapling.defaultState)
        BotaniaAPI.pureDaisyRecipes.add(recipeIrisSapling)

        recipeIrisLamp = addOreDictRecipe("irislamp", ModBlocks.irisLamp,
                " D ",
                "DLD",
                " D ",
                'D', LibOreDict.IRIS_DYES[16],
                'L', Blocks.REDSTONE_LAMP)

        recipeFunnel = addOreDictRecipe("funnel", ModBlocks.funnel,
                "L L",
                "LPL",
                " L ",
                'L', ItemStack(BotaniaBlocks.livingwood, 1, 1),
                'P', LibOreDict.CIRCUIT_PLANKS)

        recipeAquaGlass = addOreDictRecipe("aquaglass", ModBlocks.aquaGlass,
                "AAA",
                "AGA",
                "AAA",
                'A', LibOreDict.AQUAMARINE,
                'G', BotaniaBlocks.manaGlass)

        recipeAquaPane = addOreDictRecipe("aquapane", ItemStack(ModBlocks.aquaPane, 16),
                "GGG", "GGG", //GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG
                'G', ModBlocks.aquaGlass)

        recipeDeathStone = addShapelessOreDictRecipe("deathfinder", ModItems.deathFinder,
                ModItems.finder, "bone")

        recipesCrackleStar = Array(LibOreDict.DYES.size, {
            addOreDictRecipe("crackle$it", RainbowItemHelper.forColor(it, ItemStack(ModBlocks.cracklingStar, 10)),
                    " E ",
                    "GDG",
                    " G ",
                    'E', BotaniaOreDict.ENDER_AIR_BOTTLE,
                    'G', BotaniaOreDict.MANA_PEARL,
                    'D', LibOreDict.IRIS_DYES[it])
        })

        recipeNjordCloak = addOreDictRecipe("njordcloak", ItemStack(ModItems.cloak, 1, 0),
                "CCC",
                "MCM",
                "MSM",
                'C', ItemStack(Blocks.WOOL, 1, EnumDyeColor.WHITE.metadata),
                'M', "feather",
                'S', LibOreDict.AQUAMARINE)

        recipeIdunnCloak = addOreDictRecipe("idunncloak", ItemStack(ModItems.cloak, 1, 1),
                "CCC",
                "MCM",
                "MSM",
                'C', ItemStack(Blocks.WOOL, 1, EnumDyeColor.LIME.metadata),
                'M', BotaniaOreDict.LIVING_WOOD,
                'S', LibOreDict.LIFE_ROOT)

        recipeThorCloak = addOreDictRecipe("thorcloak", ItemStack(ModItems.cloak, 1, 2),
                "CCC",
                "MCM",
                "MSM",
                'C', ItemStack(Blocks.WOOL, 1, EnumDyeColor.BLACK.metadata),
                'M', "nuggetGold",
                'S', LibOreDict.THUNDERSTEEL)

        recipeHeimdallCloak = addOreDictRecipe("heimdallcloak", ItemStack(ModItems.cloak, 1, 3),
                "CCC",
                "MCM",
                "MSM",
                'C', ItemStack(Blocks.WOOL, 1, EnumDyeColor.SILVER.metadata),
                'M', Items.CHORUS_FRUIT,
                'S', LibOreDict.IRIS_DYE)

        recipeLokiCloak = addOreDictRecipe("lokicloaki", ItemStack(ModItems.cloak, 1, 4),
                "CCC",
                "MCM",
                "MSM",
                'C', ItemStack(Blocks.WOOL, 1, EnumDyeColor.BLACK.metadata),
                'M', Items.BLAZE_POWDER,
                'S', LibOreDict.HEARTHSTONE)

        recipeIronBelt = addOreDictRecipe("ironbelt", ModItems.ironBelt,
                "WI ",
                "I I",
                "AIE",
                'W', BotaniaOreDict.RUNE[0], // Water
                'I', "ingotIron",
                'A', LibOreDict.AQUAMARINE,
                'E', BotaniaOreDict.RUNE[2]) // Earth

        recipeXpTome = addOreDictRecipe("xptome", ModItems.xpTome,
                "CRP",
                "RBR",
                " RC",
                'C', BotaniaOreDict.MANAWEAVE_CLOTH,
                'R', LibOreDict.LIFE_ROOT,
                'P', BotaniaOreDict.PIXIE_DUST,
                'B', Items.BOOK)

        recipeCorporeaResonator = addOreDictRecipe("corporearesonator", ModBlocks.corporeaResonator,
                "ABA",
                "OSO",
                "BAB",
                'A', BotaniaOreDict.ENDER_AIR_BOTTLE,
                'B', LibOreDict.IRIS_DYES[3], // Black
                'O', "obsidian",
                'S', BotaniaItems.corporeaSpark)

        recipeRecallStone = addShapelessOreDictRecipe("recall", ModItems.corporeaFocus,
                ModItems.finder, BotaniaItems.corporeaSpark)

        recipeEnderActuator = addOreDictRecipe("actuator", ModBlocks.enderActuator,
                "PBE",
                "BCB",
                "EBP",
                'P', BotaniaOreDict.MANA_PEARL,
                'B', LibOreDict.IRIS_DYES[15], // Black
                'E', Items.ENDER_EYE,
                'C', "chestEnder")

        recipeSleepStone = addOreDictRecipe("sleep", ModItems.sleepStone,
                "CCS",
                "WWW",
                'C', ItemStack(Blocks.CARPET, 1, OreDictionary.WILDCARD_VALUE),
                'S', ModItems.travelStone,
                'W', ItemStack(Blocks.WOOL, 1, OreDictionary.WILDCARD_VALUE))

        recipeNetherStone = addOreDictRecipe("fracture", ModItems.portalStone,
                "OOO",
                "OSO",
                "OOO",
                'O', "obsidian",
                'S', ModItems.finder)

        recipePolyStone = addOreDictRecipe("poly", ModItems.polyStone,
                "MPM",
                "SFA",
                "MHM",
                'M', BotaniaBlocks.manaGlass,
                'P', BotaniaItems.manasteelPick,
                'S', BotaniaItems.manasteelShovel,
                'F', ModItems.travelStone,
                'A', BotaniaItems.manasteelAxe,
                'H', BotaniaItems.manasteelShears)

        recipeDisorientationTrap = addOreDictRecipe("disorient", ModBlocks.disorientTrap,
                "DHH",
                "DPD",
                "HHD",
                'H', LibOreDict.HEARTHSTONE,
                'D', "gunpowder",
                'P', BotaniaItems.phantomInk)

        recipeInfernoTrap = addOreDictRecipe("inferno", ItemStack(ModBlocks.infernoTrap, 16),
                "BPB",
                "HHH",
                'H', LibOreDict.HEARTHSTONE,
                'B', "powderBlaze",
                'P', BotaniaItems.phantomInk)

        recipeLaunchTrap = addOreDictRecipe("launch", ItemStack(ModBlocks.launchTrap),
                "SPS",
                "HHH",
                'H', LibOreDict.HEARTHSTONE,
                'S', "slimeball",
                'P', BotaniaItems.phantomInk)

        recipeRootTrap = addOreDictRecipe("root", ItemStack(ModBlocks.rootTrap, 16),
                "LPL",
                "HHH",
                'H', LibOreDict.HEARTHSTONE,
                'L', BotaniaOreDict.LIVINGWOOD_TWIG,
                'P', BotaniaItems.phantomInk)

        recipeSandTrap = addOreDictRecipe("sandtrap", ItemStack(ModBlocks.sandTrap, 16),
                "S S",
                "SPS",
                "HHH",
                'H', LibOreDict.HEARTHSTONE,
                'S', "sand",
                'P', BotaniaItems.phantomInk)

        recipeSignalTrap = addOreDictRecipe("signal", ItemStack(ModBlocks.signalTrap),
                "RPR",
                "HHH",
                'H', LibOreDict.HEARTHSTONE,
                'R', "dustRedstone",
                'P', BotaniaItems.phantomInk)

        recipeWrathTrap = addOreDictRecipe("wrath", ItemStack(ModBlocks.wrathTrap, 16),
                " M ",
                "HPH",
                " H ",
                'H', LibOreDict.HEARTHSTONE,
                'M', ModItems.mortalStone,
                'P', BotaniaItems.phantomInk)

        recipePerditionFist = addOreDictRecipe("fisto", ModItems.perditionFist,
                "G F",
                "WAC",
                "CC ",
                'G', Items.GHAST_TEAR,
                'F', Items.FIRE_CHARGE,
                'C', "blockCoal",
                'W', BotaniaOreDict.RUNE[13], // Wrath
                'A', LibOreDict.HEARTHSTONE_AWAKENED)
        recipeEclipseHelm = addHiddenRecipe(ArmorUpgradeRecipe(ItemStack(ModItems.eclipseHelm),
                " S ",
                "MAM",
                " M ",
                'S', LibOreDict.DIVINE_SPIRIT,
                'M', LibOreDict.HEARTHSTONE,
                'A', ItemStack(BotaniaItems.terrasteelHelm)).setRegistryName("${LibMisc.MOD_ID}:eclipsehelm"))
        recipeEclipseChest = addHiddenRecipe(ArmorUpgradeRecipe(ItemStack(ModItems.eclipseChest),
                " S ",
                "MAM",
                " M ",
                'S', LibOreDict.DIVINE_SPIRIT,
                'M', LibOreDict.HEARTHSTONE,
                'A', ItemStack(BotaniaItems.terrasteelChest)).setRegistryName("${LibMisc.MOD_ID}:eclipsechest"))
        recipeEclipseLegs = addHiddenRecipe(ArmorUpgradeRecipe(ItemStack(ModItems.eclipseLegs),
                " S ",
                "MAM",
                " M ",
                'S', LibOreDict.DIVINE_SPIRIT,
                'M', LibOreDict.HEARTHSTONE,
                'A', ItemStack(BotaniaItems.terrasteelLegs)).setRegistryName("${LibMisc.MOD_ID}:eclipselegs"))
        recipeEclipseBoots = addHiddenRecipe(ArmorUpgradeRecipe(ItemStack(ModItems.eclipseBoots),
                " S ",
                "MAM",
                " M ",
                'S', LibOreDict.DIVINE_SPIRIT,
                'M', LibOreDict.HEARTHSTONE,
                'A', ItemStack(BotaniaItems.terrasteelBoots)).setRegistryName("${LibMisc.MOD_ID}:eclipseboot"))
        recipeEclipseWeapon = addHiddenOreDictRecipe("flarebringer", ModItems.flarebringer,
                "DMS",
                "MT ",
                " T ",
                'S', LibOreDict.DIVINE_SPIRIT,
                'M', LibOreDict.HEARTHSTONE,
                'T', BotaniaOreDict.LIVINGWOOD_TWIG,
                'D', BotaniaOreDict.PIXIE_DUST)

        recipeSunmakerHelm = addHiddenRecipe(ArmorUpgradeRecipe(ItemStack(ModItems.sunmakerHelm),
                " S ",
                "MAM",
                " M ",
                'S', LibOreDict.DIVINE_SPIRIT,
                'M', LibOreDict.THUNDERSTEEL,
                'A', ItemStack(BotaniaItems.terrasteelHelm)).setRegistryName("${LibMisc.MOD_ID}:sunmakerhelm"))
        recipeSunmakerChest = addHiddenRecipe(ArmorUpgradeRecipe(ItemStack(ModItems.sunmakerChest),
                " S ",
                "MAM",
                " M ",
                'S', LibOreDict.DIVINE_SPIRIT,
                'M', LibOreDict.THUNDERSTEEL,
                'A', ItemStack(BotaniaItems.terrasteelChest)).setRegistryName("${LibMisc.MOD_ID}:sunmakerchest"))
        recipeSunmakerLegs = addHiddenRecipe(ArmorUpgradeRecipe(ItemStack(ModItems.sunmakerLegs),
                " S ",
                "MAM",
                " M ",
                'S', LibOreDict.DIVINE_SPIRIT,
                'M', LibOreDict.THUNDERSTEEL,
                'A', ItemStack(BotaniaItems.terrasteelLegs)).setRegistryName("${LibMisc.MOD_ID}:sunmakerleg"))
        recipeSunmakerBoots = addHiddenRecipe(ArmorUpgradeRecipe(ItemStack(ModItems.sunmakerBoots),
                " S ",
                "MAM",
                " M ",
                'S', LibOreDict.DIVINE_SPIRIT,
                'M', LibOreDict.THUNDERSTEEL,
                'A', ItemStack(BotaniaItems.terrasteelBoots)).setRegistryName("${LibMisc.MOD_ID}:sunmakerboot"))
        recipeSunmakerWeapon = addHiddenOreDictRecipe("shadowbreaker", ModItems.shadowbreaker,
                "AMS",
                "MT ",
                " T ",
                'S', LibOreDict.DIVINE_SPIRIT,
                'M', LibOreDict.THUNDERSTEEL,
                'T', BotaniaOreDict.DREAMWOOD_TWIG,
                'A', LibOreDict.AQUAMARINE)

        recipeFenrisHelm = addHiddenRecipe(ArmorUpgradeRecipe(ItemStack(ModItems.fenrisHelm),
                " S ",
                "MAM",
                " M ",
                'S', LibOreDict.DIVINE_SPIRIT_AWAKENED,
                'M', LibOreDict.LIFE_ROOT,
                'A', ItemStack(BotaniaItems.terrasteelHelm)).setRegistryName("${LibMisc.MOD_ID}:fenrishelm"))
        recipeFenrisChest = addHiddenRecipe(ArmorUpgradeRecipe(ItemStack(ModItems.fenrisChest),
                " S ",
                "MAM",
                " M ",
                'S', LibOreDict.DIVINE_SPIRIT_AWAKENED,
                'M', LibOreDict.LIFE_ROOT,
                'A', ItemStack(BotaniaItems.terrasteelChest)).setRegistryName("${LibMisc.MOD_ID}:fenrischest"))
        recipeFenrisLegs = addHiddenRecipe(ArmorUpgradeRecipe(ItemStack(ModItems.fenrisLegs),
                " S ",
                "MAM",
                " M ",
                'S', LibOreDict.DIVINE_SPIRIT_AWAKENED,
                'M', LibOreDict.LIFE_ROOT,
                'A', ItemStack(BotaniaItems.terrasteelLegs)).setRegistryName("${LibMisc.MOD_ID}:fenrislegs"))
        recipeFenrisBoots = addHiddenRecipe(ArmorUpgradeRecipe(ItemStack(ModItems.fenrisBoots),
                " S ",
                "MAM",
                " M ",
                'S', LibOreDict.DIVINE_SPIRIT_AWAKENED,
                'M', LibOreDict.LIFE_ROOT,
                'A', ItemStack(BotaniaItems.terrasteelBoots)).setRegistryName("${LibMisc.MOD_ID}:fenrisboots"))
        recipeFenrisWeapon = addHiddenOreDictRecipe("nightscourge", ModItems.nightscourge,
                "MSC",
                "MM ",
                'S', LibOreDict.DIVINE_SPIRIT_AWAKENED,
                'M', LibOreDict.LIFE_ROOT,
                'C', "bone")


        var spell = SpellRegistry.getSpell(LibNames.SPELL_NJORD_INFUSION)
        if (spell != null) {
            SpellRegistry.registerSpellRecipe("gemPrismarine", spell, of(AQUAMARINE), of(AQUAMARINE, true))
            SpellRegistry.registerSpellRecipe("blockPrismarineBrick", spell, ItemStack(ModBlocks.storage, 1, Variants.AQUAMARINE.ordinal))
        }
        spell = SpellRegistry.getSpell(LibNames.SPELL_THOR_INFUSION)
        if (spell != null) {
            SpellRegistry.registerSpellRecipe("ingotIron", spell, of(THUNDER_STEEL), of(THUNDER_STEEL, true))
            SpellRegistry.registerSpellRecipe("blockIron", spell, ItemStack(ModBlocks.storage, 1, Variants.THUNDERSTEEL.ordinal))
            if (OreDictionary.doesOreNameExist("nuggetIron"))
                SpellRegistry.registerSpellRecipe("nuggetIron", spell, of(THUNDERNUGGET))
        }

        spell = SpellRegistry.getSpell(LibNames.SPELL_IDUNN_INFUSION)
        if (spell != null) SpellRegistry.registerSpellRecipe(BotaniaOreDict.LIVING_WOOD, spell, of(LIFE_ROOT), of(LIFE_ROOT, true))

        spell = SpellRegistry.getSpell(LibNames.SPELL_RAINBOW)
        if (spell != null)
            for ((index, value) in LibOreDict.DYES.withIndex())
                SpellRegistry.registerSpellRecipe(value, spell, ItemStack(ModItems.iridescentDye, 1, index), ItemStack(ModItems.awakenedDye, 1, index))

        spell = SpellRegistry.getSpell(LibNames.SPELL_LOKI_INFUSION)
        if (spell != null)
            SpellRegistry.registerSpellRecipe("coal", spell, of(HEARTHSTONE), of(HEARTHSTONE, true))


        SaplingVariantRegistry.registerRecipe(ItemStack(ModBlocks.sealSapling), ItemStack.EMPTY, ItemStack(ModBlocks.sealLog), ItemStack(ModBlocks.sealLeaves))
        SaplingVariantRegistry.registerRecipe(ItemStack(ModBlocks.thunderSapling), ItemStack.EMPTY, ItemStack(ModBlocks.thunderLog), ItemStack(ModBlocks.thunderLeaves))
        SaplingVariantRegistry.registerRecipe(ItemStack(ModBlocks.circuitSapling), ItemStack.EMPTY, ItemStack(ModBlocks.circuitLog), ItemStack(ModBlocks.circuitLeaves))
        SaplingVariantRegistry.registerRecipe(ItemStack(ModBlocks.calicoSapling), ItemStack.EMPTY, ItemStack(ModBlocks.calicoLog), ItemStack(ModBlocks.calicoLeaves))

        SaplingVariantRegistry.registerRecipe(ItemStack(Blocks.SAPLING, 1, BlockPlanks.EnumType.OAK.metadata), ItemStack.EMPTY,
                ItemStack(Blocks.LOG, 1, BlockPlanks.EnumType.OAK.metadata), ItemStack(Blocks.LEAVES, 1, BlockPlanks.EnumType.OAK.metadata))
        SaplingVariantRegistry.registerRecipe(ItemStack(Blocks.SAPLING, 1, BlockPlanks.EnumType.SPRUCE.metadata), ItemStack.EMPTY,
                ItemStack(Blocks.LOG, 1, BlockPlanks.EnumType.SPRUCE.metadata), ItemStack(Blocks.LEAVES, 1, BlockPlanks.EnumType.SPRUCE.metadata))
        SaplingVariantRegistry.registerRecipe(ItemStack(Blocks.SAPLING, 1, BlockPlanks.EnumType.BIRCH.metadata), ItemStack.EMPTY,
                ItemStack(Blocks.LOG, 1, BlockPlanks.EnumType.BIRCH.metadata), ItemStack(Blocks.LEAVES, 1, BlockPlanks.EnumType.BIRCH.metadata))
        SaplingVariantRegistry.registerRecipe(ItemStack(Blocks.SAPLING, 1, BlockPlanks.EnumType.JUNGLE.metadata), ItemStack.EMPTY,
                ItemStack(Blocks.LOG, 1, BlockPlanks.EnumType.JUNGLE.metadata), ItemStack(Blocks.LEAVES, 1, BlockPlanks.EnumType.JUNGLE.metadata))

        SaplingVariantRegistry.registerRecipe(ItemStack(Blocks.SAPLING, 1, BlockPlanks.EnumType.ACACIA.metadata), ItemStack.EMPTY,
                ItemStack(Blocks.LOG2, 1, BlockPlanks.EnumType.ACACIA.metadata - 4), ItemStack(Blocks.LEAVES2, 1, BlockPlanks.EnumType.ACACIA.metadata - 4))
        SaplingVariantRegistry.registerRecipe(ItemStack(Blocks.SAPLING, 1, BlockPlanks.EnumType.DARK_OAK.metadata), ItemStack.EMPTY,
                ItemStack(Blocks.LOG2, 1, BlockPlanks.EnumType.DARK_OAK.metadata - 4), ItemStack(Blocks.LEAVES2, 1, BlockPlanks.EnumType.DARK_OAK.metadata - 4))
    }

    fun addOreDictRecipe(name: String, output: Item, vararg recipe: Any) = addOreDictRecipe(name, ItemStack(output), *recipe)
    fun addOreDictRecipe(name: String, output: Block, vararg recipe: Any) = addOreDictRecipe(name, ItemStack(output), *recipe)
    fun addOreDictRecipe(name: String, output: ItemStack, vararg recipe: Any): ResourceLocation {
        RecipeGeneratorHandler.addShapedRecipe(name, currentModId, output, *recipe)
        return ResourceLocation(currentModId, name)
    }

    fun addHiddenOreDictRecipe(name: String, output: Item, vararg recipe: Any) = addHiddenOreDictRecipe(name, ItemStack(output), *recipe)
    fun addHiddenOreDictRecipe(name: String, output: Block, vararg recipe: Any) = addHiddenOreDictRecipe(name, ItemStack(output), *recipe)
    fun addHiddenOreDictRecipe(name: String, output: ItemStack, vararg recipe: Any): ResourceLocation {
        return addHiddenRecipe(ShapedOreRecipe(null, output, *recipe).setRegistryName("${LibMisc.MOD_ID}:$name"))
    }

    fun addHiddenRecipe(recipe: IRecipe): ResourceLocation {
        RegistrationHandler.register(RecipeNoJEI(recipe))
        return recipe.registryName!!
    }

    fun addShapelessOreDictRecipe(name: String, output: Item, vararg recipe: Any) = addShapelessOreDictRecipe(name, ItemStack(output), *recipe)
    fun addShapelessOreDictRecipe(name: String, output: Block, vararg recipe: Any) = addShapelessOreDictRecipe(name, ItemStack(output), *recipe)
    fun addShapelessOreDictRecipe(name: String, output: ItemStack, vararg recipe: Any): ResourceLocation {
        RecipeGeneratorHandler.addShapelessRecipe(name, currentModId, output, *recipe)
        return ResourceLocation(currentModId, name)
    }
}
