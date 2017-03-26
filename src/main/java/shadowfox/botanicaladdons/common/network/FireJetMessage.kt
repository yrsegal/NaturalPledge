package shadowfox.botanicaladdons.common.network

import com.teamwizardry.librarianlib.common.network.PacketBase
import com.teamwizardry.librarianlib.common.util.autoregister.PacketRegister
import com.teamwizardry.librarianlib.common.util.saving.Save
import net.minecraft.util.math.Vec3d
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side
import shadowfox.botanicaladdons.common.BotanicalAddons
import shadowfox.botanicaladdons.common.block.trap.BlockBaseTrap
import vazkii.botania.common.core.helper.Vector3

/**
 * @author WireSegal
 * Created at 8:15 PM on 3/25/17.
 */
@PacketRegister(Side.CLIENT)
class FireJetMessage(@Save var pos: Vec3d = Vec3d.ZERO, @Save var vecTo: Vec3d = Vec3d.ZERO) : PacketBase() {
    override fun handle(ctx: MessageContext) = BotanicalAddons.PROXY.particleStream(Vector3(pos), Vector3(vecTo), BlockBaseTrap.COLOR)
}
