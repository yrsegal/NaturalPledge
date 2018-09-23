package com.wiresegal.naturalpledge.api.sapling;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * @author WireSegal
 *         Created at 3:45 PM on 5/28/16.
 */
public interface IStackConvertible {
    @Nonnull
    ItemStack itemStackFromState(@Nonnull IBlockState state);
}
