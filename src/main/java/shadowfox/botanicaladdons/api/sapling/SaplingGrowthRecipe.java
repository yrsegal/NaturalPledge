package shadowfox.botanicaladdons.api.sapling;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * @author WireSegal
 *         Created at 3:27 PM on 5/28/16.
 */
public class SaplingGrowthRecipe {
    @Nonnull
    private final ItemStack sapling, soil, wood, leaves;

    public SaplingGrowthRecipe(@Nonnull ItemStack sapling, @Nonnull ItemStack soil, @Nonnull ItemStack wood, @Nonnull ItemStack leaves) {
        this.sapling = sapling;
        this.soil = soil;
        this.wood = wood;
        this.leaves = leaves;
    }

    @Nonnull
    public ItemStack getLeaves() {
        return leaves;
    }

    @Nonnull
    public ItemStack getSapling() {
        return sapling;
    }

    @Nonnull
    public ItemStack getSoil() {
        return soil;
    }

    @Nonnull
    public ItemStack getWood() {
        return wood;
    }
}
