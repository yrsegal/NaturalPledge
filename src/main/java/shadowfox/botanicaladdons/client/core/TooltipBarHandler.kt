package shadowfox.botanicaladdons.client.core

import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.item.ItemStack
import net.minecraftforge.client.event.RenderTooltipEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

/**
 * @author WireSegal
 * Created at 3:08 PM on 1/3/17.
 */
@SideOnly(Side.CLIENT)
object TooltipBarHandler {
    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent
    fun onToolTipRender(evt: RenderTooltipEvent.PostText) {
        val stack = evt.stack ?: return
        val width = evt.width
        val height = 3
        val tooltipX = evt.x
        val tooltipY = evt.y - 4

        if (stack.item is ITooltipBarItem)
            drawBar(stack, stack.item as ITooltipBarItem, tooltipX, tooltipY, width, height)
    }

    private fun drawBar(stack: ItemStack, display: ITooltipBarItem, mouseX: Int, mouseY: Int, width: Int, height: Int) {
        val fraction = display.getPercentage(stack)
        val color = display.getColor(stack)
        val manaBarWidth = Math.ceil((width * fraction).toDouble()).toInt()

        GlStateManager.disableDepth()
        Gui.drawRect(mouseX - 1, mouseY - height - 1, mouseX + width + 1, mouseY, 0xFF000000.toInt())
        Gui.drawRect(mouseX, mouseY - height, mouseX + manaBarWidth, mouseY, color)
        Gui.drawRect(mouseX + manaBarWidth, mouseY - height, mouseX + width, mouseY, 0xFF555555.toInt())
    }
}
