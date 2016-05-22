package shadowfox.botanicaladdons.common.core.tab

import net.minecraft.item.ItemStack
import shadowfox.botanicaladdons.common.items.ModItems

/**
 * @author WireSegal
 * Created at 1:05 PM on 5/17/16.
 */
object ModTab : ModCreativeTab("divine") {
    override fun getIconItemStack(): ItemStack {
        return ItemStack(ModItems.symbol)
    }
}
