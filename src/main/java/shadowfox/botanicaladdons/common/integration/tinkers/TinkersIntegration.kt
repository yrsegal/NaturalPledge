package shadowfox.botanicaladdons.common.integration.tinkers

/**
 * @author WireSegal
 * Created at 8:50 AM on 6/25/16.
 */
object TinkersIntegration {
    val THUNDERSTEEL_COLOR = 0xAE9C90
    val THUNDERSTEEL_FLUID_COLOR = 0xFFE2D1
    val MANASTEEL_COLOR = 0x3E93E8
    val TERRASTEEL_COLOR = 0x109307
    val ELEMENTIUM_COLOR = 0xE83ED7
    val LIVINGWOOD_COLOR = 0x4C290A
    val DREAMWOOD_COLOR = 0xC8F4EE
    val LIVINGROCK_COLOR = 0xFFEBD1
    val AQUAMARINE_COLORS = arrayOf(0x51F3FF, 0x009EFE, 0x017FFF)
    val SOULROOT_COLORS = arrayOf(0xE2BD00, 0x33160B, 0xE2BD00)

    fun load() {
        TinkersFluids
        TinkersMaterials
    }
}
