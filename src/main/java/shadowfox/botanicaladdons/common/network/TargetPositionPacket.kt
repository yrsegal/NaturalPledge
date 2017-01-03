package shadowfox.botanicaladdons.common.network

import com.teamwizardry.librarianlib.common.network.PacketBase
import com.teamwizardry.librarianlib.common.util.saving.Save
import net.minecraft.util.math.Vec3d
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import shadowfox.botanicaladdons.common.BotanicalAddons
import shadowfox.botanicaladdons.common.items.travel.ItemWaystone

/**
 * @author WireSegal
 * Created at 9:18 PM on 1/2/17.
 */
class TargetPositionPacket(@Save var pos: Vec3d? = null, @Save var name: String? = null) : PacketBase() {
    override fun handle(ctx: MessageContext) {
        val position = pos ?: return
        val n = name ?: return

        ItemWaystone.LAST_KNOWN_POSITIONS[n] = BotanicalAddons.PROXY.getClientPlayer().worldObj.provider.dimension to position
    }
}
