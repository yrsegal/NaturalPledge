package com.wiresegal.naturalpledge.client.core;

import net.minecraft.client.gui.GuiIngame;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.wiresegal.naturalpledge.common.core.helper.NPLogger;
import com.wiresegal.naturalpledge.common.lib.LibObfuscation;

import javax.annotation.Nonnull;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;

/**
 * @author WireSegal
 *         Created at 11:20 PM on 5/28/16.
 */
@SideOnly(Side.CLIENT)
public class BAClientMethodHandles {
    @Nonnull
    private static final MethodHandle remainingHighlightSetter;

    static {
        try {

            Field f = ReflectionHelper.findField(GuiIngame.class, LibObfuscation.GUIINGAME_REMAININGHIGHLIGHTTICKS);
            f.setAccessible(true);
            remainingHighlightSetter = MethodHandles.publicLookup().unreflectSetter(f);

        } catch (Throwable t) {
            NPLogger.INSTANCE.severe("Couldn't initialize client methodhandles! Things will be broken!");
            t.printStackTrace();
            throw new RuntimeException(t);
        }
    }

    public static void setRemainingHighlight(@Nonnull GuiIngame gui, int ticks) {
        try {
            remainingHighlightSetter.invokeExact(gui, ticks);
        } catch (Throwable t) {
            throw propagate(t);
        }
    }

    private static RuntimeException propagate(Throwable t) {
        NPLogger.INSTANCE.severe("Client methodhandle failed!");
        t.printStackTrace();
        throw new RuntimeException(t);
    }
}
