package shadowfox.botanicaladdons.common.block.trap

import net.minecraft.block.properties.PropertyDirection
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import shadowfox.botanicaladdons.common.BotanicalAddons
import shadowfox.botanicaladdons.common.lib.LibNames
import vazkii.botania.common.Botania
import vazkii.botania.common.core.helper.Vector3
import java.util.*

/**
 * @author WireSegal
 * Created at 4:23 PM on 3/25/17.
 */
class BlockLaunchTrap : BlockBaseTrap(LibNames.LAUNCH_TRAP) {

    companion object {
        val FACING: PropertyDirection = PropertyDirection.create("facing") {
            it in EnumFacing.HORIZONTALS
        }
    }

    override fun createBlockState() = BlockStateContainer(this, TRIPPED, FACING)

    override fun getStateFromMeta(meta: Int): IBlockState {
        return super.getStateFromMeta(meta).withProperty(FACING, EnumFacing.getHorizontal((meta and 6) shr 1))
    }

    override fun getMetaFromState(state: IBlockState): Int {
        return super.getMetaFromState(state) or (state.getValue(FACING).horizontalIndex shl 1)
    }

    override fun getStateForPlacement(world: World?, pos: BlockPos?, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float, meta: Int, placer: EntityLivingBase, hand: EnumHand?): IBlockState {
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(FACING, placer.horizontalFacing.opposite)
    }

    override fun trapActivation(stateIn: IBlockState, world: World, pos: BlockPos, entityIn: EntityLivingBase) {
        val facing = stateIn.getValue(FACING)
        entityIn.motionY += 0.5
        entityIn.motionX -= facing.frontOffsetX * 2
        entityIn.motionZ -= facing.frontOffsetZ * 2
        entityIn.velocityChanged = true
    }

    override fun particlesForSeer(stateIn: IBlockState, world: World, pos: BlockPos, rand: Random) {
        val facing = stateIn.getValue(FACING)
        val y = pos.y + 0.5
        val xFrom = facing.frontOffsetX * 0.25 + pos.x + 0.5
        val xTo = -facing.frontOffsetX * 0.25 + pos.x + 0.5
        val zFrom = facing.frontOffsetZ * 0.25 + pos.z + 0.5
        val zTo = -facing.frontOffsetZ * 0.25 + pos.z + 0.5
        BotanicalAddons.PROXY.particleStream(Vector3(xFrom, y, zFrom), Vector3(xTo, y, zTo), COLOR)
        Botania.proxy.wispFX(xFrom, y, zFrom, R, G, B, 0.25f)
    }
}
