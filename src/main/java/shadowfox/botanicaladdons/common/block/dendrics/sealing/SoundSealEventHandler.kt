package shadowfox.botanicaladdons.common.block.dendrics.sealing

import net.minecraft.client.Minecraft
import net.minecraft.client.audio.ISound
import net.minecraft.client.audio.ITickableSound
import net.minecraft.client.audio.SoundHandler
import net.minecraft.util.math.BlockPos
import net.minecraftforge.client.event.sound.PlaySoundEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import shadowfox.botanicaladdons.api.sapling.ISealingBlock

/**
 * @author WireSegal
 * Created at 4:35 PM on 5/27/16.
 */
object SoundSealEventHandler {
    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    val MAXRANGE = 16

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    fun onPlaySound(e: PlaySoundEvent) {
        if (Minecraft.getMinecraft().theWorld != null && e.resultSound != null) {
            val world = Minecraft.getMinecraft().theWorld
            if (e.resultSound !is ITickableSound) {

                val x = e.resultSound.xPosF.toDouble()
                val y = e.resultSound.yPosF.toDouble()
                val z = e.resultSound.zPosF.toDouble()

                var volumeMultiplier = 1f

                for (pos in BlockPos.getAllInBox(BlockPos(x-MAXRANGE, y-MAXRANGE, z-MAXRANGE), BlockPos(x+MAXRANGE, y+MAXRANGE, z+MAXRANGE))) {
                    val state = world.getBlockState(pos)
                    val block = state.block
                    if (block is ISealingBlock) {
                        val distance = dist(pos, BlockPos(x, y, z))
                        if (distance <= MAXRANGE && block.canSeal(state, world, pos, distance, e)) {
                            volumeMultiplier *= block.getVolumeMultiplier(state, world, pos, distance, e)
                        }
                    }
                }
                if (volumeMultiplier != 1f) {
                    e.resultSound = VolumeModSound(e.resultSound, volumeMultiplier)
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    class VolumeModSound(val sound: ISound, val volumeMult: Float) : ISound {
        override fun canRepeat()                            = sound.canRepeat()
        override fun getRepeatDelay()                       = sound.repeatDelay
        override fun getVolume()                            = sound.volume * volumeMult
        override fun getPitch()                             = sound.pitch
        override fun getXPosF()                             = sound.xPosF
        override fun getYPosF()                             = sound.yPosF
        override fun getZPosF()                             = sound.zPosF
        override fun getAttenuationType()                   = sound.attenuationType
        override fun getSound()                             = sound.sound
        override fun createAccessor(handler: SoundHandler?) = sound.createAccessor(handler)
        override fun getCategory()                          = sound.category
        override fun getSoundLocation()                     = sound.soundLocation
    }

    private fun dist(pos1: BlockPos, pos2: BlockPos): Double {
        return pos1.getDistance(pos2.x, pos2.y, pos2.z)
    }
}
