package shadowfox.botanicaladdons.common.lib

/**
 * @author WireSegal
 * Created at 3:24 PM on 3/26/16.
 */
object LibMisc {
    const val MOD_NAME = "Natural Pledge"
    const val MOD_ID = "botanicaladdons"

    const val BUILD = "GRADLE:BUILD"
    const val VERSIONID = "GRADLE:VERSION"
    const val VERSION = "$VERSIONID.$BUILD"
    const val DEPENDENCIES = "required-after:Forge@[12.16.0.1865,);required-after:Botania;"

    const val PROXY_COMMON = "shadowfox.botanicaladdons.common.core.CommonProxy"
    const val PROXY_CLIENT = "shadowfox.botanicaladdons.client.core.ClientProxy"
}
