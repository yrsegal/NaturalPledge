package shadowfox.botanicaladdons.common.items

import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import shadowfox.botanicaladdons.client.core.ModelHandler
import shadowfox.botanicaladdons.common.items.base.ItemMod
import vazkii.botania.common.Botania
import java.awt.Color

/**
 * @author WireSegal
 * Created at 12:41 PM on 4/19/16.
 */
class ItemSpellIcon(name: String) : ItemMod(name, *Variants.variants), ModelHandler.IColorProvider {
    enum class Variants(val iridescent: Boolean) {
        LEAP, INTERDICT, PUSH_AWAY,
        LIGHTNING, STRENGTH, PULL,
        IRIDESCENCE(true), BIFROST_SPHERE(true),
        IRONROOT, LIFEMAKER,
        SUFFUSION;

        constructor() : this(false)

        override fun toString(): String {
            return this.name.toLowerCase().split("_").joinToString("", transform = { it.capitalizeFirst() })
        }

        companion object {
            val variants: Array<String>
                get() = Array(Variants.values().size, { "icon" + Variants.values()[it].toString().capitalizeFirst() })
        }
    }

    companion object {
        fun of(variant: Variants) = ItemStack(ModItems.spellIcon, 1, variant.ordinal)

        fun String.capitalizeFirst(): String {
            if (this.length == 0) return this
            return this.slice(0..0).capitalize() + this.slice(1..this.length - 1)
        }
    }

    @SideOnly(Side.CLIENT)
    override fun getColor() = IItemColor { itemStack, i ->
        if (itemStack.itemDamage >= 0 && itemStack.itemDamage < Variants.values().size && Variants.values()[itemStack.itemDamage].iridescent)
            Color.HSBtoRGB((Botania.proxy.worldElapsedTicks * 2L % 360L).toFloat() / 360.0f, 1.0f, 1.0f)
        else
            0xFFFFFF
    }

    override fun getSubItems(itemIn: Item, tab: CreativeTabs?, subItems: MutableList<ItemStack>) {
    }
}
