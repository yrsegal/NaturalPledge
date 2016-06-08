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

    val toolbelt: LexiconEntry
    val foodbelt: LexiconEntry
    val flowstone: LexiconEntry
    val findstone: LexiconEntry
    val star: LexiconEntry
    val shard: LexiconEntry
    val prism: LexiconEntry
    val irisDirt: LexiconEntry
    val lamp: LexiconEntry

    val thunderFist: LexiconEntry

    val sealTree: LexiconEntry
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

    init {
        val topKnowledgeTier = if (ConfigHandler.relicsEnabled) BotaniaAPI.relicKnowledge else BotaniaAPI.elvenKnowledge

        divinity = ModCategory("divinity", 1)

        divineBasics = ModEntry("divinityIntro", divinity, ModItems.symbol).setPriority()
        divineBasics.setLexiconPages(PageText("0"), PageText("1"), PageCraftingRecipe("2", ModRecipes.recipeSymbol),
                PageText("3"), PageCraftingRecipe("4", ModRecipes.recipeTerrestrialFocus),
                PageText("5"), PageCraftingRecipe("6", ModRecipes.recipeMortalStone))

        njord = EntryPriestBarredKnowledge("njord", divinity, ItemFaithBauble.emblemOf(PriestlyEmblemNjord::class.java), PriestlyEmblemNjord::class.java).setKnowledgeType(BotaniaAPI.elvenKnowledge)
        njord.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeNjordEmblem))
        idunn = EntryPriestBarredKnowledge("idunn", divinity, ItemFaithBauble.emblemOf(PriestlyEmblemIdunn::class.java), PriestlyEmblemIdunn::class.java)
        idunn.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeIdunnEmblem))
        thor = EntryPriestBarredKnowledge("thor", divinity, ItemFaithBauble.emblemOf(PriestlyEmblemThor::class.java), PriestlyEmblemThor::class.java).setKnowledgeType(BotaniaAPI.elvenKnowledge)
        thor.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeThorEmblem))
        heimdall = EntryPriestBarredKnowledge("heimdall", divinity, ItemFaithBauble.emblemOf(PriestlyEmblemHeimdall::class.java), PriestlyEmblemHeimdall::class.java).setKnowledgeType(BotaniaAPI.elvenKnowledge)
        heimdall.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeHeimdallEmblem))

        njordSpells = EntryPriestlyKnowledge("njordSpells", divinity, ItemSpellIcon.of(ItemSpellIcon.Variants.INTERDICT), PriestlyEmblemNjord::class.java).setKnowledgeType(BotaniaAPI.elvenKnowledge)
        njordSpells.setLexiconPages(PageText("0"), PageText("1"), PageText("2"), PageText("3"), PageCraftingRecipe("4", ModRecipes.recipeAquaBricks))
        idunnSpells = EntryPriestlyKnowledge("idunnSpells", divinity, ItemSpellIcon.of(ItemSpellIcon.Variants.LIFEMAKER), PriestlyEmblemIdunn::class.java)
        idunnSpells.setLexiconPages(PageText("0"), PageText("1"))
        thorSpells = EntryPriestlyKnowledge("thorSpells", divinity, ItemSpellIcon.of(ItemSpellIcon.Variants.LIGHTNING), PriestlyEmblemThor::class.java).setKnowledgeType(BotaniaAPI.elvenKnowledge)
        thorSpells.setLexiconPages(PageText("0"), PageText("1"), PageText("2"), PageText("3"), PageCraftingRecipe("4", ModRecipes.recipeThunderBlock))
        heimdallSpells = EntryPriestlyKnowledge("heimdallSpells", divinity, ItemSpellIcon.of(ItemSpellIcon.Variants.IRIDESCENCE), PriestlyEmblemHeimdall::class.java).setKnowledgeType(BotaniaAPI.elvenKnowledge)
        heimdallSpells.setLexiconPages(PageText("0"), PageText("1"))

        drabBrew = EntryPriestlyKnowledge("drabBrew", divinity, ItemStack(ModItems.iridescentDye, 1, EnumDyeColor.GRAY.metadata), PriestlyEmblemHeimdall::class.java).setKnowledgeType(BotaniaAPI.elvenKnowledge)
        drabBrew.setLexiconPages(PageBrew(ModRecipes.drabBrew, "0a", "0b"))

        toolbelt = EntryPriestlyKnowledge("toolbelt", divinity, ModItems.toolbelt, PriestlyEmblemHeimdall::class.java).setKnowledgeType(BotaniaAPI.elvenKnowledge)
        toolbelt.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeToolbelt))
        foodbelt = EntryPriestlyKnowledge("foodbelt", divinity, ModItems.foodbelt, PriestlyEmblemHeimdall::class.java).setKnowledgeType(BotaniaAPI.elvenKnowledge)
        foodbelt.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeFoodBelt))
        flowstone = EntryPriestlyKnowledge("travelStone", divinity, ModItems.travelStone, PriestlyEmblemHeimdall::class.java).setKnowledgeType(BotaniaAPI.elvenKnowledge)
        flowstone.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeTravelStone))
        findstone = EntryPriestlyKnowledge("findStone", divinity, ModItems.finder, PriestlyEmblemHeimdall::class.java).setKnowledgeType(BotaniaAPI.elvenKnowledge)
        findstone.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeFindStone))
        star = EntryPriestlyKnowledge("star", divinity, ModBlocks.star, PriestlyEmblemHeimdall::class.java).setKnowledgeType(BotaniaAPI.elvenKnowledge)
        star.setLexiconPages(PageText("0"), PageCraftingRecipe("1", listOf(*ModRecipes.recipesStar)))
        shard = EntryPriestlyKnowledge("shard", divinity, ItemStack(ModItems.manaDye, 1, 16), PriestlyEmblemHeimdall::class.java).setKnowledgeType(BotaniaAPI.elvenKnowledge)
        shard.setLexiconPages(PageText("0"), PageCraftingRecipe("1", listOf(*ModRecipes.recipesIridescentShards)))
        prism = EntryPriestlyKnowledge("prism", divinity, ModItems.lightPlacer, PriestlyEmblemHeimdall::class.java).setKnowledgeType(BotaniaAPI.elvenKnowledge)
        prism.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipePrismRod))
        irisDirt = EntryPriestlyKnowledge("irisDirt", divinity, ModBlocks.rainbowDirt, PriestlyEmblemHeimdall::class.java).setKnowledgeType(BotaniaAPI.elvenKnowledge)
        irisDirt.setLexiconPages(PageText("0"), PageCraftingRecipe("1", listOf(*ModRecipes.recipesDirt)), PageText("2"), PageCraftingRecipe("3", listOf(*ModRecipes.recipesIrisPlanks)))
        lamp = EntryPriestlyKnowledge("lamp", divinity, ModBlocks.irisLamp, PriestlyEmblemHeimdall::class.java).setKnowledgeType(BotaniaAPI.elvenKnowledge)
        lamp.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeIrisLamp))

        thunderFist = EntryPriestlyKnowledge("fist", divinity, ModItems.fists, PriestlyEmblemThor::class.java).setKnowledgeType(BotaniaAPI.elvenKnowledge)
        thunderFist.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeThunderFists))

        sealTree = EntryPriestlyKnowledge("seal", divinity, ModBlocks.sealSapling, PriestlyEmblemIdunn::class.java)
        sealTree.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeSealSapling), PageCraftingRecipe("2", ModRecipes.recipeSealPlanks))
        thunderTree = EntryPriestlyKnowledge("thunder", divinity, ModBlocks.thunderSapling, PriestlyEmblemIdunn::class.java)
        thunderTree.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeThunderSapling), PageCraftingRecipe("2", ModRecipes.recipeThunderPlanks))
        circuitTree = EntryPriestlyKnowledge("circuit", divinity, ModBlocks.circuitSapling, PriestlyEmblemIdunn::class.java)
        circuitTree.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeCircuitSapling), PageCraftingRecipe("2", ModRecipes.recipeCircuitPlanks))
        calicoTree = EntryPriestlyKnowledge("calico", divinity, ModBlocks.calicoSapling, PriestlyEmblemIdunn::class.java)
        calicoTree.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeCalicoSapling), PageCraftingRecipe("2", ModRecipes.recipeCalicoPlanks))

        awakening = EntryPriestlyKnowledge("awakening", divinity, ModBlocks.awakenerCore).setKnowledgeType(topKnowledgeTier).setPriority()
        awakening.setLexiconPages(PageText("0"), PageText("1"), PageMultiblock("2", BlockAwakenerCore.multiblock), PageCraftingRecipe("3", ModRecipes.recipeDivineCore))
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

        LexiconRecipeMappings.map(ItemStack(Blocks.DIRT), irisDirt, 0)

        for (i in ModBlocks.altLogs) LexiconRecipeMappings.map(ItemStack(i, 1, OreDictionary.WILDCARD_VALUE), sapling, 0)
        for (i in ModBlocks.irisLogs) LexiconRecipeMappings.map(ItemStack(i, 1, OreDictionary.WILDCARD_VALUE), irisDirt, 2)
        LexiconRecipeMappings.map(ItemStack(ModBlocks.rainbowLog, 1, OreDictionary.WILDCARD_VALUE), irisDirt, 2)
        LexiconRecipeMappings.map(ItemStack(ModBlocks.sealLog, 1, OreDictionary.WILDCARD_VALUE), sealTree, 0)
        LexiconRecipeMappings.map(ItemStack(ModBlocks.thunderLog, 1, OreDictionary.WILDCARD_VALUE), thunderTree, 0)
        LexiconRecipeMappings.map(ItemStack(ModBlocks.circuitLog, 1, OreDictionary.WILDCARD_VALUE), circuitTree, 0)
        LexiconRecipeMappings.map(ItemStack(ModBlocks.calicoLog, 1, OreDictionary.WILDCARD_VALUE), calicoTree, 0)

        for (i in ModBlocks.altLeaves) LexiconRecipeMappings.map(ItemStack(i, 1, OreDictionary.WILDCARD_VALUE), sapling, 0)
        for (i in ModBlocks.irisLeaves) LexiconRecipeMappings.map(ItemStack(i, 1, OreDictionary.WILDCARD_VALUE), irisDirt, 2)
        LexiconRecipeMappings.map(ItemStack(ModBlocks.rainbowLeaves, 1, OreDictionary.WILDCARD_VALUE), irisDirt, 2)
        LexiconRecipeMappings.map(ItemStack(ModBlocks.sealLeaves, 1, OreDictionary.WILDCARD_VALUE), sealTree, 0)
        LexiconRecipeMappings.map(ItemStack(ModBlocks.thunderLeaves, 1, OreDictionary.WILDCARD_VALUE), thunderTree, 0)
        LexiconRecipeMappings.map(ItemStack(ModBlocks.circuitLeaves, 1, OreDictionary.WILDCARD_VALUE), circuitTree, 0)
        LexiconRecipeMappings.map(ItemStack(ModBlocks.calicoLeaves, 1, OreDictionary.WILDCARD_VALUE), calicoTree, 0)

        LexiconRecipeMappings.map(ItemStack(ModItems.iridescentDye, 1, OreDictionary.WILDCARD_VALUE), heimdallSpells, 0)
        LexiconRecipeMappings.map(ItemStack(ModItems.awakenedDye, 1, OreDictionary.WILDCARD_VALUE), heimdallSpells, 0)

        LexiconRecipeMappings.map(ItemResource.of(ItemResource.Variants.THUNDER_STEEL), thorSpells, 3)
        LexiconRecipeMappings.map(ItemResource.of(ItemResource.Variants.THUNDER_STEEL, true), thorSpells, 3)

        LexiconRecipeMappings.map(ItemResource.of(ItemResource.Variants.AQUAMARINE), njordSpells, 3)
        LexiconRecipeMappings.map(ItemResource.of(ItemResource.Variants.AQUAMARINE, true), njordSpells, 3)

        LexiconRecipeMappings.map(ItemResource.of(ItemResource.Variants.LIFE_ROOT), idunnSpells, 3)
        LexiconRecipeMappings.map(ItemResource.of(ItemResource.Variants.LIFE_ROOT, true), idunnSpells, 3)

    }
}
