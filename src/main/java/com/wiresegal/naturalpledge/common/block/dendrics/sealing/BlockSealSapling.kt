package com.wiresegal.naturalpledge.common.block.dendrics.sealing

import com.teamwizardry.librarianlib.features.base.block.BlockModSapling
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.client.event.sound.PlaySoundEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import com.wiresegal.naturalpledge.api.sapling.ISealingBlock
import com.wiresegal.naturalpledge.common.block.ModBlocks
import com.wiresegal.naturalpledge.common.lexicon.LexiconEntries
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.lexicon.LexiconEntry
import java.util.*

/**
 * @author WireSegal
 * Created at 10:35 PM on 5/27/16.
 */
class BlockSealSapling(name: String) : BlockModSapling(name), ISealingBlock, ILexiconable {
    @SideOnly(Side.CLIENT)
    override fun getVolumeMultiplier(iBlockState: IBlockState, world: World, blockPos: BlockPos, dist: Double, event: PlaySoundEvent): Float {
        return 0.5f
    }

    @SideOnly(Side.CLIENT)
    override fun canSeal(iBlockState: IBlockState, world: World, blockPos: BlockPos, dist: Double, event: PlaySoundEvent): Boolean {
        return dist <= 8f
    }

    override fun generateTree(worldIn: World, pos: BlockPos, state: IBlockState, rand: Random) {
        defaultSaplingBehavior(worldIn, pos, state, rand, ModBlocks.sealLog, ModBlocks.sealLeaves)
    }

    override fun getEntry(p0: World?, p1: BlockPos?, p2: EntityPlayer?, p3: ItemStack): LexiconEntry? {
        return LexiconEntries.sealTree
    }
}
