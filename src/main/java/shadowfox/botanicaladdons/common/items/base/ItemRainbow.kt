package shadowfox.botanicaladdons.common.items.base

import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.FMLLog
import net.minecraftforge.oredict.OreDictionary
import shadowfox.botanicaladdons.client.core.ModelHandler

/**
 * @author WireSegal
 * Created at 9:27 AM on 4/29/16.
 */
open class ItemRainbow(name: String) : ItemMod(name, *Array(16, {name + EnumDyeColor.byMetadata(it).unlocalizedName.capitalizeFirst()})), ModelHandler.IColorProvider, ModelHandler.ICustomLogHolder {
    companion object {
        fun String.capitalizeFirst(): String {
            if (this.length == 0) return this
            return this.slice(0..0).capitalize() + this.slice(1..this.length - 1)
        }
    }

    fun mapOreDict(keys: Array<String>) : ItemRainbow {
        if (keys.size < 16) return this
        for (i in 0..15)
            OreDictionary.registerOre(keys[i], ItemStack(this, 1, i))
        return this
    }

    override fun getColor() = IItemColor { itemStack, i ->
        if (i == 0) EnumDyeColor.byMetadata(itemStack.metadata).mapColor.colorValue
        else 0xFFFFFF
    }

    override fun customLog() = "   |  Variants by dye color"

    override fun customLogVariant(variantId: Int, variant: String) = ""

    override fun shouldLogForVariant(variantId: Int, variant: String) = false

    override val sortingVariantCount: Int
        get() = 1
}
