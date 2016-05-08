package shadowfox.botanicaladdons.common.block.colored

import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.common.IPlantable
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.common.block.base.BlockMod
import shadowfox.botanicaladdons.common.items.base.ItemMod
import shadowfox.botanicaladdons.common.lexicon.LexiconEntries
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.lexicon.LexiconEntry

/**
 * @author WireSegal
 * Created at 10:38 PM on 5/6/16.
 */
class BlockRainbowDirt(name: String) : BlockMod(name, Material.ground), ILexiconable {

    init {
        soundType = SoundType.GROUND
        blockHardness = 0.5f
    }

    override fun addInformation(stack: ItemStack, player: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
        ItemMod.addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.color.16")
    }

    override fun getEntry(p0: World?, p1: BlockPos?, p2: EntityPlayer?, p3: ItemStack?): LexiconEntry? {
        return LexiconEntries.irisDirt
    }

    override fun isToolEffective(type: String?, state: IBlockState?): Boolean {
        return type == "shovel"
    }

    override fun getHarvestTool(state: IBlockState?): String? {
        return "shovel"
    }

    override fun canSustainPlant(state: IBlockState?, world: IBlockAccess?, pos: BlockPos?, direction: EnumFacing?, plantable: IPlantable?): Boolean {
        return true
    }
}
