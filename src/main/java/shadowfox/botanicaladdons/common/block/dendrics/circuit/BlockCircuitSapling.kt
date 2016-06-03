package shadowfox.botanicaladdons.common.block.dendrics.circuit

import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
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
 * Created at 10:25 PM on 5/29/16.
 */
class BlockCircuitSapling(name: String) : BlockModSapling(name), ILexiconable, ICircuitBlock {

    init {
        tickRandomly = true
    }

    override fun updateTick(worldIn: World, pos: BlockPos, state: IBlockState, rand: Random) {
        super.updateTick(worldIn, pos, state, rand)
        worldIn.notifyNeighborsOfStateChange(pos, this)
    }

    override fun generateTree(worldIn: World, pos: BlockPos, state: IBlockState, rand: Random) {
        if (!TerrainGen.saplingGrowTree(worldIn, rand, pos)) return

        worldIn.setBlockState(pos, Blocks.AIR.defaultState, 4)

        if (!WorldGenTrees(true, 4, ModBlocks.circuitLog.defaultState, ModBlocks.circuitLeaves.defaultState, false).generate(worldIn, rand, pos))
            worldIn.setBlockState(pos, state, 4)
    }

    override fun getComparatorInputOverride(blockState: IBlockState, worldIn: World, pos: BlockPos): Int {
        return ICircuitBlock.getPower(blockState, worldIn, pos)
    }

    override fun hasComparatorInputOverride(state: IBlockState?): Boolean {
        return true
    }

    override fun getWeakChanges(world: IBlockAccess?, pos: BlockPos?): Boolean {
        return true
    }

    override fun getEntry(p0: World?, p1: BlockPos?, p2: EntityPlayer?, p3: ItemStack?): LexiconEntry? {
        return LexiconEntries.circuitTree
    }
}
