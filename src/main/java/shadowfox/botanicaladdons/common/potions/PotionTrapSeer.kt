package shadowfox.botanicaladdons.common.potions

import com.teamwizardry.librarianlib.features.base.PotionMod
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import net.minecraft.util.DamageSource
import shadowfox.botanicaladdons.common.block.trap.BlockBaseTrap
import shadowfox.botanicaladdons.common.lib.LibNames

/**
 * @author WireSegal
 * Created at 11:10 AM on 4/18/16.
 */
class PotionTrapSeer : PotionMod(LibNames.TRAP_SEER, false, BlockBaseTrap.COLOR) {
    override fun getCurativeItems(): MutableList<ItemStack> = mutableListOf()
}
