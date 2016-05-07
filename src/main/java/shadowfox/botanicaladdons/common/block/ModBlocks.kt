package shadowfox.botanicaladdons.common.block

import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.oredict.OreDictionary
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.api.lib.LibNames
import shadowfox.botanicaladdons.api.lib.LibOreDict
import shadowfox.botanicaladdons.common.block.base.BlockMod
import shadowfox.botanicaladdons.common.block.colored.BlockFrozenStar
import shadowfox.botanicaladdons.common.block.colored.BlockIridescentDirt
import shadowfox.botanicaladdons.common.block.colored.BlockPrismFlame
import shadowfox.botanicaladdons.common.block.colored.BlockRainbowDirt
import shadowfox.botanicaladdons.common.block.tile.TilePrismFlame
import shadowfox.botanicaladdons.common.block.tile.TileStar
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

    init {
        awakenerCore = BlockAwakenerCore(LibNames.AWAKENER)
        star = BlockFrozenStar(LibNames.STAR)
        flame = BlockPrismFlame(LibNames.PRISM_FLAME)
        irisDirt = BlockIridescentDirt(LibNames.IRIS_DIRT)
        rainbowDirt = BlockRainbowDirt(LibNames.RAINBOW_DIRT)

        GameRegistry.registerTileEntity(TileStar::class.java, ResourceLocation(LibMisc.MOD_ID, LibNames.STAR).toString())
        GameRegistry.registerTileEntity(TilePrismFlame::class.java, ResourceLocation(LibMisc.MOD_ID, LibNames.PRISM_FLAME).toString())

        OreDictionary.registerOre(LibOreDict.DYES[16], BotaniaBlocks.bifrostPerm)
    }
}
