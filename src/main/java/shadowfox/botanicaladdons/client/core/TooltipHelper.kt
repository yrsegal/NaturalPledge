package shadowfox.botanicaladdons.client.core

import net.minecraft.client.gui.GuiScreen
import net.minecraft.util.text.translation.I18n

/**
 * @author WireSegal
 * Created at 4:54 PM on 4/12/16.
 */
object TooltipHelper {

    fun tooltipIfShift(tooltip: MutableList<String>, r: () -> Unit) {
        if (GuiScreen.isShiftKeyDown()) {
            r.invoke()
        } else {
            addToTooltip(tooltip, "psimisc.shiftForInfo")
        }

    }

    fun addToTooltip(tooltip: MutableList<String>, s: String, vararg format: Any?) {
        var locals = local(s).replace("&".toRegex(), "§")
        val formatVals = arrayOfNulls<String>(format.size)

        for (i in format.indices) {
            formatVals[i] = local(format[i].toString()).replace("&".toRegex(), "§")
        }

        if (formatVals.size > 0) {
            locals = String.format(locals, *formatVals)
        }

        tooltip.add(locals)
    }

    fun local(s: String): String {
        return I18n.translateToLocal(s)
    }
}
