package shadowfox.botanicaladdons.common.lexicon

import net.minecraft.block.BlockDirt
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import shadowfox.botanicaladdons.common.block.BlockAwakenerCore
import shadowfox.botanicaladdons.common.block.ModBlocks
import shadowfox.botanicaladdons.common.crafting.ModRecipes
import shadowfox.botanicaladdons.common.items.ModItems
import shadowfox.botanicaladdons.common.items.bauble.faith.*
import shadowfox.botanicaladdons.common.lexicon.base.EntryAwakenedKnowledge
import shadowfox.botanicaladdons.common.lexicon.base.EntryPriestlyKnowledge
import shadowfox.botanicaladdons.common.lexicon.base.ModCategory
import shadowfox.botanicaladdons.common.lexicon.base.ModEntry
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.lexicon.LexiconEntry
import vazkii.botania.common.core.handler.ConfigHandler
import vazkii.botania.common.lexicon.page.PageCraftingRecipe
import vazkii.botania.common.lexicon.page.PageMultiblock
import vazkii.botania.common.lexicon.page.PageText

/**
 * @author WireSegal
 * Created at 1:15 PM on 4/16/16.
 */
object LexiconEntries {
    val divinity: ModCategory

    val holySymbol: LexiconEntry
    val njord: LexiconEntry
    val idunn: LexiconEntry
    val thor: LexiconEntry
    val heimdall: LexiconEntry

    val focus: LexiconEntry

    val awakening: LexiconEntry
    val consequences: LexiconEntry

    init {

        val topKnowledgeTier = if (ConfigHandler.relicsEnabled) BotaniaAPI.relicKnowledge else BotaniaAPI.elvenKnowledge

        divinity = ModCategory("divinity", 1)

        holySymbol = ModEntry("holySymbol", divinity, ItemStack(ModItems.symbol)).setPriority()
        holySymbol.setLexiconPages(PageText("0"), PageText("1"), PageCraftingRecipe("2", ModRecipes.recipeSymbol))
        njord = ModEntry("njord", divinity, ItemFaithBauble.emblemOf(PriestlyEmblemNjord::class.java)).setKnowledgeType(BotaniaAPI.elvenKnowledge)
        njord.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeNjordEmblem))
        idunn = ModEntry("idunn", divinity, ItemFaithBauble.emblemOf(PriestlyEmblemIdunn::class.java))
        idunn.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeIdunnEmblem))
        thor = ModEntry("thor", divinity, ItemFaithBauble.emblemOf(PriestlyEmblemThor::class.java)).setKnowledgeType(BotaniaAPI.elvenKnowledge)
        thor.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeThorEmblem))
        heimdall = ModEntry("heimdall", divinity, ItemFaithBauble.emblemOf(PriestlyEmblemHeimdall::class.java)).setKnowledgeType(BotaniaAPI.elvenKnowledge)
        heimdall.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeHeimdallEmblem))

        focus = EntryPriestlyKnowledge("focus", divinity, ItemStack(ModItems.spellFocus)).setPriority()
        focus.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ModRecipes.recipeTerrestrialFocus))

        BlockAwakenerCore.multiblock = BlockAwakenerCore.makeMultiblockSet()
        awakening = EntryPriestlyKnowledge("awakening", divinity, ItemStack(ModBlocks.awakenerCore)).setKnowledgeType(topKnowledgeTier).setPriority()
        awakening.setLexiconPages(PageText("0"), PageText("1"), PageMultiblock("2", BlockAwakenerCore.multiblock), PageCraftingRecipe("3", ModRecipes.recipeDivineCore))
        consequences = EntryAwakenedKnowledge("wellshit", divinity, ItemStack(Blocks.dirt, 1, BlockDirt.DirtType.PODZOL.metadata)).setKnowledgeType(topKnowledgeTier).setPriority()
        consequences.setLexiconPages(PageText("0"))
    }
}
