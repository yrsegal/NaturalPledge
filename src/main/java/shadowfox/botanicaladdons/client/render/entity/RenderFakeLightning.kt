package shadowfox.botanicaladdons.client.render.entity

import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.entity.Render
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.entity.effect.EntityLightningBolt
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import shadowfox.botanicaladdons.common.block.dendrics.thunder.ThunderEventHandler
import java.util.*

/**
 * @author WireSegal
 * Created at 6:54 PM on 5/28/16.
 */
@SideOnly(Side.CLIENT)
class RenderFakeLightning(renderManager: RenderManager) : Render<ThunderEventHandler.FakeLightning>(renderManager) {

    override fun getEntityTexture(entity: ThunderEventHandler.FakeLightning?): ResourceLocation? {
        return null
    }

    override fun doRender(entity: ThunderEventHandler.FakeLightning, x: Double, y: Double, z: Double, entityYaw: Float, partialTicks: Float) {
        val tessellator = Tessellator.getInstance()
        val vertexbuffer = tessellator.buffer
        GlStateManager.disableTexture2D()
        GlStateManager.disableLighting()
        GlStateManager.enableBlend()
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE)
        val adouble = DoubleArray(8)
        val adouble1 = DoubleArray(8)
        var d0 = 0.0
        var d1 = 0.0
        val random = Random(entity.boltVertex)

        for (i in 7 downTo 0) {
            adouble[i] = d0
            adouble1[i] = d1
            d0 += (random.nextInt(11) - 5).toDouble()
            d1 += (random.nextInt(11) - 5).toDouble()
        }

        for (k1 in 0..3) {
            val random1 = Random(entity.boltVertex)

            for (j in 0..2) {
                var k = 7
                var l = 0

                if (j > 0) {
                    k = 7 - j
                }

                if (j > 0) {
                    l = k - 2
                }

                var d2 = adouble[k] - d0
                var d3 = adouble1[k] - d1

                for (i1 in k downTo l) {
                    val d4 = d2
                    val d5 = d3

                    if (j == 0) {
                        d2 += (random1.nextInt(11) - 5).toDouble()
                        d3 += (random1.nextInt(11) - 5).toDouble()
                    } else {
                        d2 += (random1.nextInt(31) - 15).toDouble()
                        d3 += (random1.nextInt(31) - 15).toDouble()
                    }

                    vertexbuffer.begin(5, DefaultVertexFormats.POSITION_COLOR)
                    var d6 = 0.1 + k1.toDouble() * 0.2

                    if (j == 0) {
                        d6 *= i1.toDouble() * 0.1 + 1.0
                    }

                    var d7 = 0.1 + k1.toDouble() * 0.2

                    if (j == 0) {
                        d7 *= (i1 - 1).toDouble() * 0.1 + 1.0
                    }

                    for (j1 in 0..4) {
                        var d8 = x + 0.5 - d6
                        var d9 = z + 0.5 - d6

                        if (j1 == 1 || j1 == 2) {
                            d8 += d6 * 2.0
                        }

                        if (j1 == 2 || j1 == 3) {
                            d9 += d6 * 2.0
                        }

                        var d10 = x + 0.5 - d7
                        var d11 = z + 0.5 - d7

                        if (j1 == 1 || j1 == 2) {
                            d10 += d7 * 2.0
                        }

                        if (j1 == 2 || j1 == 3) {
                            d11 += d7 * 2.0
                        }

                        vertexbuffer.pos(d10 + d2, y + (i1 * 16).toDouble(), d11 + d3).color(0.45f, 0.45f, 0.5f, 0.3f).endVertex()
                        vertexbuffer.pos(d8 + d4, y + ((i1 + 1) * 16).toDouble(), d9 + d5).color(0.45f, 0.45f, 0.5f, 0.3f).endVertex()
                    }

                    tessellator.draw()
                }
            }
        }

        GlStateManager.disableBlend()
        GlStateManager.enableLighting()
        GlStateManager.enableTexture2D()
    }
}

