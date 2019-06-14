package com.wiresegal.naturalpledge.common.items.armor

import com.google.common.collect.Multimap
import com.teamwizardry.librarianlib.features.base.item.IGlowingItem
import com.teamwizardry.librarianlib.features.helpers.getNBTBoolean
import com.teamwizardry.librarianlib.features.helpers.setNBTBoolean
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper
import com.wiresegal.naturalpledge.api.lib.LibMisc
import com.wiresegal.naturalpledge.client.render.entity.ModelArmorFenris
import com.wiresegal.naturalpledge.common.items.ModItems
import com.wiresegal.naturalpledge.common.items.ModItems.FENRIS
import com.wiresegal.naturalpledge.common.items.base.ItemBaseArmor
import com.wiresegal.naturalpledge.common.items.weapons.ItemNightscourge
import net.minecraft.client.renderer.block.model.IBakedModel
import net.minecraft.entity.Entity
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingAttackEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
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
                e.source.setDamageIsAbsolute()
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
                stack.setNBTBoolean(TAG_ACTIVE, true)
            else
                stack.setNBTBoolean(TAG_ACTIVE, false)
        }
    }

    override fun onUpdate(stack: ItemStack, world: World, player: Entity, slot: Int, selected: Boolean) {
        super.onUpdate(stack, world, player, slot, selected)
        if (!world.isRemote && slot < 100)
            stack.setNBTBoolean(TAG_ACTIVE, false)
    }

    override fun getAttributeModifiers(slot: EntityEquipmentSlot?, stack: ItemStack): Multimap<String, AttributeModifier> {
        val map = super.getAttributeModifiers(slot, stack)

        if (slot == armorType && stack.getNBTBoolean(TAG_ACTIVE, false)) {
            val uuid1 = UUID((getUnlocalizedNameInefficiently(stack) + slot.toString()).hashCode().toLong(), 0L)
            val uuid2 = UUID(uuid1.mostSignificantBits, 1L)
            val uuid3 = UUID(uuid1.mostSignificantBits, 2L)
            map.put(SharedMonsterAttributes.ATTACK_DAMAGE.name, AttributeModifier(uuid1, "Fenris modifier " + slot?.name, 0.5, 0))
            map.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.name, AttributeModifier(uuid2, "Fenris modifier " + slot?.name, 0.15, 0))
            map.put(SharedMonsterAttributes.MOVEMENT_SPEED.name, AttributeModifier(uuid3, "Fenris modifier " + slot?.name, 0.05, 1))
        }

        return map
    }
}
