package shadowfox.botanicaladdons.common.network

import com.teamwizardry.librarianlib.features.autoregister.PacketRegister
import com.teamwizardry.librarianlib.features.network.PacketBase
import com.teamwizardry.librarianlib.features.saving.Save
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side
import shadowfox.botanicaladdons.common.items.weapons.ItemFlarebringer

/**
 * @author WireSegal
 * Created at 8:15 PM on 3/25/17.
 */
@PacketRegister(Side.SERVER)
class AttackMessage(@Save var eID: Int = 0) : PacketBase() {

    override fun handle(ctx: MessageContext) {
        val player = ctx.serverHandler.player
        if (player.heldItemMainhand.item !is ItemFlarebringer) return
        val entity = player.world.getEntityByID(eID) ?: return
        if (player.positionVector.squareDistanceTo(entity.positionVector) > ItemFlarebringer.RANGE * ItemFlarebringer.RANGE) return
        player.attackTargetEntityWithCurrentItem(entity)
    }
}
