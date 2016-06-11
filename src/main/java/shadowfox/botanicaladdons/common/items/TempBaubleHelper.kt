package shadowfox.botanicaladdons.common.items

import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.MathHelper

/**
 * @author WireSegal
 * Created at 5:04 PM on 6/8/16.
 *
 * To be removed when Botania merges PR #337
 */
object TempBaubleHelper {
    fun rotateIfSneaking(player: EntityPlayer) {
        if (player.isSneaking)
            applySneakingRotation()
    }

    fun applySneakingRotation() {
        GlStateManager.translate(0f, 0.2f, 0f)
        GlStateManager.rotate(90f / Math.PI.toFloat(), 1.0f, 0.0f, 0.0f)
    }

    fun translateToHeadLevel(player: EntityPlayer) {
        GlStateManager.translate(0f, -player.defaultEyeHeight, 0f)
        if (player.isSneaking)
            GlStateManager.translate(0.25f * MathHelper.sin(player.rotationPitch * Math.PI.toFloat() / 180), 0.25f * MathHelper.cos(player.rotationPitch * Math.PI.toFloat() / 180), 0f)
    }

    fun translateToFace() {
        GlStateManager.rotate(90F, 0F, 1F, 0F)
        GlStateManager.rotate(180F, 1F, 0F, 0F)
        GlStateManager.translate(0f, -4.35f, -1.27f)
    }

    fun defaultTransforms() {
        GlStateManager.translate(0.0, 3.0, 1.0);
        GlStateManager.scale(0.55, 0.55, 0.55);
    }

    fun translateToChest() {
        GlStateManager.rotate(180F, 1F, 0F, 0F);
        GlStateManager.translate(0F, -3.2F, -0.85F);
    }
}
