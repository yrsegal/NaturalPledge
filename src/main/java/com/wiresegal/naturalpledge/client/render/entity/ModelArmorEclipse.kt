package com.wiresegal.naturalpledge.client.render.entity

import net.minecraft.client.model.ModelRenderer
import net.minecraft.entity.Entity
import net.minecraft.inventory.EntityEquipmentSlot

/**
 * Created by wiiv.
 */
class ModelArmorEclipse(slot: EntityEquipmentSlot) : ModelArmor(slot) {

    private val helmAnchor: ModelRenderer
    private val helm: ModelRenderer

    private val bodyAnchor: ModelRenderer
    private val bodyTop: ModelRenderer
    private val bodyBottom: ModelRenderer

    private val armLAnchor: ModelRenderer
    private val armL: ModelRenderer
    private val armLpauldron1: ModelRenderer
    private val armLpauldron2: ModelRenderer
    private val armLpauldron3: ModelRenderer

    private val armRAnchor: ModelRenderer
    private val armR: ModelRenderer
    private val armRpauldron1: ModelRenderer
    private val armRpauldron2: ModelRenderer
    private val armRpauldron3: ModelRenderer
    private val pantsAnchor: ModelRenderer
    private val belt: ModelRenderer
    private val legL: ModelRenderer
    private val legLpauldron1: ModelRenderer
    private val legLpauldron2: ModelRenderer
    private val legR: ModelRenderer
    private val legRpauldron1: ModelRenderer
    private val legRpauldron2: ModelRenderer

    private val bootL: ModelRenderer
    private val bootR: ModelRenderer

