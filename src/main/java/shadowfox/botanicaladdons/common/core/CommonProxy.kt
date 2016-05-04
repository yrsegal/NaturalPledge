package shadowfox.botanicaladdons.common.core

import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import shadowfox.botanicaladdons.common.achievements.ModAchievements
import shadowfox.botanicaladdons.common.block.ModBlocks
import shadowfox.botanicaladdons.common.crafting.ModRecipes
import shadowfox.botanicaladdons.common.items.ModItems
import shadowfox.botanicaladdons.common.items.base.IPreventBreakInCreative
import shadowfox.botanicaladdons.common.lexicon.LexiconEntries
import shadowfox.botanicaladdons.common.potions.ModPotions
import vazkii.botania.common.Botania
import java.awt.Color

/**
 * @author WireSegal
 * Created at 5:07 PM on 4/12/16.
 */
open class CommonProxy {
    open fun pre(e: FMLPreInitializationEvent) {
        ModItems
        IPreventBreakInCreative.register()
        ModBlocks
        ModAchievements
        ModPotions
    }

    open fun init(e: FMLInitializationEvent) {
        ModRecipes
        LexiconEntries
    }

    open fun post(e: FMLPostInitializationEvent) {

    }

    fun rainbow() = rainbow(1f)
    open fun rainbow(saturation: Float) = Color(0xFFFFFF)
    fun rainbow(pos: BlockPos) = rainbow(pos, 1f)
    open fun rainbow(pos: BlockPos, saturation: Float) = Color(0xFFFFFF)
}
