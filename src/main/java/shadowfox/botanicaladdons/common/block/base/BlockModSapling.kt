package shadowfox.botanicaladdons.common.block.base

import net.minecraft.block.IGrowable
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.properties.IProperty
import net.minecraft.block.properties.PropertyInteger
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraft.world.gen.feature.WorldGenTrees
import net.minecraftforge.common.EnumPlantType
import net.minecraftforge.common.IPlantable
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import shadowfox.botanicaladdons.api.SaplingVariantRegistry
import shadowfox.botanicaladdons.api.sapling.IIridescentSaplingVariant
import shadowfox.botanicaladdons.api.sapling.ISaplingBlock
import shadowfox.botanicaladdons.api.sapling.IridescentSaplingBaseVariant
import shadowfox.botanicaladdons.common.block.ModBlocks
import shadowfox.botanicaladdons.common.block.colored.BlockIridescentDirt
import vazkii.botania.api.state.BotaniaStateProps
import java.util.*

/**
 * @author WireSegal
 * Created at 10:13 PM on 5/27/16.
 */
abstract class BlockModSapling(name: String, vararg variants: String) : BlockMod(name, Material.PLANTS, *variants), IPlantable, IGrowable, ISaplingBlock {

    companion object {
        val STAGE = PropertyInteger.create("stage", 0, 1)
    }

    val AABB = AxisAlignedBB(0.1, 0.0, 0.1, 0.9, 0.8, 0.9)

    init {
        this.tickRandomly = true
        soundType = SoundType.PLANT
    }

    override fun canPlaceBlockAt(worldIn: World, pos: BlockPos): Boolean {
        val soil = worldIn.getBlockState(pos.down())
        return super.canPlaceBlockAt(worldIn, pos) && soil.block.canSustainPlant(soil, worldIn, pos.down(), net.minecraft.util.EnumFacing.UP, this)
    }

    open fun canSustain(state: IBlockState): Boolean {
        return false
    }

    override fun onNeighborChange(worldIn: IBlockAccess, pos: BlockPos, neighborBlock: BlockPos) {
        super.onNeighborChange(worldIn, pos, neighborBlock)
        if (worldIn is World)
            this.checkAndDropBlock(worldIn, pos)
    }

    fun checkAndDropBlock(worldIn: World, pos: BlockPos) {
        val state = worldIn.getBlockState(pos)
        if (!this.canBlockStay(worldIn, pos, state)) {
            this.dropBlockAsItem(worldIn, pos, state, 0)
            worldIn.setBlockState(pos, Blocks.AIR.defaultState, 3)
        }
    }

    override val ignoredProperties: Array<IProperty<*>>?
        get() = arrayOf(STAGE)

    fun canBlockStay(worldIn: World, pos: BlockPos, state: IBlockState): Boolean {
        if (state.block === this) {
            val soil = worldIn.getBlockState(pos.down())
            return canSustain(soil) || soil.block.canSustainPlant(soil, worldIn, pos.down(), EnumFacing.UP, this)
        }
        return this.canSustain(worldIn.getBlockState(pos.down()))
    }

    override fun getBoundingBox(state: IBlockState?, source: IBlockAccess?, pos: BlockPos?): AxisAlignedBB {
        return AABB
    }

    override fun getCollisionBoundingBox(blockState: IBlockState, worldIn: World, pos: BlockPos): AxisAlignedBB? {
        return NULL_AABB
    }

    override fun canGrow(worldIn: World, pos: BlockPos, state: IBlockState, isClient: Boolean): Boolean {
        return true
    }

    override fun canUseBonemeal(worldIn: World, rand: Random, pos: BlockPos, state: IBlockState): Boolean {
        return worldIn.rand.nextFloat().toDouble() < 0.45
    }

    override fun grow(worldIn: World, rand: Random, pos: BlockPos, state: IBlockState) {
        this.grow(worldIn, pos, state, rand)
    }

    override fun updateTick(worldIn: World, pos: BlockPos, state: IBlockState, rand: Random) {
        if (!worldIn.isRemote) {
            checkAndDropBlock(worldIn, pos)

            if (worldIn.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt(7) === 0) {
                this.grow(worldIn, pos, state, rand)
            }
        }
    }

    fun grow(worldIn: World, pos: BlockPos, state: IBlockState, rand: Random) {
        if ((state.getValue(STAGE) as Int).toInt() == 0) {
            worldIn.setBlockState(pos, state.cycleProperty(STAGE), 4)
        } else {
            this.generateTree(worldIn, pos, state, rand)
        }
    }

    abstract fun generateTree(worldIn: World, pos: BlockPos, state: IBlockState, rand: Random)

    override fun getStateFromMeta(meta: Int): IBlockState? {
        return defaultState.withProperty(STAGE, meta and 1)
    }

    override fun getMetaFromState(state: IBlockState?): Int {
        return (state ?: return 0).getValue(STAGE)
    }

    override fun isOpaqueCube(state: IBlockState?): Boolean {
        return false
    }

    override fun isFullCube(state: IBlockState?): Boolean {
        return false
    }

    override fun getPlantType(world: IBlockAccess, pos: BlockPos): EnumPlantType {
        return EnumPlantType.Plains
    }

    override fun getPlant(world: net.minecraft.world.IBlockAccess, pos: BlockPos): IBlockState {
        val state = world.getBlockState(pos)
        if (state.block !== this) return defaultState
        return state
    }

    override fun createBlockState(): BlockStateContainer? {
        return BlockStateContainer(this, STAGE)
    }

    @SideOnly(Side.CLIENT)
    override fun getBlockLayer(): BlockRenderLayer {
        return BlockRenderLayer.CUTOUT
    }
}

