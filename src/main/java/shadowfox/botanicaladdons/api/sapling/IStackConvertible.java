package shadowfox.botanicaladdons.api.sapling;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author WireSegal
 *         Created at 3:45 PM on 5/28/16.
 */
public interface IStackConvertible {
    @Nullable
    ItemStack itemStackFromState(@Nonnull IBlockState state);
}
