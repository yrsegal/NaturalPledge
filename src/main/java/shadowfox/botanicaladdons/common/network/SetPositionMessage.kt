package shadowfox.botanicaladdons.common.network

import com.teamwizardry.librarianlib.common.network.PacketBase
import com.teamwizardry.librarianlib.common.util.saving.Save
import net.minecraft.util.math.Vec3d
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import shadowfox.botanicaladdons.common.core.helper.BAMethodHandles


class SetPositionMessage(@Save var pos: Vec3d? = null) : PacketBase() {

    override fun handle(ctx: MessageContext) {
        val position = pos!!
        val player = ctx.serverHandler.playerEntity
        ctx.serverHandler.setPlayerLocation(position.xCoord, position.yCoord, position.zCoord, player.rotationYaw, player.rotationPitch)
        BAMethodHandles.captureCurrentPosition(ctx.serverHandler)
    }
}


