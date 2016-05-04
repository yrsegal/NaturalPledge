package shadowfox.botanicaladdons.common.items.colored

import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.client.core.ModelHandler
import shadowfox.botanicaladdons.common.BotanicalAddons
import shadowfox.botanicaladdons.common.block.ModBlocks
import shadowfox.botanicaladdons.common.items.ModItems
import shadowfox.botanicaladdons.common.items.base.ItemMod
import vazkii.botania.common.core.helper.ItemNBTHelper
import java.util.*

/**
 * @author WireSegal
 * Created at 4:33 PM on 5/4/16.
 */
class ItemStarPlacer(name: String) : ItemMod(name), ModelHandler.IColorProvider {

    companion object {
        val TAG_COLOR = "color"
        val TAG_SIZE = "size"

        val defaultColors = ArrayList<Int>()

        val DEFAULT_SIZE = 0.05f

        init {
            for (color in EnumDyeColor.values()) {
                var colorint = color.mapColor.colorValue
                defaultColors.add(colorint)
            }
            defaultColors.add(-1)
        }

        fun setColor(stack: ItemStack, color: Int) {
            ItemNBTHelper.setInt(stack, TAG_COLOR, color)
        }

        fun getColor(stack: ItemStack): Int {
            return ItemNBTHelper.getInt(stack, TAG_COLOR, -1)
        }

        fun setSize(stack: ItemStack, size: Float) {
            ItemNBTHelper.setFloat(stack, TAG_SIZE, size)
        }

        fun getSize(stack: ItemStack): Float {
            return ItemNBTHelper.getFloat(stack, TAG_SIZE, DEFAULT_SIZE)
        }

        fun colorStack(color: Int): ItemStack {
            val stack = ItemStack(ModItems.star)
            setColor(stack, color)
            return stack
        }

        fun forColor(colorVal: Int): ItemStack {
            return colorStack(defaultColors[colorVal % defaultColors.size])
        }
    }

    override fun addInformation(stack: ItemStack, playerIn: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
        super.addInformation(stack, playerIn, tooltip, advanced)
        val color = getColor(stack)
        if (color in defaultColors)
            addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.color.${defaultColors.indexOf(color)}")
        else
            addToTooltip(tooltip, "#${Integer.toHexString(color).toUpperCase()}")

        if (getSize(stack) != DEFAULT_SIZE)
            addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.customSize", getSize(stack) / 0.1f)
    }

    override fun getSubItems(itemIn: Item, tab: CreativeTabs?, subItems: MutableList<ItemStack>) {
        for (color in defaultColors) {
            subItems.add(colorStack(color))
        }
    }

    override fun getColor() = IItemColor {
        itemStack, i ->
            val color = getColor(itemStack)
            if (color == -1) BotanicalAddons.proxy.rainbow().rgb else color
    }

    override fun onItemUse(stack: ItemStack, playerIn: EntityPlayer, world: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult? {
        var placingPos = pos
        val iblockstate = world.getBlockState(placingPos)
        val block = iblockstate.block

        if (!block.isReplaceable(world, pos)) {
            placingPos = pos.offset(facing)
        }

        if (stack.stackSize != 0 && playerIn.canPlayerEdit(placingPos, facing, stack) && world.canBlockBePlaced(ModBlocks.star, placingPos, false, facing, null, stack)) {
            val i = this.getMetadata(stack.metadata)
            val iblockstate1 = ModBlocks.star.onBlockPlaced(world, placingPos, facing, hitX, hitY, hitZ, i, playerIn)

            if (placeBlockAt(stack, playerIn, world, placingPos, iblockstate1)) {
                val soundtype = ModBlocks.star.soundType
                world.playSound(playerIn, placingPos, soundtype.placeSound, SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0f) / 2.0f, soundtype.getPitch() * 0.8f)
                --stack.stackSize
            }

            return EnumActionResult.SUCCESS
        } else {
            return EnumActionResult.FAIL
        }
    }

    fun placeBlockAt(stack: ItemStack, player: EntityPlayer, world: World, pos: BlockPos, newState: IBlockState): Boolean {
        if (!world.setBlockState(pos, newState, 3)) return false

        val state = world.getBlockState(pos)
        if (state.block === ModBlocks.star) {
            ModBlocks.star.onBlockPlacedBy(world, pos, state, player, stack)
        }

        return true
    }
}
