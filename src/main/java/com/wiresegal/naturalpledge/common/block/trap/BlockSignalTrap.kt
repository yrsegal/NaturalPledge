package com.wiresegal.naturalpledge.common.block.trap

import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import com.wiresegal.naturalpledge.common.lib.LibNames
import vazkii.botania.common.Botania
import java.util.*

/**
 * @author WireSegal
 * Created at 4:23 PM on 3/25/17.
 */
class BlockSignalTrap : BlockBaseTrap(LibNames.SIGNAL_TRAP) {
    override fun trapActivation(stateIn: IBlockState, world: World, pos: BlockPos, entityIn: EntityLivingBase) {
        // NO-OP
    }

    override fun getStrongPower(blockState: IBlockState, blockAccess: IBlockAccess?, pos: BlockPos?, side: EnumFacing?)
            = if (blockState.getValue(TRIPPED)) 15 else 0

    override fun getWeakPower(blockState: IBlockState, blockAccess: IBlockAccess?, pos: BlockPos?, side: EnumFacing?)
            = getStrongPower(blockState, blockAccess, pos, side)

    override fun particlesForSeer(stateIn: IBlockState, world: World, pos: BlockPos, rand: Random) {
        val shift = if (stateIn.getValue(TRIPPED)) 0.1 else 0.25
        for (xS in 0 until 11) for (zS in 0 until 11)
            Botania.proxy.wispFX(xS / 10.0 + pos.x, shift + pos.y, zS / 10.0 + pos.z, R, G, B, 0.1f)
    }
}
