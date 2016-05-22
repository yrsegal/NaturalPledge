package shadowfox.botanicaladdons.common.block

import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.oredict.OreDictionary
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.common.block.alt.BlockAltLeaves
import shadowfox.botanicaladdons.common.block.alt.BlockAltLog
import shadowfox.botanicaladdons.common.block.alt.BlockAltPlanks
import shadowfox.botanicaladdons.common.block.base.BlockMod
import shadowfox.botanicaladdons.common.block.colored.*
import shadowfox.botanicaladdons.common.block.tile.TilePrismFlame
import shadowfox.botanicaladdons.common.block.tile.TileStar
import shadowfox.botanicaladdons.common.lib.LibNames
import shadowfox.botanicaladdons.common.lib.LibOreDict
import vazkii.botania.common.block.ModBlocks as BotaniaBlocks

/**
 * @author WireSegal
 * Created at 2:46 PM on 4/17/16.
 */
object ModBlocks {
    val awakenerCore: BlockMod
    val star: BlockMod
    val flame: BlockMod
    val irisDirt: BlockMod
    val rainbowDirt: BlockMod
    val irisLogs: Array<BlockIridescentLog>
    val rainbowLog: BlockMod
    val irisLeaves: Array<BlockIridescentLeaves>
    val rainbowLeaves: BlockMod
    val irisSapling: BlockMod
    val altLogs: Array<BlockAltLog>
    val altLeaves: Array<BlockAltLeaves>
    val altPlanks: BlockMod

    init {
        awakenerCore = BlockAwakenerCore(LibNames.AWAKENER)
        star = BlockFrozenStar(LibNames.STAR)
        flame = BlockPrismFlame(LibNames.PRISM_FLAME)
        irisDirt = BlockIridescentDirt(LibNames.IRIS_DIRT)
        rainbowDirt = BlockRainbowDirt(LibNames.RAINBOW_DIRT)
        irisLogs = Array(4) { BlockIridescentLog(LibNames.IRIS_LOG, it) }
        rainbowLog = BlockRainbowLog(LibNames.RAINBOW_LOG)
        irisLeaves = Array(4) { BlockIridescentLeaves(LibNames.IRIS_LEAVES, it) }
        rainbowLeaves = BlockRainbowLeaves(LibNames.RAINBOW_LEAVES)
        irisSapling = BlockIrisSapling(LibNames.IRIS_SAPLING)
        altLogs = Array(2) { BlockAltLog(LibNames.ALT_LOG, it) }
        altLeaves = Array(2) { BlockAltLeaves(LibNames.ALT_LEAVES, it) }
        altPlanks = BlockAltPlanks(LibNames.ALT_PLANKS)

        GameRegistry.registerTileEntity(TileStar::class.java, ResourceLocation(LibMisc.MOD_ID, LibNames.STAR).toString())
        GameRegistry.registerTileEntity(TilePrismFlame::class.java, ResourceLocation(LibMisc.MOD_ID, LibNames.PRISM_FLAME).toString())

        OreDictionary.registerOre("logWood", ItemStack(rainbowLog, 1, OreDictionary.WILDCARD_VALUE))
        for (log in irisLogs) OreDictionary.registerOre("logWood", ItemStack(log, 1, OreDictionary.WILDCARD_VALUE))
        OreDictionary.registerOre(LibOreDict.DYES[16], ItemStack(BotaniaBlocks.bifrostPerm, 1, OreDictionary.WILDCARD_VALUE))
    }
}
