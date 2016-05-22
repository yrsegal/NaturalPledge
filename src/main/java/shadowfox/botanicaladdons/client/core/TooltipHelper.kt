package shadowfox.botanicaladdons.client.core

import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.resources.I18n

/**
 * @author WireSegal
 * Created at 4:54 PM on 4/12/16.
 */
object TooltipHelper {

    fun tooltipIfShift(tooltip: MutableList<String>, r: () -> Unit) {
        if (GuiScreen.isShiftKeyDown()) {
            r.invoke()
        } else {
            addToTooltip(tooltip, "botaniamisc.shiftinfo")
        }

    }

    fun addToTooltip(tooltip: MutableList<String>, s: String, vararg format: Any?) {
        val toAdd = local(s, *format).replace("&".toRegex(), "§")

        tooltip.add(toAdd)
    }

    fun local(s: String, vararg format: Any?): String {
        return I18n.format(s, *format)
    }
}
