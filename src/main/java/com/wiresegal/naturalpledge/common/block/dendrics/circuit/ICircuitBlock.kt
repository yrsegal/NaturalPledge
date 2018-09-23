package com.wiresegal.naturalpledge.common.block.dendrics.circuit

import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess

/**
 * @author WireSegal
 * Created at 10:00 PM on 5/29/16.
 */
interface ICircuitBlock {
    companion object {

        fun getPower(blockAccess: IBlockAccess, pos: BlockPos): Int {
            for (i in 1..15) {
                val state = blockAccess.getBlockState(pos.up(i))
                if (state == null || state.block !is ICircuitBlock)
                    return i - 1
            }
            return 15
        }
    }
}
