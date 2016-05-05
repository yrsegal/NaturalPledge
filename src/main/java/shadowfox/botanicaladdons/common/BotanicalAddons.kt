package shadowfox.botanicaladdons.common

import net.minecraft.launchwrapper.Launch
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
import net.minecraftforge.fml.relauncher.Side
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.common.core.CommonProxy
import shadowfox.botanicaladdons.common.network.PlayerItemMessage

@Mod(modid = LibMisc.MOD_ID, name = LibMisc.MOD_NAME, version = LibMisc.VERSION, dependencies = LibMisc.DEPENDENCIES)
class BotanicalAddons {
    companion object {
        @Mod.Instance(LibMisc.MOD_ID)
        lateinit var instance: BotanicalAddons

        lateinit var network: SimpleNetworkWrapper

        @SidedProxy(serverSide = LibMisc.PROXY_COMMON,
                clientSide = LibMisc.PROXY_CLIENT)
        lateinit var proxy: CommonProxy

        var isDevEnv = false
    }

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        isDevEnv = Launch.blackboard["fml.deobfuscatedEnvironment"] as Boolean

        network = SimpleNetworkWrapper(LibMisc.MOD_ID)
        network.registerMessage(PlayerItemMessage.PlayerItemMessageHandler::class.java, PlayerItemMessage::class.java, 0, Side.SERVER)

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
