package shadowfox.botanicaladdons.common.items.bauble.faith

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import shadowfox.botanicaladdons.common.items.ItemTerrestrialFocus
import shadowfox.botanicaladdons.common.potions.ModPotions
import shadowfox.botanicaladdons.common.potions.base.ModPotionEffect
import java.util.*

/**
 * @author WireSegal
 * Created at 9:49 AM on 4/18/16.
 */
class PriestlyEmblemHeimdall : ItemFaithBauble.IFaithVariant {
    override val name: String
        get() = throw UnsupportedOperationException()

    override fun getSpells(stack: ItemStack, player: EntityPlayer): HashMap<String, out ItemTerrestrialFocus.IFocusSpell> {
        return hashMapOf()
    }

    override fun punishTheFaithless(stack: ItemStack, player: EntityPlayer) {
        player.addPotionEffect(ModPotionEffect(ModPotions.drab, 600))
    }
}
