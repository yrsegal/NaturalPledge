package shadowfox.botanicaladdons.common.core.tab

import net.minecraft.item.ItemStack
import shadowfox.botanicaladdons.common.block.ModBlocks
import shadowfox.botanicaladdons.common.items.ModItems

/**
 * @author WireSegal
 * Created at 1:05 PM on 5/17/16.
 */
object ModTabs {
    object TabDivinity : ModCreativeTab("divine") {
        override fun getIconItemStack(): ItemStack {
            return ItemStack(ModItems.symbol)
        }
    }

    object TabWood : ModCreativeTab("wood") {
        override fun getIconItemStack(): ItemStack {
            return ItemStack(ModBlocks.irisSapling)
        }
    }

    object TabColor : ModCreativeTab("color") {
        override fun getIconItemStack(): ItemStack {
            return ItemStack(ModItems.iridescentDye, 1, 16)
        }
    }
}
