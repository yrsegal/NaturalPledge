package shadowfox.botanicaladdons.common.block.colored

import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumBlockRenderType
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import shadowfox.botanicaladdons.common.block.base.BlockModContainer
import shadowfox.botanicaladdons.common.block.tile.TileStar
import shadowfox.botanicaladdons.common.items.colored.ItemStarPlacer

/**
 * @author WireSegal
 * Created at 1:37 PM on 5/4/16.
 */
class BlockFrozenStar(name: String) : BlockModContainer(name, Material.cloth) {
    private val AABB = AxisAlignedBB(0.25, 0.25, 0.25, 0.75, 0.75, 0.75)

    init {
        setLightLevel(1f)
        soundType = SoundType.CLOTH
    }

    override val hasItem: Boolean
        get() = false

    override fun getRenderType(state: IBlockState?): EnumBlockRenderType? {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED
    }

    override fun createNewTileEntity(worldIn: World, meta: Int): TileEntity {
        return TileStar()
    }

    override fun getDrops(world: IBlockAccess, pos: BlockPos, state: IBlockState, fortune: Int): MutableList<ItemStack> {
        val te = world.getTileEntity(pos)
        if (te is TileStar) {
            return mutableListOf(ItemStarPlacer.colorStack(te.getColor()))
        }
        return mutableListOf()
    }


    override fun isFullBlock(state: IBlockState?) = false
    override fun isBlockSolid(worldIn: IBlockAccess?, pos: BlockPos?, side: EnumFacing?) = false
    override fun isOpaqueCube(state: IBlockState?) = false
    override fun isFullCube(state: IBlockState?) = false
    override fun isPassable(worldIn: IBlockAccess?, pos: BlockPos?) = true
    override fun getBoundingBox(state: IBlockState?, source: IBlockAccess?, pos: BlockPos?) = AABB
    override fun getCollisionBoundingBox(blockState: IBlockState?, worldIn: World?, pos: BlockPos?) = NULL_AABB

    override fun getPickBlock(state: IBlockState?, target: RayTraceResult, world: World, pos: BlockPos, player: EntityPlayer?): ItemStack? {
        val te = world.getTileEntity(pos)
        if (te is TileStar) {
            return ItemStarPlacer.colorStack(te.getColor())
        }
        return super.getPickBlock(state, target, world, pos, player)
    }

    override fun onBlockPlacedBy(world: World, pos: BlockPos, state: IBlockState, placer: EntityLivingBase, stack: ItemStack) {
        val te = world.getTileEntity(pos)
        if (te is TileStar) {
            te.size = ItemStarPlacer.getSize(stack)
            te.starColor = ItemStarPlacer.getColor(stack)
        }
    }

    override fun removedByPlayer(state: IBlockState, world: World, pos: BlockPos, player: EntityPlayer, willHarvest: Boolean): Boolean {
        if (willHarvest) {
            onBlockHarvested(world, pos, state, player)
            return true
        } else {
            return super.removedByPlayer(state, world, pos, player, willHarvest)
        }
    }

    override fun harvestBlock(worldIn: World, player: EntityPlayer?, pos: BlockPos, state: IBlockState?, te: TileEntity?, stack: ItemStack?) {
        super.harvestBlock(worldIn, player, pos, state, te, stack)
        worldIn.setBlockToAir(pos)
    }
}
