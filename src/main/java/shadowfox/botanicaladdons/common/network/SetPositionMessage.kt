package shadowfox.botanicaladdons.common.network

import io.netty.buffer.ByteBuf
import net.minecraft.util.math.Vec3d
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import shadowfox.botanicaladdons.common.core.helper.BAMethodHandles


class SetPositionMessage(var pos: Vec3d? = null) : IMessage {

    class SetPositionMessageHandler() : IMessageHandler<SetPositionMessage, IMessage> {

        override fun onMessage(message: SetPositionMessage?, ctx: MessageContext?): IMessage? {
            if (ctx != null && message != null && ctx.side.isServer && message.pos != null) {
                val position = message.pos!!
                val player = ctx.serverHandler.playerEntity
                ctx.serverHandler.setPlayerLocation(position.xCoord, position.yCoord, position.zCoord, player.rotationYaw, player.rotationPitch)
                BAMethodHandles.captureCurrentPosition(ctx.serverHandler)
            }

            return null
        }
    }

    override fun fromBytes(buf: ByteBuf?) {
        buf?.let {
            pos = Vec3d(it.readDouble(), it.readDouble(), it.readDouble())
        }
    }

    override fun toBytes(buf: ByteBuf?) {
        buf?.let { b -> pos?.let { p ->
            b.writeDouble(p.xCoord)
            b.writeDouble(p.yCoord)
            b.writeDouble(p.zCoord)
        }}
    }
}


