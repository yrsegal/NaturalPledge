package com.wiresegal.naturalpledge.client.render.entity

import net.minecraft.client.model.ModelBase
import net.minecraft.client.model.ModelRenderer

/**
 * armor_cloak - wiiv
 * Created using Tabula 4.1.1
 */
class ModelOldCloak : ModelBase() {

    var armRpauldron: ModelRenderer
    var armLpauldron: ModelRenderer
    var helm: ModelRenderer

    init {
        textureWidth = 64
        textureHeight = 64
        armLpauldron = ModelRenderer(this, 0, 15)
        armLpauldron.mirror = true
        armLpauldron.setRotationPoint(0.0f, 0.0f, -0.0f)
        armLpauldron.addBox(-1.0f, 0.0f, -5.0f, 9, 20, 9, 0.0f)
        setRotateAngle(armLpauldron, 0.08726646259971647f, -0.2617993877991494f, -0.17453292519943295f)
        armRpauldron = ModelRenderer(this, 0, 15)
        armRpauldron.setRotationPoint(0.0f, 0.0f, 0.0f)
        armRpauldron.addBox(-8.0f, 0.0f, -5.0f, 9, 20, 9, 0.0f)
        setRotateAngle(armRpauldron, 0.08726646259971647f, 0.2617993877991494f, 0.17453292519943295f)
        helm = ModelRenderer(this, 0, 0)
        helm.setRotationPoint(0.0f, 0.0f, 0.0f)
        helm.addBox(-4.5f, -3.0f, -5.5f, 9, 4, 11, 0.0f)
        setRotateAngle(helm, 0.2617993877991494f, 0.0f, 0.0f)
    }

    fun render(f5: Float) {
        armLpauldron.render(f5)
        armRpauldron.render(f5)
        helm.render(f5)
    }

    fun setRotateAngle(modelRenderer: ModelRenderer, x: Float, y: Float, z: Float) {
        modelRenderer.rotateAngleX = x
        modelRenderer.rotateAngleY = y
        modelRenderer.rotateAngleZ = z
    }
}
