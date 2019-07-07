package com.wiresegal.naturalpledge.client.render.tile

import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import org.lwjgl.opengl.GL11
import com.wiresegal.naturalpledge.common.NaturalPledge
import com.wiresegal.naturalpledge.common.block.tile.TileStar
import vazkii.botania.client.core.helper.RenderHelper
import java.util.*

/**
 * @author WireSegal
 * Created at 1:44 PM on 5/4/16.
 */
class RenderTileFrozenStar : TileEntitySpecialRenderer<TileStar>() {
    override fun render(te: TileStar, x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int, alpha: Float) {
        GlStateManager.pushMatrix()
        GlStateManager.disableLighting()
        GlStateManager.enableBlend()
        GlStateManager.enableRescaleNormal()
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GlStateManager.color(1f, 1f, 1f)
        GlStateManager.translate(x+0.5,y+0.5,z+0.5)
        val seed = Objects.hash(te.pos.x, te.pos.y, te.pos.z).toLong()
        var color = te.color
        if (color == -1) color = NaturalPledge.PROXY.rainbow(te.pos)
        val size = te.size
        RenderHelper.renderStar(color, size * 0.1f, size * 0.1f, size * 0.1f, seed)
        GlStateManager.color(1f, 1f, 1f)
        GlStateManager.disableBlend()
        GlStateManager.enableRescaleNormal()
        GlStateManager.enableLighting()
        GlStateManager.popMatrix()

    }
}
