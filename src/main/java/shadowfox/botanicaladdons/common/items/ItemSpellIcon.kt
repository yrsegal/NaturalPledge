package shadowfox.botanicaladdons.common.items

import com.teamwizardry.librarianlib.common.base.item.IItemColorProvider
import com.teamwizardry.librarianlib.common.base.item.ItemMod
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import shadowfox.botanicaladdons.common.BotanicalAddons
import shadowfox.botanicaladdons.common.lib.capitalizeFirst

/**
 * @author WireSegal
 * Created at 12:41 PM on 4/19/16.
 */
class ItemSpellIcon(name: String) : ItemMod(name, *Variants.variants), IItemColorProvider {
    enum class Variants(val iridescent: Boolean) {
        LEAP, INTERDICT, PUSH_AWAY,
        LIGHTNING, STRENGTH, PULL,
        IRIDESCENCE(true), BIFROST_SPHERE(true),
        IRONROOT, LIFEMAKER,
        WIND_INFUSION, LIGHTNING_INFUSION, HYPERCHARGE,
        FIRE_INFUSION, TRUESIGHT, DISDAIN, FIRE_JET,
        SOUL_MANIFESTATION, WIN;

        constructor() : this(false)

        override fun toString(): String {
            return this.name.toLowerCase().split("_").joinToString("", transform = String::capitalizeFirst)
        }

        companion object {
            val variants: Array<String>
                get() = Array(values().size, { "icon" + values()[it].toString().capitalizeFirst() })
        }
    }

    companion object {
        fun of(variant: Variants) = ItemStack(ModItems.spellIcon, 1, variant.ordinal)
    }

    override val itemColorFunction: ((ItemStack, Int) -> Int)?
        get() = { itemStack, _ ->
            if (itemStack.itemDamage >= 0 && itemStack.itemDamage < Variants.values().size && Variants.values()[itemStack.itemDamage].iridescent)
                BotanicalAddons.PROXY.rainbow().rgb
            else
                0xFFFFFF
        }

    override fun getSubItems(itemIn: Item, tab: CreativeTabs?, subItems: NonNullList<ItemStack>) {
        // NO-OP
    }
}
