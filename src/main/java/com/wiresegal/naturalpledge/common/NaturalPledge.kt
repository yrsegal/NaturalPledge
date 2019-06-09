package com.wiresegal.naturalpledge.common

import com.wiresegal.naturalpledge.api.lib.LibMisc
import com.wiresegal.naturalpledge.common.core.CommonProxy
import com.wiresegal.naturalpledge.common.crafting.ModRecipes
import net.minecraft.item.Item
import net.minecraft.launchwrapper.Launch
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.apache.logging.log4j.LogManager

@Mod.EventBusSubscriber
@Mod(modid = LibMisc.MOD_ID, name = LibMisc.MOD_NAME, version = LibMisc.VERSION, dependencies = LibMisc.DEPENDENCIES)
class NaturalPledge {
    companion object {
        @Mod.Instance(LibMisc.MOD_ID)
        lateinit var INSTANCE: NaturalPledge

        @SidedProxy(serverSide = LibMisc.PROXY_COMMON,
                clientSide = LibMisc.PROXY_CLIENT)
        lateinit var PROXY: CommonProxy

        val LOGGER = LogManager.getLogger(LibMisc.MOD_ID)

        val DEV_ENVIRONMENT: Boolean by lazy {
            Launch.blackboard["fml.deobfuscatedEnvironment"] as Boolean
        }

//        val TINKERS_LOADED: Boolean by lazy {
//            Loader.isModLoaded("tconstruct")
//        }

        @JvmStatic
        @SubscribeEvent(priority = EventPriority.LOWEST)
        fun onItemsLoad(e: RegistryEvent.Register<Item>) {
            ModRecipes
        }
    }

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
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
