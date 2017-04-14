package shadowfox.botanicaladdons.common.network

import com.teamwizardry.librarianlib.core.LibrarianLib
import com.teamwizardry.librarianlib.features.autoregister.PacketRegister
import com.teamwizardry.librarianlib.features.network.PacketBase
import com.teamwizardry.librarianlib.features.saving.Save
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side
import shadowfox.botanicaladdons.common.block.trap.BlockBaseTrap
import vazkii.botania.common.Botania

/**
 * @author WireSegal
 * Created at 8:15 PM on 3/25/17.
 */
@PacketRegister(Side.CLIENT)
class FireSphereMessage(@Save var pos: Vec3d = Vec3d.ZERO) : PacketBase() {

    override fun handle(ctx: MessageContext) {
        val rand = LibrarianLib.PROXY.getClientPlayer().world.rand
        val shift = rand.nextInt(6)
        for (theta in shift until 360 step 10) for (azimuth in -90 + shift until 90 step 10) {
            val dist = rand.nextDouble() * 2 + 3
            val x = MathHelper.cos(theta * Math.PI.toFloat() / 180) * MathHelper.cos(azimuth * Math.PI.toFloat() / 180) * dist + pos.xCoord
            val y = MathHelper.sin(azimuth * Math.PI.toFloat() / 180) * dist + pos.yCoord
            val z = MathHelper.sin(theta * Math.PI.toFloat() / 180) * MathHelper.cos(azimuth * Math.PI.toFloat() / 180) * dist + pos.zCoord
            Botania.proxy.wispFX(x, y, z, BlockBaseTrap.R, BlockBaseTrap.G, BlockBaseTrap.B, 0.375f)
        }
    }
}
