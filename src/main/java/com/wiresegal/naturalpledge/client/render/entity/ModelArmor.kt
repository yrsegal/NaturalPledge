package com.wiresegal.naturalpledge.client.render.entity

import net.minecraft.client.model.ModelBase
import net.minecraft.client.model.ModelBiped
import net.minecraft.client.model.ModelRenderer
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityArmorStand
import net.minecraft.inventory.EntityEquipmentSlot

open class ModelArmor(protected val slot: EntityEquipmentSlot) : ModelBiped() {

    private val rotConstant = 5 * Math.PI.toFloat() / 900

    override fun setRotationAngles(limbSwing: Float, limbSwingAmount: Float, ageInTicks: Float, netHeadYaw: Float, headPitch: Float, scaleFactor: Float, entityIn: Entity) {
        if (entityIn is EntityArmorStand) {
            val entityarmorstand = entityIn
            this.bipedHead.rotateAngleX = rotConstant * entityarmorstand.headRotation.x
            this.bipedHead.rotateAngleY = rotConstant * entityarmorstand.headRotation.y
            this.bipedHead.rotateAngleZ = rotConstant * entityarmorstand.headRotation.z
            this.bipedHead.setRotationPoint(0.0f, 1.0f, 0.0f)
            this.bipedBody.rotateAngleX = rotConstant * entityarmorstand.bodyRotation.x
            this.bipedBody.rotateAngleY = rotConstant * entityarmorstand.bodyRotation.y
            this.bipedBody.rotateAngleZ = rotConstant * entityarmorstand.bodyRotation.z
            this.bipedLeftArm.rotateAngleX = rotConstant * entityarmorstand.leftArmRotation.x
            this.bipedLeftArm.rotateAngleY = rotConstant * entityarmorstand.leftArmRotation.y
            this.bipedLeftArm.rotateAngleZ = rotConstant * entityarmorstand.leftArmRotation.z
            this.bipedRightArm.rotateAngleX = rotConstant * entityarmorstand.rightArmRotation.x
            this.bipedRightArm.rotateAngleY = rotConstant * entityarmorstand.rightArmRotation.y
            this.bipedRightArm.rotateAngleZ = rotConstant * entityarmorstand.rightArmRotation.z
            this.bipedLeftLeg.rotateAngleX = rotConstant * entityarmorstand.leftLegRotation.x
            this.bipedLeftLeg.rotateAngleY = rotConstant * entityarmorstand.leftLegRotation.y
            this.bipedLeftLeg.rotateAngleZ = rotConstant * entityarmorstand.leftLegRotation.z
            this.bipedLeftLeg.setRotationPoint(1.9f, 11.0f, 0.0f)
            this.bipedRightLeg.rotateAngleX = rotConstant * entityarmorstand.rightLegRotation.x
            this.bipedRightLeg.rotateAngleY = rotConstant * entityarmorstand.rightLegRotation.y
            this.bipedRightLeg.rotateAngleZ = rotConstant * entityarmorstand.rightLegRotation.z
            this.bipedRightLeg.setRotationPoint(-1.9f, 11.0f, 0.0f)
            ModelBase.copyModelAngles(this.bipedHead, this.bipedHeadwear)
        } else
            super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn)
    }

    protected fun setRotateAngle(modelRenderer: ModelRenderer, x: Float, y: Float, z: Float) {
        modelRenderer.rotateAngleX = x
        modelRenderer.rotateAngleY = y
        modelRenderer.rotateAngleZ = z
    }
}
