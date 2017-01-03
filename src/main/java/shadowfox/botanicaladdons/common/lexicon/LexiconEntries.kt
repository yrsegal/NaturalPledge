package shadowfox.botanicaladdons.common.lexicon

import net.minecraft.block.BlockDirt
import net.minecraft.init.Blocks
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.OreDictionary
import shadowfox.botanicaladdons.common.block.BlockAwakenerCore
import shadowfox.botanicaladdons.common.block.ModBlocks
import shadowfox.botanicaladdons.common.crafting.ModRecipes
import shadowfox.botanicaladdons.common.items.ItemResource
import shadowfox.botanicaladdons.common.items.ItemSpellIcon
import shadowfox.botanicaladdons.common.items.ModItems
import shadowfox.botanicaladdons.common.items.bauble.faith.*
import shadowfox.botanicaladdons.common.lexicon.base.*
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.lexicon.LexiconEntry
import vazkii.botania.api.lexicon.LexiconRecipeMappings
import vazkii.botania.common.core.handler.ConfigHandler
import vazkii.botania.common.lexicon.page.PageBrew
import vazkii.botania.common.lexicon.page.PageCraftingRecipe
import vazkii.botania.common.lexicon.page.PageMultiblock
import vazkii.botania.common.lexicon.page.PageText

/**
 * @author WireSegal
 * Created at 1:15 PM on 4/16/16.
 */
object LexiconEntries {
    val divinity: ModCategory

    val divineBasics: LexiconEntry

    val njord: LexiconEntry
    val idunn: LexiconEntry
    val thor: LexiconEntry
    val heimdall: LexiconEntry

    val njordSpells: LexiconEntry
    val idunnSpells: LexiconEntry
    val thorSpells: LexiconEntry
    val heimdallSpells: LexiconEntry

    val drabBrew: LexiconEntry

    val ironBelt: LexiconEntry

    val toolbelt: LexiconEntry
    val foodbelt: LexiconEntry
    val flowstone: LexiconEntry
    val findstone: LexiconEntry
    val star: LexiconEntry
    val shard: LexiconEntry
    val prism: LexiconEntry
    val irisDirt: LexiconEntry
    val lamp: LexiconEntry
    val crackle: LexiconEntry

    val thunderFist: LexiconEntry

    val sealTree: LexiconEntry
    val amp: LexiconEntry
    val thunderTree: LexiconEntry
    val circuitTree: LexiconEntry
    val calicoTree: LexiconEntry

    val awakening: LexiconEntry
    val consequences: LexiconEntry

    val mjolnir: LexiconEntry
    val apple: LexiconEntry
    val ascension: LexiconEntry
    val fateHorn: LexiconEntry

    val sapling: LexiconEntry

    val funnel: LexiconEntry

    val garbNjord: LexiconEntry
    val garbIdunn: LexiconEntry
    val garbThor: LexiconEntry
    val garbHeimdall: LexiconEntry

    val xpTome: LexiconEntry

