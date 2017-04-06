package shadowfox.botanicaladdons.common.potions

import com.teamwizardry.librarianlib.common.base.PotionMod
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
import shadowfox.botanicaladdons.common.items.bauble.faith.ItemFaithBauble
import shadowfox.botanicaladdons.common.items.bauble.faith.PriestlyEmblemHeimdall
import shadowfox.botanicaladdons.common.lib.LibNames
import shadowfox.botanicaladdons.common.potions.base.ModPotionEffect

/**
 * @author WireSegal
 * Created at 9:37 AM on 4/15/16.
 */
class PotionDrabVision : PotionMod(LibNames.DRAB_VISION, true, 0x808080) {

    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    val greyscale = ResourceLocation("shaders/post/desaturate.json")

    @SubscribeEvent
    fun onLivingUpdate(e: LivingEvent.LivingUpdateEvent) {
        val entity = e.entityLiving
        if (hasEffect(entity) && entity.getActivePotionEffect(MobEffects.NIGHT_VISION) != null) {
            val effect = getEffect(entity) ?: return
            entity.removeActivePotionEffect(this)
            entity.removeActivePotionEffect(MobEffects.NIGHT_VISION)
            val newEffect = PotionEffect(this, effect.duration, Math.max(effect.amplifier - 1, 0), effect.isAmbient, effect.doesShowParticles())
            if (effect is ModPotionEffect)
                entity.addPotionEffect(ModPotionEffect(newEffect))
            else
                entity.addPotionEffect(newEffect)
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
                setShader(greyscale)
            } else Minecraft.getMinecraft().entityRenderer.stopUseShader()
        }
    }

    @SideOnly(Side.CLIENT)
    private fun setShader(target: ResourceLocation?) {
        try {
            val mc = Minecraft.getMinecraft()
            if (OpenGlHelper.shadersSupported && !mc.entityRenderer.isShaderActive) try {
                if (target == null) Minecraft.getMinecraft().entityRenderer.stopUseShader()
                else mc.entityRenderer.loadShader(target)
            } catch (var5: Exception) {
                //NO-OP
            }
        } catch (err: JsonException) {
            // NO-OP
        }
    }

    override fun getCurativeItems(): MutableList<ItemStack> = mutableListOf()
}
