package shadowfox.botanicaladdons.common.core

import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import shadowfox.botanicaladdons.common.crafting.ModRecipes
import shadowfox.botanicaladdons.common.items.ModItems
import shadowfox.botanicaladdons.common.lexicon.LexiconEntries
import shadowfox.botanicaladdons.common.potions.ModPotions

/**
 * @author WireSegal
 * Created at 5:07 PM on 4/12/16.
 */
open class CommonProxy {
    open fun pre(e: FMLPreInitializationEvent) {
        ModItems
        ModPotions
    }

    open fun init(e: FMLInitializationEvent) {
        ModRecipes
        LexiconEntries
    }

    open fun post(e: FMLPostInitializationEvent) {

    }
}
