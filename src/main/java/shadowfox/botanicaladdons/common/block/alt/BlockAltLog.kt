package shadowfox.botanicaladdons.common.block.alt

import net.minecraft.block.BlockLog
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
import shadowfox.botanicaladdons.common.block.colored.BlockIridescentLog
import shadowfox.botanicaladdons.common.lexicon.LexiconEntries
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.lexicon.LexiconEntry
import vazkii.botania.api.state.enums.AltGrassVariant

/**
 * @author WireSegal
 * Created at 11:37 AM on 5/16/16.
 */
abstract class BlockAltLog(name: String, val colorSet: Int) : BlockModLog(name + colorSet, *Array(if (colorSet == 0) 4 else 2, { name + AltGrassVariant.values()[colorSet * 4 + it].getName().capitalizeFirst() })), ILexiconable {
    companion object {
        val TYPE_PROPS = Array(2) { i ->
            PropertyEnum.create("type", AltGrassVariant::class.java) {
                (it?.ordinal ?: -1) < ((i + 1) * 4) && (it?.ordinal ?: -1) >= (i * 4)
            }
        }

        fun String.capitalizeFirst(): String {
            if (this.length == 0) return this
            return this.slice(0..0).capitalize() + this.slice(1..this.length - 1)
        }
    }

    abstract val TYPE: PropertyEnum<AltGrassVariant>

    init {
        if (colorSet < 0 || colorSet >= 2)
            throw IllegalArgumentException("Colorset out of range for Alt Log! (passed in $colorSet)")
        soundType = SoundType.WOOD
    }

    override fun getStateFromMeta(meta: Int): IBlockState {
        val j = meta and 3
        return super.getStateFromMeta(meta).withProperty(TYPE, AltGrassVariant.values()[colorSet * 4 + j])
    }

    override fun getMetaFromState(state: IBlockState?): Int {
        state ?: return 0
        return (state.getValue(TYPE).ordinal - (colorSet * 4)) or super.getMetaFromState(state)
    }

    override fun createBlockState(): BlockStateContainer? {
        return BlockStateContainer(this, TYPE, AXIS)
    }

    override fun damageDropped(state: IBlockState?): Int {
        return (state ?: return 0).getValue(TYPE).ordinal - colorSet * 4
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
