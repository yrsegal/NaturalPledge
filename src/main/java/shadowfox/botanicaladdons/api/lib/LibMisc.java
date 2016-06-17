package shadowfox.botanicaladdons.api.lib;

/**
 * @author WireSegal
 *         Created at 3:12 PM on 5/14/16.
 */
public final class LibMisc {
    public static final String MOD_NAME = "Natural Pledge";
    public static final String MOD_ID = "botanicaladdons";

    public static final String BUILD = "GRADLE:BUILD";
    public static final String VERSIONID = "GRADLE:VERSION";
    public static final String VERSION = VERSIONID + "." + BUILD;
    public static final String DEPENDENCIES = "required-after:Forge@[12.17.0.1965,);required-after:Botania;";

    public static final String PROXY_COMMON = "shadowfox.botanicaladdons.common.core.CommonProxy";
    public static final String PROXY_CLIENT = "shadowfox.botanicaladdons.client.core.ClientProxy";
}
