package shadowfox.botanicaladdons.common.block.alt

import net.minecraft.block.SoundType
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.World
import shadowfox.botanicaladdons.common.block.base.BlockModLog
import shadowfox.botanicaladdons.common.lexicon.LexiconEntries
import shadowfox.botanicaladdons.common.lib.capitalizeFirst
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.lexicon.LexiconEntry
import vazkii.botania.api.state.enums.AltGrassVariant

/**
 * @author WireSegal
 * Created at 11:37 AM on 5/16/16.
 */
@Suppress("LeakingThis")
abstract class BlockAltLog(name: String, set: Int) : BlockModLog(name + set, *Array(if (set == 0) 4 else 2, { name + AltGrassVariant.values()[set * 4 + it].getName().capitalizeFirst() })), ILexiconable {
    companion object {
        val TYPE_PROPS = Array(2) { i ->
            PropertyEnum.create("type", AltGrassVariant::class.java) {
                (it?.ordinal ?: -1) < ((i + 1) * 4) && (it?.ordinal ?: -1) >= (i * 4)
            }
        }
    }

    abstract val colorSet: Int

    init {
        if (colorSet < 0 || colorSet >= 2)
            throw IllegalArgumentException("Colorset out of range for Alt Log! (passed in $colorSet)")
        soundType = SoundType.WOOD
    }

    override fun getStateFromMeta(meta: Int): IBlockState {
        val j = meta and 3
        return super.getStateFromMeta(meta).withProperty(TYPE_PROPS[colorSet], AltGrassVariant.values()[colorSet * 4 + j])
    }

    override fun getMetaFromState(state: IBlockState?): Int {
        state ?: return 0
        return (state.getValue(TYPE_PROPS[colorSet]).ordinal - (colorSet * 4)) or super.getMetaFromState(state)
    }

    override fun createBlockState(): BlockStateContainer? {
        return BlockStateContainer(this, TYPE_PROPS[colorSet], AXIS)
    }

    override fun damageDropped(state: IBlockState?): Int {
        return (state ?: return 0).getValue(TYPE_PROPS[colorSet]).ordinal - colorSet * 4
    }

    override fun createStackedBlock(state: IBlockState): ItemStack? {
        return ItemStack(this, 1, damageDropped(state))
    }

    override fun getPickBlock(state: IBlockState?, target: RayTraceResult?, world: World?, pos: BlockPos?, player: EntityPlayer?): ItemStack? {
        return createStackedBlock(state ?: return null)
    }

    override fun getEntry(p0: World?, p1: BlockPos?, p2: EntityPlayer?, p3: ItemStack?): LexiconEntry? {
        return LexiconEntries.sapling
    }
}
