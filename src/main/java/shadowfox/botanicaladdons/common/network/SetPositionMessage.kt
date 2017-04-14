package shadowfox.botanicaladdons.common.network

import com.teamwizardry.librarianlib.features.autoregister.PacketRegister
import com.teamwizardry.librarianlib.features.network.PacketBase
import com.teamwizardry.librarianlib.features.saving.Save
import net.minecraft.util.math.Vec3d
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side
import shadowfox.botanicaladdons.common.core.helper.BAMethodHandles

@PacketRegister(Side.SERVER)
class SetPositionMessage(@Save var pos: Vec3d? = null) : PacketBase() {

    override fun handle(ctx: MessageContext) {
        val position = pos!!
        val player = ctx.serverHandler.player
        ctx.serverHandler.setPlayerLocation(position.xCoord, position.yCoord, position.zCoord, player.rotationYaw, player.rotationPitch)
        BAMethodHandles.captureCurrentPosition(ctx.serverHandler)
    }
}


