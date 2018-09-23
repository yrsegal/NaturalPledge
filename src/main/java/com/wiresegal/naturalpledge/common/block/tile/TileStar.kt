package com.wiresegal.naturalpledge.common.block.tile

import com.teamwizardry.librarianlib.features.base.block.tile.TileMod
import com.teamwizardry.librarianlib.features.saving.Save
import com.wiresegal.naturalpledge.common.block.colored.BlockFrozenStar

/**
 * @author WireSegal
 * Created at 1:46 PM on 5/4/16.
 */
class TileStar : TileMod() {
    @Save var color = -1
    @Save var size = BlockFrozenStar.DEFAULT_SIZE
}
