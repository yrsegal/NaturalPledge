package shadowfox.botanicaladdons.common.block.base

import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.properties.IProperty
import net.minecraft.item.EnumRarity
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.client.core.ModelHandler
import shadowfox.botanicaladdons.client.core.TooltipHelper
import shadowfox.botanicaladdons.common.core.tab.ModTab

/**
 * @author WireSegal
 * Created at 5:45 PM on 3/20/16.
 */
open class BlockMod(name: String, materialIn: Material, vararg variants: String) : Block(materialIn), ModelHandler.IBABlock {
    override var variants: Array<out String> = variants
    override val bareName: String

    companion object {
        fun tooltipIfShift(tooltip: MutableList<String>, r: () -> Unit) {
            TooltipHelper.tooltipIfShift(tooltip, r)
        }

        fun addToTooltip(tooltip: MutableList<String>, s: String, vararg format: Any) {
            TooltipHelper.addToTooltip(tooltip, s, *format)
        }

        fun local(s: String): String {
            return TooltipHelper.local(s)
        }
    }

    init {
        this.variants = variants
        if (variants.size == 0) {
            this.variants = arrayOf(name)
        }
        this.bareName = name
        this.unlocalizedName = name
        if (hasItem)
            ModTab.set(this)
    }

    override fun setUnlocalizedName(name: String): Block {
        super.setUnlocalizedName(name)
        setRegistryName(name)
        GameRegistry.register(this)
        if (hasItem)
            GameRegistry.register(item, ResourceLocation(LibMisc.MOD_ID, name))
        return this
    }

    open val item: ItemBlock
        get() = ItemModBlock(this)

    open val hasItem: Boolean
        get() = true

    @SideOnly(Side.CLIENT)
    override fun getCustomMeshDefinition() = null

    override val ignoredProperties: Array<IProperty<*>>?
        get() = arrayOf()

    override val variantEnum: Class<Enum<*>>?
        get() = null

    override fun getBlockRarity(stack: ItemStack): EnumRarity {
        return EnumRarity.COMMON
    }
}
