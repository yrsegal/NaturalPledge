package shadowfox.botanicaladdons.client.render.tile

import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import org.lwjgl.opengl.GL11
import shadowfox.botanicaladdons.common.BotanicalAddons
import shadowfox.botanicaladdons.common.block.tile.TileStar
import vazkii.botania.client.core.helper.RenderHelper

/**
 * @author WireSegal
 * Created at 1:44 PM on 5/4/16.
 */
class RenderTileFrozenStar : TileEntitySpecialRenderer<TileStar>() {
    override fun renderTileEntityAt(te: TileStar, x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int) {
        GlStateManager.pushMatrix()
        GlStateManager.enableBlend()
        GlStateManager.enableRescaleNormal()
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GlStateManager.color(1f, 1f, 1f)
        GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5)

        val seed = (te.pos.x xor te.pos.y xor te.pos.z).toLong()
        var color = te.getColor()
        if (color == -1) color = BotanicalAddons.proxy.rainbow(te.pos).rgb
        val size = te.size
        RenderHelper.renderStar(color, size, size, size, seed)

        GlStateManager.color(1f, 1f, 1f)
        GlStateManager.disableBlend()
        GlStateManager.enableRescaleNormal()
        GlStateManager.popMatrix()
    }
}
