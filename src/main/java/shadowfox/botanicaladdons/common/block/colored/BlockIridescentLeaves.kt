package shadowfox.botanicaladdons.common.block.colored

import net.minecraft.block.material.MapColor
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.color.IBlockColor
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.World
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.client.core.ModelHandler
import shadowfox.botanicaladdons.common.block.base.BlockModLeaves
import shadowfox.botanicaladdons.common.block.base.ItemModBlock
import shadowfox.botanicaladdons.common.lexicon.LexiconEntries
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.lexicon.LexiconEntry

/**
 * @author WireSegal
 * Created at 10:45 PM on 5/13/16.
 */
abstract class BlockIridescentLeaves(name: String, set: Int) : BlockModLeaves(name + set, *Array(4, { name + COLORS[set][it].toString().capitalizeFirst() })), ModelHandler.IBlockColorProvider, ILexiconable {
    companion object {
        val COLOR_PROPS = Array(4) { i ->
            PropertyEnum.create("color", EnumDyeColor::class.java) {
                (it?.metadata ?: -1) < ((i + 1) * 4) && (it?.metadata ?: -1) >= (i * 4)
            }
        }
        val COLORS = Array(4) { i ->
            Array(4) { j ->
                EnumDyeColor.byMetadata(i * 4 + j)
            }

        }

        fun String.capitalizeFirst(): String {
            if (this.length == 0) return this
            return this.slice(0..0).capitalize() + this.slice(1..this.length - 1)
        }
    }

    override val item: ItemBlock
        get() = object : ItemModBlock(this) {
            override fun getUnlocalizedName(stack: ItemStack?): String {
                return "tile.${LibMisc.MOD_ID}:${bareName.replace("\\d$".toRegex(), "")}"
            }
        }

    abstract val colorSet: Int

    init {
        if (colorSet < 0 || colorSet >= 4)
            throw IllegalArgumentException("Colorset out of range for Iridescent Leaves! (passed in $colorSet)")
    }

    override fun getStateFromMeta(meta: Int): IBlockState {
        val i = meta and 3
        return super.getStateFromMeta(meta).withProperty(COLOR_PROPS[colorSet], COLORS[colorSet][i])
    }

    override fun getMetaFromState(state: IBlockState?): Int {
        state ?: return 0
        return super.getMetaFromState(state) or (state.getValue(COLOR_PROPS[colorSet]).metadata - (colorSet * 4))
    }

    override fun createBlockState(): BlockStateContainer? {
        return BlockStateContainer(this, COLOR_PROPS[colorSet], DECAYABLE, CHECK_DECAY)
    }

    override fun createStackedBlock(state: IBlockState): ItemStack? {
        return ItemStack(this, 1, COLORS[colorSet].indexOf(state.getValue(COLOR_PROPS[colorSet])))
    }

    override fun getPickBlock(state: IBlockState?, target: RayTraceResult?, world: World?, pos: BlockPos?, player: EntityPlayer?): ItemStack? {
        return createStackedBlock(state ?: return null)
    }

    override fun getBlockColor(): IBlockColor? {
        return IBlockColor { iBlockState, iBlockAccess, blockPos, i -> iBlockState.getValue(COLOR_PROPS[colorSet]).mapColor.colorValue }
    }

    override fun getItemColor(): IItemColor? {
        return IItemColor { itemStack, i -> EnumDyeColor.byMetadata(colorSet * 4 + itemStack.itemDamage).mapColor.colorValue }
    }

    override fun addInformation(stack: ItemStack?, player: EntityPlayer?, tooltip: MutableList<String>, advanced: Boolean) {
        addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.color.${colorSet * 4 + (stack?.itemDamage ?: 0)}")
    }

    override fun getMapColor(state: IBlockState): MapColor {
        return state.getValue(COLOR_PROPS[colorSet]).mapColor
    }

    override fun getEntry(p0: World?, p1: BlockPos?, p2: EntityPlayer?, p3: ItemStack?): LexiconEntry? {
        return LexiconEntries.irisDirt
    }
}
