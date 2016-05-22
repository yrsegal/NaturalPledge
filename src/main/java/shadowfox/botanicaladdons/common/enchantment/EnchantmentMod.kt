package shadowfox.botanicaladdons.common.enchantment

import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnumEnchantmentType
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.GameRegistry
import shadowfox.botanicaladdons.api.lib.LibMisc

/**
 * @author WireSegal
 * Created at 9:37 AM on 5/19/16.
 */
open class EnchantmentMod(name: String, rarity: Rarity, type: EnumEnchantmentType, vararg applicableSlots: EntityEquipmentSlot) : Enchantment(rarity, type, applicableSlots) {
    init {
        setName("${LibMisc.MOD_ID}.$name")
        GameRegistry.register(this, ResourceLocation(LibMisc.MOD_ID, name))
    }
}
