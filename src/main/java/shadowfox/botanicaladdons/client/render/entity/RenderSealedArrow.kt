package shadowfox.botanicaladdons.client.render.entity

import net.minecraft.client.renderer.entity.RenderArrow
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.util.ResourceLocation
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.common.entity.EntitySealedArrow

/**
 * @author WireSegal
 * Created at 9:26 PM on 5/25/16.
 */
class RenderSealedArrow(renderManager: RenderManager) : RenderArrow<EntitySealedArrow>(renderManager) {

    companion object {
        val TEX = ResourceLocation(LibMisc.MOD_ID, "textures/model/sealArrow.png")
    }

    override fun getEntityTexture(entity: EntitySealedArrow?) = TEX
}
