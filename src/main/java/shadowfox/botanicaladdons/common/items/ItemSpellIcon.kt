package shadowfox.botanicaladdons.common.items

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import shadowfox.botanicaladdons.common.items.base.ItemMod

/**
 * @author WireSegal
 * Created at 12:41 PM on 4/19/16.
 */
class ItemSpellIcon(name: String) : ItemMod(name, *Variants.variants) {
    enum class Variants {
        LEAP, INTERDICT, PUSH_AWAY;

        override fun toString(): String {
            return this.name.toLowerCase().split("_").joinToString("", transform = {it.capitalizeFirst()})
        }

        companion object {
            val variants: Array<String>
                get() = Array(Variants.values().size, {"icon" + Variants.values()[it].toString().capitalizeFirst()})
        }
    }

    companion object {
        fun of(variant: Variants) = ItemStack(ModItems.spellIcon, 1, variant.ordinal)

        fun String.capitalizeFirst(): String {
            if (this.length == 0) return this
            return this.slice(0..0).capitalize() + this.slice(1..this.length - 1)
        }
    }

    override fun getSubItems(itemIn: Item, tab: CreativeTabs?, subItems: MutableList<ItemStack>) {}
}
