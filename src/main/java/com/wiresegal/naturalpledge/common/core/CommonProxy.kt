package com.wiresegal.naturalpledge.common.core

//import com.wiresegal.naturalpledge.common.integration.tinkers.TinkersProxy
import com.teamwizardry.librarianlib.features.config.EasyConfigHandler
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import com.wiresegal.naturalpledge.api.lib.LibMisc
import com.wiresegal.naturalpledge.common.block.ModBlocks
import com.wiresegal.naturalpledge.common.core.tab.ModTab
import com.wiresegal.naturalpledge.common.enchantment.ModEnchantments
import com.wiresegal.naturalpledge.common.entity.ModEntities
import com.wiresegal.naturalpledge.common.items.ModItems
import com.wiresegal.naturalpledge.common.items.bauble.faith.ModSpells
import com.wiresegal.naturalpledge.common.lexicon.LexiconEntries
import com.wiresegal.naturalpledge.common.potions.ModPotions
import com.wiresegal.naturalpledge.common.potions.brew.ModBrews
import vazkii.botania.common.core.helper.Vector3
import java.awt.Color


/**
 * @author WireSegal
 * Created at 5:07 PM on 4/12/16.
 */
open class CommonProxy {

    open fun pre(e: FMLPreInitializationEvent) {
        ModTab
        ModItems
        ModBlocks
        ModPotions
        ModBrews
        ModEnchantments
        ModEntities
        EasyConfigHandler.init()
    }

    open fun init(e: FMLInitializationEvent) {
        ModSpells
        LexiconEntries
        ModItems.OreDict()
        ModBlocks.OreDict()
//        TinkersProxy.loadTinkers()
    }

    open fun post(e: FMLPostInitializationEvent) {
        // NO-OP
    }

    fun particleEmission(pos: Vector3, color: Int) {
        particleEmission(pos, color, 0.3F)
    }

    open fun particleEmission(pos: Vector3, color: Int, probability: Float) {
        // NO-OP
    }

    open fun particleStream(from: Vector3, to: Vector3, color: Int) {
        //NO-OP
    }

    open fun wispLine(start: Vector3, line: Vector3, color: Int, stepsPerBlock: Double, time: Int) {
        //NO-OP
    }

    fun particleRing(x: Double, y: Double, z: Double, range: Double, r: Float, g: Float, b: Float) {
        particleRing(x, y, z, range, r, g, b, 0.15F, 0.35F, 0.2F)
    }

    open fun particleRing(x: Double, y: Double, z: Double, range: Double, r: Float, g: Float, b: Float, motion: Float, verticalMotion: Float, size: Float) {
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

    fun getAdvancement(s: String): ResourceLocation {
        return ResourceLocation(LibMisc.MOD_ID, "naturalpledge/" + s)
    }

    open fun hasAdvancement(player: EntityPlayer, s: String): Boolean {
        if (player is EntityPlayerMP) {
            val advancement = player.serverWorld.advancementManager.getAdvancement(getAdvancement(s))
            if (advancement != null)
                return player.advancements.getProgress(advancement).isDone
        }
        return false
    }
}

