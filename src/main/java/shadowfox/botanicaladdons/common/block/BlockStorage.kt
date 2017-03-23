package shadowfox.botanicaladdons.common.block

import com.teamwizardry.librarianlib.common.base.block.BlockMod
import net.minecraft.block.SoundType
import net.minecraft.block.material.MapColor
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.IStringSerializable
import net.minecraft.util.math.BlockPos
import net.minecraft.world.Explosion
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import shadowfox.botanicaladdons.common.lexicon.LexiconEntries
import shadowfox.botanicaladdons.common.lib.capitalizeFirst
import shadowfox.botanicaladdons.common.lib.lowercaseFirst
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.lexicon.LexiconEntry

/**
 * @author WireSegal
 * Created at 4:16 PM on 5/25/16.
 */
class BlockStorage(name: String) : BlockMod(name, Material.IRON, *Variants.variants), ILexiconable {

    companion object {
        val TYPE = PropertyEnum.create("type", Variants::class.java)
    }

    init {
        blockHardness = 5f
        blockResistance = 10f
        soundType = SoundType.METAL
    }

    enum class Variants(val material: Material, val hardness: Float, val resistance: Float, val level: Int, val lightLevel: Int, val mapColor: MapColor) : IStringSerializable {
        THUNDERSTEEL(Material.IRON, 5f, 10f, 1, 0, MapColor.SILVER),
        AQUAMARINE(Material.ROCK, 1.5f, 10f, -1, 10, MapColor.BLUE);

        override fun toString(): String {
            return this.name.toLowerCase().split("_").joinToString("", transform = String::capitalizeFirst).lowercaseFirst()
        }

        override fun getName() = name.toLowerCase()

        companion object {
            val variants: Array<String>
                get() = Array(values().size, { values()[it].toString() + "Block" })
        }
    }

    override fun createBlockState(): BlockStateContainer? {
        return BlockStateContainer(this, TYPE)
    }

    override fun getMetaFromState(state: IBlockState): Int {
        return state.getValue(TYPE).ordinal
    }

    override fun damageDropped(state: IBlockState): Int {
        return getMetaFromState(state)
    }

    override fun getStateFromMeta(meta: Int): IBlockState {
        return defaultState.withProperty(TYPE, Variants.values()[meta])
    }

    override fun getMaterial(state: IBlockState): Material? {
        return state.getValue(TYPE).material
    }

    override fun getBlockHardness(state: IBlockState, worldIn: World?, pos: BlockPos?): Float {
        return state.getValue(TYPE).hardness
    }

    override fun getHarvestLevel(state: IBlockState): Int {
        return state.getValue(TYPE).level
    }

    override fun isBeaconBase(world: IBlockAccess, pos: BlockPos, beacon: BlockPos?): Boolean {
        return world.getBlockState(pos).getValue(TYPE).material == Material.IRON
    }

    override fun getExplosionResistance(world: World, pos: BlockPos, exploder: Entity?, explosion: Explosion?): Float {
        return world.getBlockState(pos).getValue(TYPE).resistance / 5f
    }

    override fun getLightValue(state: IBlockState, world: IBlockAccess?, pos: BlockPos?): Int {
        return state.getValue(TYPE).lightLevel
    }

    override fun getMapColor(state: IBlockState): MapColor? {
        return state.getValue(TYPE).mapColor
    }

    override fun getEntry(p0: World, p1: BlockPos, p2: EntityPlayer, p3: ItemStack): LexiconEntry? {
        return when (p0.getBlockState(p1).getValue(TYPE)) {
            Variants.THUNDERSTEEL -> LexiconEntries.thorSpells
            Variants.AQUAMARINE -> LexiconEntries.njordSpells
            else -> null
        }
    }
}
