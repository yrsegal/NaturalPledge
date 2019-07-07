package com.wiresegal.naturalpledge.client.render.tile

import com.wiresegal.naturalpledge.common.block.BlockEnderBind.Companion.DEFAULT_COLOR
import com.wiresegal.naturalpledge.common.block.BlockEnderBind.TileEnderBind
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import org.lwjgl.opengl.GL11
import vazkii.botania.client.core.helper.RenderHelper
import java.awt.Color
import java.util.*

/**
 * @author WireSegal
 * Created at 1:44 PM on 5/4/16.
 */
class RenderTileEnderActuator : TileEntitySpecialRenderer<TileEnderBind>() {

    val color: FloatArray = Color.RGBtoHSB((DEFAULT_COLOR and 0xff0000) shl 16, (DEFAULT_COLOR and 0xff00) shl 8, (DEFAULT_COLOR and 0xff), null)
    val h = color[0]
    val s = color[1]
    val maxV = color[2]

    val ticksToActivate = 85
    val warmupTicks = 15

    override fun render(te: TileEnderBind, x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int, alpha: Float) {
        if (te.playerName == null) return
        val v = maxV * Math.min(Math.max(te.world.totalWorldTime - te.tickSet - warmupTicks, 0).toInt(), ticksToActivate) / ticksToActivate.toFloat()
        if (v == 0f) return

        GlStateManager.pushMatrix()
        GlStateManager.enableBlend()
        GlStateManager.enableRescaleNormal()
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GlStateManager.color(1f, 1f, 1f)
        GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5)

        val seed = Objects.hash(te.pos.x, te.pos.y, te.pos.z).toLong()
        val color = Color.HSBtoRGB(h, s, v)
        RenderHelper.renderStar(color, 0.05f, 0.05f, 0.05f, seed)

        GlStateManager.color(1f, 1f, 1f)
        GlStateManager.disableBlend()
        GlStateManager.enableRescaleNormal()
        GlStateManager.popMatrix()
    }
}
