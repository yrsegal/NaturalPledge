package shadowfox.botanicaladdons.common.potions.base

import com.teamwizardry.librarianlib.common.base.PotionMod
import net.minecraft.item.ItemStack
import net.minecraft.potion.Potion
import net.minecraft.potion.PotionEffect

/**
 * @author WireSegal
 * Created at 8:17 PM on 4/17/16.
 */
class ModPotionEffect : PotionEffect {

    constructor(potion: Potion) : this(potion, 0, 0)

    constructor(potion: Potion, duration: Int) : this(potion, duration, 0)

    constructor(potion: Potion, duration: Int, amplifier: Int) : this(potion, duration, amplifier, false, true)

    constructor(potion: Potion, duration: Int, amplifier: Int, ambient: Boolean, showParticles: Boolean) : super(potion, duration, amplifier, ambient, showParticles)

    constructor(potionEffect: PotionEffect) : super(potionEffect)

    override fun isCurativeItem(stack: ItemStack?): Boolean {
        if (potion is PotionMod && potion.isBadEffect)
            return false
        else
            return super.isCurativeItem(stack)
    }
}
