package shadowfox.botanicaladdons.common.items

import com.google.common.collect.Multimap
import com.teamwizardry.librarianlib.features.base.item.ItemMod
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraft.util.DamageSource
import shadowfox.botanicaladdons.common.core.helper.BAMethodHandles
import shadowfox.botanicaladdons.common.items.base.IPreventBreakInCreative
import vazkii.botania.common.entity.EntityDoppleganger

/**
 * @author WireSegal
 * Created at 5:34 PM on 4/17/16.
 */
class ItemGaiaSlayer(name: String) : ItemMod(name), IPreventBreakInCreative {
    init {
        setMaxStackSize(1)
    }

    override fun hitEntity(stack: ItemStack, target: EntityLivingBase, attacker: EntityLivingBase): Boolean {
        if (target is EntityDoppleganger) {
            if (target.health >= 0.5f) target.health = 0.5f
            BAMethodHandles.setMobSpawnTicks(target, 0)
            BAMethodHandles.setTpDelay(target, 10000)
            target.onDeath(DamageSource.OUT_OF_WORLD)
            target.setDead()
        }
        return super.hitEntity(stack, target, attacker)
    }

    override fun getAttributeModifiers(slot: EntityEquipmentSlot, stack: ItemStack): Multimap<String, AttributeModifier> {
        val multimap = super.getAttributeModifiers(slot, stack)

        if (slot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.name, AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 8.0, 0))
        }

        return multimap
    }
}
