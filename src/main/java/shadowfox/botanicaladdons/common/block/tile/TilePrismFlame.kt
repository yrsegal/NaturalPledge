package shadowfox.botanicaladdons.common.block.tile

import com.teamwizardry.librarianlib.common.util.saving.Save
import net.minecraft.nbt.NBTTagCompound
import shadowfox.botanicaladdons.common.BotanicalAddons
import shadowfox.botanicaladdons.common.core.helper.RainbowItemHelper
import vazkii.botania.common.core.helper.Vector3

/**
 * @author WireSegal
 * Created at 1:46 PM on 5/4/16.
 */
class TilePrismFlame : TileModTickable() {
    @Save var color = -1
    @Save var phantomInk = false

    override fun updateEntity() {
        if (worldObj.isRemote)
            if (!phantomInk || BotanicalAddons.PROXY.playerHasMonocle())
                BotanicalAddons.PROXY.particleEmission(Vector3.fromBlockPos(pos), RainbowItemHelper.colorFromIntAndPos(color, pos))
    }
}