    init {
        val topKnowledgeTier = if (ConfigHandler.relicsEnabled) BotaniaAPI.relicKnowledge else BotaniaAPI.elvenKnowledge

        divinity = ModCategory("divinity", 1)

        divineBasics = ModEntry("divinityIntro", divinity, ModItems.symbol).setPriority()
        divineBasics.setLexiconPages(PageText("0"), PageText("1"), PageCraftingRecipe("2", ModRecipes.recipeSymbol),
                PageText("3"), PageCraftingRecipe("4", ModRecipes.recipeTerrestrialFocus),
                PageText("5"), PageCraftingRecipe("6", ModRecipes.recipeMortalStone))

        njord = EntryPriestBarredKnowledge("njord", divinity, ItemFaithBauble.emblemOf(PriestlyEmblemNjord::class.java), PriestlyEmblemNjord::class.java)
        njord.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeNjordEmblem))
        idunn = EntryPriestBarredKnowledge("idunn", divinity, ItemFaithBauble.emblemOf(PriestlyEmblemIdunn::class.java), PriestlyEmblemIdunn::class.java)
        idunn.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeIdunnEmblem))
        thor = EntryPriestBarredKnowledge("thor", divinity, ItemFaithBauble.emblemOf(PriestlyEmblemThor::class.java), PriestlyEmblemThor::class.java)
        thor.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeThorEmblem))
        heimdall = EntryPriestBarredKnowledge("heimdall", divinity, ItemFaithBauble.emblemOf(PriestlyEmblemHeimdall::class.java), PriestlyEmblemHeimdall::class.java)
        heimdall.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeHeimdallEmblem))

        njordSpells = EntryPriestlyKnowledge("njordSpells", divinity, ItemSpellIcon.of(ItemSpellIcon.Variants.INTERDICT), PriestlyEmblemNjord::class.java)
        njordSpells.setLexiconPages(PageText("0"), PageText("1"), PageText("2"), PageText("3"), PageCraftingRecipe("4", ModRecipes.recipeAquaBricks), PageCraftingRecipe("5", ModRecipes.recipeAquaDeconversion), PageCraftingRecipe("6", ModRecipes.recipeAquaGlass), PageCraftingRecipe("7", ModRecipes.recipeAquaPane))
        idunnSpells = EntryPriestlyKnowledge("idunnSpells", divinity, ItemSpellIcon.of(ItemSpellIcon.Variants.LIFEMAKER), PriestlyEmblemIdunn::class.java)
        idunnSpells.setLexiconPages(PageText("0"), PageText("1"))
        thorSpells = EntryPriestlyKnowledge("thorSpells", divinity, ItemSpellIcon.of(ItemSpellIcon.Variants.LIGHTNING), PriestlyEmblemThor::class.java)
        thorSpells.setLexiconPages(PageText("0"), PageText("1"), PageText("2"), PageText("3"), PageText("4"), PageCraftingRecipe("5", ModRecipes.recipeThunderBlock), PageCraftingRecipe("6", ModRecipes.recipeThunderDeconversion))
        heimdallSpells = EntryPriestlyKnowledge("heimdallSpells", divinity, ItemSpellIcon.of(ItemSpellIcon.Variants.IRIDESCENCE), PriestlyEmblemHeimdall::class.java)
        heimdallSpells.setLexiconPages(PageText("0"), PageText("1"))

        garbNjord = EntryPriestlyKnowledge("garbNjord", divinity, ItemStack(ModItems.cloak, 1, 0), PriestlyEmblemNjord::class.java)
        garbNjord.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeNjordCloak))
        garbIdunn = EntryPriestlyKnowledge("garbIdunn", divinity, ItemStack(ModItems.cloak, 1, 1), PriestlyEmblemIdunn::class.java)
        garbIdunn.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeIdunnCloak))
        garbThor = EntryPriestlyKnowledge("garbThor", divinity, ItemStack(ModItems.cloak, 1, 2), PriestlyEmblemThor::class.java)
        garbThor.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeThorCloak))
        garbHeimdall = EntryPriestlyKnowledge("garbHeimdall", divinity, ItemStack(ModItems.cloak, 1, 3), PriestlyEmblemHeimdall::class.java)
        garbHeimdall.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeHeimdallCloak))

        drabBrew = EntryPriestlyKnowledge("drabBrew", divinity, ItemStack(ModItems.iridescentDye, 1, EnumDyeColor.GRAY.metadata), PriestlyEmblemHeimdall::class.java)
        drabBrew.setLexiconPages(PageBrew(ModRecipes.drabBrew, "0a", "0b"))

        ironBelt = EntryPriestlyKnowledge("ironBelt", divinity, ModItems.ironBelt, PriestlyEmblemNjord::class.java)
        ironBelt.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeIronBelt))

        toolbelt = EntryPriestlyKnowledge("toolbelt", divinity, ModItems.toolbelt, PriestlyEmblemHeimdall::class.java)
        toolbelt.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeToolbelt))
        foodbelt = EntryPriestlyKnowledge("foodbelt", divinity, ModItems.foodbelt, PriestlyEmblemHeimdall::class.java)
        foodbelt.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeFoodBelt))
        flowstone = EntryPriestlyKnowledge("travelStone", divinity, ModItems.travelStone, PriestlyEmblemHeimdall::class.java)
        flowstone.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeTravelStone))
        findstone = EntryPriestlyKnowledge("findStone", divinity, ModItems.finder, PriestlyEmblemHeimdall::class.java)
        findstone.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeFindStone), PageText("2"), PageCraftingRecipe("3", ModRecipes.recipeDeathStone))
        star = EntryPriestlyKnowledge("star", divinity, ModBlocks.star, PriestlyEmblemHeimdall::class.java)
        star.setLexiconPages(PageText("0"), PageCraftingRecipe("1", listOf(*ModRecipes.recipesStar)))
        shard = EntryPriestlyKnowledge("shard", divinity, ItemStack(ModItems.manaDye, 1, 16), PriestlyEmblemHeimdall::class.java).setKnowledgeType(BotaniaAPI.elvenKnowledge)
        shard.setLexiconPages(PageText("0"), PageCraftingRecipe("1", listOf(*ModRecipes.recipesIridescentShards)))
        prism = EntryPriestlyKnowledge("prism", divinity, ModItems.lightPlacer, PriestlyEmblemHeimdall::class.java).setKnowledgeType(BotaniaAPI.elvenKnowledge)
        prism.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipePrismRod))
        irisDirt = EntryPriestlyKnowledge("irisDirt", divinity, ModBlocks.rainbowDirt, PriestlyEmblemHeimdall::class.java)
        irisDirt.setLexiconPages(PageText("0"), PageCraftingRecipe("1", listOf(*ModRecipes.recipesDirt)), PageText("2"), PageCraftingRecipe("3", listOf(*ModRecipes.recipesIrisPlanks)))
        lamp = EntryPriestlyKnowledge("lamp", divinity, ModBlocks.irisLamp, PriestlyEmblemHeimdall::class.java)
        lamp.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeIrisLamp))
        crackle = EntryPriestlyKnowledge("crackle", divinity, ModBlocks.cracklingStar, PriestlyEmblemHeimdall::class.java)
        crackle.setLexiconPages(PageText("0"), PageCraftingRecipe("1", listOf(*ModRecipes.recipesCrackleStar)))

        thunderFist = EntryPriestlyKnowledge("fist", divinity, ModItems.fists, PriestlyEmblemThor::class.java)
        thunderFist.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeThunderFists))

        sealTree = EntryPriestlyKnowledge("seal", divinity, ModBlocks.sealSapling, PriestlyEmblemIdunn::class.java)
        sealTree.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeSealSapling), PageCraftingRecipe("2", ModRecipes.recipeSealPlanks))
        amp = EntryPriestlyKnowledge("amp", divinity, ModBlocks.amp, PriestlyEmblemIdunn::class.java)
        amp.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeAmp))
        thunderTree = EntryPriestlyKnowledge("thunder", divinity, ModBlocks.thunderSapling, PriestlyEmblemIdunn::class.java)
        thunderTree.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeThunderSapling), PageCraftingRecipe("2", ModRecipes.recipeThunderPlanks))
        circuitTree = EntryPriestlyKnowledge("circuit", divinity, ModBlocks.circuitSapling, PriestlyEmblemIdunn::class.java)
        circuitTree.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeCircuitSapling), PageCraftingRecipe("2", ModRecipes.recipeCircuitPlanks))
        calicoTree = EntryPriestlyKnowledge("calico", divinity, ModBlocks.calicoSapling, PriestlyEmblemIdunn::class.java)
        calicoTree.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeCalicoSapling), PageCraftingRecipe("2", ModRecipes.recipeCalicoPlanks))

        awakening = EntryPriestlyKnowledge("awakening", divinity, ModBlocks.awakenerCore).setKnowledgeType(topKnowledgeTier).setPriority()
        awakening.setLexiconPages(PageText("0"), PageText("1"), PageMultiblock("2", BlockAwakenerCore.multiblock), PageCraftingRecipe("3", listOf(*ModRecipes.recipesDivineCore)))
        consequences = EntryAwakenedKnowledge("wellshit", divinity, ItemStack(Blocks.DIRT, 1, BlockDirt.DirtType.PODZOL.metadata)).setKnowledgeType(topKnowledgeTier).setPriority()
        consequences.setLexiconPages(PageText("0"))

        mjolnir = EntryAwakenedKnowledge("mjolnir", divinity, ModItems.mjolnir, PriestlyEmblemThor::class.java).setKnowledgeType(topKnowledgeTier)
        mjolnir.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeMjolnir))
        apple = EntryAwakenedKnowledge("apple", divinity, ModItems.apple, PriestlyEmblemIdunn::class.java).setKnowledgeType(topKnowledgeTier)
        apple.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeImmortalApple), PageBrew(ModRecipes.immortalBrew, "2a", "2b"))
        ascension = EntryAwakenedKnowledge("ascension", divinity, ModItems.sealArrow, PriestlyEmblemNjord::class.java).setKnowledgeType(topKnowledgeTier)
        ascension.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeAscensionArrow), PageCraftingRecipe("2", ModRecipes.recipeAscensionDupe))
        fateHorn = EntryAwakenedKnowledge("fateHorn", divinity, ModItems.fateHorn, PriestlyEmblemHeimdall::class.java).setKnowledgeType(topKnowledgeTier)
        fateHorn.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeFateHorn))

        sapling = ModEntry("sapling", BotaniaAPI.categoryMisc, ModBlocks.irisSapling)
        sapling.setLexiconPages(PageText("0"), PageCraftingRecipe("1", listOf(*ModRecipes.recipesAltPlanks)))

        funnel = EntryPriestlyKnowledge("funnel", divinity, ModBlocks.funnel, PriestlyEmblemIdunn::class.java)
        funnel.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeFunnel))

        xpTome = EntryPriestlyKnowledge("xpTome", divinity, ModItems.xpTome, PriestlyEmblemIdunn::class.java).setKnowledgeType(BotaniaAPI.elvenKnowledge)
        xpTome.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeXpTome))

        LexiconRecipeMappings.map(ItemStack(Blocks.DIRT), irisDirt, 0)

        LexiconRecipeMappings.map(ItemStack(ModBlocks.irisSapling), sapling, 0)

        for (i in ModBlocks.altLogs) for (j in i.variants.indices) LexiconRecipeMappings.map(ItemStack(i, 1, j), sapling, 0)
        for (i in ModBlocks.irisLogs) LexiconRecipeMappings.map(ItemStack(i, 1, OreDictionary.WILDCARD_VALUE), irisDirt, 2)
        LexiconRecipeMappings.map(ItemStack(ModBlocks.rainbowLog, 1, OreDictionary.WILDCARD_VALUE), irisDirt, 2)
        LexiconRecipeMappings.map(ItemStack(ModBlocks.sealLog, 1, OreDictionary.WILDCARD_VALUE), sealTree, 0)
        LexiconRecipeMappings.map(ItemStack(ModBlocks.thunderLog, 1, OreDictionary.WILDCARD_VALUE), thunderTree, 0)
        LexiconRecipeMappings.map(ItemStack(ModBlocks.circuitLog, 1, OreDictionary.WILDCARD_VALUE), circuitTree, 0)
        LexiconRecipeMappings.map(ItemStack(ModBlocks.calicoLog, 1, OreDictionary.WILDCARD_VALUE), calicoTree, 0)

        for (i in ModBlocks.altLeaves) for (j in i.variants.indices) LexiconRecipeMappings.map(ItemStack(i, 1, j), sapling, 0)
        for (i in ModBlocks.irisLeaves) LexiconRecipeMappings.map(ItemStack(i, 1, OreDictionary.WILDCARD_VALUE), irisDirt, 2)
        LexiconRecipeMappings.map(ItemStack(ModBlocks.rainbowLeaves, 1, OreDictionary.WILDCARD_VALUE), irisDirt, 2)
        LexiconRecipeMappings.map(ItemStack(ModBlocks.sealLeaves, 1, OreDictionary.WILDCARD_VALUE), sealTree, 0)
        LexiconRecipeMappings.map(ItemStack(ModBlocks.thunderLeaves, 1, OreDictionary.WILDCARD_VALUE), thunderTree, 0)
        LexiconRecipeMappings.map(ItemStack(ModBlocks.circuitLeaves, 1, OreDictionary.WILDCARD_VALUE), circuitTree, 0)
        LexiconRecipeMappings.map(ItemStack(ModBlocks.calicoLeaves, 1, OreDictionary.WILDCARD_VALUE), calicoTree, 0)

        for (i in 0..16) LexiconRecipeMappings.map(ItemStack(ModItems.iridescentDye, 1, i), heimdallSpells, 0)
        for (i in 0..16) LexiconRecipeMappings.map(ItemStack(ModItems.awakenedDye, 1, i), heimdallSpells, 0)

        LexiconRecipeMappings.map(ItemResource.of(ItemResource.Variants.THUNDER_STEEL), thorSpells, 4)
        LexiconRecipeMappings.map(ItemResource.of(ItemResource.Variants.THUNDER_STEEL, true), thorSpells, 4)

        LexiconRecipeMappings.map(ItemResource.of(ItemResource.Variants.AQUAMARINE), njordSpells, 3)
        LexiconRecipeMappings.map(ItemResource.of(ItemResource.Variants.AQUAMARINE, true), njordSpells, 3)

        LexiconRecipeMappings.map(ItemResource.of(ItemResource.Variants.LIFE_ROOT), idunnSpells, 1)
        LexiconRecipeMappings.map(ItemResource.of(ItemResource.Variants.LIFE_ROOT, true), idunnSpells, 1)

    }
}
