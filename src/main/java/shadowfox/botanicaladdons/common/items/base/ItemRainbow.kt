package shadowfox.botanicaladdons.common.items.base

import com.teamwizardry.librarianlib.features.base.item.IItemColorProvider
import com.teamwizardry.librarianlib.features.base.item.ItemMod
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.OreDictionary
import shadowfox.botanicaladdons.common.BotanicalAddons
import shadowfox.botanicaladdons.common.lib.LibOreDict

/**
 * @author WireSegal
 * Created at 9:27 AM on 4/29/16.
 */
open class ItemRainbow(name: String, val rainbow: Boolean) : ItemMod(name, *Array(16 + if (rainbow) 1 else 0, { name + LibOreDict.COLORS[it] })), IItemColorProvider {
    val types = 16 + if (rainbow) 1 else 0

    open fun mapOreDict(keys: Array<String>): ItemRainbow {
        if (keys.size < types) return this
        for (i in 0..types - 1)
            OreDictionary.registerOre(keys[i], ItemStack(this, 1, i))
        return this
    }

    open fun mapOreKey(key: String): ItemRainbow {
        for (variant in variants.indices)
            OreDictionary.registerOre(key, ItemStack(this, 1, variant))
        return this
    }

    override val itemColorFunction: ((ItemStack, Int) -> Int)?
        get() = { itemStack, i ->
            if (i == 0) when (itemStack.metadata) {
                16 -> BotanicalAddons.PROXY.rainbow().rgb
                else -> EnumDyeColor.byMetadata(itemStack.metadata).mapColor.colorValue
            } else 0xFFFFFF
        }
}
