package shadowfox.botanicaladdons.common.block.tile

import net.minecraft.nbt.NBTTagCompound

/**
 * @author WireSegal
 * Created at 1:46 PM on 5/4/16.
 */
class TileStar : TileMod() {
    private val TAG_COLOR = "color"
    private val TAG_SIZE = "size"
    var starColor = -1
    var size = 0.05f

    override fun writeCustomNBT(cmp: NBTTagCompound) {
        cmp.setInteger(TAG_COLOR, starColor)
        cmp.setFloat(TAG_SIZE, size)
    }

    override fun readCustomNBT(cmp: NBTTagCompound) {
        if (cmp.hasKey(TAG_COLOR))
            this.starColor = cmp.getInteger(TAG_COLOR)
        if (cmp.hasKey(TAG_SIZE))
            this.size = cmp.getFloat(TAG_SIZE)
    }

    fun getColor(): Int {
        return this.starColor
    }
}
