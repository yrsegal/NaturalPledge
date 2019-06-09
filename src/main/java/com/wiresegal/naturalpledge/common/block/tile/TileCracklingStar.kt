package com.wiresegal.naturalpledge.common.block.tile

import com.teamwizardry.librarianlib.features.autoregister.TileRegister
import com.teamwizardry.librarianlib.features.saving.Save
import com.wiresegal.naturalpledge.common.NaturalPledge
import com.wiresegal.naturalpledge.common.core.helper.RainbowItemHelper
import com.wiresegal.naturalpledge.common.lib.LibNames
import net.minecraft.util.math.BlockPos
import vazkii.botania.common.Botania
import vazkii.botania.common.core.helper.Vector3
import java.awt.Color

/**
 * @author WireSegal
 * Created at 1:46 PM on 5/4/16.
 */
@TileRegister(LibNames.CRACKLING)
class TileCracklingStar : TileModTickable() {
    @Save var color = -1

    @Save var blockPos: BlockPos? = BlockPos(0, -1, 0)

    override fun updateEntity() {
        if (world.isRemote) {
            if (blockPos == null) blockPos = BlockPos(0, -1, 0)
            if (blockPos?.y != -1 && blockPos != getPos()) {
                val vec = Vector3.fromBlockPos(blockPos).subtract(Vector3.fromBlockPos(getPos()))
                NaturalPledge.PROXY.wispLine(Vector3.fromBlockPos(getPos()).add(0.5 + (Math.random() - 0.5) * 0.05, 0.5 + (Math.random() - 0.5) * 0.05, 0.5 + (Math.random() - 0.5) * 0.05), vec, RainbowItemHelper.colorFromInt(color), Math.random() * 6.0, 10)
                NaturalPledge.PROXY.wispLine(Vector3.fromBlockPos(blockPos).add(0.5 + (Math.random() - 0.5) * 0.05, 0.5 + (Math.random() - 0.5) * 0.05, 0.5 + (Math.random() - 0.5) * 0.05), vec.negate(), RainbowItemHelper.colorFromInt(color), Math.random() * 6.0, 10)
            } else {
                val color = Color(RainbowItemHelper.colorFromIntAndPos(color, pos))
                Botania.proxy.wispFX(pos.x + 0.5, pos.y + 0.5, pos.z + 0.5, color.red / 255f, color.green / 255f, color.blue / 255f, 0.25f)
            }
        } else if (blockPos != null){
            val other = world.getTileEntity(blockPos) as? TileCracklingStar
            if (other == null) {
                blockPos = null
                markDirty()
            }
        }
    }
}
