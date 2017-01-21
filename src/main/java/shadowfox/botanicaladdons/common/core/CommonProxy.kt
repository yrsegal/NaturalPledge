package shadowfox.botanicaladdons.common.core

import com.teamwizardry.librarianlib.common.network.PacketHandler
import com.teamwizardry.librarianlib.common.util.EasyConfigHandler
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import shadowfox.botanicaladdons.common.achievements.ModAchievements
import shadowfox.botanicaladdons.common.block.ModBlocks
import shadowfox.botanicaladdons.common.core.tab.ModTab
import shadowfox.botanicaladdons.common.crafting.ModRecipes
import shadowfox.botanicaladdons.common.enchantment.ModEnchantments
import shadowfox.botanicaladdons.common.entity.ModEntities
import shadowfox.botanicaladdons.common.integration.tinkers.TinkersProxy
import shadowfox.botanicaladdons.common.items.ModItems
import shadowfox.botanicaladdons.common.items.bauble.faith.ModSpells
import shadowfox.botanicaladdons.common.lexicon.LexiconEntries
import shadowfox.botanicaladdons.common.network.SpamlessMessagePacket
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
        ModTab
        ModItems
        ModBlocks
        ModAchievements
        ModPotions
        ModBrews
        ModEnchantments
        ModEntities
        EasyConfigHandler.init()
    }

    open fun init(e: FMLInitializationEvent) {
        ModSpells
        ModRecipes
        LexiconEntries
        TinkersProxy.loadTinkers()
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

    open fun particleRing(x: Double, y: Double, z: Double, range: Double, r: Float, g: Float, b: Float) {
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

    open fun getClientPlayer(): EntityPlayer {
        throw UnsupportedOperationException("Server side doesn't have a client side player!")
    }

    // From ShadowMC
    open fun sendSpamlessMessage(player: EntityPlayer, msg: ITextComponent, id: Int) {
        if (player !is EntityPlayerMP) return
        PacketHandler.NETWORK.sendTo(SpamlessMessagePacket(msg, id), player)
    }

}
