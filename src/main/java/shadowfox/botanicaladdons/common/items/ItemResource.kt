package shadowfox.botanicaladdons.common.items

import net.minecraft.item.ItemStack
import shadowfox.botanicaladdons.common.items.base.ItemMod

/**
 * @author WireSegal
 * Created at 11:14 PM on 5/20/16.
 */
class ItemResource(name: String) : ItemMod(name, *Variants.variants) {
    enum class Variants {
        THUNDER_STEEL, LIFE_ROOT, AQUAMARINE;

        override fun toString(): String {
            return this.name.toLowerCase().split("_").joinToString("", transform = { it.capitalizeFirst() }).lowercaseFirst()
        }

        companion object {
            val variants: Array<String>
                get() = Array(values().size, { values()[it].toString() }).plus(Array(values().size, { values()[it].toString() + "Active" }))
        }
    }

    companion object {
        fun of(v: Variants, active: Boolean = false) = ItemStack(ModItems.resource, 1, v.ordinal + if (active) Variants.values().size else 0)

        fun String.capitalizeFirst(): String {
            if (this.length == 0) return this
            return this.slice(0..0).capitalize() + this.slice(1..this.length - 1)
        }

        fun String.lowercaseFirst(): String {
            if (this.length == 0) return this
            return this.slice(0..0).toLowerCase() + this.slice(1..this.length - 1)
        }
    }

    override fun hasEffect(stack: ItemStack): Boolean {
        return stack.itemDamage >= Variants.values().size
    }
}
