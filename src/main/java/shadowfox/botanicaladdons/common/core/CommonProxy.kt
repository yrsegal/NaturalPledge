package shadowfox.botanicaladdons.common.core

import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import shadowfox.botanicaladdons.common.achievements.ModAchievements
import shadowfox.botanicaladdons.common.block.ModBlocks
import shadowfox.botanicaladdons.common.crafting.ModRecipes
import shadowfox.botanicaladdons.common.enchantment.ModEnchantments
import shadowfox.botanicaladdons.common.entity.ModEntities
import shadowfox.botanicaladdons.common.items.ModItems
import shadowfox.botanicaladdons.common.items.bauble.faith.ModSpells
import shadowfox.botanicaladdons.common.lexicon.LexiconEntries
import shadowfox.botanicaladdons.common.potions.ModPotions
import shadowfox.botanicaladdons.common.potions.brew.ModBrews
import vazkii.botania.common.core.helper.Vector3
import java.awt.Color

/**
 * @author WireSegal
 * Created at 5:07 PM on 4/12/16.
 */
open class CommonProxy {
    open fun pre(e: FMLPreInitializationEvent) {
        ModItems
        ModBlocks
        ModAchievements
        ModPotions
        ModBrews
        ModEnchantments
        ModEntities
    }

    open fun init(e: FMLInitializationEvent) {
        ModSpells
        ModRecipes
        LexiconEntries
    }

    open fun post(e: FMLPostInitializationEvent) {
        // NO-OP
    }

    fun particleEmission(world: World, pos: Vector3, color: Int) {
        particleEmission(world, pos, color, 0.3F)
    }

    open fun particleEmission(world: World, pos: Vector3, color: Int, probability: Float) {
        // NO-OP
    }

    open fun particleStream(world: World, from: Vector3, to: Vector3, color: Int) {
        //NO-OP
    }

    open fun pulseColor(color: Color) = Color(0xFFFFFF)

    fun rainbow() = rainbow(1f)
    open fun rainbow(saturation: Float) = Color(0xFFFFFF)
    fun rainbow2() = rainbow2(0.005f, 1f)
    open fun rainbow2(speed: Float, saturation: Float) = Color(0xFFFFFF)
    fun rainbow(pos: BlockPos) = rainbow(pos, 1f)
    open fun rainbow(pos: BlockPos, saturation: Float) = Color(0xFFFFFF)

    fun wireFrameRainbow() = wireFrameRainbow(0.6f)
    open fun wireFrameRainbow(saturation: Float) = Color(0xFFFFFF)

    open fun playerHasMonocle(): Boolean {
        return false
    }
}
