package shadowfox.botanicaladdons.common.block.alt

import com.teamwizardry.librarianlib.common.base.block.BlockModLeaves
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.World
import shadowfox.botanicaladdons.api.sapling.IStackConvertible
import shadowfox.botanicaladdons.common.block.ModBlocks
import shadowfox.botanicaladdons.common.lexicon.LexiconEntries
import shadowfox.botanicaladdons.common.lib.capitalizeFirst
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.lexicon.LexiconEntry
import vazkii.botania.api.state.enums.AltGrassVariant
import java.util.*

/**
 * @author WireSegal
 * Created at 8:15 PM on 5/16/16.
 */
@Suppress("LeakingThis")
abstract class BlockAltLeaves(name: String, set: Int) : BlockModLeaves(name + set, *Array(if (set == 0) 4 else 2, { name + AltGrassVariant.values()[set * 4 + it].getName().capitalizeFirst() })), ILexiconable {

    companion object {
        val TYPE_PROPS = Array(2) { i ->
            PropertyEnum.create("type", AltGrassVariant::class.java) {
                (it?.ordinal ?: -1) < ((i + 1) * 4) && (it?.ordinal ?: -1) >= (i * 4)
            }
        }
    }

    override val canBeOpaque: Boolean
        get() = false

    abstract val colorSet: Int

    init {
        if (colorSet < 0 || colorSet >= 2)
            throw IllegalArgumentException("Colorset out of range for Alt Leaves! (passed in $colorSet)")
    }

    override fun getItemDropped(state: IBlockState, rand: Random, fortune: Int): Item? {
        return ModBlocks.irisSapling.itemForm
    }

    override fun getStateFromMeta(meta: Int): IBlockState {
        val i = meta and 3
        return super.getStateFromMeta(meta).withProperty(TYPE_PROPS[colorSet], AltGrassVariant.values()[colorSet * 4 + i])
    }

    override fun getMetaFromState(state: IBlockState?): Int {
        state ?: return 0
        return super.getMetaFromState(state) or (state.getValue(TYPE_PROPS[colorSet]).ordinal - (colorSet * 4))
    }

    override fun createBlockState(): BlockStateContainer? {
        return BlockStateContainer(this, TYPE_PROPS[colorSet], DECAYABLE, CHECK_DECAY)
    }

    override fun getPickBlock(state: IBlockState, target: RayTraceResult?, world: World?, pos: BlockPos?, player: EntityPlayer?): ItemStack {
        return ItemStack(this, 1, state.getValue(TYPE_PROPS[colorSet]).ordinal - colorSet * 4)
    }

    override fun getEntry(p0: World?, p1: BlockPos?, p2: EntityPlayer?, p3: ItemStack): LexiconEntry? {
        return LexiconEntries.sapling
    }
}
