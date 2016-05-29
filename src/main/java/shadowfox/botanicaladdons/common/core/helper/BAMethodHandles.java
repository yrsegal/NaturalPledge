package shadowfox.botanicaladdons.common.core.helper;

import com.google.common.base.Throwables;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.CooldownTracker;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import shadowfox.botanicaladdons.common.lib.LibObfuscation;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;

import static java.lang.invoke.MethodHandles.publicLookup;

/**
 @author WireSegal
         Created at 10:50 PM on 5/28/16.
 */
@SuppressWarnings("unchecked")
public class BAMethodHandles {

    private static final MethodHandle cooldownsGetter;
    public static Map getCooldowns(CooldownTracker cooldownTracker) {
        try {
            return (Map) cooldownsGetter.invokeExact(cooldownTracker);
        } catch (Throwable t) {
            FMLLog.severe("[BA]: Methodhandle failed!");
            t.printStackTrace();
            throw Throwables.propagate(t);
        }
    }

    public static void addNewCooldown(CooldownTracker cooldownTracker, Item item, int createTicks, int expireTicks) {
        Map cooldowns = getCooldowns(cooldownTracker);
        cooldowns.put(item, newCooldown(cooldownTracker, createTicks, expireTicks));
    }

    private static final MethodHandle cooldownTicksGetter;
    public static int getCooldownTicks(CooldownTracker cooldownTracker) {
        try {
            return (int) cooldownTicksGetter.invokeExact(cooldownTracker);
        } catch (Throwable t) {
            FMLLog.severe("[BA]: Methodhandle failed!");
            t.printStackTrace();
            throw Throwables.propagate(t);
        }
    }

    private static final MethodHandle cooldownMaker;
    public static Object newCooldown(CooldownTracker tracker, int createTicks, int expireTicks) {
        try {
            return (Object) cooldownMaker.invokeExact(tracker, createTicks, expireTicks);
        } catch (Throwable t) {
            FMLLog.severe("[BA]: Methodhandle failed!");
            t.printStackTrace();
            throw Throwables.propagate(t);
        }
    }

    public static final Class cooldownClass;

    private static final MethodHandle expireTicksGetter;
    public static int getExpireTicks(Object cooldown) {
        try {
            return (int) expireTicksGetter.invokeExact(cooldown);
        } catch (Throwable t) {
            FMLLog.severe("[BA]: Methodhandle failed!");
            t.printStackTrace();
            throw Throwables.propagate(t);
        }
    }
    private static final MethodHandle createTicksGetter;
    public static int getCreateTicks(Object cooldown) {
        try {
            return (int) createTicksGetter.invokeExact(cooldown);
        } catch (Throwable t) {
            FMLLog.severe("[BA]: Methodhandle failed!");
            t.printStackTrace();
            throw Throwables.propagate(t);
        }
    }


    private static final MethodHandle swingTicksGetter;
    public static int getSwingTicks(EntityLivingBase entity) {
        try {
            return (int) swingTicksGetter.invokeExact(entity);
        } catch (Throwable t) {
            FMLLog.severe("[BA]: Methodhandle failed!");
            t.printStackTrace();
            throw Throwables.propagate(t);
        }
    }

    private static final MethodHandle swingTicksSetter;
    public static void setSwingTicks(EntityLivingBase entity, int ticks) {
        try {
            swingTicksSetter.invokeExact(entity, ticks);
        } catch (Throwable t) {
            FMLLog.severe("[BA]: Methodhandle failed!");
            t.printStackTrace();
            throw Throwables.propagate(t);
        }
    }

    static {
        try {
            Field f = ReflectionHelper.findField(CooldownTracker.class, LibObfuscation.COOLDOWNTRACKER_COOLDOWNS);
            f.setAccessible(true);
            cooldownsGetter = publicLookup().unreflectGetter(f);

            f = ReflectionHelper.findField(CooldownTracker.class, LibObfuscation.COOLDOWNTRACKER_TICKS);
            f.setAccessible(true);
            cooldownTicksGetter = publicLookup().unreflectGetter(f);

            cooldownClass = Class.forName("net.minecraft.util.CooldownTracker$Cooldown");
            Constructor ctor = cooldownClass.getDeclaredConstructor(CooldownTracker.class, int.class, int.class);
            ctor.setAccessible(true);
            cooldownMaker = publicLookup().unreflectConstructor(ctor).asType(MethodType.methodType(Object.class, CooldownTracker.class, int.class, int.class));

            f = ReflectionHelper.findField(cooldownClass, LibObfuscation.COOLDOWN_EXPIRETICKS);
            f.setAccessible(true);
            expireTicksGetter = publicLookup().unreflectGetter(f).asType(MethodType.methodType(int.class, Object.class));

            f = ReflectionHelper.findField(cooldownClass, LibObfuscation.COOLDOWN_CREATETICKS);
            f.setAccessible(true);
            createTicksGetter = publicLookup().unreflectGetter(f).asType(MethodType.methodType(int.class, Object.class));

            f = ReflectionHelper.findField(EntityLivingBase.class, LibObfuscation.ENTITYLIVINGBASE_TICKSSINCELASTSWING);
            f.setAccessible(true);
            swingTicksGetter = publicLookup().unreflectGetter(f);
            swingTicksSetter = publicLookup().unreflectSetter(f);

        } catch (Throwable t) {
            FMLLog.severe("[BA]: Couldn't initialize methodhandles! Things will be broken!");
            t.printStackTrace();
            throw Throwables.propagate(t);
        }
    }
}
