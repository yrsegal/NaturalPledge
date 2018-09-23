package com.wiresegal.naturalpledge.common.block.colored

import com.teamwizardry.librarianlib.features.base.block.tile.BlockModContainer
import com.teamwizardry.librarianlib.features.kotlin.isNotEmpty
import net.minecraft.block.SoundType
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumBlockRenderType
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import com.wiresegal.naturalpledge.common.block.ModMaterials
import com.wiresegal.naturalpledge.common.block.tile.TilePrismFlame
import com.wiresegal.naturalpledge.common.core.helper.RainbowItemHelper
import com.wiresegal.naturalpledge.common.lexicon.LexiconEntries
import vazkii.botania.api.item.IPhantomInkable
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.lexicon.LexiconEntry
import vazkii.botania.common.item.ModItems
import vazkii.botania.common.world.WorldTypeSkyblock

/**
 * @author WireSegal
 * Created at 1:37 PM on 5/4/16.
 */
class BlockPrismFlame(name: String) : BlockModContainer(name, ModMaterials.TRANSPARENT), ILexiconable {
    private val AABB = AxisAlignedBB(0.25, 0.25, 0.25, 0.75, 0.75, 0.75)

    init {
        setLightLevel(1f)
        soundType = SoundType.CLOTH
    }

    override fun createItemForm() = null

    override fun getBoundingBox(state: IBlockState?, world: IBlockAccess?, pos: BlockPos?) = AABB
    override fun getRenderType(state: IBlockState?) = EnumBlockRenderType.INVISIBLE
    override fun isOpaqueCube(state: IBlockState?) = false
    override fun isFullCube(state: IBlockState?) = false
    override fun isPassable(world: IBlockAccess?, pos: BlockPos?) = true
    override fun getCollisionBoundingBox(blockState: IBlockState?, worldIn: IBlockAccess?, pos: BlockPos?) = NULL_AABB
    override fun canSpawnInBlock(): Boolean = true
    override fun isReplaceable(worldIn: IBlockAccess?, pos: BlockPos?) = false

    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        val stack = playerIn.getHeldItem(hand)
        if (WorldTypeSkyblock.isWorldSkyblock(worldIn)) {
            if (stack.isNotEmpty && stack.item === Item.getItemFromBlock(Blocks.SAPLING) && !playerIn.inventory.hasItemStack(ItemStack(ModItems.lexicon))) {
                if (!worldIn.isRemote)
                    stack.count--
                if (!playerIn.inventory.addItemStackToInventory(ItemStack(ModItems.lexicon)))
                    playerIn.dropItem(ItemStack(ModItems.lexicon), false)
                return true
            }
        }
        return false
    }

    override fun onBlockPlacedBy(world: World, pos: BlockPos, state: IBlockState?, placer: EntityLivingBase?, stack: ItemStack) {
        val te = world.getTileEntity(pos)
        if (te is TilePrismFlame) {
            te.color = RainbowItemHelper.getColor(stack)
            if (stack.item is IPhantomInkable)
                te.phantomInk = (stack.item as IPhantomInkable).hasPhantomInk(stack)
        }
    }

    override fun createTileEntity(world: World, state: IBlockState): TileEntity? {
        return TilePrismFlame()
    }

    override fun getDrops(world: IBlockAccess?, pos: BlockPos?, state: IBlockState, fortune: Int): List<ItemStack> {
        return listOf()
    }

    override fun getEntry(world: World, pos: BlockPos, player: EntityPlayer, lexicon: ItemStack): LexiconEntry? {
        return LexiconEntries.prism
    }
}
