package shadowfox.botanicaladdons.common.potions

import shadowfox.botanicaladdons.api.lib.LibNames
import shadowfox.botanicaladdons.common.potions.base.PotionMod

/**
 * @author WireSegal
 * Created at 9:37 AM on 4/15/16.
 */
class PotionDrabVision(iconIndex: Int) : PotionMod(LibNames.DRAB_VISION, true, 0x808080, iconIndex, true) {

    //todo make work

    //    init {
    //        MinecraftForge.EVENT_BUS.register(this)
    //    }
    //
    //    //totally not copied from thaumcraft >.>
    //
    //    val greyscale = ResourceLocation("shaders/post/desaturate.json")
    //
    //    @SideOnly(Side.CLIENT)
    //    @SubscribeEvent
    //    fun updateShaders(e: TickEvent.PlayerTickEvent) {
    //        if (FMLLaunchHandler.side().isServer) return
    //        val mc = Minecraft.getMinecraft()
    //        if (mc.thePlayer == null) return
    //        if (hasEffect(mc.thePlayer)) {
    //            try {
    //                setShader(ShaderGroup(mc.textureManager, mc.resourceManager, mc.framebuffer, greyscale), 1)
    //            } catch (err: JsonException) {}
    //        } else {
    //            deactivateShader(1)
    //        }
    //
    //    }
    //
    //    val shaderGroups = HashMap<Int, ShaderGroup>()
    //    var resetShaders = false
    //
    //    @SideOnly(Side.CLIENT)
    //    @Throws(JsonException::class)
    //    internal fun setShader(target: ShaderGroup?, shaderId: Int) {
    //        if (OpenGlHelper.shadersSupported && !shaderGroups.containsKey(shaderId)) {
    //            val mc = Minecraft.getMinecraft()
    //            if (shaderGroups.containsKey(shaderId)) {
    //                shaderGroups[shaderId]?.deleteShaderGroup()
    //                shaderGroups.remove(shaderId)
    //            }
    //
    //            try {
    //                if (target == null) {
    //                    this.deactivateShader(shaderId)
    //                } else {
    //                    resetShaders = true
    //                    shaderGroups.put(shaderId, target)
    //                }
    //            } catch (var5: Exception) {
    //                try {
    //                    shaderGroups.remove(shaderId)
    //                } catch (err: RuntimeException) {}
    //            }
    //
    //        }
    //
    //    }
    //
    //    @SideOnly(Side.CLIENT)
    //    @SubscribeEvent
    //    fun renderShaders(event: RenderGameOverlayEvent.Pre) {
    //        if (event.type == RenderGameOverlayEvent.ElementType.ALL) {
    //            val mc = Minecraft.getMinecraft()
    //            val time = System.nanoTime() / 1000000L
    //            GlStateManager.pushMatrix()
    //            if (OpenGlHelper.shadersSupported && shaderGroups.size > 0) {
    //                this.updateShaderFrameBuffers(mc)
    //                GL11.glMatrixMode(5890)
    //                GL11.glLoadIdentity()
    //
    //                val `i$` = shaderGroups.values.iterator()
    //                while (`i$`.hasNext()) {
    //                    GL11.glPushMatrix()
    //
    //                    try {
    //                        `i$`.next().loadShaderGroup(event.partialTicks)
    //                    } catch (var8: Exception) {
    //                    }
    //
    //                    GL11.glPopMatrix()
    //                }
    //
    //                mc.framebuffer.bindFramebuffer(true)
    //            }
    //            GlStateManager.popMatrix()
    //        }
    //
    //    }
    //
    //    private var oldDisplayWidth = 0
    //    private var oldDisplayHeight = 0
    //
    //    private fun updateShaderFrameBuffers(mc: Minecraft) {
    //        if (resetShaders || mc.displayWidth != oldDisplayWidth || oldDisplayHeight != mc.displayHeight) {
    //            val `i$` = shaderGroups.values.iterator()
    //
    //            while (`i$`.hasNext()) {
    //                `i$`.next().createBindFramebuffers(mc.displayWidth, mc.displayHeight)
    //            }
    //
    //            oldDisplayWidth = mc.displayWidth
    //            oldDisplayHeight = mc.displayHeight
    //            resetShaders = false
    //        }
    //
    //    }
    //
    //    fun deactivateShader(shaderId: Int) {
    //        if (shaderGroups.containsKey(shaderId)) {
    //            shaderGroups[shaderId]?.deleteShaderGroup()
    //        }
    //        shaderGroups.remove(shaderId)
    //    }
}
