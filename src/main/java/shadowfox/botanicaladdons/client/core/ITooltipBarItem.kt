package shadowfox.botanicaladdons.client.core

import net.minecraft.item.ItemStack

/**
 * @author WireSegal
 * Created at 3:09 PM on 1/3/17.
 */
interface ITooltipBarItem {
    fun getPercentage(stack: ItemStack): Float
    fun getColor(stack: ItemStack): Int
}
