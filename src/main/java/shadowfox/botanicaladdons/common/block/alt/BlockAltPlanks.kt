package shadowfox.botanicaladdons.common.block.alt

import com.teamwizardry.librarianlib.common.base.block.BlockModPlanks
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import shadowfox.botanicaladdons.common.lexicon.LexiconEntries
import shadowfox.botanicaladdons.common.lib.capitalizeFirst
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.lexicon.LexiconEntry
import vazkii.botania.api.state.enums.AltGrassVariant

/**
 * @author WireSegal
 * Created at 3:22 PM on 5/17/16.
 */
class BlockAltPlanks(name: String) : BlockModPlanks(name, *Array(6, { name + AltGrassVariant.values()[it].getName().capitalizeFirst() })), ILexiconable {
    companion object {
        val TYPE = PropertyEnum.create("type", AltGrassVariant::class.java)
    }

    override fun getStateFromMeta(meta: Int): IBlockState? {
        return defaultState.withProperty(TYPE, AltGrassVariant.values()[meta])
    }

    override fun getMetaFromState(state: IBlockState?): Int {
        return (state ?: return 0).getValue(TYPE).ordinal
    }

    override fun damageDropped(state: IBlockState?): Int {
        return getMetaFromState(state)
    }

    override fun createBlockState(): BlockStateContainer? {
        return BlockStateContainer(this, TYPE)
    }

    override fun getEntry(p0: World?, p1: BlockPos?, p2: EntityPlayer?, p3: ItemStack): LexiconEntry? {
        return LexiconEntries.sapling
    }
}
