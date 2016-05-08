package shadowfox.botanicaladdons.common.block

import net.minecraft.item.ItemBlock
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.oredict.OreDictionary
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.api.lib.LibNames
import shadowfox.botanicaladdons.api.lib.LibOreDict
import shadowfox.botanicaladdons.common.block.base.BlockMod
import shadowfox.botanicaladdons.common.block.base.BlockModLog
import shadowfox.botanicaladdons.common.block.colored.*
import shadowfox.botanicaladdons.common.block.tile.TilePrismFlame
import shadowfox.botanicaladdons.common.block.tile.TileStar
import shadowfox.botanicaladdons.common.block.tile.TileSuffuser
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
    val soulSuffuser: BlockMod
    val irisLogs: Array<BlockMod>
    val rainbowLog: BlockMod

    init {
        awakenerCore = BlockAwakenerCore(LibNames.AWAKENER)
        star = BlockFrozenStar(LibNames.STAR)
        flame = BlockPrismFlame(LibNames.PRISM_FLAME)
        irisDirt = BlockIridescentDirt(LibNames.IRIS_DIRT)
        rainbowDirt = BlockRainbowDirt(LibNames.RAINBOW_DIRT)
        soulSuffuser = BlockSoulSuffuser(LibNames.SOUL_SUFFUSER)
        irisLogs = Array(4) {BlockIridescentLog(LibNames.IRIS_LOG, it)}
        rainbowLog = BlockRainbowLog(LibNames.RAINBOW_LOG)

        GameRegistry.registerTileEntity(TileSuffuser::class.java, ResourceLocation(LibMisc.MOD_ID, LibNames.SOUL_SUFFUSER).toString())
        GameRegistry.registerTileEntity(TileStar::class.java, ResourceLocation(LibMisc.MOD_ID, LibNames.STAR).toString())
        GameRegistry.registerTileEntity(TilePrismFlame::class.java, ResourceLocation(LibMisc.MOD_ID, LibNames.PRISM_FLAME).toString())

        OreDictionary.registerOre("logWood", rainbowLog)
        for (log in irisLogs) OreDictionary.registerOre("logWood", log)
        OreDictionary.registerOre(LibOreDict.DYES[16], BotaniaBlocks.bifrostPerm)
    }
}
