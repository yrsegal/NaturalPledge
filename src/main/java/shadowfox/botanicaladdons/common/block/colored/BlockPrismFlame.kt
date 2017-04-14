package shadowfox.botanicaladdons.common.block.colored

import com.teamwizardry.librarianlib.features.base.block.BlockModContainer
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
import shadowfox.botanicaladdons.common.block.ModMaterials
import shadowfox.botanicaladdons.common.block.tile.TilePrismFlame
import shadowfox.botanicaladdons.common.core.helper.RainbowItemHelper
import shadowfox.botanicaladdons.common.lexicon.LexiconEntries
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

    override fun onBlockActivated(world: World, pos: BlockPos, state: IBlockState, player: EntityPlayer, hand: EnumHand, s: EnumFacing?, xs: Float, ys: Float, zs: Float): Boolean {
        val stack = player.getHeldItem(hand)
        if (WorldTypeSkyblock.isWorldSkyblock(world)) {
            if (stack != null && stack.item === Item.getItemFromBlock(Blocks.SAPLING) && !player.inventory.hasItemStack(ItemStack(ModItems.lexicon))) {
                if (!world.isRemote)
                    stack.count--
                if (!player.inventory.addItemStackToInventory(ItemStack(ModItems.lexicon)))
                    player.dropItem(ItemStack(ModItems.lexicon), false)
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
