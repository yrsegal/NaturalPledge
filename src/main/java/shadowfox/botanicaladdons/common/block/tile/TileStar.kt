package shadowfox.botanicaladdons.common.block.tile

import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.NetworkManager
import net.minecraft.network.Packet
import net.minecraft.network.play.server.SPacketUpdateTileEntity
import net.minecraft.tileentity.TileEntity

/**
 * @author WireSegal
 * Created at 1:46 PM on 5/4/16.
 */
class TileStar : TileEntity() {
    private val TAG_COLOR = "color"
    private val TAG_SIZE = "size"
    var starColor = -1
    var size = 0.05f

    fun writeCustomNBT(nbttagcompound: NBTTagCompound) {
        nbttagcompound.setInteger(TAG_COLOR, this.starColor)
        nbttagcompound.setFloat(TAG_SIZE, this.size)
    }

    fun readCustomNBT(nbttagcompound: NBTTagCompound) {
        this.starColor = nbttagcompound.getInteger(TAG_COLOR)
        this.size = nbttagcompound.getFloat(TAG_SIZE)
    }

    fun getColor(): Int {
        return this.starColor
    }

    override fun getDescriptionPacket(): Packet<*> {
        val nbttagcompound = NBTTagCompound()
        this.writeCustomNBT(nbttagcompound)
        return SPacketUpdateTileEntity(this.pos, -999, nbttagcompound)
    }

    override fun onDataPacket(net: NetworkManager?, pkt: SPacketUpdateTileEntity) {
        super.onDataPacket(net, pkt)
        this.readCustomNBT(pkt.nbtCompound)
    }
}
