package shadowfox.botanicaladdons.api;

import com.google.common.collect.HashBiMap;
import net.minecraftforge.fml.common.Loader;
import shadowfox.botanicaladdons.api.lib.LibMisc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author WireSegal
 *         Created at 11:32 AM on 4/24/16.
 */
public final class SpellRegistry {
    private final static
    @Nonnull
    HashBiMap<String, IFocusSpell> spellRegistry = HashBiMap.create();

    public static
    @Nonnull
    HashBiMap<String, IFocusSpell> getSpellRegistry() {
        return spellRegistry;
    }

    public static
    @Nullable
    IFocusSpell registerSpell(@Nonnull String name, @Nonnull IFocusSpell spell) {
        return registerSpell(name, spell, false);
    }

    public static
    @Nullable
    IFocusSpell registerSpell(@Nonnull String name, @Nonnull IFocusSpell spell, boolean force) {
        String modId = Loader.instance().activeModContainer().getModId();
        String transformedName = name;
        if (!modId.equals(LibMisc.MOD_ID))
            transformedName = modId + ":" + name;

        if (spellRegistry.containsKey(transformedName) && !force)
            return null;
        spellRegistry.put(transformedName, spell);
        return spell;
    }

    public static
    @Nullable
    IFocusSpell getSpell(@Nonnull String name) {
        return spellRegistry.get(name);
    }

    public static
    @Nullable
    String getSpellName(@Nonnull IFocusSpell spell) {
        return spellRegistry.inverse().get(spell);
    }
}
