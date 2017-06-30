package shadowfox.botanicaladdons.common.network

import com.teamwizardry.librarianlib.features.autoregister.PacketRegister
import com.teamwizardry.librarianlib.features.kotlin.readString
import com.teamwizardry.librarianlib.features.kotlin.writeString
import com.teamwizardry.librarianlib.features.network.PacketBase
import io.netty.buffer.ByteBuf
import net.minecraft.util.math.Vec3d
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side
import shadowfox.botanicaladdons.common.items.travel.stones.ItemWaystone

/**
 * @author WireSegal
 * Created at 9:18 PM on 1/2/17.
 */
@PacketRegister(Side.CLIENT)
class TargetPositionPacket(var keys: Array<String> = arrayOf(), var dims: Array<Int> = arrayOf(), var poses: Array<Vec3d> = arrayOf()) : PacketBase() {
    override fun handle(ctx: MessageContext) = keys.indices.forEach { ItemWaystone.LAST_KNOWN_POSITIONS[keys[it]] = dims[it] to poses[it] }

    override fun writeCustomBytes(buf: ByteBuf) {
        buf.writeShort(keys.size)
        for (key in keys)
            buf.writeString(key)
        for (dim in dims)
            buf.writeInt(dim)
        for (pos in poses) {
            buf.writeDouble(pos.x)
            buf.writeDouble(pos.y)
            buf.writeDouble(pos.z)
        }
    }

    override fun readCustomBytes(buf: ByteBuf) {
        val length = buf.readShort().toInt()
        keys = Array(length) { buf.readString() }
        dims = Array(length) { buf.readInt() }
        poses = Array(length) { Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble()) }
    }
}
