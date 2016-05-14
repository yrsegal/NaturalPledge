package shadowfox.botanicaladdons.api.priest;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;

import javax.annotation.Nonnull;

/**
 * @author WireSegal
 *         Created at 11:33 AM on 4/24/16.
 */
public interface IFocusSpell {
    @Nonnull
    ItemStack getIconStack();

    default int getCooldown(@Nonnull EntityPlayer player, @Nonnull ItemStack focus, @Nonnull EnumHand hand) {
        return 0;
    }

    EnumActionResult onCast(@Nonnull EntityPlayer player, @Nonnull ItemStack focus, @Nonnull EnumHand hand);

    default void onCooldownTick(@Nonnull EntityPlayer player, @Nonnull ItemStack focus, int slot, boolean selected, int cooldownRemaining) {
    }

}
