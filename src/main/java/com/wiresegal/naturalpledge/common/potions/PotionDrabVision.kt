package com.wiresegal.naturalpledge.common.potions

import com.teamwizardry.librarianlib.features.base.PotionMod
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.util.JsonException
import net.minecraft.init.MobEffects
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.FMLLaunchHandler
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import com.wiresegal.naturalpledge.common.items.bauble.faith.ItemFaithBauble
import com.wiresegal.naturalpledge.common.items.bauble.faith.PriestlyEmblemHeimdall
import com.wiresegal.naturalpledge.common.lib.LibNames

/**
 * @author WireSegal
 * Created at 9:37 AM on 4/15/16.
 */
class PotionDrabVision : PotionMod(LibNames.DRAB_VISION, true, 0x808080) {

    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    val greyscale = ResourceLocation("shaders/post/desaturate.json")
    var grayscaleState = false

    @SubscribeEvent
    fun onLivingUpdate(e: LivingEvent.LivingUpdateEvent) {
        val entity = e.entityLiving
        if (hasEffect(entity) && entity.getActivePotionEffect(MobEffects.NIGHT_VISION) != null) {
            val effect = getEffect(entity) ?: return
            entity.removeActivePotionEffect(this)
            entity.removeActivePotionEffect(MobEffects.NIGHT_VISION)
            val newEffect = PotionEffect(this, effect.duration, Math.max(effect.amplifier - 1, 0), effect.isAmbient, effect.doesShowParticles())
            entity.addPotionEffect(PotionEffect(newEffect))
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    fun updateShaders(e: RenderGameOverlayEvent.Pre) {
        if (FMLLaunchHandler.side().isServer) return
        val mc = Minecraft.getMinecraft()
        if (mc.player == null) return
        if (e.type == RenderGameOverlayEvent.ElementType.ALL) {
            if ((getEffect(mc.player)?.amplifier ?: 0) > 0 && ItemFaithBauble.getEmblem(mc.player, PriestlyEmblemHeimdall::class.java) == null) {
                val render = Minecraft.getMinecraft().entityRenderer
                val group = render.shaderGroup
                if (!grayscaleState && (group == null || group.shaderGroupName != greyscale.toString())) {
                    setShader(greyscale)
                    grayscaleState = true
                }
            } else {
                val render = Minecraft.getMinecraft().entityRenderer
                val group = render.shaderGroup
                if (grayscaleState && group != null && group.shaderGroupName == greyscale.toString()) {
                    Minecraft.getMinecraft().entityRenderer.stopUseShader()
                    grayscaleState = false
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private fun setShader(target: ResourceLocation) {
        try {
            val mc = Minecraft.getMinecraft()
            if (OpenGlHelper.shadersSupported && !mc.entityRenderer.isShaderActive) try {
                mc.entityRenderer.loadShader(target)
            } catch (var5: Exception) {
                //NO-OP
            }
        } catch (err: JsonException) {
            // NO-OP
        }
    }

    override fun getCurativeItems(): MutableList<ItemStack> = mutableListOf()
}
