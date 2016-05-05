package shadowfox.botanicaladdons.common.items.base

import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.OreDictionary
import shadowfox.botanicaladdons.api.lib.LibOreDict
import shadowfox.botanicaladdons.client.core.ModelHandler
import shadowfox.botanicaladdons.common.BotanicalAddons

/**
 * @author WireSegal
 * Created at 9:27 AM on 4/29/16.
 */
open class ItemRainbow(name: String, val rainbow: Boolean) : ItemMod(name, *Array(16 + if (rainbow) 1 else 0, { name + LibOreDict.COLORS[it] })), ModelHandler.IColorProvider, ModelHandler.ICustomLogHolder {
    companion object {
        fun String.capitalizeFirst(): String {
            if (this.length == 0) return this
            return this.slice(0..0).capitalize() + this.slice(1..this.length - 1)
        }
    }

    val types = 16 + if (rainbow) 1 else 0

    fun mapOreDict(keys: Array<String>): ItemRainbow {
        if (keys.size < types) return this
        for (i in 0..types - 1)
            OreDictionary.registerOre(keys[i], ItemStack(this, 1, i))
        return this
    }

    fun mapOreKey(key: String): ItemRainbow {
        OreDictionary.registerOre(key, ItemStack(this, 1, OreDictionary.WILDCARD_VALUE))
        return this
    }

    override fun getColor() = IItemColor { itemStack, i ->
        if (i == 0) when (itemStack.metadata) {
            16 -> BotanicalAddons.proxy.rainbow().rgb
            else -> EnumDyeColor.byMetadata(itemStack.metadata).mapColor.colorValue
        } else 0xFFFFFF
    }

    override fun customLog() = "   |  Variants by dye color${if (rainbow) " and rainbow" else ""}"

    override fun customLogVariant(variantId: Int, variant: String) = ""

    override fun shouldLogForVariant(variantId: Int, variant: String) = false

    override val sortingVariantCount: Int
        get() = 1
}
