package com.wiresegal.naturalpledge.api.lib;

/**
 * @author WireSegal
 *         Created at 3:12 PM on 5/14/16.
 */
public final class LibMisc {
    public static final String MOD_NAME = "Natural Pledge";
    public static final String MOD_ID = "naturalpledge";

    public static final String BUILD = "0.3";
    public static final String VERSIONID = "3";
    public static final String VERSION = VERSIONID + "." + BUILD;
    public static final String DEPENDENCIES = "required-after:forge@[14.23.4.2705,);required-after:botania;required-after:librarianlib;";

    public static final String PROXY_COMMON = "com.wiresegal.naturalpledge.common.core.CommonProxy";
    public static final String PROXY_CLIENT = "com.wiresegal.naturalpledge.client.core.ClientProxy";
}
