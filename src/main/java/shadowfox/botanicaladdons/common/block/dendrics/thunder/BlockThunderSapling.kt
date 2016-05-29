package shadowfox.botanicaladdons.common.block.dendrics.thunder

import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.gen.feature.WorldGenTrees
import net.minecraftforge.client.event.sound.PlaySoundEvent
import net.minecraftforge.event.terraingen.TerrainGen
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import shadowfox.botanicaladdons.api.sapling.ISealingBlock
import shadowfox.botanicaladdons.common.block.ModBlocks
import shadowfox.botanicaladdons.common.block.base.BlockModSapling
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.lexicon.LexiconEntry
import java.util.*

/**
 * @author WireSegal
 * Created at 10:35 PM on 5/27/16.
 */
class BlockThunderSapling(name: String) : BlockModSapling(name), IThunderAbsorber, ILexiconable {
    override fun generateTree(worldIn: World, pos: BlockPos, state: IBlockState, rand: Random) {
        if (!TerrainGen.saplingGrowTree(worldIn, rand, pos)) return

        worldIn.setBlockState(pos, Blocks.AIR.defaultState, 4)

        if (!WorldGenTrees(true, 4, ModBlocks.thunderLog.defaultState, ModBlocks.thunderLeaves.defaultState, false).generate(worldIn, rand, pos))
            worldIn.setBlockState(pos, state, 4)
    }

    override fun getEntry(p0: World?, p1: BlockPos?, p2: EntityPlayer?, p3: ItemStack?): LexiconEntry? {
        return null //todo
    }
}
