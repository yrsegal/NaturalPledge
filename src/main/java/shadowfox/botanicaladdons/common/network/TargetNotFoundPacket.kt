package shadowfox.botanicaladdons.common.network

import com.teamwizardry.librarianlib.common.network.PacketBase
import com.teamwizardry.librarianlib.common.util.saving.Save
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import shadowfox.botanicaladdons.common.items.travel.ItemWaystone

/**
 * @author WireSegal
 * Created at 9:18 PM on 1/2/17.
 */
class TargetNotFoundPacket(@Save var name: String? = null) : PacketBase() {
    override fun handle(ctx: MessageContext) {
        ItemWaystone.LAST_KNOWN_POSITIONS.remove(name ?: return)
    }
}
