package shadowfox.botanicaladdons.common.block.tile

import com.teamwizardry.librarianlib.common.base.block.TileMod
import net.minecraft.util.ITickable

/**
 * @author WireSegal
 * Created at 5:14 PM on 6/12/16.
 */
abstract class TileModTickable : TileMod(), ITickable {
    override fun update() {
        if (!isInvalid && worldObj.isBlockLoaded(getPos(), !worldObj.isRemote)) {
            updateEntity()
        }
    }

    abstract fun updateEntity()
}
