package shadowfox.botanicaladdons.api;

import com.google.common.collect.HashBiMap;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.Loader;
import shadowfox.botanicaladdons.api.lib.LibMisc;
import shadowfox.botanicaladdons.api.sapling.IIridescentSaplingVariant;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author WireSegal
 *         Created at 3:11 PM on 5/14/16.
 */
public final class SaplingVariantRegistry {
    @Nonnull
    private static final HashBiMap<String, IIridescentSaplingVariant> variantRegistry = HashBiMap.create();

    @Nonnull
    public static HashBiMap<String, IIridescentSaplingVariant> getVariantRegistry() {
        return variantRegistry;
    }

    @Nullable
    public static IIridescentSaplingVariant registerVariant(@Nonnull String name, @Nonnull IIridescentSaplingVariant variant) {
        return registerVariant(name, variant, false);
    }

    @Nullable
    public static IIridescentSaplingVariant registerVariant(@Nonnull String name, @Nonnull IIridescentSaplingVariant variant, boolean force) {
        String modId = Loader.instance().activeModContainer().getModId();
        String transformedName = name;
        if (!modId.equals(LibMisc.MOD_ID))
            transformedName = modId + ":" + name;

        if (variantRegistry.containsKey(transformedName) && !force)
            return null;
        variantRegistry.put(transformedName, variant);
        return variant;
    }

    @Nullable
    public static IIridescentSaplingVariant getVariant(@Nonnull String name) {
        return variantRegistry.get(name);
    }

    @Nullable
    public static String getVariantName(@Nonnull IIridescentSaplingVariant variant) {
        return variantRegistry.inverse().get(variant);
    }

    @Nullable
    public static IIridescentSaplingVariant getVariant(@Nonnull IBlockState soil) {
        for (IIridescentSaplingVariant variant : variantRegistry.values())
            if (variant.matchesSoil(soil))
                return variant;
        return null;
    }
}
