package shadowfox.botanicaladdons.common.core.helper

import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper
import net.minecraft.block.Block
import net.minecraft.block.material.MapColor
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import shadowfox.botanicaladdons.common.BotanicalAddons
import java.util.*

/**
 * @author WireSegal
 * Created at 5:52 PM on 5/6/16.
 */
object RainbowItemHelper {

    fun forColor(colorVal: Int, item: Item) = forColor(colorVal, ItemStack(item))
    fun forColor(colorVal: Int, block: Block) = forColor(colorVal, ItemStack(block))

    fun forColor(colorVal: Int, stack: ItemStack) = colorStack(defaultColors[colorVal % defaultColors.size], stack)

    fun colorStack(color: Int, item: Item) = colorStack(color, ItemStack(item))
    fun colorStack(color: Int, block: Block) = colorStack(color, ItemStack(block))

    fun colorStack(color: Int, stack: ItemStack): ItemStack {
        val newStack = stack.copy()
        setColor(newStack, color)
        return newStack
    }

    val defaultColors = ArrayList<Int>()

    val TAG_COLOR = "color"

    init {
        EnumDyeColor.values().mapTo(defaultColors) { MapColor.getBlockColor(it).colorValue }
        defaultColors.add(-1)
    }

    fun setColor(stack: ItemStack, color: Int) = ItemNBTHelper.setInt(stack, TAG_COLOR, color)
    fun getColor(stack: ItemStack) = ItemNBTHelper.getInt(stack, TAG_COLOR, -1)

    fun colorFromInt(color: Int): Int = if (color == -1) BotanicalAddons.PROXY.rainbow().rgb else color
    fun colorFromIntAndPos(color: Int, pos: BlockPos) = if (color == -1) BotanicalAddons.PROXY.rainbow(pos).rgb else color
}
