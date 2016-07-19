package shadowfox.botanicaladdons.common.integration.tinkers

import shadowfox.botanicaladdons.common.BotanicalAddons

/**
 * @author WireSegal
 * Created at 8:49 AM on 6/25/16.
 */
object TinkersProxy {

    // Attempting to get around problems with loading when tinkers isn't present.
    fun loadTinkers() = load()

    private val load = if (BotanicalAddons.TINKERS_LOADED) { -> TinkersIntegration.load() } else { -> }
}
