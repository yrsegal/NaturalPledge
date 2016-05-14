package shadowfox.botanicaladdons.api.item;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * @author WireSegal
 *         Created at 12:35 PM on 4/25/16.
 */
public interface IDiscordantItem {
    boolean isDiscordant(@Nonnull ItemStack stack);
}
