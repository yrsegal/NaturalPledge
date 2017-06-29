/**
 * This class was created by <wiiv>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania

 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php

 * File Created @ [? (GMT)]
</wiiv> */
package shadowfox.botanicaladdons.client.render.entity

import net.minecraft.client.model.ModelBiped
import net.minecraft.client.model.ModelRenderer
import net.minecraft.entity.Entity
import net.minecraft.inventory.EntityEquipmentSlot

class ModelArmorSunmaker(private val slot: EntityEquipmentSlot) : ModelBiped() {

    private val helmAnchor: ModelRenderer
    private val helm: ModelRenderer
    private val helmFront: ModelRenderer

    private val bodyAnchor: ModelRenderer
    private val bodyTop: ModelRenderer
    private val bodyBottom: ModelRenderer

    private val armLAnchor: ModelRenderer
    private val armL: ModelRenderer
    private val armLpauldron: ModelRenderer
    private val armLpauldronRay1: ModelRenderer
    private val armLpauldronRay2: ModelRenderer
    private val armLpauldronRay3: ModelRenderer
    private val armLray1: ModelRenderer
    private val armLray2: ModelRenderer
    private val armLcluster: ModelRenderer

    private val armRAnchor: ModelRenderer
    private val armR: ModelRenderer
    private val armRpauldron: ModelRenderer
    private val armRpauldronRay1: ModelRenderer
    private val armRpauldronRay2: ModelRenderer
    private val armRpauldronRay3: ModelRenderer
    private val armRray1: ModelRenderer
    private val armRray2: ModelRenderer
    private val armRcluster: ModelRenderer

    private val pantsAnchor: ModelRenderer
    private val belt: ModelRenderer
    private val legL: ModelRenderer
    private val legLray1: ModelRenderer
    private val legLray2: ModelRenderer
    private val legR: ModelRenderer
    private val legRray1: ModelRenderer
    private val legRray2: ModelRenderer

    private val bootL: ModelRenderer
    private val bootLTop: ModelRenderer
    private val bootR: ModelRenderer
    private val bootRTop: ModelRenderer

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
        this.helm.addBox(-4.5f, -9.0f, -4.5f, 9, 11, 9, s)
        this.helmFront = ModelRenderer(this, 0, 20)
        this.helmFront.setRotationPoint(0.0f, -4.0f, -4.5f)
        this.helmFront.addBox(-1.5f, -7.0f, -3.0f, 3, 7, 9, s)

        //body
        this.bodyAnchor = ModelRenderer(this, 0, 0)
        this.bodyAnchor.setRotationPoint(0.0f, 0.0f, 0.0f)
        this.bodyAnchor.addBox(-1.0f, 0.0f, -1.0f, 2, 2, 2, s)
        this.bodyTop = ModelRenderer(this, 0, 36)
        this.bodyTop.setRotationPoint(0.0f, 0.0f, 0.0f)
        this.bodyTop.addBox(-4.5f, -0.5f, -3.0f, 9, 6, 6, s)
        this.bodyBottom = ModelRenderer(this, 0, 48)
        this.bodyBottom.setRotationPoint(0.0f, 0.0f, 0.0f)
        this.bodyBottom.addBox(-4.5f, 5.5f, -2.5f, 9, 3, 5, s)

