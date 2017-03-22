package shadowfox.botanicaladdons.common.integration.tinkers

import com.teamwizardry.librarianlib.common.util.ConfigPropertyBoolean
import shadowfox.botanicaladdons.common.BotanicalAddons

/**
 * @author WireSegal
 * Created at 8:49 AM on 6/25/16.
 */
object TinkersProxy {

    @ConfigPropertyBoolean(category = "tinkers",
            id = "LoadBotaniaTinkersIntegration",
            comment = "Whether to load Botania tinkers integration or not while tinkers construct is present.",
            defaultValue = true)
    var loadBotaniaTinkers = true

    @ConfigPropertyBoolean(category = "tinkers",
            id = "LoadNaturalPledgeTinkersIntegration",
            comment = "Whether to load Natural Pledge tinkers integration or not while tinkers construct is present.",
            defaultValue = true)
    var loadNpTinkers = true

    // Attempting to get around problems with loading when tinkers isn't present.
    fun loadTinkers() = if (loadBotaniaTinkers || loadNpTinkers) load() else {}

    private val load = if (BotanicalAddons.TINKERS_LOADED) { -> TinkersIntegration.load() } else { -> }
}
