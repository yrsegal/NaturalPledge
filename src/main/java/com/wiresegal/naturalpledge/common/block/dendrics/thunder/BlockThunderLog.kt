package com.wiresegal.naturalpledge.common.block.dendrics.thunder

import com.teamwizardry.librarianlib.features.base.block.BlockModLog
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import com.wiresegal.naturalpledge.common.lexicon.LexiconEntries
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.lexicon.LexiconEntry

/**
 * @author WireSegal
 * Created at 4:31 PM on 5/27/16.
 */
class BlockThunderLog(name: String) : BlockModLog(name), IThunderAbsorber, ILexiconable {

    override fun getEntry(p0: World?, p1: BlockPos?, p2: EntityPlayer?, p3: ItemStack): LexiconEntry? {
        return LexiconEntries.thunderTree
    }
}
