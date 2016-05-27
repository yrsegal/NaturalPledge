package shadowfox.botanicaladdons.common.entity

import net.minecraftforge.fml.common.registry.EntityRegistry
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.common.BotanicalAddons

/**
 * @author WireSegal
 * Created at 9:36 PM on 5/25/16.
 */
object ModEntities {

    var id = 0

    init {
        EntityRegistry.registerModEntity(EntitySealedArrow::class.java, "${LibMisc.MOD_ID}:sealArrow", id++, BotanicalAddons.instance, 256, 2, true)
    }
}
