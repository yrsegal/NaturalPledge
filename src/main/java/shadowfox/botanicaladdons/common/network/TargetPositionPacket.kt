package shadowfox.botanicaladdons.common.network

import com.teamwizardry.librarianlib.common.network.PacketBase
import com.teamwizardry.librarianlib.common.util.readString
import com.teamwizardry.librarianlib.common.util.writeString
import io.netty.buffer.ByteBuf
import net.minecraft.util.math.Vec3d
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import shadowfox.botanicaladdons.common.items.travel.ItemWaystone

/**
 * @author WireSegal
 * Created at 9:18 PM on 1/2/17.
 */
class TargetPositionPacket(var keys: Array<String> = arrayOf(), var dims: Array<Int> = arrayOf(), var poses: Array<Vec3d> = arrayOf()) : PacketBase() {
    override fun handle(ctx: MessageContext) = keys.indices.forEach { ItemWaystone.LAST_KNOWN_POSITIONS[keys[it]] = dims[it] to poses[it] }

    override fun writeCustomBytes(buf: ByteBuf) {
        buf.writeShort(keys.size)
        for (key in keys)
            buf.writeString(key)
        for (dim in dims)
            buf.writeInt(dim)
        for (pos in poses) {
            buf.writeDouble(pos.xCoord)
            buf.writeDouble(pos.yCoord)
            buf.writeDouble(pos.zCoord)
        }
    }

    override fun readCustomBytes(buf: ByteBuf) {
        val length = buf.readShort().toInt()
        keys = Array(length) { buf.readString() }
        dims = Array(length) { buf.readInt() }
        poses = Array(length) { Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble()) }
    }
}
