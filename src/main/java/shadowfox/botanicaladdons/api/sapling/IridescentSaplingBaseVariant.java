package shadowfox.botanicaladdons.api.sapling;

import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class IridescentSaplingBaseVariant implements IIridescentSaplingVariant {
    @Nonnull
    public IBlockState soil, wood, leaves;

    public IridescentSaplingBaseVariant(@Nonnull IBlockState soil, @Nonnull IBlockState wood, @Nonnull IBlockState leaves) {
        this.soil = soil;
        this.wood = wood;
        this.leaves = leaves;
    }

    @Override
    public boolean matchesSoil(@Nonnull IBlockState soil) {
        return soil == this.soil;
    }

    @Override
    @Nonnull
    public IBlockState getLeaves(@Nonnull IBlockState soil) {
        return leaves;
    }

    @Override
    @Nonnull
    public IBlockState getWood(@Nonnull IBlockState soil) {
        return wood;
    }

    @Override
    public List<IBlockState> getDisplaySoilBlockstates() {
        return Lists.newArrayList(soil);
    }
}
