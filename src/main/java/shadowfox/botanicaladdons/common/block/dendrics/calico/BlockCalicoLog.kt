package shadowfox.botanicaladdons.common.block.dendrics.calico

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.Explosion
import net.minecraft.world.World
import shadowfox.botanicaladdons.common.block.base.BlockModLog
import shadowfox.botanicaladdons.common.lexicon.LexiconEntries
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.lexicon.LexiconEntry

/**
 * @author WireSegal
 * Created at 4:31 PM on 5/27/16.
 */
class BlockCalicoLog(name: String) : BlockModLog(name), IExplosionDampener, ILexiconable {

    override fun onBlockExploded(world: World?, pos: BlockPos?, explosion: Explosion?) {}

    override fun getEntry(p0: World?, p1: BlockPos?, p2: EntityPlayer?, p3: ItemStack?): LexiconEntry? {
        return LexiconEntries.calicoTree
    }
}
