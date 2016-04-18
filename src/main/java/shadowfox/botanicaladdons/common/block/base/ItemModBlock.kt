package shadowfox.botanicaladdons.common.block.base

import net.minecraft.block.Block
import net.minecraft.client.renderer.ItemMeshDefinition
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.EnumRarity
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.GameRegistry
import shadowfox.botanicaladdons.client.core.ModelHandler
import shadowfox.botanicaladdons.common.items.base.ItemMod
import shadowfox.botanicaladdons.common.lib.LibMisc

/**
 * @author WireSegal
 * Created at 5:48 PM on 3/20/16.
 */
class ItemModBlock(block: Block) : ItemBlock(block), ModelHandler.IVariantHolder, ModelHandler.IColorProvider {

    private val psiBlock: ModelHandler.IBABlock

    init {
        this.psiBlock = block as ModelHandler.IBABlock
        if (this.variants.size > 1) {
            this.setHasSubtypes(true)
        }
        ModelHandler.variantCache.add(this)
    }

    override fun getMetadata(damage: Int): Int {
        return damage
    }

    override fun setUnlocalizedName(par1Str: String): ItemBlock {
        val rl = ResourceLocation(LibMisc.MOD_ID, par1Str)
        GameRegistry.register(this, rl)
        return super.setUnlocalizedName(par1Str)
    }

    override fun getUnlocalizedName(par1ItemStack: ItemStack?): String {
        val dmg = par1ItemStack!!.itemDamage
        val variants = this.variants
        val name: String
        if (dmg >= variants.size) {
            name = this.psiBlock.bareName
        } else {
            name = variants[dmg]
        }

        return "tile.${LibMisc.MOD_ID}:" + name
    }

    override fun getSubItems(itemIn: Item, tab: CreativeTabs?, subItems: MutableList<ItemStack>) {
        val variants = this.variants

        for (i in variants.indices) {
            subItems.add(ItemStack(itemIn, 1, i))
        }

    }

    override val customMeshDefinition: ItemMeshDefinition?
        get() = this.psiBlock.customMeshDefinition

    override val variants: Array<out String>
        get() = this.psiBlock.variants

    override val color: IItemColor?
        get() = if (this.psiBlock is ModelHandler.IColorProvider) this.psiBlock.color else null

    override fun getRarity(stack: ItemStack): EnumRarity? {
        return this.psiBlock.getBlockRarity(stack)
    }
}

