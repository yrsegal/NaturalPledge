package com.wiresegal.naturalpledge.common.entity

import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.EntityRegistry
import com.wiresegal.naturalpledge.api.lib.LibMisc
import com.wiresegal.naturalpledge.common.NaturalPledge

/**
 * @author WireSegal
 * Created at 9:36 PM on 5/25/16.
 */
object ModEntities {

    var id = 0

    init {
        EntityRegistry.registerModEntity(ResourceLocation("${LibMisc.MOD_ID}:sealArrow"), EntitySealedArrow::class.java, "${LibMisc.MOD_ID}:sealArrow", id++, NaturalPledge.INSTANCE, 256, 2, true)
    }
}
