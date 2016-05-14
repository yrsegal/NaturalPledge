package shadowfox.botanicaladdons.common.block.colored

import net.minecraft.block.BlockLog
import net.minecraft.block.BlockPlanks
import net.minecraft.block.SoundType
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
import shadowfox.botanicaladdons.common.block.base.BlockModLog
import shadowfox.botanicaladdons.common.block.base.ItemModBlock
import shadowfox.botanicaladdons.common.items.base.ItemMod
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.lexicon.LexiconEntry

/**
 * @author WireSegal
 * Created at 10:36 AM on 5/7/16.
 */
class BlockIridescentLog(name: String, val colorSet: Int) : BlockModLog(name + colorSet, *Array(4, {name + COLORS[colorSet][it].toString().capitalizeFirst()})), ModelHandler.IBlockColorProvider, ILexiconable {
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
            override fun getUnlocalizedName(par1ItemStack: ItemStack?): String {
                return "tile.${LibMisc.MOD_ID}:${bareName.replace("\\d$".toRegex(), "")}"
            }
        }

    var COLOR: PropertyEnum<EnumDyeColor>? = null

    init {
        if (colorSet < 0 || colorSet >= 4)
            throw IllegalArgumentException("Colorset out of range for Iridescent Log! (passed in $colorSet)")
        soundType = SoundType.WOOD
        COLOR = COLOR_PROPS[colorSet]
        blockState = createBlockState()
        defaultState = blockState.baseState.withProperty(AXIS, BlockLog.EnumAxis.Y)
    }

    override fun getStateFromMeta(meta: Int): IBlockState {
        var axis = BlockLog.EnumAxis.Y
        val i = meta and 12
        val j = meta and 3

        if (i == 4) {
            axis = BlockLog.EnumAxis.X
        } else if (i == 8) {
            axis = BlockLog.EnumAxis.Z
        }

        return this.defaultState.withProperty(AXIS, axis).withProperty(COLOR, COLORS[colorSet][j])
    }

    override fun getMetaFromState(state: IBlockState?): Int {
        state ?: return 0
        var i = 0
        i = i or (state.getValue(COLOR ?: COLOR_PROPS[0]).metadata - (colorSet * 4))

        when (state.getValue(BlockLog.LOG_AXIS)) {
            BlockLog.EnumAxis.X -> i = i or 4
            BlockLog.EnumAxis.Z -> i = i or 8
            BlockLog.EnumAxis.NONE -> i = i or 12
            else -> i = i or 0
        }

        return i
    }

    override fun createBlockState(): BlockStateContainer? {
        return BlockStateContainer(this, COLOR ?: COLOR_PROPS[0], AXIS)
    }

    override fun damageDropped(state: IBlockState?): Int {
        return COLORS[colorSet].indexOf(state?.getValue(COLOR) ?: return 0)
    }

    override fun createStackedBlock(state: IBlockState): ItemStack? {
        return ItemStack(this, 1, COLORS[colorSet].indexOf(state.getValue(COLOR)))
    }

    override fun getBlockColor(): IBlockColor? {
        return IBlockColor { iBlockState, iBlockAccess, blockPos, i -> iBlockState.getValue(COLOR).mapColor.colorValue }
    }

    override fun getColor(): IItemColor? {
        return IItemColor { itemStack, i -> EnumDyeColor.byMetadata(colorSet * 4 + itemStack.itemDamage).mapColor.colorValue }
    }

    override fun addInformation(stack: ItemStack?, player: EntityPlayer?, tooltip: MutableList<String>, advanced: Boolean) {
        ItemMod.addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.color.${colorSet * 4 + (stack?.itemDamage ?: 0)}")
    }

    override fun getMapColor(state: IBlockState): MapColor {
        return state.getValue(COLOR).mapColor
    }

    override fun getEntry(p0: World?, p1: BlockPos?, p2: EntityPlayer?, p3: ItemStack?): LexiconEntry? {
        return null //todo
    }
}
