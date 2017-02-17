package shadowfox.botanicaladdons.common.block.tile

import com.teamwizardry.librarianlib.common.base.block.TileMod
import com.teamwizardry.librarianlib.common.util.saving.Save
import net.minecraft.nbt.NBTTagCompound
import shadowfox.botanicaladdons.common.block.colored.BlockFrozenStar

/**
 * @author WireSegal
 * Created at 1:46 PM on 5/4/16.
 */
class TileStar : TileMod() {
    @Save var color = -1
    @Save var size = BlockFrozenStar.DEFAULT_SIZE
}
