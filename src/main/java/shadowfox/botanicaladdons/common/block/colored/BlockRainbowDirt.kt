package shadowfox.botanicaladdons.common.block.colored

import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.common.block.base.BlockMod
import shadowfox.botanicaladdons.common.items.base.ItemMod

/**
 * @author WireSegal
 * Created at 10:38 PM on 5/6/16.
 */
class BlockRainbowDirt(name: String) : BlockMod(name, Material.ground) {

    init {
        soundType = SoundType.GROUND
    }

    override fun addInformation(stack: ItemStack, player: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
        ItemMod.addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.color.16")
    }
}
