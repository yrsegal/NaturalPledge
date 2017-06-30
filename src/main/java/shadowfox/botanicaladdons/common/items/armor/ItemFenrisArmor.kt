package shadowfox.botanicaladdons.common.items.armor

import com.google.common.collect.Multimap
import com.teamwizardry.librarianlib.features.base.item.IGlowingItem
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper
import net.minecraft.client.renderer.block.model.IBakedModel
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.Entity
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingAttackEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.client.render.entity.ModelArmorFenris
import shadowfox.botanicaladdons.common.items.ModItems
import shadowfox.botanicaladdons.common.items.ModItems.FENRIS
import shadowfox.botanicaladdons.common.items.base.ItemBaseArmor
import shadowfox.botanicaladdons.common.items.bauble.faith.ItemRagnarokPendant
import shadowfox.botanicaladdons.common.items.weapons.ItemNightscourge
import java.util.*

/**
 * @author WireSegal
 * Created at 5:09 PM on 4/2/17.
 */
class ItemFenrisArmor(name: String, type: EntityEquipmentSlot) : ItemBaseArmor(name, type, FENRIS), IGlowingItem {
    companion object {
        val TAG_ACTIVE = "active"

        init {
            MinecraftForge.EVENT_BUS.register(this)
        }

        @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
        fun onLivingAttack(e: LivingAttackEvent) {
            val attacker = e.source.immediateSource
            if (e.source.damageType == "player" && attacker is EntityPlayer && (
                    (ModItems.fenrisHelm.hasFullSet(attacker) && attacker.heldItemMainhand.isEmpty) ||
                            (attacker.heldItemMainhand.item is ItemNightscourge))) {
                e.source.setDamageBypassesArmor()
                e.isCanceled = false
            }
        }
    }

    override fun makeArmorModel(slot: EntityEquipmentSlot) = ModelArmorFenris(slot)


    override fun getArmorTexture(type: String?) = "${LibMisc.MOD_ID}:textures/armor/fenris_layer_${if (type == "glow") 1 else 0}.png"

    @SideOnly(Side.CLIENT)
    override fun transformToGlow(itemStack: ItemStack, model: IBakedModel) = IGlowingItem.Helper.wrapperBake(model, false, 1)
    @SideOnly(Side.CLIENT)
    override fun shouldDisableLightingForGlow(itemStack: ItemStack, model: IBakedModel) = true

    override fun getSubItems(itemIn: Item, tab: CreativeTabs?, subItems: NonNullList<ItemStack>) {
        if (ItemRagnarokPendant.hasAwakenedRagnarok())
            super.getSubItems(itemIn, tab, subItems)
    }

    override val armorSetStacks: ArmorSet by lazy {
        ArmorSet(ModItems.fenrisHelm, ModItems.fenrisChest, ModItems.fenrisLegs, ModItems.fenrisBoots)
    }

    override val manaDiscount: Float
        get() = 0.2f

    override fun addArmorSetDescription(list: MutableList<String>) {
        TooltipHelper.addToTooltip(list, "$modId.armorset.$matName.desc")
        TooltipHelper.addToTooltip(list, "$modId.armorset.$matName.desc1")
    }

    override fun onArmorTick(world: World, player: EntityPlayer, stack: ItemStack) {
        super.onArmorTick(world, player, stack)
        if (!world.isRemote) {
            if (hasFullSet(player))
                ItemNBTHelper.setBoolean(stack, TAG_ACTIVE, true)
            else
                ItemNBTHelper.setBoolean(stack, TAG_ACTIVE, false)
        }
    }

    override fun onUpdate(stack: ItemStack, world: World, player: Entity, slot: Int, selected: Boolean) {
        super.onUpdate(stack, world, player, slot, selected)
        if (!world.isRemote && slot < 100)
            ItemNBTHelper.setBoolean(stack, TAG_ACTIVE, false)
    }

    override fun getAttributeModifiers(slot: EntityEquipmentSlot?, stack: ItemStack): Multimap<String, AttributeModifier> {
        val map = super.getAttributeModifiers(slot, stack)

        if (slot == armorType && ItemNBTHelper.getBoolean(stack, TAG_ACTIVE, false)) {
            val uuid = UUID((unlocalizedName + slot.toString()).hashCode().toLong(), 0L)
            map.put(SharedMonsterAttributes.ATTACK_DAMAGE.name, AttributeModifier(uuid, "Fenris modifier " + slot?.name, 0.5, 0))
        }

        return map
    }
}
