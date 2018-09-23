package com.wiresegal.naturalpledge.client.render.entity

import net.minecraft.client.renderer.entity.RenderArrow
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.util.ResourceLocation
import com.wiresegal.naturalpledge.api.lib.LibMisc
import com.wiresegal.naturalpledge.common.entity.EntitySealedArrow

/**
 * @author WireSegal
 * Created at 9:26 PM on 5/25/16.
 */
class RenderSealedArrow(renderManager: RenderManager) : RenderArrow<EntitySealedArrow>(renderManager) {

    companion object {
        val TEX = ResourceLocation(LibMisc.MOD_ID, "textures/model/seal_arrow.png")
    }

    override fun getEntityTexture(entity: EntitySealedArrow?) = TEX
}
