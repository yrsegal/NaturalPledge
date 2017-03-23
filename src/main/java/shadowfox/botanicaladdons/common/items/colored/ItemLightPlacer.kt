package shadowfox.botanicaladdons.common.items.colored

import com.teamwizardry.librarianlib.client.util.TooltipHelper.addToTooltip
import com.teamwizardry.librarianlib.common.base.item.IItemColorProvider
import com.teamwizardry.librarianlib.common.base.item.ItemMod
import com.teamwizardry.librarianlib.common.util.ItemNBTHelper
import net.minecraft.block.state.IBlockState
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
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.common.BotanicalAddons
import shadowfox.botanicaladdons.common.block.ModBlocks
import shadowfox.botanicaladdons.common.core.helper.RainbowItemHelper
import vazkii.botania.api.item.IPhantomInkable
import vazkii.botania.api.mana.IManaUsingItem
import vazkii.botania.api.mana.ManaItemHandler
import java.awt.Color

/**
 * @author WireSegal
 * Created at 5:50 PM on 5/6/16.
 */
class ItemLightPlacer(name: String) : ItemMod(name), IItemColorProvider, IManaUsingItem, IPhantomInkable {

    init {
        setMaxStackSize(1)
    }

    val MANA_PER_FLAME = 100
    val TAG_INK = "phantomInk"

    override fun usesMana(p0: ItemStack?) = true

    override val itemColorFunction: ((ItemStack, Int) -> Int)?
        get() = { itemStack, i ->
            if (i == 0) {
                val color = (RainbowItemHelper.getColor(itemStack))
                if (color == -1) BotanicalAddons.PROXY.rainbow().rgb else BotanicalAddons.PROXY.pulseColor(Color(color)).rgb
            } else 0xFFFFFF
        }

    override fun addInformation(stack: ItemStack, playerIn: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
        if (hasPhantomInk(stack))
            addToTooltip(tooltip, "botaniamisc.hasPhantomInk")

        val color = RainbowItemHelper.getColor(stack)
        if (color in RainbowItemHelper.defaultColors)
            addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.color.${RainbowItemHelper.defaultColors.indexOf(color)}")
        else if (advanced)
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

        if (ManaItemHandler.requestManaExactForTool(stack, playerIn, MANA_PER_FLAME, false) && playerIn.canPlayerEdit(pos, facing, stack) && worldIn.mayPlace(ModBlocks.flame, pos, false, facing, null)) {
            val i = this.getMetadata(stack.metadata)
            val iblockstate1 = ModBlocks.flame.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, i, playerIn)

            if (placeBlockAt(stack, playerIn, worldIn, pos, iblockstate1)) {
                val soundtype = ModBlocks.flame.soundType
                worldIn.playSound(playerIn, pos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0f) / 2.0f, soundtype.getPitch() * 0.8f)
                ManaItemHandler.requestManaExactForTool(stack, playerIn, MANA_PER_FLAME, true)
            }

            return EnumActionResult.SUCCESS
        } else {
            return EnumActionResult.FAIL
        }
    }

    fun placeBlockAt(stack: ItemStack, player: EntityPlayer, world: World, pos: BlockPos, newState: IBlockState): Boolean {
        if (!world.setBlockState(pos, newState, 3)) return false

        val state = world.getBlockState(pos)
        if (state.block === ModBlocks.flame) {
            ItemBlock.setTileEntityNBT(world, player, pos, stack)
            ModBlocks.flame.onBlockPlacedBy(world, pos, state, player, stack)
        }

        return true
    }

    override fun hasPhantomInk(stack: ItemStack): Boolean {
        return ItemNBTHelper.getBoolean(stack, TAG_INK, false)
    }

    override fun setPhantomInk(stack: ItemStack, ink: Boolean) {
        ItemNBTHelper.setBoolean(stack, TAG_INK, ink)
    }
}
