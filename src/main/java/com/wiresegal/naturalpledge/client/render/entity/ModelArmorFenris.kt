package com.wiresegal.naturalpledge.client.render.entity

import net.minecraft.client.model.ModelRenderer
import net.minecraft.entity.Entity
import net.minecraft.inventory.EntityEquipmentSlot

/**
 * Created by wiiv.
 */
class ModelArmorFenris(slot: EntityEquipmentSlot) : ModelArmor(slot) {

    private val helmAnchor: ModelRenderer
    private val helm: ModelRenderer
    private val helmFur: ModelRenderer
    private val helmSnout: ModelRenderer
    private val helmEarL: ModelRenderer
    private val helmEarR: ModelRenderer

    private val bodyAnchor: ModelRenderer
    private val bodyTop: ModelRenderer
    private val bodyBottom: ModelRenderer

    private val armLAnchor: ModelRenderer
    private val armL: ModelRenderer
    private val armLpauldron: ModelRenderer
    private val armLpaw: ModelRenderer

    private val armRAnchor: ModelRenderer
    private val armR: ModelRenderer
    private val armRpauldron: ModelRenderer
    private val armRpaw: ModelRenderer

    private val pantsAnchor: ModelRenderer
    private val belt: ModelRenderer
    private val legL: ModelRenderer
    private val legR: ModelRenderer

    private val bootL: ModelRenderer
    private val bootR: ModelRenderer

