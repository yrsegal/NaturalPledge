package shadowfox.botanicaladdons.common

import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import shadowfox.botanicaladdons.common.core.CommonProxy
import shadowfox.botanicaladdons.common.lib.LibMisc

@Mod(modid = LibMisc.MOD_ID, name = LibMisc.MOD_NAME, version = LibMisc.VERSION, dependencies = LibMisc.DEPENDENCIES)
class BotanicalAddons {
    companion object {
        @Mod.Instance(LibMisc.MOD_ID)
        lateinit var instance: BotanicalAddons

        @SidedProxy(serverSide = LibMisc.PROXY_COMMON,
                clientSide = LibMisc.PROXY_CLIENT)
        lateinit var proxy: CommonProxy
    }

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        proxy.pre(event)
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        proxy.init(event)
    }

    @Mod.EventHandler
    fun post(event: FMLPostInitializationEvent) {
        proxy.post(event)
    }
}
