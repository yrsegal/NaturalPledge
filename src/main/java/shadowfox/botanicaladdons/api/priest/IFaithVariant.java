package shadowfox.botanicaladdons.api.priest;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.item.IBaubleRender;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author WireSegal
 *         Created at 12:00 PM on 4/24/16.
 */
public interface IFaithVariant {
    @Nonnull
    String getName();

    @Nonnull
    List<String> getSpells(@Nonnull ItemStack stack, @Nonnull EntityPlayer player);

    void punishTheFaithless(@Nonnull ItemStack stack, @Nonnull EntityPlayer player);

    default boolean hasSubscriptions() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Nullable
    default IItemColor getColor() {
        return null;
    }

    default void onUpdate(@Nonnull ItemStack stack, @Nonnull EntityPlayer player) {
        // NO-OP
    }

    default void onAwakenedUpdate(@Nonnull ItemStack stack, @Nonnull EntityPlayer player) {
        onUpdate(stack, player);
    }

    default void onRenderTick(@Nonnull ItemStack stack, @Nonnull EntityPlayer player, @Nonnull IBaubleRender.RenderType render, float renderTick) {
        // NO-OP
    }

    default void addToTooltip(@Nonnull ItemStack stack, @Nullable EntityPlayer player, @Nonnull List<String> tooltip, boolean advanced) {
        // NO-OP
    }

}
