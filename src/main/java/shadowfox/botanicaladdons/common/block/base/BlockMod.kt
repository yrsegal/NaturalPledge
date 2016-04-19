package shadowfox.botanicaladdons.common.block.base

import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.properties.IProperty
import net.minecraft.client.renderer.ItemMeshDefinition
import net.minecraft.item.EnumRarity
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.GameRegistry
import shadowfox.botanicaladdons.client.core.ModelHandler
import shadowfox.botanicaladdons.common.core.CreativeTab
import shadowfox.botanicaladdons.common.lib.LibMisc

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
        CreativeTab.set(this)
    }

    override fun setUnlocalizedName(name: String): Block {
        super.setUnlocalizedName(name)
        setRegistryName(name)
        GameRegistry.register(this)
        GameRegistry.register(ItemModBlock(this), ResourceLocation(LibMisc.MOD_ID, name))
        return this
    }

    override val customMeshDefinition: ItemMeshDefinition?
        get() = null

    override val ignoredProperties: Array<IProperty<*>>?
        get() = arrayOf()

    override val variantEnum: Class<Enum<*>>?
        get() = null

    override fun getBlockRarity(stack: ItemStack): EnumRarity {
        return EnumRarity.COMMON
    }
}
