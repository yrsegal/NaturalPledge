package shadowfox.botanicaladdons.common.block.dendrics.calico

import com.teamwizardry.librarianlib.common.base.block.BlockModLeaves
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.Explosion
import net.minecraft.world.World
import shadowfox.botanicaladdons.common.block.ModBlocks
import shadowfox.botanicaladdons.common.lexicon.LexiconEntries
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.lexicon.LexiconEntry
import java.util.*

/**
 * @author WireSegal
 * Created at 10:36 PM on 5/27/16.
 */
class BlockCalicoLeaves(name: String) : BlockModLeaves(name), IExplosionDampener, ILexiconable {
    override val canBeOpaque: Boolean
        get() = false

    override fun getItemDropped(state: IBlockState, rand: Random, fortune: Int): Item? {
        return ModBlocks.calicoSapling.itemForm
    }

    override fun onBlockExploded(world: World?, pos: BlockPos?, explosion: Explosion?) {
        //NO-OP
    }

    override fun getEntry(p0: World?, p1: BlockPos?, p2: EntityPlayer?, p3: ItemStack?): LexiconEntry? {
        return LexiconEntries.calicoTree
    }
}
