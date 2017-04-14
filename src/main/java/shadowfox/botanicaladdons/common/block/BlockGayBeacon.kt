package shadowfox.botanicaladdons.common.block

import com.teamwizardry.librarianlib.features.autoregister.TileRegister
import com.teamwizardry.librarianlib.features.base.block.BlockModContainer
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntityBeacon
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import shadowfox.botanicaladdons.common.BotanicalAddons
import shadowfox.botanicaladdons.common.block.tile.TileModTickable
import shadowfox.botanicaladdons.common.lib.LibNames
import java.util.*

/**
 * @author WireSegal
 * Created at 10:56 AM on 3/30/17.
 */
class BlockGayBeacon : BlockModContainer(LibNames.GAY_BEACON, Material.GLASS) {
    init {
        tickRandomly = true
    }

    override fun getBeaconColorMultiplier(state: IBlockState?, world: World?, pos: BlockPos, beaconPos: BlockPos): FloatArray? {
        return BotanicalAddons.PROXY.rainbow(beaconPos).getColorComponents(null)
    }

    override fun isOpaqueCube(state: IBlockState) = false
    override fun canSilkHarvest(world: World?, pos: BlockPos?, state: IBlockState?, player: EntityPlayer?) = true
    override fun isFullCube(state: IBlockState) = false

    @SideOnly(Side.CLIENT)
    override fun getBlockLayer() = BlockRenderLayer.TRANSLUCENT

    @SideOnly(Side.CLIENT)
    override fun shouldSideBeRendered(blockState: IBlockState, blockAccess: IBlockAccess, pos: BlockPos, side: EnumFacing): Boolean {
        val iblockstate = blockAccess.getBlockState(pos.offset(side))
        val block = iblockstate.block

        return blockState !== iblockstate && if (block === this) false else super.shouldSideBeRendered(blockState, blockAccess, pos, side)
    }

    override fun createTileEntity(world: World, state: IBlockState): TileEntity? {
        return TileGayBeacon()
    }

    @TileRegister("gay")
    class TileGayBeacon : TileModTickable() {
        override fun updateEntity() {
            val mut = BlockPos.MutableBlockPos(pos.down())
            for (i in mut.y downTo 0) {
                mut.y = i
                val tile = world.getTileEntity(mut)
                if (tile is TileEntityBeacon) {
                    tile.updateBeacon()
                    break
                }
            }
        }
    }
}
