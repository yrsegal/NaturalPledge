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
import shadowfox.botanicaladdons.common.core.tab.ModCreativeTab
import shadowfox.botanicaladdons.common.core.tab.ModTabs

/**
 * @author WireSegal
 * Created at 5:45 PM on 3/20/16.
 */
open class BlockMod(name: String, materialIn: Material, vararg variants: String) : Block(materialIn), ModelHandler.IBABlock {
    override var variants: Array<out String> = variants
    override val bareName: String

    init {
        this.variants = variants
        if (variants.size == 0) {
            this.variants = arrayOf(name)
        }
        this.bareName = name
        this.unlocalizedName = name
        if (hasItem)
            creativeTab?.set(this)
    }

    open val creativeTab: ModCreativeTab?
        get() = ModTabs.TabDivinity

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
