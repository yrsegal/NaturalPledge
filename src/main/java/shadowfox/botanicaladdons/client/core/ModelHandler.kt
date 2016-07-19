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
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.apache.logging.log4j.Logger
import java.util.*

/**
 * @author WireSegal
 * Created at 2:12 PM on 3/20/16.
 */
object ModelHandler {

    lateinit var modName: String
    lateinit var logger: Logger
    var debug = false
    val namePad: String by lazy {
        "".padEnd(modName.length)
    }

    interface IVariantHolder {
        @SideOnly(Side.CLIENT)
        fun getCustomMeshDefinition(): ItemMeshDefinition?

        val variants: Array<out String>
    }

    interface IExtraVariantHolder : IVariantHolder {
        val extraVariants: Array<out String>
    }

    interface IModBlock : IVariantHolder {
        val variantEnum: Class<Enum<*>>?
        val ignoredProperties: Array<IProperty<*>>?
        val bareName: String

        fun getBlockRarity(stack: ItemStack): EnumRarity
    }

    interface IItemColorProvider {
        @SideOnly(Side.CLIENT)
        fun getItemColor(): IItemColor?
    }

    interface IBlockColorProvider : IItemColorProvider {
        @SideOnly(Side.CLIENT)
        fun getBlockColor(): IBlockColor?

        @SideOnly(Side.CLIENT)
        override fun getItemColor(): IItemColor? = null
    }

    interface ICustomLogHolder : IVariantHolder {
        fun customLog(): String

        fun customLogVariant(variantId: Int, variant: String): String

        fun shouldLogForVariant(variantId: Int, variant: String): Boolean

        val sortingVariantCount: Int
    }

    val variantCache = ArrayList<IVariantHolder>()

    val resourceLocations = HashMap<String, ModelResourceLocation>()

    fun preInit(name: String, shouldDebug: Boolean, logger: Logger) {
        modName = name
        debug = shouldDebug
        this.logger = logger
        log("$modName | Starting model load")
        for (holder in variantCache.sortedBy { (255 - getVariantCount(it)).toChar() + if (it is ItemBlock) "b" else "I" + if (it is Item) it.registryName.resourcePath else "" }) {
            registerModels(holder)
        }
    }

    fun getVariantCount(holder: IVariantHolder) = if (holder is ICustomLogHolder) holder.sortingVariantCount else holder.variants.size

    fun init() {
        val itemColors = Minecraft.getMinecraft().itemColors
        val blockColors = Minecraft.getMinecraft().blockColors
        for (holder in variantCache) {
            if (holder is IItemColorProvider && holder is Item) {
                val color = holder.getItemColor()
                if (color != null)
                    itemColors.registerItemColorHandler(color, holder)
            }

            if (holder is ItemBlock && holder.getBlock() is IBlockColorProvider) {
                val color = (holder.getBlock() as IBlockColorProvider).getBlockColor()
                if (color != null)
                    blockColors.registerBlockColorHandler(color, holder.getBlock())
            } else if (holder is Block && holder is IBlockColorProvider) {
                val color = holder.getBlockColor()
                if (color != null)
                    blockColors.registerBlockColorHandler(color, holder)
            }
        }
    }

    // The following is a blatant copy of Psi's ModelHandler.

    fun registerModels(holder: IVariantHolder) {
        val def = holder.getCustomMeshDefinition()
        if (def != null && holder is Item) {
            ModelLoader.setCustomMeshDefinition(holder, def)
        } else {
            registerModels(holder, holder.variants, false)
        }
        if (holder is IExtraVariantHolder) {
            registerModels(holder, holder.extraVariants, true)
        }
    }

    fun registerModels(item: IVariantHolder, variants: Array<out String>, extra: Boolean) {
        if (item is ItemBlock && item.getBlock() is IModBlock) {
            val i = item.getBlock() as IModBlock
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
        } else if (item is IModBlock) {
            val loc = item.ignoredProperties
            if (loc != null && loc.size > 0) {
                val builder = StateMap.Builder()
                val var7 = loc
                val var8 = loc.size

                for (var9 in 0..var8 - 1) {
                    val p = var7[var9]
                    builder.ignore(p)
                }

                ModelLoader.setCustomStateMapper(item as Block, builder.build())
            }
        }

        if (item is Item) {
            for (variant in variants.withIndex()) {
                if (variant.index == 0) {
                    var print = "$namePad | Registering "
                    if (variant.value != item.registryName.resourcePath || variants.size != 1)
                        print += "variant" + if (variants.size == 1) "" else "s" + " of "
                    print += if (item is ItemBlock) "block" else "item"
                    print += " ${item.registryName.resourcePath}"
                    log(print)
                    if (item is ICustomLogHolder)
                        log(item.customLog())
                }
                if ((variant.value != item.registryName.resourcePath || variants.size != 1)) {
                    if (item is ICustomLogHolder) {
                        if (item.shouldLogForVariant(variant.index + 1, variant.value))
                            log(item.customLogVariant(variant.index + 1, variant.value))
                    } else
                        log("$namePad |  Variant #${variant.index + 1}: ${variant.value}")
                }

                val model = ModelResourceLocation(ResourceLocation(modName, variant.value).toString(), "inventory")
                if (!extra) {
                    ModelLoader.setCustomModelResourceLocation(item, variant.index, model)
                    resourceLocations.put(getKey(item, variant.index), model)
                } else {
                    ModelBakery.registerItemVariants(item, model)
                    resourceLocations.put(variant.value, model)
                }
            }
        }

    }

    private fun registerVariantsDefaulted(item: Item, block: Block, enumclazz: Class<*>, variantHeader: String) {
        val locName = Block.REGISTRY.getNameForObject(block).toString()
        if (enumclazz.enumConstants != null)
            for (e in enumclazz.enumConstants) {
                if (e is IStringSerializable && e is Enum<*>) {
                    val variantName = variantHeader + "=" + e.name

                    if (e.ordinal == 0) {
                        var print = "${namePad} | Registering "
                        if (variantName != item.registryName.resourcePath || enumclazz.enumConstants.size != 1)
                            print += "variant" + (if (enumclazz.enumConstants.size == 1) "" else "s") + " of "
                        print += if (item is ItemBlock) "block" else "item"
                        print += " " + item.registryName.resourcePath
                        log(print)
                        if (item is ICustomLogHolder)
                            log(item.customLog())
                    }
                    if (e.name != item.registryName.resourcePath || enumclazz.enumConstants.size != 1) {
                        if (item is ICustomLogHolder) {
                            if (item.shouldLogForVariant(e.ordinal, variantName))
                                log(item.customLogVariant(e.ordinal + 1, variantName))
                        } else
                            log("$namePad |  Variant #${e.ordinal + 1}: $variantName")
                    }

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

    fun log(text: String) {
        if (debug) logger.info(text)
    }
}
