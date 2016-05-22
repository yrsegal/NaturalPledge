package shadowfox.botanicaladdons.api.sapling;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaCollisionGhost;

/**
 * @author WireSegal
 *         Created at 4:10 PM on 5/14/16.
 */
public interface ISaplingBlock extends IManaCollisionGhost {
    @Override
    default boolean isGhost(IBlockState iBlockState, World world, BlockPos blockPos) {
        return true;
    }
}
