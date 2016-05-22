package shadowfox.botanicaladdons.common.enchantment

import net.minecraft.block.material.Material
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.enchantment.EnumEnchantmentType
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.EnumCreatureAttribute
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import shadowfox.botanicaladdons.api.item.IWeightEnchantable

/**
 * @author WireSegal
 * Created at 9:45 AM on 5/19/16.
 */
class EnchantmentWeight(name: String, val heavy: Boolean) : EnchantmentMod(name, Rarity.COMMON, EnumEnchantmentType.WEAPON, EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND) {

    companion object {
        init {
            MinecraftForge.EVENT_BUS.register(this)
        }

        @SubscribeEvent
        fun updatePlayerStep(e: LivingEvent.LivingUpdateEvent) {
            val player = e.entityLiving
            if (player is EntityPlayer) {
                val lightWeight = EnchantmentHelper.getMaxEnchantmentLevel(ModEnchantments.lightweight, player) - EnchantmentHelper.getMaxEnchantmentLevel(ModEnchantments.heavy, player)
                if ((player.onGround || player.capabilities.isFlying) && player.moveForward > 0F && !player.isInsideOfMaterial(Material.WATER))
                    player.moveRelative(0.0f, 1.0f, 0.01f * lightWeight)
            }
        }
    }

    override fun canApplyAtEnchantingTable(stack: ItemStack): Boolean {
        if (stack.item is IWeightEnchantable)
            return (stack.item as IWeightEnchantable).canApplyWeightEnchantment(stack, this)
        return super.canApplyAtEnchantingTable(stack)
    }

    override fun getMinEnchantability(enchantmentLevel: Int): Int {
        return 1 + 11 * (enchantmentLevel - 1)
    }

    override fun getMaxEnchantability(enchantmentLevel: Int): Int {
        return getMinEnchantability(enchantmentLevel) + 50
    }

    override fun calcDamageByCreature(level: Int, creatureType: EnumCreatureAttribute?): Float {
        return 1.0f + Math.max(0, level - 1).toFloat() * 0.5f * if (heavy) 1 else -3
    }

    override fun onEntityDamaged(user: EntityLivingBase, target: Entity, level: Int) {
        val mul = if (heavy) 0.75 / level else 1 + .25 * level

        target.motionX *= mul
        target.motionZ *= mul
    }

    override fun canApplyTogether(ench: Enchantment?) = ench !is EnchantmentWeight

    override fun getMaxLevel() = 5
}
