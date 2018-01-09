package shadowfox.botanicaladdons.common.block.colored

import com.teamwizardry.librarianlib.features.base.block.BlockModLeaves
import com.teamwizardry.librarianlib.features.base.block.IBlockColorProvider
import com.teamwizardry.librarianlib.features.base.block.ItemModBlock
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper.addToTooltip
import net.minecraft.block.material.MapColor
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.api.sapling.IStackConvertible
import shadowfox.botanicaladdons.common.block.ModBlocks
import shadowfox.botanicaladdons.common.lexicon.LexiconEntries
import shadowfox.botanicaladdons.common.lib.capitalizeFirst
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.lexicon.LexiconEntry
import java.util.*

/**
 * @author WireSegal
 * Created at 10:45 PM on 5/13/16.
 */
@Suppress("LeakingThis")
abstract class BlockIridescentLeaves(name: String, set: Int) : BlockModLeaves(name + set, *Array(4, { name + COLORS[set][it].toString().capitalizeFirst() })), IBlockColorProvider, ILexiconable, IStackConvertible {
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
    }

    override fun createItemForm(): ItemBlock? {
        return object : ItemModBlock(this) {
            override fun getUnlocalizedName(stack: ItemStack): String {
                return "tile.${LibMisc.MOD_ID}:${bareName.replace("\\d$".toRegex(), "")}"
            }
        }
    }

    override fun itemStackFromState(state: IBlockState) = ItemStack(this, 1, state.getValue(COLOR_PROPS[colorSet]).metadata - (colorSet * 4))

    override fun getItemDropped(state: IBlockState, rand: Random, fortune: Int): Item? {
        return ModBlocks.irisSapling.itemForm
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

    override fun getMetaFromState(state: IBlockState): Int {
        state ?: return 0
        return super.getMetaFromState(state) or (state.getValue(COLOR_PROPS[colorSet]).metadata - (colorSet * 4))
    }

    override fun createBlockState(): BlockStateContainer {
        return BlockStateContainer(this, COLOR_PROPS[colorSet], DECAYABLE, CHECK_DECAY)
    }

    fun createStackedBlock(state: IBlockState): ItemStack {
        return ItemStack(this, 1, COLORS[colorSet].indexOf(state.getValue(COLOR_PROPS[colorSet])))
    }

    override fun getPickBlock(state: IBlockState, target: RayTraceResult, world: World, pos: BlockPos, player: EntityPlayer): ItemStack {
        return createStackedBlock(state)
    }

    override val blockColorFunction: ((IBlockState, IBlockAccess?, BlockPos?, Int) -> Int)?
        get() = { iBlockState, _, _, _ -> MapColor.getBlockColor(iBlockState.getValue(COLOR_PROPS[colorSet])).colorValue }

    override val itemColorFunction: ((ItemStack, Int) -> Int)?
        get() = { itemStack, _ -> MapColor.getBlockColor(EnumDyeColor.byMetadata(colorSet * 4 + itemStack.itemDamage)).colorValue }

    override fun addInformation(stack: ItemStack, player: World?, tooltip: MutableList<String>, advanced: ITooltipFlag) {
        addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.color.${colorSet * 4 + stack.itemDamage}")
    }

    override fun getMapColor(state: IBlockState, worldIn: IBlockAccess?, pos: BlockPos?): MapColor {
        return MapColor.getBlockColor(state.getValue(COLOR_PROPS[colorSet]))
    }

    override fun getEntry(p0: World?, p1: BlockPos?, p2: EntityPlayer?, p3: ItemStack): LexiconEntry? {
        return LexiconEntries.irisDirt
    }
}
