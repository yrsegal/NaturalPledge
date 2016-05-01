package shadowfox.botanicaladdons.common.block

import shadowfox.botanicaladdons.api.lib.LibNames
import shadowfox.botanicaladdons.common.block.base.BlockMod

/**
 * @author WireSegal
 * Created at 2:46 PM on 4/17/16.
 */
object ModBlocks {
    val awakenerCore: BlockMod

    init {
        awakenerCore = BlockAwakenerCore(LibNames.AWAKENER)
    }
}
