package shadowfox.botanicaladdons.common.block.dendrics.circuit

import net.minecraft.block.properties.IProperty
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
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

    override fun canProvidePower(state: IBlockState?): Boolean {
        return true
    }

    override fun getWeakPower(blockState: IBlockState, blockAccess: IBlockAccess, pos: BlockPos, side: EnumFacing?): Int {
        return getActualState(blockState, blockAccess, pos).getValue(ICircuitBlock.POWER)
    }

    override fun getActualState(state: IBlockState, worldIn: IBlockAccess, pos: BlockPos): IBlockState {
        return state.withProperty(ICircuitBlock.POWER, ICircuitBlock.getPower(worldIn, pos))
    }

    override val ignoredProperties: Array<IProperty<*>>?
        get() = arrayOf(ICircuitBlock.POWER)

    override fun createBlockState(): BlockStateContainer? {
        return BlockStateContainer(this, BlockModSapling.STAGE, ICircuitBlock.POWER)
    }

    override fun getEntry(p0: World?, p1: BlockPos?, p2: EntityPlayer?, p3: ItemStack?): LexiconEntry? {
        return LexiconEntries.circuitTree
    }
}
