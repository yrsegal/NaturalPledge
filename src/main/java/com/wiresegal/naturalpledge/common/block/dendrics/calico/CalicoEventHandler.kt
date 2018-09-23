package com.wiresegal.naturalpledge.common.block.dendrics.calico

import net.minecraft.util.math.BlockPos
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.world.ExplosionEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import com.wiresegal.naturalpledge.common.core.helper.BAMethodHandles

/**
 * @author WireSegal
 * Created at 6:33 PM on 5/28/16.
 */
object CalicoEventHandler {
    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    val MAXRANGE = 8

    @SubscribeEvent
    fun catchExplosionPre(e: ExplosionEvent.Start) {
        val explosiondampeners = mutableListOf<BlockPos>()

        val atState = e.world.getBlockState(BlockPos(e.explosion.position))
        if (atState.block is IExplosionDampener) return

        for (pos in BlockPos.getAllInBox(BlockPos(e.explosion.position).add(-MAXRANGE, -MAXRANGE, -MAXRANGE),
                BlockPos(e.explosion.position).add(MAXRANGE, MAXRANGE, MAXRANGE))) if (pos.distanceSq(BlockPos(e.explosion.position)) <= 64) {
            val state = e.world.getBlockState(pos)
            if (state.block is IExplosionDampener)
                explosiondampeners.add(pos)
        }
        if (explosiondampeners.size == 0) return

        val dampener = explosiondampeners[e.world.rand.nextInt(explosiondampeners.size)]

        e.world.newExplosion(null, dampener.x.toDouble(), dampener.y.toDouble(), dampener.z.toDouble(), BAMethodHandles.getExplosionSize(e.explosion), false, false)
        e.isCanceled = true
    }

    @SubscribeEvent
    fun catchExplosionAfter(e: ExplosionEvent.Detonate) {
        val atState = e.world.getBlockState(BlockPos(e.explosion.position))
        if (atState.block is IExplosionDampener) {
            e.affectedEntities.clear()
            e.affectedBlocks.clear()
        }
    }
}
