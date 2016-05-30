package shadowfox.botanicaladdons.common.block.tile

import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ITickable
import shadowfox.botanicaladdons.common.BotanicalAddons
import vazkii.botania.common.Botania
import vazkii.botania.common.core.helper.Vector3

/**
 * @author WireSegal
 * Created at 1:46 PM on 5/4/16.
 */
class TilePrismFlame : TileMod(), ITickable {
    private val TAG_COLOR = "color"
    private val TAG_INK = "phantomInk"
    var color = -1
    var inked = false

    override fun update() {
        try {
            if (!inked || Botania.proxy.isClientPlayerWearingMonocle)
                BotanicalAddons.proxy.particleEmission(worldObj, Vector3.fromBlockPos(pos), getLightColor())
        } catch (ignored: NullPointerException) {}
    }

    fun getLightColor(): Int {
        return when (color) {
            -1 -> BotanicalAddons.proxy.rainbow(pos).rgb
            else -> color
        }
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
