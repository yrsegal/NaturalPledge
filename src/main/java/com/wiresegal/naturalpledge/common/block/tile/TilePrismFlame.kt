package com.wiresegal.naturalpledge.common.block.tile

import com.teamwizardry.librarianlib.features.autoregister.TileRegister
import com.teamwizardry.librarianlib.features.saving.Save
import com.wiresegal.naturalpledge.common.NaturalPledge
import com.wiresegal.naturalpledge.common.core.helper.RainbowItemHelper
import com.wiresegal.naturalpledge.common.lib.LibNames
import vazkii.botania.common.core.helper.Vector3

/**
 * @author WireSegal
 * Created at 1:46 PM on 5/4/16.
 */
@TileRegister(LibNames.PRISM_FLAME)
class TilePrismFlame : TileModTickable() {
    @Save var color = -1
    @Save var phantomInk = false

    override fun updateEntity() {
        if (world.isRemote)
            if (!phantomInk || NaturalPledge.PROXY.playerHasMonocle())
                NaturalPledge.PROXY.particleEmission(Vector3.fromBlockPos(pos), RainbowItemHelper.colorFromIntAndPos(color, pos))
    }
}
