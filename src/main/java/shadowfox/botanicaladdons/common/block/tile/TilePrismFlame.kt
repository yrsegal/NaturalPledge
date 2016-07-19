package shadowfox.botanicaladdons.common.block.tile

import net.minecraft.nbt.NBTTagCompound
import shadowfox.botanicaladdons.common.BotanicalAddons
import shadowfox.botanicaladdons.common.core.helper.RainbowItemHelper
import vazkii.botania.common.core.helper.Vector3

/**
 * @author WireSegal
 * Created at 1:46 PM on 5/4/16.
 */
class TilePrismFlame : TileModTickable() {
    private val TAG_COLOR = "color"
    private val TAG_INK = "phantomInk"
    var color = -1
    var inked = false

    override fun updateEntity() {
        if (worldObj.isRemote)
            if (!inked || BotanicalAddons.PROXY.playerHasMonocle())
                BotanicalAddons.PROXY.particleEmission(worldObj, Vector3.fromBlockPos(pos), RainbowItemHelper.colorFromIntAndPos(color, pos))
    }

    override fun writeCustomNBT(cmp: NBTTagCompound) {
        cmp.setInteger(TAG_COLOR, color)
        cmp.setBoolean(TAG_INK, inked)
    }

    override fun readCustomNBT(cmp: NBTTagCompound) {
        if (cmp.hasKey(TAG_COLOR))
            this.color = cmp.getInteger(TAG_COLOR)
        if (cmp.hasKey(TAG_INK))
            this.inked = cmp.getBoolean(TAG_INK)
    }
}
