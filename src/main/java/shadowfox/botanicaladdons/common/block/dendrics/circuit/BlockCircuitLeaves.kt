package shadowfox.botanicaladdons.common.block.dendrics.circuit

import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import shadowfox.botanicaladdons.common.block.ModBlocks
import shadowfox.botanicaladdons.common.block.base.BlockModLeaves
import shadowfox.botanicaladdons.common.lexicon.LexiconEntries
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.lexicon.LexiconEntry
import java.util.*

/**
 * @author WireSegal
 * Created at 10:19 PM on 5/29/16.
 */
class BlockCircuitLeaves(name: String) : BlockModLeaves(name), ICircuitBlock, ILexiconable {

    init {
        tickRandomly = true
    }

    override fun updateTick(worldIn: World, pos: BlockPos, state: IBlockState, rand: Random?) {
        worldIn.notifyNeighborsOfStateChange(pos, this)
    }

    override fun getEntry(p0: World?, p1: BlockPos?, p2: EntityPlayer?, p3: ItemStack?): LexiconEntry? {
        return LexiconEntries.circuitTree
    }

    override fun getItemDropped(state: IBlockState?, rand: Random?, fortune: Int): Item? {
        return Item.getItemFromBlock(ModBlocks.circuitSapling)
    }

    override fun getWeakChanges(world: IBlockAccess?, pos: BlockPos?): Boolean {
        return true
    }

    override fun getComparatorInputOverride(blockState: IBlockState, worldIn: World, pos: BlockPos): Int {
        return ICircuitBlock.getPower(blockState, worldIn, pos)
    }

    override fun hasComparatorInputOverride(state: IBlockState?): Boolean {
        return true
    }
}
