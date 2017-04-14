package shadowfox.botanicaladdons.common.block

import com.teamwizardry.librarianlib.features.base.block.BlockMod
import net.minecraft.block.SoundType
import net.minecraft.block.material.MapColor
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.passive.EntitySheep
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.ItemStack
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import shadowfox.botanicaladdons.common.lexicon.LexiconEntries
import vazkii.botania.api.lexicon.ILexiconable

/**
 * @author WireSegal
 * Created at 11:49 AM on 6/4/16.
 */
class BlockAquamarineGlass(name: String) : BlockMod(name, Material.GLASS, MapColor.BLUE), ILexiconable {

    init {
        blockHardness = 0.3f
        soundType = SoundType.GLASS
    }

    override fun getBeaconColorMultiplier(state: IBlockState?, world: World?, pos: BlockPos?, beaconPos: BlockPos?): FloatArray {
        return floatArrayOf(0.3F, 0.45F, 0.775F)
    }

    override fun getLightValue(state: IBlockState?, world: IBlockAccess?, pos: BlockPos?) = 10
    override fun getEntry(p0: World?, p1: BlockPos?, p2: EntityPlayer?, p3: ItemStack) = LexiconEntries.njordSpells

    override fun isOpaqueCube(state: IBlockState) = false
    override fun canSilkHarvest(world: World?, pos: BlockPos?, state: IBlockState?, player: EntityPlayer?) = true
    override fun isFullCube(state: IBlockState) = false

    @SideOnly(Side.CLIENT)
    override fun getBlockLayer() = BlockRenderLayer.TRANSLUCENT

    @SideOnly(Side.CLIENT)
    override fun shouldSideBeRendered(blockState: IBlockState, blockAccess: IBlockAccess, pos: BlockPos, side: EnumFacing): Boolean {
        val iblockstate = blockAccess.getBlockState(pos.offset(side))
        val block = iblockstate.block

        return blockState !== iblockstate && if (block === this) false else super.shouldSideBeRendered(blockState, blockAccess, pos, side)
    }
}
