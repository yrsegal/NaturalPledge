package shadowfox.botanicaladdons.common.events

import net.minecraft.item.crafting.IRecipe
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import shadowfox.botanicaladdons.common.crafting.ModRecipes

object Registry {
    var toRegister = ArrayList<Any>()

    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent
    fun register(e: RegistryEvent.Register<IRecipe>) {
        ModRecipes
        for (o: Any in toRegister)
            if (o is IRecipe)
                e.registry.register(o)
    }
}