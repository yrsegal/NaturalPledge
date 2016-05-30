package shadowfox.botanicaladdons.common.block.dendrics.circuit

import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import shadowfox.botanicaladdons.common.block.base.BlockModLeaves
import shadowfox.botanicaladdons.common.block.base.BlockModLog
import shadowfox.botanicaladdons.common.block.dendrics.thunder.IThunderAbsorber
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.lexicon.LexiconEntry

/**
 * @author WireSegal
 * Created at 10:19 PM on 5/29/16.
 */
class BlockCircuitLeaves(name: String) : BlockModLog(name), ICircuitBlock, ILexiconable {

    override fun getEntry(p0: World?, p1: BlockPos?, p2: EntityPlayer?, p3: ItemStack?): LexiconEntry? {
        return null //todo
    }

    override fun canConnectRedstone(state: IBlockState?, world: IBlockAccess?, pos: BlockPos?, side: EnumFacing?): Boolean {
        return true
    }

    override fun getWeakPower(blockState: IBlockState, blockAccess: IBlockAccess, pos: BlockPos, side: EnumFacing): Int {
        return ICircuitBlock.getPower(blockState, blockAccess, pos)
    }
}
