package shadowfox.botanicaladdons.common.block.colored

import net.minecraft.block.SoundType
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.common.block.base.BlockModLog
import shadowfox.botanicaladdons.common.items.base.ItemMod
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.lexicon.LexiconEntry

/**
 * @author WireSegal
 * Created at 6:33 PM on 5/7/16.
 */
class BlockRainbowLog(name: String) : BlockModLog(name), ILexiconable {

    init {
        soundType = SoundType.WOOD
    }

    override fun getEntry(p0: World?, p1: BlockPos?, p2: EntityPlayer?, p3: ItemStack?): LexiconEntry? {
        return null //todo
    }

    override fun addInformation(stack: ItemStack?, player: EntityPlayer?, tooltip: MutableList<String>, advanced: Boolean) {
        ItemMod.addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.color.16")
    }
}
