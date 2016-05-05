package shadowfox.botanicaladdons.api;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * @author WireSegal
 *         Created at 10:03 AM on 5/5/16.
 */
public interface IToolbeltBlacklisted {
    /**
     * @param stack The stack to check.
     * @return Whether the stack is allowed in the Botanist's Toolbelt.
     */
    boolean allowedInToolbelt(@Nonnull ItemStack stack);
}
