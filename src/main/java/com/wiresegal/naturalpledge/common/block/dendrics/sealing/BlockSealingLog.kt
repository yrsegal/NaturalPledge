package com.wiresegal.naturalpledge.common.block.dendrics.sealing

import com.teamwizardry.librarianlib.features.base.block.BlockModLog
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.client.event.sound.PlaySoundEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import com.wiresegal.naturalpledge.api.sapling.ISealingBlock
import com.wiresegal.naturalpledge.common.lexicon.LexiconEntries
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.lexicon.LexiconEntry

/**
 * @author WireSegal
 * Created at 4:31 PM on 5/27/16.
 */
class BlockSealingLog(name: String) : BlockModLog(name), ISealingBlock, ILexiconable {
    @SideOnly(Side.CLIENT)
    override fun canSeal(iBlockState: IBlockState, world: World, blockPos: BlockPos, dist: Double, event: PlaySoundEvent) = dist <= 8

    @SideOnly(Side.CLIENT)
    override fun getVolumeMultiplier(iBlockState: IBlockState, world: World, blockPos: BlockPos, dist: Double, event: PlaySoundEvent) = 0.5f

    override fun getEntry(p0: World?, p1: BlockPos?, p2: EntityPlayer?, p3: ItemStack): LexiconEntry? {
        return LexiconEntries.sealTree
    }
}
