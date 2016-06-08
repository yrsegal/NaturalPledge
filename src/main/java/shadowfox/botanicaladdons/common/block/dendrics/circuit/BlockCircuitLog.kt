package shadowfox.botanicaladdons.common.block.dendrics.circuit

import net.minecraft.block.properties.IProperty
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import shadowfox.botanicaladdons.common.block.base.BlockModLog
import shadowfox.botanicaladdons.common.lexicon.LexiconEntries
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.lexicon.LexiconEntry
import java.util.*

/**
 * @author WireSegal
 * Created at 9:32 PM on 5/29/16.
 */
class BlockCircuitLog(name: String) : BlockModLog(name), ILexiconable, ICircuitBlock {

    init {
        tickRandomly = true
    }

    override fun updateTick(worldIn: World, pos: BlockPos?, state: IBlockState?, rand: Random?) {
        worldIn.notifyNeighborsOfStateChange(pos, this)
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

    override fun createBlockState(): BlockStateContainer? {
        return BlockStateContainer(this, BlockModLog.AXIS, ICircuitBlock.POWER)
    }

    override val ignoredProperties: Array<IProperty<*>>?
        get() = arrayOf(ICircuitBlock.POWER)

    override fun getEntry(p0: World?, p1: BlockPos?, p2: EntityPlayer?, p3: ItemStack?): LexiconEntry? {
        return LexiconEntries.circuitTree
    }
}
