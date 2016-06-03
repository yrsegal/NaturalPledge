package shadowfox.botanicaladdons.common.block.dendrics.calico

import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.gen.feature.WorldGenTrees
import net.minecraftforge.event.terraingen.TerrainGen
import shadowfox.botanicaladdons.common.block.ModBlocks
import shadowfox.botanicaladdons.common.block.base.BlockModSapling
import shadowfox.botanicaladdons.common.lexicon.LexiconEntries
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.lexicon.LexiconEntry
import java.util.*

/**
 * @author WireSegal
 * Created at 10:35 PM on 5/27/16.
 */
class BlockCalicoSapling(name: String) : BlockModSapling(name), IExplosionDampener, ILexiconable {
    override fun generateTree(worldIn: World, pos: BlockPos, state: IBlockState, rand: Random) {
        if (!TerrainGen.saplingGrowTree(worldIn, rand, pos)) return

        worldIn.setBlockState(pos, Blocks.AIR.defaultState, 4)

        if (!WorldGenTrees(true, 4, ModBlocks.calicoLog.defaultState, ModBlocks.calicoLeaves.defaultState, false).generate(worldIn, rand, pos))
            worldIn.setBlockState(pos, state, 4)
    }

    override fun getEntry(p0: World?, p1: BlockPos?, p2: EntityPlayer?, p3: ItemStack?): LexiconEntry? {
        return LexiconEntries.calicoTree
    }
}
