package shadowfox.botanicaladdons.common.items

import net.minecraft.item.EnumRarity
import net.minecraft.item.ItemStack
import shadowfox.botanicaladdons.common.items.base.ItemMod
import vazkii.botania.api.BotaniaAPI

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
                get() {
                    val out = arrayListOf<String>()
                    for (variant in values()) {
                        out.add(variant.toString())
                        out.add(variant.toString() + "Active")
                    }
                    return out.toTypedArray()
                }
        }
    }

    companion object {
        fun of(v: Variants, active: Boolean = false) = ItemStack(ModItems.resource, 1, v.ordinal * 2 + if (active) 1 else 0)

        fun String.capitalizeFirst(): String {
            if (this.length == 0) return this
            return this.slice(0..0).capitalize() + this.slice(1..this.length - 1)
        }

        fun String.lowercaseFirst(): String {
            if (this.length == 0) return this
            return this.slice(0..0).toLowerCase() + this.slice(1..this.length - 1)
        }
    }

    override fun getRarity(stack: ItemStack): EnumRarity? {
        return if (hasEffect(stack)) BotaniaAPI.rarityRelic else EnumRarity.COMMON
    }

    override fun hasEffect(stack: ItemStack): Boolean {
        return stack.itemDamage % 2 == 1
    }
}
