package shadowfox.botanicaladdons.common.items.armor

import com.teamwizardry.librarianlib.features.base.item.IGlowingItem
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper
import net.minecraft.client.model.ModelBiped
import net.minecraft.client.renderer.block.model.IBakedModel
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.init.MobEffects
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.potion.Potion
import net.minecraft.util.DamageSource
import net.minecraft.util.NonNullList
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.client.render.entity.ModelArmorSunmaker
import shadowfox.botanicaladdons.common.items.ModItems
import shadowfox.botanicaladdons.common.items.ModItems.ECLIPSE
import shadowfox.botanicaladdons.common.items.ModItems.SUNMAKER
import shadowfox.botanicaladdons.common.items.base.ItemBaseArmor
import shadowfox.botanicaladdons.common.items.bauble.faith.ItemRagnarokPendant
import shadowfox.botanicaladdons.common.network.ManastormLightningMessage
import shadowfox.botanicaladdons.common.potions.ModPotions
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.core.helper.Vector3
import vazkii.botania.common.item.equipment.tool.ToolCommons
import java.util.*

/**
 * @author WireSegal
 * Created at 5:09 PM on 4/2/17.
 */
class ItemSunmakerArmor(name: String, type: EntityEquipmentSlot) : ItemBaseArmor(name, type, SUNMAKER), IGlowingItem {
    override fun getSubItems(tab: CreativeTabs?, subItems: NonNullList<ItemStack>) {
        if (ItemRagnarokPendant.hasAwakenedRagnarok())
            super.getSubItems(tab, subItems)
    }

    @SideOnly(Side.CLIENT)
    override fun transformToGlow(itemStack: ItemStack, model: IBakedModel) = IGlowingItem.Helper.wrapperBake(model, false, 1)
    @SideOnly(Side.CLIENT)
    override fun shouldDisableLightingForGlow(itemStack: ItemStack, model: IBakedModel) = true

    override fun getArmorTexture(type: String?) = "${LibMisc.MOD_ID}:textures/armor/sunmaker_layer_${if (type == "glow") 1 else 0}.png"

    override fun makeArmorModel(slot: EntityEquipmentSlot) = ModelArmorSunmaker(slot)

    override val armorSetStacks: ArmorSet by lazy {
        ArmorSet(ModItems.sunmakerHelm, ModItems.sunmakerChest, ModItems.sunmakerLegs, ModItems.sunmakerBoots)
    }

    override val manaDiscount: Float
        get() = 0.2f

    override fun addArmorSetDescription(list: MutableList<String>) {
        TooltipHelper.addToTooltip(list, "$modId.armorset.$matName.desc")
        TooltipHelper.addToTooltip(list, "$modId.armorset.$matName.desc1")
    }

    override fun onArmorTick(world: World, player: EntityPlayer, stack: ItemStack) {
        super.onArmorTick(world, player, stack)
        if (!world.isRemote && armorSetStacks.helm == this && world.totalWorldTime % 5 == 0L && hasFullSet(player)) {
            val mainHand = player.heldItemMainhand
            if (mainHand.isItemDamaged && world.rand.nextDouble() < 0.25 && ManaItemHandler.requestManaExact(mainHand, player, 300, true))
                mainHand.damageItem(-1, player)

            val offHand = player.heldItemOffhand
            if (offHand.isItemDamaged && world.rand.nextDouble() < 0.25 && ManaItemHandler.requestManaExact(offHand, player, 300, true))
                offHand.damageItem(-1, player)


            player.removePotionEffect(MobEffects.SLOWNESS)
            player.removePotionEffect(MobEffects.MINING_FATIGUE)
            player.removePotionEffect(MobEffects.NAUSEA)
            player.removePotionEffect(MobEffects.INVISIBILITY)
            player.removePotionEffect(MobEffects.BLINDNESS)
            player.removePotionEffect(MobEffects.WEAKNESS)
            player.removePotionEffect(MobEffects.POISON)
            player.removePotionEffect(MobEffects.WITHER)
            player.removePotionEffect(ModPotions.rooted)
        }
    }
}