    init {

        textureWidth = 64
        textureHeight = 128
        val s = 0.01f

        //helm
        helmAnchor = ModelRenderer(this, 0, 0)
        helmAnchor.setRotationPoint(0.0f, 0.0f, 0.0f)
        helmAnchor.addBox(-1.0f, -2.0f, 0.0f, 2, 2, 2, s)
        helm = ModelRenderer(this, 0, 0)
        helm.setRotationPoint(0.0f, 0.0f, 0.0f)
        helm.addBox(-4.5f, -3.5f, -4.5f, 9, 4, 9, s)

        //body
        bodyAnchor = ModelRenderer(this, 0, 0)
        bodyAnchor.setRotationPoint(0.0f, 0.0f, 0.0f)
        bodyAnchor.addBox(-1.0f, 0.0f, -1.0f, 2, 2, 2, s)
        bodyTop = ModelRenderer(this, 0, 13)
        bodyTop.setRotationPoint(0.0f, 0.0f, 0.0f)
        bodyTop.addBox(-4.5f, -0.5f, -3.0f, 9, 6, 6, s)
        bodyBottom = ModelRenderer(this, 0, 25)
        bodyBottom.setRotationPoint(0.0f, 0.0f, 0.0f)
        bodyBottom.addBox(-3.5f, 5.5f, -2.5f, 7, 3, 5, s)

        //armL
        armLAnchor = ModelRenderer(this, 0, 0)
        armLAnchor.mirror = true
        armLAnchor.setRotationPoint(4.0f, 2.0f, 0.0f)
        armLAnchor.addBox(0.0f, -1.0f, -1.0f, 2, 2, 2, s)
        armL = ModelRenderer(this, 0, 35)
        armL.mirror = true
        armL.setRotationPoint(0.0f, 0.0f, 0.0f)
        armL.addBox(-1.5f, -2.5f, -2.5f, 5, 13, 5, s)
        armLpauldron1 = ModelRenderer(this, 20, 35)
        armLpauldron1.mirror = true
        armLpauldron1.setRotationPoint(-1.5f, 1.0f, 0.0f)
        armLpauldron1.addBox(0.0f, -4.0f, -3.0f, 7, 4, 6, s)
        setRotateAngle(armLpauldron1, 0.0f, 0.0f, -0.08726646259971647f)
        armLpauldron2 = ModelRenderer(this, 20, 65)
        armLpauldron2.mirror = true
        armLpauldron2.setRotationPoint(-1.5f, 1.0f, 0.0f)
        armLpauldron2.addBox(0.0f, 0.0f, -3.0f, 6, 2, 6, s)
        setRotateAngle(armLpauldron2, 0.0f, 0.0f, 0.08726646259971647f)
        armLpauldron3 = ModelRenderer(this, 20, 73)
        armLpauldron3.mirror = true
        armLpauldron3.setRotationPoint(0.0f, 0.0f, 0.0f)
        armLpauldron3.addBox(0.5f, 5.5f, -3.0f, 4, 2, 6, s)

        //armR
        armRAnchor = ModelRenderer(this, 0, 0)
        armRAnchor.mirror = true
        armRAnchor.setRotationPoint(-4.0f, 2.0f, 0.0f)
        armRAnchor.addBox(-2.0f, -1.0f, -1.0f, 2, 2, 2, s)
        armR = ModelRenderer(this, 0, 35)
        armR.setRotationPoint(0.0f, 0.0f, 0.0f)
        armR.addBox(-3.5f, -2.5f, -2.5f, 5, 13, 5, 0.0f)
        armRpauldron1 = ModelRenderer(this, 20, 35)
        armRpauldron1.setRotationPoint(1.5f, 1.0f, 0.0f)
        armRpauldron1.addBox(-7.0f, -4.0f, -3.0f, 7, 4, 6, s)
        setRotateAngle(armRpauldron1, 0.0f, 0.0f, 0.08726646259971647f)
        armRpauldron2 = ModelRenderer(this, 20, 65)
        armRpauldron2.setRotationPoint(1.5f, 1.0f, 0.0f)
        armRpauldron2.addBox(-6.0f, 0.0f, -3.0f, 6, 2, 6, s)
        setRotateAngle(armRpauldron2, 0.0f, 0.0f, -0.08726646259971647f)
        armRpauldron3 = ModelRenderer(this, 20, 73)
        armRpauldron3.setRotationPoint(0.0f, 0.0f, 0.0f)
        armRpauldron3.addBox(-4.5f, 5.5f, -3.0f, 4, 2, 6, s)

        //pants
        pantsAnchor = ModelRenderer(this, 0, 0)
        pantsAnchor.setRotationPoint(0.0f, 0.0f, 0.0f)
        pantsAnchor.addBox(-1.0f, 0.0f, -1.0f, 2, 2, 2, s)
        belt = ModelRenderer(this, 0, 54)
        belt.setRotationPoint(0.0f, 0.0f, 0.0f)
        belt.addBox(-4.5f, 8.5f, -3.0f, 9, 5, 6, s)
        legL = ModelRenderer(this, 0, 65)
        legL.mirror = true
        legL.setRotationPoint(1.9f, 12.0f, 0.0f)
        legL.addBox(-2.39f, 0.0f, -2.5f, 5, 8, 5, s)
        legLpauldron1 = ModelRenderer(this, 20, 81)
        legLpauldron1.mirror = true
        legLpauldron1.setRotationPoint(0.0f, 4.0f, -0.0f)
        legLpauldron1.addBox(0.0f, -2.0f, -3.0f, 5, 2, 6, s)
        setRotateAngle(legLpauldron1, 0.0f, 0.0f, -0.08726646259971647f)
        legLpauldron2 = ModelRenderer(this, 20, 89)
        legLpauldron2.mirror = true
        legLpauldron2.setRotationPoint(0.0f, 4.0f, -0.0f)
        legLpauldron2.addBox(0.0f, 0.0f, -3.0f, 5, 2, 6, s)
        setRotateAngle(legLpauldron2, 0.0f, 0.0f, 0.08726646259971647f)
        legR = ModelRenderer(this, 0, 65)
        legR.setRotationPoint(-1.9f, 12.0f, 0.0f)
        legR.addBox(-2.61f, -0.01f, -2.5f, 5, 8, 5, s)
        legRpauldron1 = ModelRenderer(this, 20, 81)
        legRpauldron1.setRotationPoint(0.5f, 4.0f, 0.0f)
        legRpauldron1.addBox(-5.0f, -2.0f, -3.0f, 5, 2, 6, s)
        setRotateAngle(legRpauldron1, 0.0f, 0.0f, 0.08726646259971647f)
        legRpauldron2 = ModelRenderer(this, 20, 89)
        legRpauldron2.setRotationPoint(0.5f, 4.0f, 0.0f)
        legRpauldron2.addBox(-5.0f, 0.0f, -3.0f, 5, 2, 6, s)
        setRotateAngle(legRpauldron2, 0.0f, 0.0f, -0.08726646259971647f)

        //boots
        bootL = ModelRenderer(this, 0, 78)
        bootL.mirror = true
        bootL.setRotationPoint(1.9f, 12.0f, 0.0f)
        bootL.addBox(-2.39f, 8.0f, -2.5f, 5, 5, 5, s)
        bootR = ModelRenderer(this, 0, 78)
        bootR.setRotationPoint(-1.9f, 12.0f, 0.0f)
        bootR.addBox(-2.61f, 8.0f, -2.5f, 5, 5, 5, s)

        //hierarchy
        helmAnchor.addChild(helm)

        bodyAnchor.addChild(bodyTop)
        bodyTop.addChild(bodyBottom)
        armLAnchor.addChild(armL)
        armL.addChild(armLpauldron1)
        armL.addChild(armLpauldron2)
        armL.addChild(armLpauldron3)
        armRAnchor.addChild(armR)
        armR.addChild(armRpauldron1)
        armR.addChild(armRpauldron2)
        armR.addChild(armRpauldron3)

        pantsAnchor.addChild(belt)
        legL.addChild(legLpauldron1)
        legL.addChild(legLpauldron2)
        legR.addChild(legRpauldron1)
        legR.addChild(legRpauldron2)
    }

    override fun render(entity: Entity, limbSwing: Float, limbSwingAmount: Float, ageInTicks: Float, netHeadYaw: Float, headPitch: Float, scale: Float) {

        helmAnchor.showModel = slot == EntityEquipmentSlot.HEAD
        bodyAnchor.showModel = slot == EntityEquipmentSlot.CHEST
        armRAnchor.showModel = slot == EntityEquipmentSlot.CHEST
        armLAnchor.showModel = slot == EntityEquipmentSlot.CHEST
        legR.showModel = slot == EntityEquipmentSlot.LEGS
        legL.showModel = slot == EntityEquipmentSlot.LEGS
        bootL.showModel = slot == EntityEquipmentSlot.FEET
        bootR.showModel = slot == EntityEquipmentSlot.FEET
        bipedHeadwear.showModel = false

        bipedHead = helmAnchor
        bipedBody = bodyAnchor
        bipedRightArm = armRAnchor
        bipedLeftArm = armLAnchor
        if (slot == EntityEquipmentSlot.LEGS) {
            bipedBody = pantsAnchor
            bipedRightLeg = legR
            bipedLeftLeg = legL
        } else {
            bipedRightLeg = bootR
            bipedLeftLeg = bootL
        }

        super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale)
    }
}
