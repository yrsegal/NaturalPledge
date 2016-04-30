package shadowfox.botanicaladdons.client.core

import net.minecraft.block.Block
import net.minecraft.block.properties.IProperty
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.ItemMeshDefinition
import net.minecraft.client.renderer.block.model.ModelBakery
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.client.renderer.block.statemap.StateMap
import net.minecraft.client.renderer.color.IBlockColor
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.item.EnumRarity
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.util.IStringSerializable
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.common.FMLLog
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import shadowfox.botanicaladdons.common.lib.LibMisc
import java.util.*

/**
 * @author WireSegal
 * Created at 2:12 PM on 3/20/16.
 */
object ModelHandler {

    interface IVariantHolder {
        @SideOnly(Side.CLIENT)
        fun getCustomMeshDefinition(): ItemMeshDefinition?

        val variants: Array<out String>
    }

    interface IExtraVariantHolder : IVariantHolder {
        val extraVariants: Array<out String>
    }

    interface IBABlock : IVariantHolder {
        val variantEnum: Class<Enum<*>>?
        val ignoredProperties: Array<IProperty<*>>?
        val bareName: String

        fun getBlockRarity(stack: ItemStack): EnumRarity
    }

    interface IColorProvider {
        @SideOnly(Side.CLIENT)
        fun getColor(): IItemColor?
    }

    interface IBlockColorProvider : IColorProvider {
        @SideOnly(Side.CLIENT)
        fun getBlockColor(): IBlockColor?
    }

    interface ICustomLogHolder : IVariantHolder {
        fun customLog(): String

        fun customLogVariant(variantId: Int, variant: String): String

        fun shouldLogForVariant(variantId: Int, variant: String): Boolean

        val sortingVariantCount: Int
    }

    val variantCache = ArrayList<IVariantHolder>()

    val resourceLocations = HashMap<String, ModelResourceLocation>()

    fun preInit() {
        FMLLog.info("BA | Starting model load")
        for (holder in variantCache.sortedBy { (255-getVariantCount(it)).toChar() + if (it is ItemBlock) "b" else "I" + (it as Item).registryName.resourcePath }) {
            registerModels(holder)
        }
    }

    fun getVariantCount(holder: IVariantHolder) = if (holder is ICustomLogHolder) holder.sortingVariantCount else holder.variants.size

    fun init() {
        val itemColors = Minecraft.getMinecraft().itemColors
        val blockColors = Minecraft.getMinecraft().blockColors
        for (holder in variantCache) {
            if (holder is IColorProvider && holder is Item) {
                val color = holder.getColor()
                if (color != null)
                    itemColors.registerItemColorHandler(color, holder)
            }
            if (holder is ItemBlock && holder.getBlock() is IBlockColorProvider) {
                val color = (holder.getBlock() as IBlockColorProvider).getBlockColor()
                if (color != null)
                    blockColors.registerBlockColorHandler(color, holder.getBlock())
            }
        }
    }

    // The following is a blatant copy of Psi's ModelHandler.

    fun registerModels(holder: IVariantHolder) {
        val def = holder.getCustomMeshDefinition()
        if (def != null) {
            ModelLoader.setCustomMeshDefinition(holder as Item, def)
        } else {
            val i = holder as Item
            registerModels(i, holder.variants, false)
            if (holder is IExtraVariantHolder) {
                registerModels(i, holder.extraVariants, true)
            }
        }

    }

    fun registerModels(item: Item, variants: Array<out String>, extra: Boolean) {
        if (item is ItemBlock && item.getBlock() is IBABlock) {
            val i = item.getBlock() as IBABlock
            val name = i.variantEnum
            val loc = i.ignoredProperties
            if (loc != null && loc.size > 0) {
                val builder = StateMap.Builder()
                val var7 = loc
                val var8 = loc.size

                for (var9 in 0..var8 - 1) {
                    val p = var7[var9]
                    builder.ignore(p)
                }

                ModelLoader.setCustomStateMapper(i as Block, builder.build())
            }

            if (name != null) {
                registerVariantsDefaulted(item, i as Block, name, "variant")
                return
            }
        }

        for (var11 in variants.indices) {
            if (var11 == 0) {
                var print = "   | Registering "
                if (variants[var11] != item.registryName.resourcePath || variants.size != 1)
                    print += "variant" + if (variants.size == 1) "" else "s" + " of "
                print += if (item is ItemBlock) "block" else "item"
                print += " ${item.registryName.resourcePath}"
                FMLLog.info(print)
                if (item is ICustomLogHolder)
                    FMLLog.info(item.customLog())
            }
            if ((variants[var11] != item.registryName.resourcePath || variants.size != 1)) {
                if (item is ICustomLogHolder) {
                    if (item.shouldLogForVariant(var11 + 1, variants[var11]))
                        FMLLog.info(item.customLogVariant(var11 + 1, variants[var11]))
                } else
                    FMLLog.info("   |  Variant #${var11 + 1}: ${variants[var11]}")
            }

            val var13 = ModelResourceLocation(ResourceLocation(LibMisc.MOD_ID, variants[var11]).toString(), "inventory")
            if (!extra) {
                ModelLoader.setCustomModelResourceLocation(item, var11, var13)
                resourceLocations.put(getKey(item, var11), var13)
            } else {
                ModelBakery.registerItemVariants(item, var13)
                resourceLocations.put(variants[var11], var13)
            }
        }

    }

    private fun registerVariantsDefaulted(item: Item, block: Block, enumclazz: Class<*>, variantHeader: String) {
        val locName = Block.blockRegistry.getNameForObject(block).toString()
        if (enumclazz.enumConstants != null)
            for (e in enumclazz.enumConstants) {
                if (e is IStringSerializable && e is Enum<*>) {
                    val variantName = variantHeader + "=" + e.name
                    val loc = ModelResourceLocation(locName, variantName)
                    val i = e.ordinal
                    ModelLoader.setCustomModelResourceLocation(item, i, loc)
                    resourceLocations.put(getKey(item, i), loc)
                }
            }

    }

    private fun getKey(item: Item, meta: Int): String {
        return "i_" + item.registryName + "@" + meta
    }
}
