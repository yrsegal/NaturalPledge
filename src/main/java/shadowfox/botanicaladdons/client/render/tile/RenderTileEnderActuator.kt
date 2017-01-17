package shadowfox.botanicaladdons.client.render.tile

import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import org.lwjgl.opengl.GL11
import shadowfox.botanicaladdons.common.block.BlockEnderBind.TileEnderBind
import vazkii.botania.client.core.helper.RenderHelper
import java.awt.Color
import java.util.*

/**
 * @author WireSegal
 * Created at 1:44 PM on 5/4/16.
 */
class RenderTileEnderActuator : TileEntitySpecialRenderer<TileEnderBind>() {

    val h = 98 / 255f
    val s = 0.76f
    val maxV = 0.46f

    val ticksToActivate = 50
    val warmupTicks = 30

    override fun renderTileEntityAt(te: TileEnderBind, x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int) {
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
