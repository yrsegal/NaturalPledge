package com.wiresegal.naturalpledge.api.sapling;

import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nonnull;
import java.util.List;

public class IridescentSaplingBaseVariant implements IIridescentSaplingVariant {
    @Nonnull
    public final IBlockState soil, wood, leaves;

    @Nonnull
    private final String modid = Loader.instance().activeModContainer().getModId();

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

    @Override
    public String toString() {
        return modid + ":{ soil=" + soil.toString() + " wood=" + wood.toString() + " leaves=" + leaves.toString() + " }";
    }
}
