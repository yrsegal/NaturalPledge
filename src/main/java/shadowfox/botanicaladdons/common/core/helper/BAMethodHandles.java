package shadowfox.botanicaladdons.common.core.helper;

import com.google.common.base.Throwables;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.util.CooldownTracker;
import net.minecraft.world.Explosion;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import shadowfox.botanicaladdons.common.lib.LibObfuscation;
import vazkii.botania.common.entity.EntityDoppleganger;

import javax.annotation.Nonnull;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.lang.invoke.MethodHandles.publicLookup;

/**
 * @author WireSegal
 *         Created at 10:50 PM on 5/28/16.
 */
@SuppressWarnings("unchecked")
public class BAMethodHandles {

    @Nonnull
    public static final Class cooldownClass;
    @Nonnull
    private static final MethodHandle cooldownsGetter, cooldownTicksGetter, cooldownMaker, expireTicksGetter, createTicksGetter,
            swingTicksGetter, swingTicksSetter, lightningEffectGetter, explosionSizeGetter, alwaysEdibleGetter, playersWhoAttackedGetter;

    static {
        try {
            Field f = ReflectionHelper.findField(CooldownTracker.class, LibObfuscation.COOLDOWNTRACKER_COOLDOWNS);
            cooldownsGetter = publicLookup().unreflectGetter(f);

            f = ReflectionHelper.findField(CooldownTracker.class, LibObfuscation.COOLDOWNTRACKER_TICKS);
            cooldownTicksGetter = publicLookup().unreflectGetter(f);

            cooldownClass = Class.forName("net.minecraft.util.CooldownTracker$Cooldown");
            Constructor ctor = cooldownClass.getDeclaredConstructor(CooldownTracker.class, int.class, int.class);
            ctor.setAccessible(true);
            cooldownMaker = publicLookup().unreflectConstructor(ctor).asType(MethodType.methodType(Object.class, CooldownTracker.class, int.class, int.class));

            f = ReflectionHelper.findField(cooldownClass, LibObfuscation.COOLDOWN_EXPIRETICKS);
            expireTicksGetter = publicLookup().unreflectGetter(f).asType(MethodType.methodType(int.class, Object.class));

            f = ReflectionHelper.findField(cooldownClass, LibObfuscation.COOLDOWN_CREATETICKS);
            createTicksGetter = publicLookup().unreflectGetter(f).asType(MethodType.methodType(int.class, Object.class));

            f = ReflectionHelper.findField(EntityLivingBase.class, LibObfuscation.ENTITYLIVINGBASE_TICKSSINCELASTSWING);
            swingTicksGetter = publicLookup().unreflectGetter(f);
            swingTicksSetter = publicLookup().unreflectSetter(f);

            f = ReflectionHelper.findField(EntityLightningBolt.class, LibObfuscation.ENTITYLIGHTNINGBOLT_EFFECTONLY);
            lightningEffectGetter = publicLookup().unreflectGetter(f);

            f = ReflectionHelper.findField(Explosion.class, LibObfuscation.EXPLOSION_EXPLOSIONSIZE);
            explosionSizeGetter = publicLookup().unreflectGetter(f);

            f = ReflectionHelper.findField(ItemFood.class, LibObfuscation.ITEMFOOD_ALWAYSEDIBLE);
            alwaysEdibleGetter = publicLookup().unreflectGetter(f);

            f = ReflectionHelper.findField(EntityDoppleganger.class, "playersWhoAttacked");
            playersWhoAttackedGetter = publicLookup().unreflectGetter(f);

        } catch (Throwable t) {
            BALogger.INSTANCE.severe("Couldn't initialize methodhandles! Things will be broken!");
            t.printStackTrace();
            throw Throwables.propagate(t);
        }
    }

    @Nonnull
    public static Map getCooldowns(@Nonnull CooldownTracker cooldownTracker) {
        try {
            return (Map) cooldownsGetter.invokeExact(cooldownTracker);
        } catch (Throwable t) {
            throw propagate(t);
        }
    }

    public static void addNewCooldown(@Nonnull CooldownTracker cooldownTracker, @Nonnull Item item, int createTicks, int expireTicks) {
        Map cooldowns = getCooldowns(cooldownTracker);
        cooldowns.put(item, newCooldown(cooldownTracker, createTicks, expireTicks));
    }

    public static int getCooldownTicks(@Nonnull CooldownTracker cooldownTracker) {
        try {
            return (int) cooldownTicksGetter.invokeExact(cooldownTracker);
        } catch (Throwable t) {
            throw propagate(t);
        }
    }

    @Nonnull
    public static Object newCooldown(@Nonnull CooldownTracker tracker, int createTicks, int expireTicks) {
        try {
            return (Object) cooldownMaker.invokeExact(tracker, createTicks, expireTicks);
        } catch (Throwable t) {
            throw propagate(t);
        }
    }

    public static int getExpireTicks(@Nonnull Object cooldown) {
        try {
            return (int) expireTicksGetter.invokeExact(cooldown);
        } catch (Throwable t) {
            throw propagate(t);
        }
    }

    public static int getCreateTicks(@Nonnull Object cooldown) {
        try {
            return (int) createTicksGetter.invokeExact(cooldown);
        } catch (Throwable t) {
            throw propagate(t);
        }
    }

    public static int getSwingTicks(@Nonnull EntityLivingBase entity) {
        try {
            return (int) swingTicksGetter.invokeExact(entity);
        } catch (Throwable t) {
            throw propagate(t);
        }
    }

    public static void setSwingTicks(@Nonnull EntityLivingBase entity, int ticks) {
        try {
            swingTicksSetter.invokeExact(entity, ticks);
        } catch (Throwable t) {
            throw propagate(t);
        }
    }

    public static boolean getEffectOnly(@Nonnull EntityLightningBolt entity) {
        try {
            return (boolean) lightningEffectGetter.invokeExact(entity);
        } catch (Throwable t) {
            throw propagate(t);
        }
    }

    public static float getExplosionSize(@Nonnull Explosion entity) {
        try {
            return (float) explosionSizeGetter.invokeExact(entity);
        } catch (Throwable t) {
            throw propagate(t);
        }
    }

    public static boolean isAlwaysEdible(@Nonnull ItemFood food) {
        try {
            return (boolean) alwaysEdibleGetter.invokeExact(food);
        } catch (Throwable t) {
            throw propagate(t);
        }
    }

    public static List getPlayersWhoAttacked(@Nonnull EntityDoppleganger gaiaGuardian) {
        try {
            return (List) playersWhoAttackedGetter.invokeExact(gaiaGuardian);
        } catch (Throwable t) {
            throw propagate(t);
        }
    }

    private static RuntimeException propagate(Throwable t) {
        BALogger.INSTANCE.severe("Methodhandle failed!");
        t.printStackTrace();
        return Throwables.propagate(t);
    }
}
