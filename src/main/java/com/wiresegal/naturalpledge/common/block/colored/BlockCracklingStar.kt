package com.wiresegal.naturalpledge.common.block.colored

import com.teamwizardry.librarianlib.features.base.block.ItemModBlock
import com.teamwizardry.librarianlib.features.base.block.tile.BlockModContainer
import com.teamwizardry.librarianlib.features.base.item.IItemColorProvider
import com.teamwizardry.librarianlib.features.utilities.DimWithPos
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper.addToTooltip
import net.minecraft.block.SoundType
import net.minecraft.block.state.IBlockState
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumBlockRenderType
import net.minecraft.util.EnumFacing
import net.minecraft.util.NonNullList
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import com.wiresegal.naturalpledge.api.lib.LibMisc
import com.wiresegal.naturalpledge.common.block.ModMaterials
import com.wiresegal.naturalpledge.common.block.tile.TileCracklingStar
import com.wiresegal.naturalpledge.common.core.helper.RainbowItemHelper
import com.wiresegal.naturalpledge.common.lexicon.LexiconEntries
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.lexicon.LexiconEntry
import vazkii.botania.api.wand.IWandable
import java.util.*

/**
 * @author WireSegal
 * Created at 1:37 PM on 5/4/16.
 */
class BlockCracklingStar(name: String) : BlockModContainer(name, ModMaterials.TRANSPARENT), IItemColorProvider, IWandable, ILexiconable {
    private val AABB = AxisAlignedBB(0.25, 0.25, 0.25, 0.75, 0.75, 0.75)

    override fun createItemForm() = object : ItemModBlock(this) {
        override fun getSubItems(tab: CreativeTabs, subItems: NonNullList<ItemStack>) {
            if (isInCreativeTab(tab))
                RainbowItemHelper.defaultColors.mapTo(subItems) { RainbowItemHelper.colorStack(it, this) }
        }
    }

    init {
        setLightLevel(1f)
        soundType = SoundType.CLOTH
    }

    override fun getRenderType(state: IBlockState?): EnumBlockRenderType? {
        return EnumBlockRenderType.INVISIBLE
    }

    override fun createTileEntity(world: World, state: IBlockState): TileEntity? {
        return TileCracklingStar()
    }

    override fun getDrops(world: IBlockAccess, pos: BlockPos, state: IBlockState, fortune: Int): MutableList<ItemStack> {
        val te = world.getTileEntity(pos)
        if (te is TileCracklingStar) {
            val stack = RainbowItemHelper.colorStack(te.color, this)
            return mutableListOf(stack)
        }
        return mutableListOf()
    }

    override val itemColorFunction: ((ItemStack, Int) -> Int)?
        get() = {
            itemStack, _ ->
            RainbowItemHelper.colorFromInt(RainbowItemHelper.getColor(itemStack))
        }

    override fun addInformation(stack: ItemStack, player: World?, tooltip: MutableList<String>, advanced: ITooltipFlag) {
        super.addInformation(stack, player, tooltip, advanced)
        val color = RainbowItemHelper.getColor(stack)
        if (color in RainbowItemHelper.defaultColors)
            addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.color.${RainbowItemHelper.defaultColors.indexOf(color)}")
        else
            addToTooltip(tooltip, "#${Integer.toHexString(color).toUpperCase()}")
    }


    override fun isFullBlock(state: IBlockState?) = false
    override fun isTopSolid(state: IBlockState?) = false
    override fun isOpaqueCube(state: IBlockState?) = false
    override fun isFullCube(state: IBlockState?) = false
    override fun isPassable(worldIn: IBlockAccess?, pos: BlockPos?) = true
    override fun getBoundingBox(state: IBlockState?, source: IBlockAccess?, pos: BlockPos?) = AABB
    override fun getCollisionBoundingBox(blockState: IBlockState?, worldIn: IBlockAccess?, pos: BlockPos?) = NULL_AABB
    override fun canSpawnInBlock(): Boolean = true
    override fun isReplaceable(worldIn: IBlockAccess?, pos: BlockPos?) = false

    override fun getPickBlock(state: IBlockState?, target: RayTraceResult, world: World, pos: BlockPos, player: EntityPlayer?): ItemStack {
        val te = world.getTileEntity(pos)
        if (te is TileCracklingStar) {
            val stack = RainbowItemHelper.colorStack(te.color, this)
            return stack
        }
        return super.getPickBlock(state!!, target, world, pos, player!!)
    }

    override fun onBlockPlacedBy(world: World, pos: BlockPos, state: IBlockState, placer: EntityLivingBase, stack: ItemStack) {
        val te = world.getTileEntity(pos)
        if (te is TileCracklingStar) {
            te.color = RainbowItemHelper.getColor(stack)
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

    override fun harvestBlock(worldIn: World, player: EntityPlayer?, pos: BlockPos, state: IBlockState?, te: TileEntity?, stack: ItemStack) {
        super.harvestBlock(worldIn, player!!, pos, state!!, te, stack)
        worldIn.setBlockToAir(pos)
    }

    override fun getSubBlocks(tab: CreativeTabs, list: NonNullList<ItemStack>) {
        // NO-OP
    }

    companion object {
        val playerPositions = mutableMapOf<UUID, DimWithPos>()
    }

    override fun onUsedByWand(player: EntityPlayer?, stack: ItemStack, world: World, pos: BlockPos, side: EnumFacing): Boolean {
        if (player == null || world.isRemote) return false
        val dwp = playerPositions[player.uniqueID]

        val here = DimWithPos(world.provider.dimension, pos)

        if (dwp == null)
            playerPositions.put(player.uniqueID, here)
        else {
            playerPositions.remove(player.uniqueID)
            if (dwp == here) {
                val te = world.getTileEntity(pos) as? TileCracklingStar ?: return true
                te.blockPos = null
            } else if (dwp.dim == here.dim) {
                val otherTe = world.getTileEntity(dwp.pos) as? TileCracklingStar ?: return true
                otherTe.blockPos = pos
                otherTe.markDirty()
            }
        }
        return true
    }

    override fun getEntry(p0: World?, p1: BlockPos?, p2: EntityPlayer?, p3: ItemStack): LexiconEntry? {
        return LexiconEntries.crackle
    }
}