        //armL
        this.armLAnchor = ModelRenderer(this, 0, 0)
        this.armLAnchor.mirror = true
        this.armLAnchor.setRotationPoint(4.0f, 2.0f, 0.0f)
        this.armLAnchor.addBox(0.0f, -1.0f, -1.0f, 2, 2, 2, s)
        this.armL = ModelRenderer(this, 0, 67)
        this.armL.mirror = true
        this.armL.setRotationPoint(0.0f, 0.0f, 0.0f)
        this.armL.addBox(-1.5f, -2.5f, -2.49f, 5, 13, 5, s)
        this.armLpauldron = ModelRenderer(this, 20, 72)
        this.armLpauldron.mirror = true
        this.armLpauldron.setRotationPoint(0.0f, 0.0f, 0.0f)
        this.armLpauldron.addBox(0.0f, -3.5f, -3.5f, 7, 6, 7, s)
        this.armLpauldron.setRotationPoint(0.0f, 0.0f, 0.17453292519943295f)
        this.armLpauldronRay1 = ModelRenderer(this, 30, 56)
        this.armLpauldronRay1.mirror = true
        this.armLpauldronRay1.setRotationPoint(7.5f, 2.5f, 0.0f)
        this.armLpauldronRay1.addBox(-5.0f, -6.5f, -2.5f, 5, 6, 1, s)
        this.armLpauldronRay2 = ModelRenderer(this, 30, 56)
        this.armLpauldronRay2.setRotationPoint(7.5f, 2.5f, 0.0f)
        this.armLpauldronRay2.addBox(-5.0f, -6.5f, -0.5f, 5, 6, 1, s)
        this.armLpauldronRay3 = ModelRenderer(this, 30, 56)
        this.armLpauldronRay3.mirror = true
        this.armLpauldronRay3.setRotationPoint(7.5f, 2.5f, 0.0f)
        this.armLpauldronRay3.addBox(-5.0f, -6.5f, 1.5f, 5, 6, 1, s)
        this.armLray1 = ModelRenderer(this, 20, 86)
        this.armLray1.mirror = true
        this.armLray1.setRotationPoint(0.0f, 0.0f, 0.0f)
        this.armLray1.addBox(-0.5f, 4.5f, -3.0f, 5, 1, 6, s)
        this.armLray2 = ModelRenderer(this, 20, 86)
        this.armLray2.setRotationPoint(0.0f, 0.0f, 0.0f)
        this.armLray2.addBox(-0.5f, 6.5f, -3.0f, 5, 1, 6, s)
        this.armLcluster = ModelRenderer(this, 20, 88)
        this.armLcluster.setRotationPoint(0.0f, 0.0f, 0.0f)
        this.armLcluster.addBox(2.5f, -5.5f, -1.0f, 2, 2, 2, s)
        this.armLcluster.setRotationPoint(0.0f, 0.0f, -0.08726646259971647f)

        //armR
        this.armRAnchor = ModelRenderer(this, 0, 0)
        this.armRAnchor.mirror = true
        this.armRAnchor.setRotationPoint(-4.0f, 2.0f, 0.0f)
        this.armRAnchor.addBox(-2.0f, -1.0f, -1.0f, 2, 2, 2, s)
        this.armR = ModelRenderer(this, 0, 67)
        this.armR.setRotationPoint(0.0f, 0.0f, 0.0f)
        this.armR.addBox(-3.5f, -2.5f, -2.51f, 5, 13, 5, s)
        this.armRpauldron = ModelRenderer(this, 20, 72)
        this.armRpauldron.setRotationPoint(0.0f, 0.0f, 0.0f)
        this.armRpauldron.addBox(-7.0f, -3.5f, -3.5f, 7, 6, 7, s)
        this.armRpauldron.setRotationPoint(0.0f, 0.0f, -0.17453292519943295f)
        this.armRpauldronRay1 = ModelRenderer(this, 30, 56)
        this.armRpauldronRay1.setRotationPoint(-7.5f, 2.5f, 0.0f)
        this.armRpauldronRay1.addBox(0.0f, -6.5f, -2.5f, 5, 6, 1, s)
        this.armRpauldronRay2 = ModelRenderer(this, 30, 56)
        this.armRpauldronRay2.mirror = true
        this.armRpauldronRay2.setRotationPoint(-7.5f, 2.5f, 0.0f)
        this.armRpauldronRay2.addBox(0.0f, -6.5f, -0.5f, 5, 6, 1, s)
        this.armRpauldronRay3 = ModelRenderer(this, 30, 56)
        this.armRpauldronRay3.setRotationPoint(-7.5f, 2.5f, 0.0f)
        this.armRpauldronRay3.addBox(0.0f, -6.5f, 1.5f, 5, 6, 1, s)
        this.armRray1 = ModelRenderer(this, 20, 86)
        this.armRray1.setRotationPoint(0.0f, 0.0f, 0.0f)
        this.armRray1.addBox(-4.5f, 4.5f, -3.0f, 5, 1, 6, s)
        this.armRray2 = ModelRenderer(this, 20, 86)
        this.armRray2.mirror = true
        this.armRray2.setRotationPoint(0.0f, 0.0f, 0.0f)
        this.armRray2.addBox(-4.5f, 6.5f, -3.0f, 5, 1, 6, s)
        this.armRcluster = ModelRenderer(this, 20, 88)
        this.armRcluster.setRotationPoint(0.0f, 0.0f, 0.0f)
        this.armRcluster.addBox(-4.5f, -5.5f, -1.0f, 2, 2, 2, s)
        this.armRcluster.setRotationPoint(0.0f, 0.0f, 0.08726646259971647f)

