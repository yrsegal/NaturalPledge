package shadowfox.botanicaladdons.common.items.base

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.item.ItemStack

/**
 * @author WireSegal
 * Created at 4:10 PM on 4/13/16.
 */
abstract class ItemAttributeBauble(name: String, vararg variants: String) : ItemModBauble(name, *variants) {
    internal var attributes: Multimap<String, AttributeModifier> = HashMultimap.create<String, AttributeModifier>()

    override fun onEquippedOrLoadedIntoWorld(stack: ItemStack, player: EntityLivingBase) {
        this.attributes.clear()
        this.fillModifiers(this.attributes, stack)
        player.attributeMap.applyAttributeModifiers(this.attributes)
    }

    override fun onUnequipped(stack: ItemStack, player: EntityLivingBase) {
        this.attributes.clear()
        this.fillModifiers(this.attributes, stack)
        player.attributeMap.removeAttributeModifiers(this.attributes)
    }

    abstract fun fillModifiers(map: Multimap<String, AttributeModifier>, stack: ItemStack)
}
