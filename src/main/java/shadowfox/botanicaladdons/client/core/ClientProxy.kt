package shadowfox.botanicaladdons.client.core

import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import shadowfox.botanicaladdons.common.core.CommonProxy

/**
 * @author WireSegal
 * Created at 8:37 AM on 3/20/16.
 */
class ClientProxy : CommonProxy() {
    override fun pre(e: FMLPreInitializationEvent) {
        super.pre(e)
        ModelHandler.preInit()
    }

    override fun init(e: FMLInitializationEvent) {
        super.init(e)
        ModelHandler.init()
    }
}
