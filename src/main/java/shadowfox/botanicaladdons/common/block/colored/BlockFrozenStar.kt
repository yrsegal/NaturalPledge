package shadowfox.botanicaladdons.common.block.colored

import net.minecraft.block.SoundType
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumBlockRenderType
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.client.core.ModelHandler
import shadowfox.botanicaladdons.common.block.ModMaterials
import shadowfox.botanicaladdons.common.block.base.BlockModContainer
import shadowfox.botanicaladdons.common.block.base.ItemModBlock
import shadowfox.botanicaladdons.common.block.tile.TileStar
import shadowfox.botanicaladdons.common.core.helper.RainbowItemHelper
import shadowfox.botanicaladdons.common.items.base.ItemMod
import shadowfox.botanicaladdons.common.lexicon.LexiconEntries
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.lexicon.LexiconEntry
import vazkii.botania.common.core.helper.ItemNBTHelper

/**
 * @author WireSegal
 * Created at 1:37 PM on 5/4/16.
 */
class BlockFrozenStar(name: String) : BlockModContainer(name, ModMaterials.TRANSPARENT), ModelHandler.IItemColorProvider, ILexiconable {
    private val AABB = AxisAlignedBB(0.25, 0.25, 0.25, 0.75, 0.75, 0.75)

    companion object {
        val TAG_SIZE = "size"

        val DEFAULT_SIZE = 0.5f

        fun getSize(stack: ItemStack) = ItemNBTHelper.getFloat(stack, TAG_SIZE, DEFAULT_SIZE)
        fun setSize(stack: ItemStack, size: Float) = ItemNBTHelper.setFloat(stack, TAG_SIZE, size)
    }

    override val item: ItemBlock
        get() = object : ItemModBlock(this) {
            override fun getSubItems(itemIn: Item, tab: CreativeTabs?, subItems: MutableList<ItemStack>) {
                for (color in RainbowItemHelper.defaultColors) {
                    subItems.add(RainbowItemHelper.colorStack(color, this))
                }
            }
        }

    init {
        setLightLevel(1f)
        soundType = SoundType.CLOTH
    }

    override fun getRenderType(state: IBlockState?): EnumBlockRenderType? {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED
    }

    override fun createNewTileEntity(worldIn: World, meta: Int): TileEntity {
        return TileStar()
    }

    override fun getDrops(world: IBlockAccess, pos: BlockPos, state: IBlockState, fortune: Int): MutableList<ItemStack> {
        val te = world.getTileEntity(pos)
        if (te is TileStar) {
            val stack = RainbowItemHelper.colorStack(te.getColor(), this)
            if (te.size != DEFAULT_SIZE)
                setSize(stack, te.size)
            return mutableListOf(stack)
        }
        return mutableListOf()
    }

    @SideOnly(Side.CLIENT)
    override fun getItemColor(): IItemColor = IItemColor {
        itemStack, i ->
        RainbowItemHelper.colorFromInt(RainbowItemHelper.getColor(itemStack))
    }

    override fun addInformation(stack: ItemStack, player: EntityPlayer?, tooltip: MutableList<String>, advanced: Boolean) {
        super.addInformation(stack, player, tooltip, advanced)
        val color = RainbowItemHelper.getColor(stack)
        if (color in RainbowItemHelper.defaultColors)
            ItemMod.addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.color.${RainbowItemHelper.defaultColors.indexOf(color)}")
        else
            ItemMod.addToTooltip(tooltip, "#${Integer.toHexString(color).toUpperCase()}")

        if (getSize(stack) != DEFAULT_SIZE)
            ItemMod.addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.customSize", getSize(stack))
    }


    override fun isFullBlock(state: IBlockState?) = false
    override fun isBlockSolid(worldIn: IBlockAccess?, pos: BlockPos?, side: EnumFacing?) = false
    override fun isOpaqueCube(state: IBlockState?) = false
    override fun isFullCube(state: IBlockState?) = false
    override fun isPassable(worldIn: IBlockAccess?, pos: BlockPos?) = true
    override fun getBoundingBox(state: IBlockState?, source: IBlockAccess?, pos: BlockPos?) = AABB
    override fun getCollisionBoundingBox(blockState: IBlockState?, worldIn: World?, pos: BlockPos?) = NULL_AABB
    override fun canSpawnInBlock(): Boolean = true
    override fun isReplaceable(worldIn: IBlockAccess?, pos: BlockPos?) = false

    override fun getPickBlock(state: IBlockState?, target: RayTraceResult, world: World, pos: BlockPos, player: EntityPlayer?): ItemStack? {
        val te = world.getTileEntity(pos)
        if (te is TileStar) {
            val stack = RainbowItemHelper.colorStack(te.getColor(), this)
            if (te.size != DEFAULT_SIZE)
                setSize(stack, te.size)
            return stack
        }
        return super.getPickBlock(state, target, world, pos, player)
    }

    override fun onBlockPlacedBy(world: World, pos: BlockPos, state: IBlockState, placer: EntityLivingBase, stack: ItemStack) {
        val te = world.getTileEntity(pos)
        if (te is TileStar) {
            te.size = getSize(stack)
            te.starColor = RainbowItemHelper.getColor(stack)
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

    override fun getSubBlocks(itemIn: Item?, tab: CreativeTabs?, list: MutableList<ItemStack>) {
        // NO-OP
    }

    override fun getEntry(p0: World?, p1: BlockPos?, p2: EntityPlayer?, p3: ItemStack?): LexiconEntry? {
        return LexiconEntries.star
    }
}
