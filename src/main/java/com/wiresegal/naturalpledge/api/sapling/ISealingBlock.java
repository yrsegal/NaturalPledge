package com.wiresegal.naturalpledge.api.sapling;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * @author WireSegal
 *         Created at 4:32 PM on 5/27/16.
 */
public interface ISealingBlock {
    @SideOnly(Side.CLIENT)
    boolean canSeal(@Nonnull IBlockState iBlockState, @Nonnull World world, @Nonnull BlockPos blockPos, double dist, @Nonnull PlaySoundEvent event);

    @SideOnly(Side.CLIENT)
    float getVolumeMultiplier(@Nonnull IBlockState iBlockState, @Nonnull World world, @Nonnull BlockPos blockPos, double dist, @Nonnull PlaySoundEvent event);
}
