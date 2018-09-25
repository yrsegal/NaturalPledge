package com.wiresegal.naturalpledge.common.block.dendrics.thunder

import net.minecraft.entity.Entity
import net.minecraft.entity.effect.EntityLightningBolt
import net.minecraft.init.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.EntityStruckByLightningEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import com.wiresegal.naturalpledge.common.core.helper.NPMethodHandles

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
        val toAdd = mutableListOf<BlockPos>()
        for (effect in e.world.weatherEffects) if (effect is EntityLightningBolt && !NPMethodHandles.getEffectOnly(effect)) {

            if (effect.ticksExisted > 5) continue

            val thunderabsorbers = mutableListOf<BlockPos>()
            for (pos in BlockPos.getAllInBox(effect.position.add(-MAXRANGE, -MAXRANGE, -MAXRANGE),
                    effect.position.add(MAXRANGE, MAXRANGE, MAXRANGE))) if (pos.distanceSq(effect.position) <= 64) {
                val state = e.world.getBlockState(pos)
                if (state.block is IThunderAbsorber)
                    thunderabsorbers.add(pos)
            }
            if (thunderabsorbers.size == 0) continue

            val absorber = thunderabsorbers[e.world.rand.nextInt(thunderabsorbers.size)]
            toAdd.add(absorber)
            toRemove.add(effect)

            BlockPos.getAllInBoxMutable(effect.position.add(-FIRERANGE, -FIRERANGE, -FIRERANGE), effect.position.add(FIRERANGE, FIRERANGE, FIRERANGE))
                    .filter { e.world.getBlockState(it).block == Blocks.FIRE }
                    .forEach { e.world.setBlockState(it, Blocks.AIR.defaultState) }
        }

        for (effect in toAdd) e.world.addWeatherEffect(EntityLightningBolt(e.world, effect.x.toDouble(), effect.y.toDouble(), effect.z.toDouble(), true))

        for (effect in toRemove) {
            e.world.removeEntityDangerously(effect)
            e.world.weatherEffects.remove(effect)
        }
    }

    @SubscribeEvent
    fun catchPlayerStruck(e: EntityStruckByLightningEvent) {
        for (pos in BlockPos.getAllInBox(e.lightning.position.add(-MAXRANGE, -MAXRANGE, -MAXRANGE), e.lightning.position.add(MAXRANGE, MAXRANGE, MAXRANGE))) {
            val state = e.lightning.world.getBlockState(pos)
            if (state.block is IThunderAbsorber) {
                e.isCanceled = true
                return
            }
        }
    }
}
