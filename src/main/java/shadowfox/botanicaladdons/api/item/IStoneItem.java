package shadowfox.botanicaladdons.api.item;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * @author WireSegal
 *         Created at 10:03 PM on 1/20/17.
 */
public interface IStoneItem {
    /**
     * @param stack The stack to check.
     * @return Whether the stack is allowed in the Stone of Collection.
     */
    boolean allowedInHolderStone(@Nonnull ItemStack stack);
}
