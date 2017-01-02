package shadowfox.botanicaladdons.common.items

import com.teamwizardry.librarianlib.common.base.item.ItemMod
import net.minecraft.item.EnumRarity
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import shadowfox.botanicaladdons.common.lib.capitalizeFirst
import shadowfox.botanicaladdons.common.lib.lowercaseFirst
import vazkii.botania.api.BotaniaAPI

/**
 * @author WireSegal
 * Created at 11:14 PM on 5/20/16.
 */
class ItemResource(name: String) : ItemMod(name, *Variants.variants) {
    enum class Variants(val awakenable: Boolean) {
        THUNDER_STEEL, LIFE_ROOT, AQUAMARINE, THUNDERNUGGET(false);

        constructor() : this(true)

        override fun toString(): String {
            return this.name.toLowerCase().split("_").joinToString("", transform = String::capitalizeFirst).lowercaseFirst()
        }

        companion object {
            val variants: Array<String> by lazy {
                val out = arrayListOf<String>()
                for (variant in values()) {
                    out.add(variant.toString())
                    if (variant.awakenable) out.add(variant.toString() + "Active")
                }
                out.toTypedArray()
            }

            val variantPairs: Array<Pair<Variants, Boolean>> by lazy {
                val out = arrayListOf<Pair<Variants, Boolean>>()
                for (variant in values()) {
                    out.add(variant to false)
                    if (variant.awakenable) out.add(variant to true)
                }
                out.toTypedArray()
            }

        }
    }

    companion object {
        fun of(v: Variants, active: Boolean = false, size: Int = 1) = ItemStack(ModItems.resource, size, v.ordinal * 2 + if (active && v.awakenable) 1 else 0)

        fun variantFor(stack: ItemStack) = Variants.variantPairs.elementAtOrNull(stack.itemDamage)


    }

    override fun getRarity(stack: ItemStack): EnumRarity? {
        return if (variantFor(stack)?.second ?: false) BotaniaAPI.rarityRelic else EnumRarity.COMMON
    }

    @SideOnly(Side.CLIENT)
    override fun hasEffect(stack: ItemStack): Boolean {
        return variantFor(stack)?.second ?: false || super.hasEffect(stack)
    }
}
