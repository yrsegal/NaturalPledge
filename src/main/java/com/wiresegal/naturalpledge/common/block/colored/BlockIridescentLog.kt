package com.wiresegal.naturalpledge.common.block.colored

import com.teamwizardry.librarianlib.features.base.block.BlockModLog
import com.teamwizardry.librarianlib.features.base.block.IBlockColorProvider
import com.teamwizardry.librarianlib.features.base.block.ItemModBlock
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper
import net.minecraft.block.material.MapColor
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import com.wiresegal.naturalpledge.api.lib.LibMisc
import com.wiresegal.naturalpledge.common.lexicon.LexiconEntries
import com.wiresegal.naturalpledge.common.lib.capitalizeFirst
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.lexicon.LexiconEntry

/**
 * @author WireSegal
 * Created at 10:36 AM on 5/7/16.
 */
@Suppress("LeakingThis")
abstract class BlockIridescentLog(name: String, set: Int) : BlockModLog(name + set, *Array(4, { name + COLORS[set][it].toString().capitalizeFirst() })), IBlockColorProvider, ILexiconable {
    companion object {
        val COLOR_PROPS: Array<PropertyEnum<EnumDyeColor>> = Array(4) { i ->
            PropertyEnum.create("color", EnumDyeColor::class.java) {
                (it?.metadata ?: -1) < ((i + 1) * 4) && (it?.metadata ?: -1) >= (i * 4)
            }
        }
        val COLORS: Array<Array<EnumDyeColor>> = Array(4) { i ->
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

    abstract val colorSet: Int

    init {
        if (colorSet < 0 || colorSet >= 4)
            throw IllegalArgumentException("Colorset out of range for Iridescent Log! (passed in $colorSet)")
    }

    override fun getStateFromMeta(meta: Int): IBlockState {
        val j = meta and 3
        return super.getStateFromMeta(meta).withProperty(COLOR_PROPS[colorSet], COLORS[colorSet][j])
    }

    override fun getMetaFromState(state: IBlockState): Int {
        state ?: return 0
        return (state.getValue(COLOR_PROPS[colorSet]).ordinal - (colorSet * 4)) or super.getMetaFromState(state)
    }

    override fun createBlockState(): BlockStateContainer {
        return BlockStateContainer(this, COLOR_PROPS[colorSet], AXIS)
    }

    override fun damageDropped(state: IBlockState?): Int {
        return COLORS[colorSet].indexOf(state?.getValue(COLOR_PROPS[colorSet]) ?: return 0)
    }

    override val blockColorFunction: ((IBlockState, IBlockAccess?, BlockPos?, Int) -> Int)?
        get() = { iBlockState, _, _, _ -> MapColor.getBlockColor(iBlockState.getValue(COLOR_PROPS[colorSet])).colorValue }

    override val itemColorFunction: ((ItemStack, Int) -> Int)?
        get() = { itemStack, _ -> MapColor.getBlockColor(EnumDyeColor.byMetadata(colorSet * 4 + itemStack.itemDamage)).colorValue }

    override fun addInformation(stack: ItemStack, player: World?, tooltip: MutableList<String>, advanced: ITooltipFlag) {
        TooltipHelper.addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.color.${colorSet * 4 + stack.itemDamage}")
    }

    override fun getMapColor(state: IBlockState, worldIn: IBlockAccess?, pos: BlockPos?): MapColor {
        return MapColor.getBlockColor(state.getValue(COLOR_PROPS[colorSet]))
    }

    override fun getEntry(p0: World?, p1: BlockPos?, p2: EntityPlayer?, p3: ItemStack): LexiconEntry? {
        return LexiconEntries.irisDirt
    }
}
