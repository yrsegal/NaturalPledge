package shadowfox.botanicaladdons.common.enchantment

import com.google.common.collect.Lists
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.enchantment.EnumEnchantmentType
import net.minecraft.entity.EntityLivingBase
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.GameRegistry
import shadowfox.botanicaladdons.api.lib.LibMisc

/**
 * @author WireSegal
 * Created at 9:37 AM on 5/19/16.
 */
@Suppress("LeakingThis")
open class EnchantmentMod(name: String, rarity: Rarity, type: EnumEnchantmentType, vararg applicableSlots: EntityEquipmentSlot) : Enchantment(rarity, type, applicableSlots) {
    init {
        setName("${LibMisc.MOD_ID}.$name")
        GameRegistry.register(this, ResourceLocation(LibMisc.MOD_ID, name))
    }

    open val applicableSlots: Array<EntityEquipmentSlot>
        get() = arrayOf()

    private fun getEntityEquipmentForLevel(entityIn: EntityLivingBase): Iterable<ItemStack>? {
        val list = Lists.newArrayList<ItemStack>()

        applicableSlots.mapNotNullTo(list) { entityIn.getItemStackFromSlot(it) }

        return if (list.size > 0) list else null
    }

    fun getMaxLevel(entity: EntityLivingBase): Int {
        val iterable = getEntityEquipmentForLevel(entity) ?: return 0

        val i = iterable
                .map { EnchantmentHelper.getEnchantmentLevel(this, it) }
                .max()
                ?: 0
        return i
    }

    fun getTotalLevel(entity: EntityLivingBase): Int {
        val iterable = getEntityEquipmentForLevel(entity) ?: return 0

        val i = iterable
                .map { EnchantmentHelper.getEnchantmentLevel(this, it) }
                .sum()
        return i
    }
}
