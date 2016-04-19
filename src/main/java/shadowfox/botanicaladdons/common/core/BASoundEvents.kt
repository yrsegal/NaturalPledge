package shadowfox.botanicaladdons.common.core

import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundEvent
import net.minecraftforge.fml.common.registry.GameRegistry
import shadowfox.botanicaladdons.common.lib.LibMisc

/**
 * @author WireSegal
 * Created at 1:18 PM on 4/19/16.
 */
object BASoundEvents {

    val woosh: SoundEvent

    init {
        var loc = ResourceLocation(LibMisc.MOD_ID, "woosh")
        woosh = SoundEvent(loc)
        GameRegistry.register(woosh, loc)
    }
}
