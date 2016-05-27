package shadowfox.botanicaladdons.api;

import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import shadowfox.botanicaladdons.api.lib.LibMisc;
import shadowfox.botanicaladdons.api.priest.IFocusSpell;
import shadowfox.botanicaladdons.api.priest.SpellRecipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

/**
 * @author WireSegal
 *         Created at 11:32 AM on 4/24/16.
 */
public final class SpellRegistry {
    @Nonnull
    private final static HashBiMap<String, IFocusSpell> spellRegistry = HashBiMap.create();

    @Nonnull
    private final static ArrayList<SpellRecipe> spellRecipeRegistry = Lists.newArrayList();

    @Nonnull
    public static HashBiMap<String, IFocusSpell> getSpellRegistry() {
        return spellRegistry;
    }

    @Nonnull
    public static ArrayList<SpellRecipe> getSpellRecipeRegistry() {
        return spellRecipeRegistry;
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

    @Nonnull
    public static SpellRecipe registerSpellRecipe(@Nonnull SpellRecipe recipe) {
        spellRecipeRegistry.add(recipe);
        return recipe;
    }

    @Nonnull
    public static SpellRecipe registerSpellRecipe(@Nonnull String input, @Nonnull IFocusSpell spell, @Nonnull ItemStack... output) {
        return registerSpellRecipe(new SpellRecipe(input, spell, output));
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
