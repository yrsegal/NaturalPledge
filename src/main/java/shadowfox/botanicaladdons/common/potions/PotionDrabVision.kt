package shadowfox.botanicaladdons.common.potions

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.util.JsonException
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.FMLLaunchHandler
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import shadowfox.botanicaladdons.api.lib.LibNames
import shadowfox.botanicaladdons.common.potions.base.PotionMod

/**
 * @author WireSegal
 * Created at 9:37 AM on 4/15/16.
 */
class PotionDrabVision(iconIndex: Int) : PotionMod(LibNames.DRAB_VISION, true, 0x808080, iconIndex, true) {

        init {
            MinecraftForge.EVENT_BUS.register(this)
        }

        val greyscale = ResourceLocation("shaders/post/desaturate.json")

        @SideOnly(Side.CLIENT)
        @SubscribeEvent
        fun updateShaders(e: RenderGameOverlayEvent.Pre) {
            if (FMLLaunchHandler.side().isServer) return
            val mc = Minecraft.getMinecraft()
            if (mc.thePlayer == null) return
            if (e.type == RenderGameOverlayEvent.ElementType.ALL) {
                if (hasEffect(mc.thePlayer)) {
                    try {
                        setShader(greyscale)
                    } catch (err: JsonException) {
                    }
                } else {
                    Minecraft.getMinecraft().entityRenderer.stopUseShader()
                }
            }
        }

        @SideOnly(Side.CLIENT)
        @Throws(JsonException::class)
        internal fun setShader(target: ResourceLocation?) {
            val mc = Minecraft.getMinecraft()
            if (OpenGlHelper.shadersSupported && !mc.entityRenderer.isShaderActive) {
                try {
                    if (target == null) {
                        Minecraft.getMinecraft().entityRenderer.stopUseShader()
                    } else {
                        mc.entityRenderer.loadShader(target)
                    }
                } catch (var5: Exception) {}
            }
        }
}
