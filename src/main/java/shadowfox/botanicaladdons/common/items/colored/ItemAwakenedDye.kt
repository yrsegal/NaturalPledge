package shadowfox.botanicaladdons.common.items.colored

import net.minecraft.item.ItemStack
import shadowfox.botanicaladdons.common.items.base.ItemRainbow

/**
 * @author WireSegal
 * Created at 10:20 AM on 5/21/16.
 */
class ItemAwakenedDye(name: String) : ItemRainbow(name, true) {
    override fun hasEffect(stack: ItemStack?): Boolean {
        return true
    }

    override fun mapOreDict(keys: Array<String>): ItemAwakenedDye {
        super.mapOreDict(keys)
        return this
    }

    override fun mapOreKey(key: String): ItemAwakenedDye {
        super.mapOreKey(key)
        return this
    }
}
