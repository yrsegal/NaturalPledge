package shadowfox.botanicaladdons.common.integration.tinkers.traits

import net.minecraft.item.ItemStack
import net.minecraftforge.event.entity.player.PlayerEvent
import shadowfox.botanicaladdons.common.integration.tinkers.TinkersIntegration
import slimeknights.tconstruct.library.traits.AbstractTrait

/**
 * @author WireSegal
 * Created at 5:00 PM on 6/25/16.
 */
class TraitAerodynamic : AbstractTrait("aerodynamic", TinkersIntegration.SOULROOT_COLORS[0]) {
    override fun miningSpeed(tool: ItemStack?, event: PlayerEvent.BreakSpeed) {
        if (!event.entityPlayer.onGround)
            event.newSpeed *= 5 // Exact negation of the fly-speed modifier.
    }
}
