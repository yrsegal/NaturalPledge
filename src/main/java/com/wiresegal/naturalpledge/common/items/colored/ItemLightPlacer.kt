package com.wiresegal.naturalpledge.common.items.colored

import com.teamwizardry.librarianlib.features.base.item.IItemColorProvider
import com.teamwizardry.librarianlib.features.base.item.ItemMod
import com.teamwizardry.librarianlib.features.helpers.getNBTBoolean
import com.teamwizardry.librarianlib.features.helpers.setNBTBoolean
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper.addToTooltip
import com.wiresegal.naturalpledge.api.lib.LibMisc
import com.wiresegal.naturalpledge.common.NaturalPledge
import com.wiresegal.naturalpledge.common.block.ModBlocks
import com.wiresegal.naturalpledge.common.core.helper.RainbowItemHelper
import net.minecraft.block.state.IBlockState
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import vazkii.botania.api.item.IPhantomInkable
import vazkii.botania.api.mana.IManaUsingItem
import vazkii.botania.api.mana.ManaItemHandler

/**
 * @author WireSegal
 * Created at 5:50 PM on 5/6/16.
 */
class ItemLightPlacer(name: String) : ItemMod(name), IItemColorProvider, IManaUsingItem, IPhantomInkable {

    init {
        setMaxStackSize(1)
    }

    private val MANA_PER_FLAME = 100
    private val TAG_INK = "phantomInk"

    override fun usesMana(p0: ItemStack) = true

    override val itemColorFunction: ((ItemStack, Int) -> Int)?
        get() = { itemStack, i ->
            if (i == 0) {
                val color = (RainbowItemHelper.getColor(itemStack))
                if (color == -1) NaturalPledge.PROXY.rainbow() else NaturalPledge.PROXY.pulseColor(color)
            } else 0xFFFFFF
        }

    override fun addInformation(stack: ItemStack, playerIn: World?, tooltip: MutableList<String>, advanced: ITooltipFlag) {
        if (hasPhantomInk(stack))
            addToTooltip(tooltip, "botaniamisc.hasPhantomInk")

        val color = RainbowItemHelper.getColor(stack)
        if (color in RainbowItemHelper.defaultColors)
            addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.color.${RainbowItemHelper.defaultColors.indexOf(color)}")
        else if (advanced.isAdvanced)
            addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.color.format", Integer.toHexString(color).toUpperCase())
        else
            addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.color.mixed")
    }

    override fun onItemUse(playerIn: EntityPlayer, worldIn: World, posIn: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult? {
        val stack = playerIn.getHeldItem(hand)
        var pos = posIn
        val iblockstate = worldIn.getBlockState(pos)
        val block = iblockstate.block

        if (!block.isReplaceable(worldIn, pos)) {
            pos = pos.offset(facing)
        }

        return if (ManaItemHandler.requestManaExactForTool(stack, playerIn, MANA_PER_FLAME, false) && playerIn.canPlayerEdit(pos, facing, stack) && worldIn.mayPlace(ModBlocks.flame, pos, false, facing, null)) {
            val i = this.getMetadata(stack.metadata)
            val iblockstate1 = ModBlocks.flame.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, i, playerIn)

            if (placeBlockAt(stack, playerIn, worldIn, pos, iblockstate1)) {
                val soundtype = ModBlocks.flame.getSoundType(iblockstate1, worldIn, pos, playerIn)
                worldIn.playSound(playerIn, pos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0f) / 2.0f, soundtype.getPitch() * 0.8f)
                ManaItemHandler.requestManaExactForTool(stack, playerIn, MANA_PER_FLAME, true)
            }
            EnumActionResult.SUCCESS
        } else {
            EnumActionResult.FAIL
        }
    }

    private fun placeBlockAt(stack: ItemStack, player: EntityPlayer, world: World, pos: BlockPos, newState: IBlockState): Boolean {
        if (!world.setBlockState(pos, newState, 3)) return false

        val state = world.getBlockState(pos)
        if (state.block === ModBlocks.flame) {
            ItemBlock.setTileEntityNBT(world, player, pos, stack)
            ModBlocks.flame.onBlockPlacedBy(world, pos, state, player, stack)
        }

        return true
    }

    override fun hasPhantomInk(stack: ItemStack): Boolean {
        return stack.getNBTBoolean(TAG_INK, false)
    }

    override fun setPhantomInk(stack: ItemStack, ink: Boolean) {
        stack.setNBTBoolean(TAG_INK, ink)
    }
}
