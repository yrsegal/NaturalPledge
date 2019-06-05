package com.wiresegal.naturalpledge.common.lexicon

import net.minecraft.block.BlockDirt
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.ItemStack
import net.minecraft.util.text.TextFormatting
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.oredict.OreDictionary
import com.wiresegal.naturalpledge.common.NaturalPledge
import com.wiresegal.naturalpledge.common.block.BlockAwakenerCore
import com.wiresegal.naturalpledge.common.block.ModBlocks
import com.wiresegal.naturalpledge.common.crafting.ModRecipes
import com.wiresegal.naturalpledge.common.items.ItemResource
import com.wiresegal.naturalpledge.common.items.ItemSpellIcon
import com.wiresegal.naturalpledge.common.items.ModItems
import com.wiresegal.naturalpledge.common.items.bauble.faith.*
import com.wiresegal.naturalpledge.common.lexicon.base.EntryAwakenedKnowledge
import com.wiresegal.naturalpledge.common.lexicon.base.EntryPriestlyKnowledge
import com.wiresegal.naturalpledge.common.lexicon.base.ModCategory
import com.wiresegal.naturalpledge.common.lexicon.base.ModEntry
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.BotaniaAPI.registerKnowledgeType
import vazkii.botania.api.lexicon.ILexicon
import vazkii.botania.api.lexicon.KnowledgeType
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
    val forbidden: KnowledgeType
    val divinity: ModCategory

    val divineBasics: LexiconEntry

    val aurora: LexiconEntry

    val njord: LexiconEntry
    val idunn: LexiconEntry
    val thor: LexiconEntry
    val heimdall: LexiconEntry
    val loki: LexiconEntry

    val njordSpells: LexiconEntry
    val idunnSpells: LexiconEntry
    val thorSpells: LexiconEntry
    val heimdallSpells: LexiconEntry
    val lokiSpells: LexiconEntry

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
    val perdition: LexiconEntry

    val sapling: LexiconEntry

    val funnel: LexiconEntry

    val garbNjord: LexiconEntry
    val garbIdunn: LexiconEntry
    val garbThor: LexiconEntry
    val garbHeimdall: LexiconEntry
    val garbLoki: LexiconEntry

    val xpTome: LexiconEntry

    val corporeaRecall: LexiconEntry
    val enderActuator: LexiconEntry

    val sleepStone: LexiconEntry
    val netherStone: LexiconEntry
    val polyStone: LexiconEntry

    val traps: LexiconEntry

    val ragnarok: LexiconEntry
    val faithInRagnarok: LexiconEntry
    val eclipse: LexiconEntry
    val sunmaker: LexiconEntry
    val fenris: LexiconEntry

    init {
        MinecraftForge.EVENT_BUS.register(this)

        val topKnowledgeTier = if (ConfigHandler.relicsEnabled) BotaniaAPI.relicKnowledge else BotaniaAPI.elvenKnowledge
        forbidden = registerKnowledgeType("forbidden", TextFormatting.DARK_RED, false)

        EntryPriestlyKnowledge.ACHIEVEMENT_MAP[PriestlyEmblemNjord::class.java] = { it.definitelyHasAchievement("create_aqua") }
        EntryPriestlyKnowledge.ACHIEVEMENT_MAP[PriestlyEmblemIdunn::class.java] = { it.definitelyHasAchievement("create_life") }
        EntryPriestlyKnowledge.ACHIEVEMENT_MAP[PriestlyEmblemThor::class.java] = { it.definitelyHasAchievement("create_thunder") }
        EntryPriestlyKnowledge.ACHIEVEMENT_MAP[PriestlyEmblemHeimdall::class.java] = { it.definitelyHasAchievement("iridescence") }
        EntryPriestlyKnowledge.ACHIEVEMENT_MAP[PriestlyEmblemLoki::class.java] = { it.definitelyHasAchievement("create_fire") }
        EntryPriestlyKnowledge.ACHIEVEMENT_MAP[ItemRagnarokPendant.Ragnarok::class.java] = { it.definitelyHasAchievement("ragnarok") }

        EntryAwakenedKnowledge.ACHIEVEMENT_MAP[PriestlyEmblemNjord::class.java] = { it.definitelyHasAchievement("sacred_aqua") }
        EntryAwakenedKnowledge.ACHIEVEMENT_MAP[PriestlyEmblemIdunn::class.java] = { it.definitelyHasAchievement("sacred_life") }
        EntryAwakenedKnowledge.ACHIEVEMENT_MAP[PriestlyEmblemThor::class.java] = { it.definitelyHasAchievement("sacred_thunder") }
        EntryAwakenedKnowledge.ACHIEVEMENT_MAP[PriestlyEmblemHeimdall::class.java] = { it.definitelyHasAchievement("sacred_horn") }
        EntryAwakenedKnowledge.ACHIEVEMENT_MAP[PriestlyEmblemLoki::class.java] = { it.definitelyHasAchievement("create_fire") }
        EntryAwakenedKnowledge.ACHIEVEMENT_MAP[ItemRagnarokPendant.Ragnarok::class.java] = { it.definitelyHasAchievement("begin_ragnarok") }

        divinity = ModCategory("divinity", 1)

        divineBasics = ModEntry("divinityIntro", divinity, ModItems.symbol).setPriority()
        divineBasics.setLexiconPages(PageText("0"), PageText("1"), PageCraftingRecipe("2", ModRecipes.recipeSymbol),
                PageText("3"), PageCraftingRecipe("4", ModRecipes.recipeTerrestrialFocus),
                PageText("5"), PageCraftingRecipe("6", ModRecipes.recipeMortalStone))

        njord = ModEntry("njord", divinity, ItemFaithBauble.emblemOf(PriestlyEmblemNjord::class.java))
        njord.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeNjordEmblem))
        idunn = ModEntry("idunn", divinity, ItemFaithBauble.emblemOf(PriestlyEmblemIdunn::class.java))
        idunn.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeIdunnEmblem))
        thor = ModEntry("thor", divinity, ItemFaithBauble.emblemOf(PriestlyEmblemThor::class.java))
        thor.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeThorEmblem))
        heimdall = ModEntry("heimdall", divinity, ItemFaithBauble.emblemOf(PriestlyEmblemHeimdall::class.java)).setKnowledgeType(BotaniaAPI.elvenKnowledge)
        heimdall.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeHeimdallEmblem))
        loki = ModEntry("loki", divinity, ItemFaithBauble.emblemOf(PriestlyEmblemLoki::class.java))
        loki.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeLokiEmblem))

        njordSpells = EntryPriestlyKnowledge("njordSpells", divinity, ItemSpellIcon.of(ItemSpellIcon.Variants.INTERDICT), PriestlyEmblemNjord::class.java)
        njordSpells.setLexiconPages(PageText("0"), PageText("1"), PageText("2"), PageText("3"), PageCraftingRecipe("4", ModRecipes.recipeAquaBricks), PageCraftingRecipe("5", ModRecipes.recipeAquaDeconversion), PageCraftingRecipe("6", ModRecipes.recipeAquaGlass), PageCraftingRecipe("7", ModRecipes.recipeAquaPane))
        idunnSpells = EntryPriestlyKnowledge("idunnSpells", divinity, ItemSpellIcon.of(ItemSpellIcon.Variants.LIFEMAKER), PriestlyEmblemIdunn::class.java)
        idunnSpells.setLexiconPages(PageText("0"), PageText("1"))
        thorSpells = EntryPriestlyKnowledge("thorSpells", divinity, ItemSpellIcon.of(ItemSpellIcon.Variants.LIGHTNING), PriestlyEmblemThor::class.java)
        thorSpells.setLexiconPages(PageText("0"), PageText("1"), PageText("2"), PageText("3"), PageText("4"), PageCraftingRecipe("5", ModRecipes.recipeThunderBlock), PageCraftingRecipe("6", ModRecipes.recipeThunderDeconversion))
        heimdallSpells = EntryPriestlyKnowledge("heimdallSpells", divinity, ItemSpellIcon.of(ItemSpellIcon.Variants.IRIDESCENCE), PriestlyEmblemHeimdall::class.java).setKnowledgeType(BotaniaAPI.elvenKnowledge)
        heimdallSpells.setLexiconPages(PageText("0"), PageText("1"))
        lokiSpells = EntryPriestlyKnowledge("lokiSpells", divinity, ItemSpellIcon.of(ItemSpellIcon.Variants.FIRE_INFUSION), PriestlyEmblemLoki::class.java)
        lokiSpells.setLexiconPages(PageText("0"), PageText("1"), PageText("2"), PageText("3"))

        garbNjord = EntryPriestlyKnowledge("garbNjord", divinity, ItemStack(ModItems.cloak, 1, 0), PriestlyEmblemNjord::class.java)
        garbNjord.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeNjordCloak))
        garbIdunn = EntryPriestlyKnowledge("garbIdunn", divinity, ItemStack(ModItems.cloak, 1, 1), PriestlyEmblemIdunn::class.java)
        garbIdunn.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeIdunnCloak))
        garbThor = EntryPriestlyKnowledge("garbThor", divinity, ItemStack(ModItems.cloak, 1, 2), PriestlyEmblemThor::class.java)
        garbThor.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeThorCloak))
        garbHeimdall = EntryPriestlyKnowledge("garbHeimdall", divinity, ItemStack(ModItems.cloak, 1, 3), PriestlyEmblemHeimdall::class.java).setKnowledgeType(BotaniaAPI.elvenKnowledge)
        garbHeimdall.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeHeimdallCloak))
        garbLoki = EntryPriestlyKnowledge("garbLoki", divinity, ItemStack(ModItems.cloak, 1, 4), PriestlyEmblemLoki::class.java)
        garbLoki.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeLokiCloak))

        drabBrew = EntryPriestlyKnowledge("drabBrew", divinity, ItemStack(ModItems.iridescentDye, 1, EnumDyeColor.GRAY.metadata), PriestlyEmblemHeimdall::class.java).setKnowledgeType(BotaniaAPI.elvenKnowledge)
        drabBrew.setLexiconPages(PageBrew(ModRecipes.drabBrew, "0a", "0b"))

        ironBelt = EntryPriestlyKnowledge("ironBelt", divinity, ModItems.ironBelt, PriestlyEmblemNjord::class.java)
        ironBelt.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeIronBelt))

        toolbelt = EntryPriestlyKnowledge("toolbelt", divinity, ModItems.toolbelt, PriestlyEmblemHeimdall::class.java).setKnowledgeType(BotaniaAPI.elvenKnowledge)
        toolbelt.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeToolbelt))
        foodbelt = EntryPriestlyKnowledge("foodbelt", divinity, ModItems.foodbelt, PriestlyEmblemHeimdall::class.java).setKnowledgeType(BotaniaAPI.elvenKnowledge)
        foodbelt.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeFoodBelt))
        flowstone = EntryPriestlyKnowledge("travelStone", divinity, ModItems.travelStone, PriestlyEmblemHeimdall::class.java).setKnowledgeType(BotaniaAPI.elvenKnowledge)
        flowstone.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeTravelStone))
        findstone = EntryPriestlyKnowledge("findStone", divinity, ModItems.finder, PriestlyEmblemHeimdall::class.java).setKnowledgeType(BotaniaAPI.elvenKnowledge)
        findstone.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeFindStone), PageText("2"), PageCraftingRecipe("3", ModRecipes.recipeDeathStone))
        star = EntryPriestlyKnowledge("star", divinity, ModBlocks.star, PriestlyEmblemHeimdall::class.java).setKnowledgeType(BotaniaAPI.elvenKnowledge)
        star.setLexiconPages(PageText("0"), PageCraftingRecipe("1", listOf(*ModRecipes.recipesStar)))
        shard = EntryPriestlyKnowledge("shard", divinity, ItemStack(ModItems.manaDye, 1, 16), PriestlyEmblemHeimdall::class.java).setKnowledgeType(BotaniaAPI.elvenKnowledge)
        shard.setLexiconPages(PageText("0"), PageCraftingRecipe("1", listOf(*ModRecipes.recipesIridescentShards)))
        prism = EntryPriestlyKnowledge("prism", divinity, ModItems.lightPlacer, PriestlyEmblemHeimdall::class.java).setKnowledgeType(BotaniaAPI.elvenKnowledge)
        prism.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipePrismRod))
        irisDirt = EntryPriestlyKnowledge("irisDirt", divinity, ModBlocks.rainbowDirt, PriestlyEmblemHeimdall::class.java).setKnowledgeType(BotaniaAPI.elvenKnowledge)
        irisDirt.setLexiconPages(PageText("0"), PageCraftingRecipe("1", listOf(*ModRecipes.recipesDirt)), PageText("2"), PageCraftingRecipe("3", listOf(*ModRecipes.recipesIrisPlanks)))
        lamp = EntryPriestlyKnowledge("lamp", divinity, ModBlocks.irisLamp, PriestlyEmblemHeimdall::class.java)
        lamp.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeIrisLamp))
        crackle = EntryPriestlyKnowledge("crackle", divinity, ModBlocks.cracklingStar, PriestlyEmblemHeimdall::class.java).setKnowledgeType(BotaniaAPI.elvenKnowledge)
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
        perdition = EntryAwakenedKnowledge("perdition", divinity, ModItems.perditionFist, PriestlyEmblemLoki::class.java).setKnowledgeType(topKnowledgeTier)
        perdition.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipePerditionFist))


        aurora = EntryPriestlyKnowledge("aurora", BotaniaAPI.categoryMisc, ModBlocks.auroraDirt)
        aurora.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeAuroraDirt), PageCraftingRecipe("2", ModRecipes.recipeAuroraPlanks))

        sapling = ModEntry("sapling", BotaniaAPI.categoryMisc, ModBlocks.irisSapling)
        sapling.setLexiconPages(PageText("0"), PageCraftingRecipe("1", listOf(*ModRecipes.recipesAltPlanks)))

        funnel = EntryPriestlyKnowledge("funnel", divinity, ModBlocks.funnel, PriestlyEmblemIdunn::class.java)
        funnel.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeFunnel))

        xpTome = EntryPriestlyKnowledge("xpTome", divinity, ModItems.xpTome, PriestlyEmblemIdunn::class.java).setKnowledgeType(BotaniaAPI.elvenKnowledge)
        xpTome.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeXpTome))


        corporeaRecall = EntryPriestlyKnowledge("corporeaRecall", BotaniaAPI.categoryEnder, ModBlocks.corporeaResonator, PriestlyEmblemHeimdall::class.java).setKnowledgeType(BotaniaAPI.elvenKnowledge)
        corporeaRecall.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeCorporeaResonator), PageCraftingRecipe("2", ModRecipes.recipeRecallStone))

        enderActuator = EntryPriestlyKnowledge("enderActuator", BotaniaAPI.categoryEnder, ModBlocks.enderActuator, PriestlyEmblemHeimdall::class.java).setKnowledgeType(BotaniaAPI.elvenKnowledge)
        enderActuator.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeEnderActuator))

        sleepStone = EntryPriestlyKnowledge("sleepStone", divinity, ModItems.sleepStone, PriestlyEmblemHeimdall::class.java).setKnowledgeType(BotaniaAPI.elvenKnowledge)
        sleepStone.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeSleepStone))

        netherStone = EntryPriestlyKnowledge("netherStone", divinity, ModItems.portalStone, PriestlyEmblemHeimdall::class.java).setKnowledgeType(BotaniaAPI.elvenKnowledge)
        netherStone.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeNetherStone))

        polyStone = EntryPriestlyKnowledge("polyStone", divinity, ModItems.polyStone, PriestlyEmblemHeimdall::class.java).setKnowledgeType(BotaniaAPI.elvenKnowledge)
        polyStone.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipePolyStone))

        traps = EntryPriestlyKnowledge("traps", divinity, ModBlocks.disorientTrap, PriestlyEmblemLoki::class.java)
        traps.setLexiconPages(PageText("0"),
                PageText("1"), PageCraftingRecipe("2", ModRecipes.recipeDisorientationTrap),
                PageText("3"), PageCraftingRecipe("4", ModRecipes.recipeInfernoTrap),
                PageText("5"), PageCraftingRecipe("6", ModRecipes.recipeLaunchTrap),
                PageText("7"), PageCraftingRecipe("8", ModRecipes.recipeRootTrap),
                PageText("9"), PageCraftingRecipe("10", ModRecipes.recipeSandTrap),
                PageText("11"), PageCraftingRecipe("12", ModRecipes.recipeSignalTrap),
                PageText("13"), PageCraftingRecipe("14", ModRecipes.recipeWrathTrap))

        ragnarok = EntryPriestlyKnowledge("ragnarok", divinity, ItemSpellIcon.of(ItemSpellIcon.Variants.SOUL_MANIFESTATION), ItemRagnarokPendant.Ragnarok::class.java).setKnowledgeType(forbidden).setPriority()
        ragnarok.setLexiconPages(PageText("0"), PageText("1"))

        faithInRagnarok = EntryPriestlyKnowledge("faithInRagnarok", divinity, ModItems.ragnarok, ItemRagnarokPendant.Ragnarok::class.java).setKnowledgeType(forbidden).setPriority()
        faithInRagnarok.setLexiconPages(PageText("0"), PageText("1"))

        eclipse = EntryPriestlyKnowledge("eclipse", divinity, ModItems.flarebringer, ItemRagnarokPendant.Ragnarok::class.java).setKnowledgeType(forbidden)
        eclipse.setLexiconPages(PageText("0"),
                PageCraftingRecipe("1", ModRecipes.recipeEclipseWeapon),
                PageCraftingRecipe("2", ModRecipes.recipeEclipseHelm),
                PageCraftingRecipe("3", ModRecipes.recipeEclipseChest),
                PageCraftingRecipe("4", ModRecipes.recipeEclipseLegs),
                PageCraftingRecipe("5", ModRecipes.recipeEclipseBoots))
        sunmaker = EntryPriestlyKnowledge("sunmaker", divinity, ModItems.shadowbreaker, ItemRagnarokPendant.Ragnarok::class.java).setKnowledgeType(forbidden)
        sunmaker.setLexiconPages(PageText("0"),
                PageCraftingRecipe("1", ModRecipes.recipeSunmakerWeapon),
                PageCraftingRecipe("2", ModRecipes.recipeSunmakerHelm),
                PageCraftingRecipe("3", ModRecipes.recipeSunmakerChest),
                PageCraftingRecipe("4", ModRecipes.recipeSunmakerLegs),
                PageCraftingRecipe("5", ModRecipes.recipeSunmakerBoots))
        fenris = EntryAwakenedKnowledge("fenris", divinity, ModItems.nightscourge, ItemRagnarokPendant.Ragnarok::class.java).setKnowledgeType(forbidden)
        fenris.setLexiconPages(PageText("0"),
                PageCraftingRecipe("1", ModRecipes.recipeFenrisWeapon),
                PageCraftingRecipe("2", ModRecipes.recipeFenrisHelm),
                PageCraftingRecipe("3", ModRecipes.recipeFenrisChest),
                PageCraftingRecipe("4", ModRecipes.recipeFenrisLegs),
                PageCraftingRecipe("5", ModRecipes.recipeFenrisBoots))

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

        LexiconRecipeMappings.map(ItemResource.of(ItemResource.Variants.HEARTHSTONE), lokiSpells, 1)
        LexiconRecipeMappings.map(ItemResource.of(ItemResource.Variants.HEARTHSTONE, true), lokiSpells, 1)

        LexiconRecipeMappings.map(ItemStack(ModItems.ragnarok), ragnarok, 1)
        LexiconRecipeMappings.map(ItemResource.of(ItemResource.Variants.GOD_SOUL), faithInRagnarok, 2)
        LexiconRecipeMappings.map(ItemResource.of(ItemResource.Variants.GOD_SOUL, true), faithInRagnarok, 2)

    }

    @SubscribeEvent fun onItemClick(e: PlayerInteractEvent.RightClickItem) = unlockEntry(e.entityPlayer, e.itemStack)
    @SubscribeEvent fun onItemClick(e: PlayerInteractEvent.RightClickBlock) = unlockEntry(e.entityPlayer, e.itemStack)

    private fun unlockEntry(player: EntityPlayer, stack: ItemStack) {
        if (ItemRagnarokPendant.hasAwakenedRagnarok(player) && stack.item is ILexicon)
            (stack.item as ILexicon).unlockKnowledge(stack, forbidden)
    }
}

private fun EntityPlayer.definitelyHasAchievement(string: String): Boolean {
        return NaturalPledge.PROXY.hasAdvancement(this, string)
}
