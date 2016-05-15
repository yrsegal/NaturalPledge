package shadowfox.botanicaladdons.api.sapling;

import net.minecraft.block.state.IBlockState;

import javax.annotation.Nonnull;

public interface IIridescentSaplingVariant {

    boolean matchesSoil(@Nonnull IBlockState soil);

    @Nonnull
    IBlockState getLeaves(@Nonnull IBlockState soil);

    @Nonnull
    IBlockState getWood(@Nonnull IBlockState soil);
}
