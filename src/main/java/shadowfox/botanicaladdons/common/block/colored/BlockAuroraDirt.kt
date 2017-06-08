package shadowfox.botanicaladdons.common.block.colored

import com.teamwizardry.librarianlib.core.LibrarianLib
import com.teamwizardry.librarianlib.features.base.block.BlockMod
import com.teamwizardry.librarianlib.features.base.block.IBlockColorProvider
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper.addToTooltip
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.ColorizerFoliage
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.common.IPlantable
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.common.lexicon.LexiconEntries
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.lexicon.LexiconEntry
import net.minecraft.world.biome.BiomeColorHelper
import kotlin.coroutines.experimental.EmptyCoroutineContext.plus


/**
 * @author WireSegal
 * Created at 10:38 PM on 5/6/16.
 */
class BlockAuroraDirt(name: String) : BlockMod(name, Material.GROUND), ILexiconable, IBlockColorProvider {

    companion object {
        fun fromPos(pos: BlockPos?): Int {
            if (pos == null) return 0x97C683
            val x = pos.x
            val y = pos.y
            val z = pos.z
            var red = x * 32 + y * 16
            if (red and 256 != 0) {
                red = 255 - (red and 255)
            }
            red = red and 255

            var blue = y * 32 + z * 16
            if (blue and 256 != 0) {
                blue = 255 - (blue and 255)
            }
            blue = blue xor 255

            var green = x * 16 + z * 32
            if (green and 256 != 0) {
                green = 255 - (green and 255)
            }
            green = green and 255

            return red shl 16 or (blue shl 8) or green
        }
    }

    init {
        soundType = SoundType.GROUND
        blockHardness = 0.5f
    }

    override fun addInformation(stack: ItemStack, player: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
        addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.color.aurora")
    }

    override fun getEntry(p0: World?, p1: BlockPos?, p2: EntityPlayer?, p3: ItemStack): LexiconEntry? {
        return LexiconEntries.aurora
    }

    override fun isToolEffective(type: String?, state: IBlockState?): Boolean {
        return type == "shovel"
    }

    override fun getHarvestTool(state: IBlockState?): String? {
        return "shovel"
    }

    override val blockColorFunction: ((state: IBlockState, world: IBlockAccess?, pos: BlockPos?, tintIndex: Int) -> Int)?
        get() = { _, _, pos, _ -> fromPos(pos) }

    override val itemColorFunction: ((ItemStack, Int) -> Int)?
        get() = { _, _ -> fromPos(LibrarianLib.PROXY.getClientPlayer().position) }

    override fun canSustainPlant(state: IBlockState?, world: IBlockAccess?, pos: BlockPos?, direction: EnumFacing?, plantable: IPlantable?): Boolean {
        return true
    }
}
