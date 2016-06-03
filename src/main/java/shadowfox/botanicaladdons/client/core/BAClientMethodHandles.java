package shadowfox.botanicaladdons.client.core;

import com.google.common.base.Throwables;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shadowfox.botanicaladdons.common.lib.LibObfuscation;
import vazkii.botania.client.core.handler.ClientMethodHandles;

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
            FMLLog.severe("[BA]: Couldn't initialize client methodhandles! Things will be broken!");
            t.printStackTrace();
            throw Throwables.propagate(t);
        }
    }

    public static void setRemainingHighlight(@Nonnull GuiIngame gui, int ticks) {
        try {
            remainingHighlightSetter.invokeExact(gui, ticks);
        } catch (Throwable t) {
            FMLLog.severe("[BA]: Methodhandle failed!");
            t.printStackTrace();
            throw Throwables.propagate(t);
        }
    }

    public static double getRenderPosX(@Nonnull RenderManager renderManager) {
        try {
            return (double) ClientMethodHandles.renderPosX_getter.invokeExact(renderManager);
        } catch (Throwable t) {
            FMLLog.severe("[BA]: Methodhandle failed!");
            t.printStackTrace();
            throw Throwables.propagate(t);
        }
    }

    public static double getRenderPosY(@Nonnull RenderManager renderManager) {
        try {
            return (double) ClientMethodHandles.renderPosY_getter.invokeExact(renderManager);
        } catch (Throwable t) {
            FMLLog.severe("[BA]: Methodhandle failed!");
            t.printStackTrace();
            throw Throwables.propagate(t);
        }
    }

    public static double getRenderPosZ(@Nonnull RenderManager renderManager) {
        try {
            return (double) ClientMethodHandles.renderPosZ_getter.invokeExact(renderManager);
        } catch (Throwable t) {
            FMLLog.severe("[BA]: Methodhandle failed!");
            t.printStackTrace();
            throw Throwables.propagate(t);
        }
    }
}
