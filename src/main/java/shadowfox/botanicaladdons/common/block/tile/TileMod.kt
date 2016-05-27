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
        super.writeToNBT(par1nbtTagCompound)

        writeCustomNBT(par1nbtTagCompound)
        return par1nbtTagCompound
    }

    override fun readFromNBT(par1nbtTagCompound: NBTTagCompound) {
        super.readFromNBT(par1nbtTagCompound)

        readCustomNBT(par1nbtTagCompound)
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
