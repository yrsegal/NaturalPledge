package shadowfox.botanicaladdons.common.block.dendrics.thunder

import net.minecraft.block.state.IBlockState
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.client.event.sound.PlaySoundEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import shadowfox.botanicaladdons.api.sapling.ISealingBlock
import shadowfox.botanicaladdons.common.block.ModBlocks
import shadowfox.botanicaladdons.common.block.base.BlockModLeaves
import java.util.*

/**
 * @author WireSegal
 * Created at 10:36 PM on 5/27/16.
 */
class BlockThunderLeaves(name: String) : BlockModLeaves(name), IThunderAbsorber {
    override val canBeFancy: Boolean
        get() = false

    override fun getItemDropped(state: IBlockState?, rand: Random?, fortune: Int): Item? {
        return Item.getItemFromBlock(ModBlocks.sealSapling)
    }
}
