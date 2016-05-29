package shadowfox.botanicaladdons.common.block.dendrics.thunder

import net.minecraft.block.material.Material
import net.minecraft.entity.Entity
import net.minecraft.entity.effect.EntityLightningBolt
import net.minecraft.entity.effect.EntityWeatherEffect
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.init.SoundEvents
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.EnumDifficulty
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import java.util.*

/**
 * @author WireSegal
 * Created at 6:33 PM on 5/28/16.
 */
object ThunderEventHandler {
    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    val MAXRANGE = 8
    val FIRERANGE = 3

    @SubscribeEvent
    fun catchWorldTick(e: TickEvent.WorldTickEvent) {
        val toRemove = mutableListOf<Entity>()
        val toAdd = mutableListOf<Entity>()
        for (effect in e.world.weatherEffects) if (effect is EntityLightningBolt) {

            if (effect.ticksExisted > 5) continue

            val thunderabsorbers = mutableListOf<BlockPos>()
            for (pos in BlockPos.getAllInBox(effect.position.add(-MAXRANGE, -MAXRANGE, -MAXRANGE), effect.position.add(MAXRANGE, MAXRANGE, MAXRANGE))) {
                val state = e.world.getBlockState(pos)
                if (state.block is IThunderAbsorber)
                    thunderabsorbers.add(pos)
            }
            if (thunderabsorbers.size == 0) continue

            val absorber = thunderabsorbers[e.world.rand.nextInt(thunderabsorbers.size)]
            toAdd.add(FakeLightning(e.world, absorber))
            toRemove.add(effect)

            for (pos in BlockPos.getAllInBox(effect.position.add(-FIRERANGE, -FIRERANGE, -FIRERANGE), effect.position.add(FIRERANGE, FIRERANGE, FIRERANGE))) {
                if (e.world.getBlockState(pos).block == Blocks.FIRE)
                    e.world.setBlockState(pos, Blocks.AIR.defaultState)
            }
        }

        for (effect in toAdd) {
            println("adding: $effect")
            e.world.addWeatherEffect(effect)
        }

        for (effect in toRemove) {
            println("removing: $effect")
            e.world.removeEntity(effect)
        }
    }

    class FakeLightning(world: World, location: BlockPos) : EntityWeatherEffect(world) {
        private var lightningState: Int = 0
        var boltVertex: Long = 0
        private var boltLivingTime: Int = 0

        init {
            val x = location.x
            val y = location.y
            val z = location.z
            this.setLocationAndAngles(x.toDouble(), y.toDouble(), z.toDouble(), 0.0f, 0.0f)
            this.lightningState = 2
            this.boltVertex = this.rand.nextLong()
            this.boltLivingTime = this.rand.nextInt(3) + 1
        }

        override fun getSoundCategory(): SoundCategory {
            return SoundCategory.WEATHER
        }

        override fun onUpdate() {
            super.onUpdate()

            if (this.lightningState == 2) {
                this.worldObj.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.WEATHER, 10000.0f, 0.8f + this.rand.nextFloat() * 0.2f)
                this.worldObj.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_LIGHTNING_IMPACT, SoundCategory.WEATHER, 2.0f, 0.5f + this.rand.nextFloat() * 0.2f)
            }

            --this.lightningState

            if (this.lightningState < 0) {
                if (this.boltLivingTime == 0) {
                    this.setDead()
                } else if (this.lightningState < -this.rand.nextInt(10)) {
                    --this.boltLivingTime
                    this.lightningState = 1
                }
            }

            if (this.lightningState >= 0) {
                if (this.worldObj.isRemote) {
                    this.worldObj.lastLightningBolt = 2
                }
            }
        }

        override fun entityInit() {
            //NO-OP
        }

        override fun readEntityFromNBT(compound: NBTTagCompound) {
            //NO-OP
        }

        override fun writeEntityToNBT(compound: NBTTagCompound) {
            //NO-OP
        }
    }
}