    init {

        this.textureWidth = 64
        this.textureHeight = 128
        val s = 0.01f

        //helm
        this.helmAnchor = ModelRenderer(this, 0, 0)
        this.helmAnchor.setRotationPoint(0.0f, 0.0f, 0.0f)
        this.helmAnchor.addBox(-1.0f, -2.0f, 0.0f, 2, 2, 2, s)
        this.helm = ModelRenderer(this, 0, 0)
        this.helm.setRotationPoint(0.0f, 0.0f, 0.0f)
        this.helm.addBox(-4.5f, -9.0f, -4.5f, 9, 9, 9, s)
        this.helmFur = ModelRenderer(this, 0, 18)
        this.helmFur.setRotationPoint(0.0f, -2.0f, -4.5f)
        this.helmFur.addBox(-5.5f, 0.0f, -1.0f, 11, 5, 11, s)
        this.setRotateAngle(helmFur, 0.2617993877991494f, 0.0f, 0.0f)
        this.helmSnout = ModelRenderer(this, 36, 0)
        this.helmSnout.setRotationPoint(0.0f, -3.0f, -4.5f)
        this.helmSnout.addBox(-2.5f, 0.0f, -5.0f, 5, 4, 6, s)
        this.setRotateAngle(helmSnout, 0.2617993877991494f, 0.0f, 0.0f)
        this.helmEarL = ModelRenderer(this, 36, 10)
        this.helmEarL.mirror = true
        this.helmEarL.setRotationPoint(3.5f, -9.0f, -0.5f)
        this.helmEarL.addBox(-3.0f, -3.0f, 0.0f, 4, 5, 2, s)
        this.setRotateAngle(helmEarL, 0.0f, 0.0f, 0.2617993877991494f)
        this.helmEarR = ModelRenderer(this, 36, 10)
        this.helmEarR.setRotationPoint(-3.5f, -9.0f, -0.5f)
        this.helmEarR.addBox(-1.0f, -3.0f, 0.0f, 4, 5, 2, s)
        this.setRotateAngle(helmEarR, 0.0f, 0.0f, -0.2617993877991494f)

        //body
        this.bodyAnchor = ModelRenderer(this, 0, 0)
        this.bodyAnchor.setRotationPoint(0.0f, 0.0f, 0.0f)
        this.bodyAnchor.addBox(-1.0f, 0.0f, -1.0f, 2, 2, 2, s)
        this.bodyTop = ModelRenderer(this, 0, 35)
        this.bodyTop.setRotationPoint(0.0f, 0.0f, 0.0f)
        this.bodyTop.addBox(-4.5f, -0.5f, -3.0f, 9, 6, 7, s)
        this.bodyBottom = ModelRenderer(this, 0, 48)
        this.bodyBottom.setRotationPoint(0.0f, 0.0f, 0.0f)
        this.bodyBottom.addBox(-3.5f, 4.5f, -2.5f, 7, 5, 5, s)

        //armL
        this.armLAnchor = ModelRenderer(this, 0, 0)
        this.armLAnchor.mirror = true
        this.armLAnchor.setRotationPoint(4.0f, 2.0f, 0.0f)
        this.armLAnchor.addBox(0.0f, -1.0f, -1.0f, 2, 2, 2, s)
        this.armL = ModelRenderer(this, 0, 58)
        this.armL.mirror = true
        this.armL.setRotationPoint(0.0f, 0.0f, 0.0f)
        this.armL.addBox(-1.5f, 2.5f, -2.5f, 5, 3, 5, s)
        this.armLpauldron = ModelRenderer(this, 0, 66)
        this.armLpauldron.mirror = true
        this.armLpauldron.addBox(-0.5f, -2.5f, -3.5f, 5, 5, 7, s)
        this.armLpaw = ModelRenderer(this, 24, 66)
        this.armLpaw.mirror = true
        this.armLpaw.setRotationPoint(2.5f, 5.5f, 0.0f)
        this.armLpaw.addBox(-1.0f, 0.0f, -2.5f, 3, 6, 5, s)
        this.setRotateAngle(armLpaw, 0.0f, 0.0f, 0.2617993877991494f)

        //armR
        this.armRAnchor = ModelRenderer(this, 0, 0)
        this.armRAnchor.mirror = true
        this.armRAnchor.setRotationPoint(-4.0f, 2.0f, 0.0f)
        this.armRAnchor.addBox(-2.0f, -1.0f, -1.0f, 2, 2, 2, s)
        this.armR = ModelRenderer(this, 0, 58)
        this.armR.setRotationPoint(0.0f, 0.0f, 0.0f)
        this.armR.addBox(-3.5f, 2.5f, -2.5f, 5, 3, 5, s)
        this.armRpauldron = ModelRenderer(this, 0, 66)
        this.armRpauldron.addBox(-4.5f, -2.5f, -3.5f, 5, 5, 7, s)
        this.armRpaw = ModelRenderer(this, 24, 66)
        this.armRpaw.setRotationPoint(-2.5f, 5.5f, 0.0f)
        this.armRpaw.addBox(-2.0f, 0.0f, -2.5f, 3, 6, 5, s)
        this.setRotateAngle(armRpaw, 0.0f, 0.0f, -0.2617993877991494f)

        //pants
        this.pantsAnchor = ModelRenderer(this, 0, 0)
        this.pantsAnchor.setRotationPoint(0.0f, 0.0f, 0.0f)
        this.pantsAnchor.addBox(-1.0f, 0.0f, -1.0f, 2, 2, 2, s)
        this.belt = ModelRenderer(this, 0, 78)
        this.belt.setRotationPoint(0.0f, 0.0f, 0.0f)
        this.belt.addBox(-4.5f, 8.5f, -3.0f, 9, 5, 6, s)
        this.legL = ModelRenderer(this, 0, 89)
        this.legL.mirror = true
        this.legL.setRotationPoint(1.9f, 12.0f, 0.0f)
        this.legL.addBox(-2.39f, -0.5f, -2.5f, 5, 7, 5, s)
        this.legR = ModelRenderer(this, 0, 89)
        this.legR.setRotationPoint(-1.9f, 12.0f, 0.0f)
        this.legR.addBox(-2.61f, -0.5f, -2.5f, 5, 7, 5, s)

        //boots
        this.bootL = ModelRenderer(this, 0, 101)
        this.bootL.mirror = true
        this.bootL.setRotationPoint(1.9f, 12.0f, 0.0f)
        this.bootL.addBox(-2.39f, 8.5f, -2.5f, 5, 4, 5, s)
        this.bootR = ModelRenderer(this, 0, 101)
        this.bootR.setRotationPoint(-1.9f, 12.0f, 0.0f)
        this.bootR.addBox(-2.61f, 8.5f, -2.5f, 5, 4, 5, s)

        //hierarchy
        this.helmAnchor.addChild(this.helm)
        this.helm.addChild(this.helmEarL)
        this.helm.addChild(this.helmEarR)
        this.helm.addChild(this.helmSnout)
        this.helm.addChild(this.helmFur)

        this.bodyAnchor.addChild(this.bodyTop)
        this.bodyTop.addChild(this.bodyBottom)
        this.armLAnchor.addChild(this.armL)
        this.armL.addChild(this.armLpauldron)
        this.armL.addChild(this.armLpaw)
        this.armRAnchor.addChild(this.armR)
        this.armR.addChild(this.armRpauldron)
        this.armR.addChild(this.armRpaw)

        this.pantsAnchor.addChild(this.belt)
        this.belt.addChild(this.legL)
        this.belt.addChild(this.legR)
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
