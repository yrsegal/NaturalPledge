package com.wiresegal.naturalpledge.common.block.colored

import com.teamwizardry.librarianlib.features.base.block.BlockMod
import com.teamwizardry.librarianlib.features.base.block.IBlockColorProvider
import com.teamwizardry.librarianlib.features.base.block.ItemModBlock
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper.addToTooltip
import net.minecraft.block.SoundType
import net.minecraft.block.material.MapColor
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.common.IPlantable
import com.wiresegal.naturalpledge.api.lib.LibMisc
import com.wiresegal.naturalpledge.common.lexicon.LexiconEntries
import com.wiresegal.naturalpledge.common.lib.LibOreDict
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.lexicon.LexiconEntry

/**
 * @author WireSegal
 * Created at 9:50 PM on 5/6/16.
 */
class BlockIridescentDirt(val name: String) : BlockMod(name, Material.GROUND, *Array(16, { name + LibOreDict.COLORS[it] })), IBlockColorProvider, ILexiconable {
    companion object {
        val COLOR = PropertyEnum.create("color", EnumDyeColor::class.java)
    }

    init {
        soundType = SoundType.GROUND
        blockHardness = 0.5f
        defaultState = defaultState.withProperty(COLOR, EnumDyeColor.WHITE)
    }


    override fun createItemForm() = object : ItemModBlock(this) {
            override fun getUnlocalizedNameInefficiently(stack: ItemStack): String {
                return "tile.${LibMisc.MOD_ID}:" + bareName
            }
        }

    override fun addInformation(stack: ItemStack, player: World?, tooltip: MutableList<String>, advanced: ITooltipFlag) {
        addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.color.${stack.itemDamage}")
    }

    override val blockColorFunction: ((IBlockState, IBlockAccess?, BlockPos?, Int) -> Int)?
        get() = { iBlockState, _, _, _ -> MapColor.getBlockColor(iBlockState.getValue(COLOR)).colorValue }

    override val itemColorFunction: ((ItemStack, Int) -> Int)?
        get() = { itemStack, _ -> MapColor.getBlockColor(EnumDyeColor.byMetadata(itemStack.itemDamage)).colorValue }

    override fun createBlockState(): BlockStateContainer {
        return BlockStateContainer(this, COLOR)
    }

    override fun damageDropped(state: IBlockState): Int {
        return state.getValue(COLOR).metadata
    }

    override fun getMapColor(state: IBlockState, worldIn: IBlockAccess?, pos: BlockPos?): MapColor {
        return MapColor.getBlockColor(state.getValue(COLOR))
    }


    override fun getStateFromMeta(meta: Int): IBlockState {
        return this.defaultState.withProperty(COLOR, EnumDyeColor.byMetadata(meta))
    }

    override fun getMetaFromState(state: IBlockState): Int {
        return state.getValue(COLOR).metadata
    }

    override fun getEntry(p0: World?, p1: BlockPos?, p2: EntityPlayer?, p3: ItemStack): LexiconEntry? {
        return LexiconEntries.irisDirt
    }

    override fun isToolEffective(type: String?, state: IBlockState?): Boolean {
        return type == "shovel"
    }

    override fun getHarvestTool(state: IBlockState?): String? {
        return "shovel"
    }

    override fun canSustainPlant(state: IBlockState?, world: IBlockAccess?, pos: BlockPos?, direction: EnumFacing?, plantable: IPlantable?): Boolean {
        return true
    }
}
