package shadowfox.botanicaladdons.common.block.tile

import net.minecraft.block.state.IBlockState
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SPacketUpdateTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

/**
 * @author WireSegal
 * Created at 8:14 PM on 5/6/16.
 */
open class TileMod : TileEntity() {
    override fun shouldRefresh(world: World, pos: BlockPos, oldState: IBlockState, newState: IBlockState): Boolean {
        return oldState.block !== newState.block
    }

    override fun writeToNBT(par1nbtTagCompound: NBTTagCompound): NBTTagCompound {
        writeCustomNBT(par1nbtTagCompound)
        super.writeToNBT(par1nbtTagCompound)

        return par1nbtTagCompound
    }

    override fun readFromNBT(par1nbtTagCompound: NBTTagCompound) {
        readCustomNBT(par1nbtTagCompound)
        super.readFromNBT(par1nbtTagCompound)
    }

    override fun getUpdateTag(): NBTTagCompound {
        return writeToNBT(NBTTagCompound())
    }

    override fun getUpdatePacket(): SPacketUpdateTileEntity? {
        return SPacketUpdateTileEntity(pos, -999, updateTag)
    }

    open fun writeCustomNBT(cmp: NBTTagCompound) {
        // NO-OP
    }

    open fun readCustomNBT(cmp: NBTTagCompound) {
        // NO-OP
    }

    override fun onDataPacket(net: NetworkManager, packet: SPacketUpdateTileEntity) {
        super.onDataPacket(net, packet)
        readCustomNBT(packet.nbtCompound)
    }
}
