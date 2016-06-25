package shadowfox.botanicaladdons.common.items.sacred

import net.minecraft.block.BlockDispenser
import net.minecraft.client.renderer.ItemMeshDefinition
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.dispenser.BehaviorProjectileDispense
import net.minecraft.dispenser.IPosition
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.IProjectile
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.projectile.EntityArrow
import net.minecraft.inventory.IInventory
import net.minecraft.item.EnumRarity
import net.minecraft.item.Item
import net.minecraft.item.ItemArrow
import net.minecraft.item.ItemStack
import net.minecraft.stats.Achievement
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.client.core.ModelHandler
import shadowfox.botanicaladdons.client.core.TooltipHelper
import shadowfox.botanicaladdons.common.achievements.ModAchievements
import shadowfox.botanicaladdons.common.core.tab.ModTab
import shadowfox.botanicaladdons.common.entity.EntitySealedArrow
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.common.achievement.ICraftAchievement

/**
 * @author WireSegal
 * Created at 11:49 AM on 5/22/16.
 */
class ItemSealerArrow(name: String, vararg variants: String) : ItemArrow(), ModelHandler.IVariantHolder, ICraftAchievement {

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
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, object : BehaviorProjectileDispense() {
            override fun getProjectileEntity(worldIn: World, position: IPosition, stackIn: ItemStack): IProjectile {
                val arrow = EntitySealedArrow(worldIn, position.x, position.y, position.z)
                arrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED
                return arrow
            }
        })
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

    override fun getUnlocalizedName(stack: ItemStack?): String {
        val dmg = stack!!.itemDamage
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
        return EntitySealedArrow(world, player)
    }

    override fun getRarity(stack: ItemStack?): EnumRarity? {
        return BotaniaAPI.rarityRelic
    }

    override fun isInfinite(stack: ItemStack?, bow: ItemStack?, player: EntityPlayer?) = false

    override fun getAchievementOnCraft(p0: ItemStack?, p1: EntityPlayer?, p2: IInventory?): Achievement? {
        return ModAchievements.sacredAqua
    }
}
