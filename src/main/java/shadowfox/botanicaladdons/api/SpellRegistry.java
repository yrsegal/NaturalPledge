package shadowfox.botanicaladdons.api;

import com.google.common.collect.HashBiMap;
import net.minecraftforge.fml.common.Loader;
import shadowfox.botanicaladdons.api.lib.LibMisc;
import shadowfox.botanicaladdons.api.priest.IFocusSpell;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author WireSegal
 *         Created at 11:32 AM on 4/24/16.
 */
public final class SpellRegistry {
    @Nonnull
    private final static HashBiMap<String, IFocusSpell> spellRegistry = HashBiMap.create();

    @Nonnull
    public static HashBiMap<String, IFocusSpell> getSpellRegistry() {
        return spellRegistry;
    }

    @Nullable
    public static IFocusSpell registerSpell(@Nonnull String name, @Nonnull IFocusSpell spell) {
        return registerSpell(name, spell, false);
    }

    @Nullable
    public static IFocusSpell registerSpell(@Nonnull String name, @Nonnull IFocusSpell spell, boolean force) {
        String modId = Loader.instance().activeModContainer().getModId();
        String transformedName = name;
        if (!modId.equals(LibMisc.MOD_ID))
            transformedName = modId + ":" + name;

        if (spellRegistry.containsKey(transformedName) && !force)
            return null;
        spellRegistry.put(transformedName, spell);
        return spell;
    }

    @Nullable
    public static IFocusSpell getSpell(@Nonnull String name) {
        return spellRegistry.get(name);
    }

    @Nullable
    public static String getSpellName(@Nonnull IFocusSpell spell) {
        return spellRegistry.inverse().get(spell);
    }
}
