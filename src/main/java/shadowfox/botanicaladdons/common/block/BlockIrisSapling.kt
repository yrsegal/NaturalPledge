package shadowfox.botanicaladdons.common.block

import net.minecraft.block.Block
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
import shadowfox.botanicaladdons.common.block.base.BlockMod
import shadowfox.botanicaladdons.common.block.colored.BlockIridescentDirt
import shadowfox.botanicaladdons.common.core.tab.ModCreativeTab
import shadowfox.botanicaladdons.common.core.tab.ModTabs
import vazkii.botania.api.state.BotaniaStateProps
import java.util.*
import vazkii.botania.common.block.ModBlocks as BotaniaBlocks

/**
 * @author WireSegal
 * Created at 4:01 PM on 5/14/16.
 */
class BlockIrisSapling(name: String) : BlockMod(name, Material.plants), IPlantable, IGrowable, ISaplingBlock {

    companion object {
        val STAGE = PropertyInteger.create("stage", 0, 1)

        class IridescentDirtSaplingVariant : IIridescentSaplingVariant {
            override fun getLeaves(soil: IBlockState): IBlockState {
                val dye = soil.getValue(BlockIridescentDirt.COLOR)
                val colorSet = dye.metadata / 4
                val block = ModBlocks.irisLeaves[colorSet]
                return block.defaultState.withProperty(block.COLOR, dye)
            }

            override fun getWood(soil: IBlockState): IBlockState {
                val dye = soil.getValue(BlockIridescentDirt.COLOR)
                val colorSet = dye.metadata / 4
                val block = ModBlocks.irisLogs[colorSet]
                return block.defaultState.withProperty(block.COLOR, dye)
            }

            override fun matchesSoil(soil: IBlockState): Boolean {
                return soil.block == ModBlocks.irisDirt
            }
        }

        class AltGrassSaplingVariant: IIridescentSaplingVariant {
            override fun getLeaves(soil: IBlockState): IBlockState {
                val variant = soil.getValue(BotaniaStateProps.ALTGRASS_VARIANT)
                val colorSet = variant.ordinal / 4
                val block = ModBlocks.altLeaves[colorSet]
                return block.defaultState.withProperty(block.TYPE, variant)
            }

            override fun getWood(soil: IBlockState): IBlockState {
                val variant = soil.getValue(BotaniaStateProps.ALTGRASS_VARIANT)
                val colorSet = variant.ordinal / 4
                val block = ModBlocks.altLogs[colorSet]
                return block.defaultState.withProperty(block.TYPE, variant)
            }

            override fun matchesSoil(soil: IBlockState): Boolean {
                return soil.block == BotaniaBlocks.altGrass
            }
        }
    }

    val AABB = AxisAlignedBB(0.1, 0.0, 0.1, 0.9, 0.8, 0.9)

    init {
        this.tickRandomly = true
        soundType = SoundType.PLANT

        SaplingVariantRegistry.registerVariant("irisDirt", IridescentDirtSaplingVariant())
        SaplingVariantRegistry.registerVariant("altGrass", AltGrassSaplingVariant())
        SaplingVariantRegistry.registerVariant("rainbowDirt",
                IridescentSaplingBaseVariant(
                        ModBlocks.rainbowDirt.defaultState,
                        ModBlocks.rainbowLog.defaultState,
                        ModBlocks.rainbowLeaves.defaultState))
    }

    override val creativeTab: ModCreativeTab?
        get() = ModTabs.TabWood

    override fun canPlaceBlockAt(worldIn: World, pos: BlockPos): Boolean {
        val soil = worldIn.getBlockState(pos.down())
        return super.canPlaceBlockAt(worldIn, pos) && soil.block.canSustainPlant(soil, worldIn, pos.down(), net.minecraft.util.EnumFacing.UP, this)
    }

    fun canSustain(state: IBlockState): Boolean {
        return SaplingVariantRegistry.getVariant(state) != null
    }

    override fun onNeighborBlockChange(worldIn: World, pos: BlockPos, state: IBlockState, neighborBlock: Block?) {
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock)
        this.checkAndDropBlock(worldIn, pos, state)
    }

    protected fun checkAndDropBlock(worldIn: World, pos: BlockPos, state: IBlockState) {
        if (!this.canBlockStay(worldIn, pos, state)) {
            this.dropBlockAsItem(worldIn, pos, state, 0)
            worldIn.setBlockState(pos, Blocks.air.defaultState, 3)
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
        return canSustain(worldIn.getBlockState(pos.down()))
    }

    override fun canUseBonemeal(worldIn: World, rand: Random, pos: BlockPos, state: IBlockState): Boolean {
        return worldIn.rand.nextFloat().toDouble() < 0.45
    }

    override fun grow(worldIn: World, rand: Random, pos: BlockPos, state: IBlockState) {
        this.grow(worldIn, pos, state, rand)
    }

    override fun updateTick(worldIn: World, pos: BlockPos, state: IBlockState, rand: Random) {
        if (!worldIn.isRemote) {
            checkAndDropBlock(worldIn, pos, state)

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

    fun generateTree(worldIn: World, pos: BlockPos, state: IBlockState, rand: Random) {
        val soil = worldIn.getBlockState(pos.down()) ?: return
        val variant = SaplingVariantRegistry.getVariant(soil) ?: return

        if (!net.minecraftforge.event.terraingen.TerrainGen.saplingGrowTree(worldIn, rand, pos)) return

        worldIn.setBlockState(pos, Blocks.air.defaultState, 4)

        if (!WorldGenTrees(true, 4, variant.getWood(soil), variant.getLeaves(soil), false).generate(worldIn, rand, pos)) {
            worldIn.setBlockState(pos, state, 4)
        } else {
            worldIn.setBlockState(pos.down(), soil, 4)
        }
    }

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
