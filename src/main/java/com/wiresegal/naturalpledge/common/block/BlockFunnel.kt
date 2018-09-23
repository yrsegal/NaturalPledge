package com.wiresegal.naturalpledge.common.block

import com.teamwizardry.librarianlib.features.base.block.tile.BlockModContainer
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.properties.IProperty
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.properties.PropertyDirection
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.InventoryHelper.spawnItemStack
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.*
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.items.CapabilityItemHandler
import com.wiresegal.naturalpledge.common.block.tile.TileLivingwoodFunnel
import com.wiresegal.naturalpledge.common.lexicon.LexiconEntries
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.lexicon.LexiconEntry
import vazkii.botania.api.wand.IWandHUD

/**
 * @author L0neKitsune
 * Created on 3/20/16.
 */
class BlockFunnel(name: String) : BlockModContainer(name, Material.WOOD), ILexiconable, IWandHUD {
    companion object {
        val FACING = PropertyDirection.create("facing", { facing -> facing != EnumFacing.UP })
        val ENABLED = PropertyBool.create("enabled")


        val BASE_AABB = AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.625, 1.0)
        val SOUTH_AABB = AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 0.125)
        val NORTH_AABB = AxisAlignedBB(0.0, 0.0, 0.875, 1.0, 1.0, 1.0)
        val WEST_AABB = AxisAlignedBB(0.875, 0.0, 0.0, 1.0, 1.0, 1.0)
        val EAST_AABB = AxisAlignedBB(0.0, 0.0, 0.0, 0.125, 1.0, 1.0)

    }

    override val ignoredProperties: Array<IProperty<*>>
        get() = arrayOf(ENABLED)

    init {
        blockHardness = 2f
        defaultState = defaultState.withProperty(FACING, EnumFacing.DOWN).withProperty(ENABLED, true)
    }

    override fun getBoundingBox(state: IBlockState?, source: IBlockAccess?, pos: BlockPos?): AxisAlignedBB {
        return FULL_BLOCK_AABB
    }

    override fun addCollisionBoxToList(state: IBlockState, worldIn: World, pos: BlockPos, axis: AxisAlignedBB, lists: List<AxisAlignedBB>, collider: Entity?, actual: Boolean) {
        addCollisionBoxToList(pos, axis, lists, BASE_AABB)
        addCollisionBoxToList(pos, axis, lists, SOUTH_AABB)
        addCollisionBoxToList(pos, axis, lists, NORTH_AABB)
        addCollisionBoxToList(pos, axis, lists, WEST_AABB)
        addCollisionBoxToList(pos, axis, lists, EAST_AABB)
    }

    override fun getStateForPlacement(world: World?, pos: BlockPos?, facing: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float, meta: Int, placer: EntityLivingBase?, hand: EnumHand?): IBlockState {
        var enumfacing = facing!!.opposite
        if (enumfacing == EnumFacing.UP) {
            enumfacing = EnumFacing.DOWN
        }
        return this.defaultState.withProperty(FACING, enumfacing).withProperty(ENABLED, true)
    }

    override fun createTileEntity(world: World, state: IBlockState): TileEntity? {
        return TileLivingwoodFunnel()
    }

    override fun isSideSolid(base_state: IBlockState?, world: IBlockAccess?, pos: BlockPos?, side: EnumFacing?): Boolean {
        return side == EnumFacing.UP
    }

    override fun onBlockAdded(worldIn: World, pos: BlockPos, state: IBlockState) {
        this.updateState(worldIn, pos, state)
    }

    override fun neighborChanged(state: IBlockState, worldIn: World, pos: BlockPos, blockIn: Block?, fromPos: BlockPos?) {
        this.updateState(worldIn, pos, state)
    }

    private fun updateState(worldIn: World, pos: BlockPos, state: IBlockState) {
        val flag = !worldIn.isBlockPowered(pos)
        if (flag != state.getValue(ENABLED)) worldIn.setBlockState(pos, state.withProperty(ENABLED, flag), 4)
    }

    override fun breakBlock(worldIn: World, pos: BlockPos, state: IBlockState) {
        val tileentity = worldIn.getTileEntity(pos)
        if (tileentity != null && tileentity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
            val cap = tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)!!
            (0 until cap.slots)
                    .map { cap.getStackInSlot(it) }
                    .filterNot { it.isEmpty }
                    .forEach { spawnItemStack(worldIn, pos.x + 0.5, pos.y + 0.5, pos.z + 0.5, it) }
            worldIn.updateComparatorOutputLevel(pos, this)
        }

        super.breakBlock(worldIn, pos, state)
    }

    override fun getRenderType(state: IBlockState?): EnumBlockRenderType {
        return EnumBlockRenderType.MODEL
    }

    override fun isFullCube(state: IBlockState?): Boolean {
        return false
    }

    override fun isOpaqueCube(state: IBlockState?): Boolean = false

    @SideOnly(Side.CLIENT)
    override fun shouldSideBeRendered(blockState: IBlockState, blockAccess: IBlockAccess, pos: BlockPos, side: EnumFacing): Boolean = true

    override fun hasComparatorInputOverride(state: IBlockState): Boolean = true

    override fun getComparatorInputOverride(blockState: IBlockState, worldIn: World, pos: BlockPos): Int {
        val tile = worldIn.getTileEntity(pos) ?: return 0
        return if (tile is TileLivingwoodFunnel) {
            if (tile.inventory.getStackInSlot(0).isEmpty) 0 else 15
        } else 0
    }

    @SideOnly(Side.CLIENT)
    override fun getBlockLayer(): BlockRenderLayer = BlockRenderLayer.CUTOUT_MIPPED

    override fun getStateFromMeta(meta: Int): IBlockState {
        return this.defaultState.withProperty(FACING, EnumFacing.getFront(meta and 7)).withProperty(ENABLED, (meta and 8) != 0)
    }

    override fun getMetaFromState(state: IBlockState): Int {
        return (if (state.getValue(ENABLED)) 8 else 0) or state.getValue(FACING).index
    }

    override fun withRotation(state: IBlockState, rot: Rotation): IBlockState {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)))
    }

    override fun withMirror(state: IBlockState, mirrorIn: Mirror): IBlockState {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)))
    }

    override fun createBlockState(): BlockStateContainer {
        return BlockStateContainer(this, FACING, ENABLED)
    }

    override fun getEntry(p0: World?, p1: BlockPos?, p2: EntityPlayer?, p3: ItemStack): LexiconEntry? {
        return LexiconEntries.funnel
    }

    @SideOnly(Side.CLIENT)
    override fun renderHUD(mc: Minecraft, res: ScaledResolution, world: World, pos: BlockPos) {
        val te = world.getTileEntity(pos) ?: return
        if (te is TileLivingwoodFunnel) {
            val stack = te.inventory.getStackInSlot(0)
            if (!stack.isEmpty) {
                RenderHelper.enableGUIStandardItemLighting()
                mc.renderItem.renderItemIntoGUI(stack, res.scaledWidth / 2, res.scaledHeight / 2)
                RenderHelper.disableStandardItemLighting()
            }
        }
    }
}