        //pants
        this.pantsAnchor = ModelRenderer(this, 0, 0)
        this.pantsAnchor.setRotationPoint(0.0f, 0.0f, 0.0f)
        this.pantsAnchor.addBox(-1.0f, 0.0f, -1.0f, 2, 2, 2, s)
        this.belt = ModelRenderer(this, 0, 56)
        this.belt.setRotationPoint(0.0f, 0.0f, 0.0f)
        this.belt.addBox(-4.5f, 8.5f, -3.0f, 9, 5, 6, s)
        this.legL = ModelRenderer(this, 0, 85)
        this.legL.mirror = true
        this.legL.setRotationPoint(1.9f, 12.0f, 0.0f)
        this.legL.addBox(-2.39f, -0.5f, -2.5f, 5, 7, 5, s)
        this.legLray1 = ModelRenderer(this, 20, 86)
        this.legLray1.mirror = true
        this.legLray1.setRotationPoint(0.0f, 0.0f, 0.0f)
        this.legLray1.addBox(-1.5f, 2.5f, -3.0f, 5, 1, 6, s)
        this.legLray2 = ModelRenderer(this, 20, 86)
        this.legLray2.setRotationPoint(0.0f, 0.0f, 0.0f)
        this.legLray2.addBox(-1.5f, 4.5f, -3.0f, 5, 1, 6, s)
        this.legR = ModelRenderer(this, 0, 85)
        this.legR.setRotationPoint(-1.9f, 12.0f, 0.0f)
        this.legR.addBox(-2.61f, -0.5f, -2.5f, 5, 7, 5, s)
        this.legRray1 = ModelRenderer(this, 20, 86)
        this.legRray1.setRotationPoint(0.0f, 0.0f, 0.0f)
        this.legRray1.addBox(-3.5f, 2.5f, -3.0f, 5, 1, 6, s)
        this.legRray2 = ModelRenderer(this, 20, 86)
        this.legRray2.mirror = true
        this.legRray2.setRotationPoint(0.0f, 0.0f, 0.0f)
        this.legRray2.addBox(-3.5f, 4.5f, -3.0f, 5, 1, 6, s)

        //boots
        this.bootL = ModelRenderer(this, 0, 97)
        this.bootL.mirror = true
        this.bootL.setRotationPoint(1.9f, 12.0f, 0.0f)
        this.bootL.addBox(-2.39f, 8.5f, -2.5f, 5, 4, 5, s)
        this.bootLTop = ModelRenderer(this, 20, 97)
        this.bootLTop.mirror = true
        this.bootLTop.setRotationPoint(1.9f, -2.0f, 0.0f)
        this.bootLTop.addBox(-4.29f, 8.5f, -2.5f, 7, 2, 5, s)
        this.bootR = ModelRenderer(this, 0, 97)
        this.bootR.setRotationPoint(-1.9f, 12.0f, 0.0f)
        this.bootR.addBox(-2.61f, 8.5f, -2.5f, 5, 4, 5, s)
        this.bootRTop = ModelRenderer(this, 20, 97)
        this.bootRTop.setRotationPoint(-1.9f, -2.0f, 0.0f)
        this.bootRTop.addBox(-2.71f, 8.5f, -2.5f, 7, 2, 5, s)

        //hierarchy
        this.helmAnchor.addChild(this.helm)
        this.helm.addChild(this.helmFront)

        this.bodyAnchor.addChild(this.bodyTop)
        this.bodyTop.addChild(this.bodyBottom)
        this.armLAnchor.addChild(this.armL)
        this.armL.addChild(this.armLpauldron)
        this.armL.addChild(this.armLray1)
        this.armL.addChild(this.armLray2)
        this.armL.addChild(this.armLcluster)
        this.armLpauldron.addChild(this.armLpauldronRay1)
        this.armLpauldron.addChild(this.armLpauldronRay2)
        this.armLpauldron.addChild(this.armLpauldronRay3)
        this.armRAnchor.addChild(this.armR)
        this.armR.addChild(this.armRpauldron)
        this.armR.addChild(this.armRray1)
        this.armR.addChild(this.armRray2)
        this.armR.addChild(this.armRcluster)
        this.armRpauldron.addChild(this.armRpauldronRay1)
        this.armRpauldron.addChild(this.armRpauldronRay2)
        this.armRpauldron.addChild(this.armRpauldronRay3)

        this.pantsAnchor.addChild(this.belt)
        this.belt.addChild(this.legL)
        this.belt.addChild(this.legR)
        this.legL.addChild(this.legLray1)
        this.legL.addChild(this.legLray2)
        this.legR.addChild(this.legRray1)
        this.legR.addChild(this.legRray2)

        this.bootL.addChild(this.bootLTop)
        this.bootR.addChild(this.bootRTop)
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
