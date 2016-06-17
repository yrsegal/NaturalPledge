package shadowfox.botanicaladdons.common

import net.minecraft.launchwrapper.Launch
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
import net.minecraftforge.fml.relauncher.Side
import org.apache.logging.log4j.LogManager
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.common.core.CommonProxy
import shadowfox.botanicaladdons.common.network.PlayerItemMessage

@Mod(modid = LibMisc.MOD_ID, name = LibMisc.MOD_NAME, version = LibMisc.VERSION, dependencies = LibMisc.DEPENDENCIES)
class BotanicalAddons {
    companion object {
        @Mod.Instance(LibMisc.MOD_ID)
        lateinit var INSTANCE: BotanicalAddons

        @SidedProxy(serverSide = LibMisc.PROXY_COMMON,
                clientSide = LibMisc.PROXY_CLIENT)
        lateinit var PROXY: CommonProxy

        val LOGGER = LogManager.getLogger(LibMisc.MOD_ID)

        val DEV_ENVIRONMENT: Boolean by lazy {
            Launch.blackboard["fml.deobfuscatedEnvironment"] as Boolean
        }

        val NETWORK: SimpleNetworkWrapper by lazy {
            SimpleNetworkWrapper(LibMisc.MOD_ID)
        }
    }

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        NETWORK.registerMessage(PlayerItemMessage.PlayerItemMessageHandler::class.java, PlayerItemMessage::class.java, 0, Side.SERVER)

        PROXY.pre(event)
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        PROXY.init(event)
    }

    @Mod.EventHandler
    fun post(event: FMLPostInitializationEvent) {
        PROXY.post(event)
    }
}
