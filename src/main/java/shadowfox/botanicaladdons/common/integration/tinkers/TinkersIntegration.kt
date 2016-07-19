package shadowfox.botanicaladdons.common.integration.tinkers

/**
 * @author WireSegal
 * Created at 8:50 AM on 6/25/16.
 */
object TinkersIntegration {
    val THUNDERSTEEL_COLOR = 0xAE9C90
    val THUNDERSTEEL_FLUID_COLOR = 0xFFE2D1
    val AQUAMARINE_COLORS = arrayOf(0x51F3FF, 0x009EFE, 0x017FFF)
    val SOULROOT_COLORS = arrayOf(0xE2BD00, 0x33160B, 0xE2BD00)

    fun load() {
        TinkersFluids
        TinkersMaterials
    }
}
