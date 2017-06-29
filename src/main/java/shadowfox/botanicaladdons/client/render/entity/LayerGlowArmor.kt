package shadowfox.botanicaladdons.client.render.entity

import com.teamwizardry.librarianlib.features.kotlin.toRl
import com.teamwizardry.librarianlib.features.utilities.client.GlUtils.useLightmap
import com.teamwizardry.librarianlib.features.utilities.client.GlUtils.withLighting
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.entity.RenderLivingBase
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor
import net.minecraft.entity.EntityLivingBase
import net.minecraft.inventory.EntityEquipmentSlot
import shadowfox.botanicaladdons.common.items.base.ItemBaseArmor
import vazkii.botania.client.lib.LibResources

/**
 * @author WireSegal
 * Created at 2:10 PM on 6/29/17.
 */
class LayerGlowArmor(val renderer: RenderLivingBase<*>) : LayerBipedArmor(renderer) {
    override fun doRenderLayer(entitylivingbaseIn: EntityLivingBase, limbSwing: Float, limbSwingAmount: Float, partialTicks: Float, ageInTicks: Float, netHeadYaw: Float, headPitch: Float, scale: Float) {
        this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.CHEST)
        this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.LEGS)
        this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.FEET)
        this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.HEAD)
    }

    fun renderArmorLayer(entityLivingBaseIn: EntityLivingBase, limbSwing: Float, limbSwingAmount: Float, partialTicks: Float, ageInTicks: Float, netHeadYaw: Float, headPitch: Float, scale: Float, slotIn: EntityEquipmentSlot) {
        val itemstack = entityLivingBaseIn.getItemStackFromSlot(slotIn)

        if (!itemstack.isEmpty && itemstack.item is ItemBaseArmor) {
            val itemarmor = itemstack.item as ItemBaseArmor

            val tex = itemarmor.getArmorTexture("glow")

            if (tex != null && tex != LibResources.MODEL_INVISIBLE_ARMOR && itemarmor.equipmentSlot == slotIn) {
                var t = this.getModelFromSlot(slotIn)
                t = getArmorModelHook(entityLivingBaseIn, itemstack, slotIn, t)
                t.setModelAttributes(this.renderer.mainModel)
                t.setLivingAnimations(entityLivingBaseIn, limbSwing, limbSwingAmount, partialTicks)
                this.setModelSlotVisible(t, slotIn)

                this.renderer.bindTexture(tex.toRl())
                GlStateManager.color(1f, 1f, 1f, 1f)
                withLighting(false) { useLightmap(0xf000f0) {
                    t.render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale)
                }}
            }
        }
    }
}
