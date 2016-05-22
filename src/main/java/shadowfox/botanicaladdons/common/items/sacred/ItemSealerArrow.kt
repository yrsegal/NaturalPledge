package shadowfox.botanicaladdons.common.items.sacred

import net.minecraft.client.renderer.ItemMeshDefinition
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.projectile.EntityArrow
import net.minecraft.entity.projectile.EntityTippedArrow
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemArrow
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionUtils
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.client.core.ModelHandler
import shadowfox.botanicaladdons.client.core.TooltipHelper
import shadowfox.botanicaladdons.common.core.tab.ModTab
import shadowfox.botanicaladdons.common.potions.ModPotions
import shadowfox.botanicaladdons.common.potions.base.ModPotionEffect

/**
 * @author WireSegal
 * Created at 11:49 AM on 5/22/16.
 */
class ItemSealerArrow(name: String, vararg variants: String) : ItemArrow(), ModelHandler.IVariantHolder {

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

        val arrowStack: ItemStack by lazy {
            PotionUtils.appendEffects(ItemStack(Items.TIPPED_ARROW), listOf(ModPotionEffect(ModPotions.featherweight, 900)))
        }
    }

    override val variants: Array<out String>

    @SideOnly(Side.CLIENT)
    override fun getCustomMeshDefinition(): ItemMeshDefinition? = null

    private val bareName: String

    init {
        var variantTemp = variants
        this.unlocalizedName = name

        ModTab.set(this)

        if (variantTemp.size > 1) {
            this.setHasSubtypes(true)
        }

        if (variantTemp.size == 0) {
            variantTemp = arrayOf(name)
        }

        this.bareName = name
        this.variants = variantTemp
        ModelHandler.variantCache.add(this)
    }

    override fun setUnlocalizedName(name: String): Item {
        val rl = ResourceLocation(LibMisc.MOD_ID, name)
        GameRegistry.register(this, rl)
        return super.setUnlocalizedName(name)
    }

    override fun getUnlocalizedName(par1ItemStack: ItemStack?): String {
        val dmg = par1ItemStack!!.itemDamage
        val variants = this.variants
        val name: String
        if (dmg >= variants.size) {
            name = this.bareName
        } else {
            name = variants[dmg]
        }

        return "item.${LibMisc.MOD_ID}:" + name
    }

    override fun getSubItems(itemIn: Item, tab: CreativeTabs?, subItems: MutableList<ItemStack>) {
        for (i in 0..this.variants.size - 1) {
            subItems.add(ItemStack(itemIn, 1, i))
        }

    }

    override fun createArrow(world: World, stack: ItemStack, player: EntityLivingBase?): EntityArrow? {
        val entity = object : EntityTippedArrow(world, player) {
            override fun getArrowStack(): ItemStack? {
                return ItemStack(this@ItemSealerArrow)
            }
        }
        entity.setPotionEffect(arrowStack)
        return entity
    }
}
