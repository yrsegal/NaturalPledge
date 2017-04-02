package shadowfox.botanicaladdons.common.items.base

import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.event.world.BlockEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

/**
 * @author WireSegal
 * Created at 6:28 PM on 4/23/16.
 */
interface IPreventBreakInCreative {
    companion object {
        init {
            MinecraftForge.EVENT_BUS.register(this)
        }

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        fun harvest(e: PlayerEvent.HarvestCheck) {
            if (e.entityPlayer.isCreative && e.entityPlayer.heldItemMainhand.item is IPreventBreakInCreative) {
                e.isCanceled = true
            }
        }

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        fun speed(e: PlayerEvent.BreakSpeed) {
            if (e.entityPlayer.isCreative && e.entityPlayer.heldItemMainhand.item is IPreventBreakInCreative) {
                e.isCanceled = true
            }
        }

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        fun click(e: PlayerInteractEvent.LeftClickBlock) {
            if (e.entityPlayer.isCreative && e.itemStack.item is IPreventBreakInCreative) {
                e.isCanceled = true
            }
        }

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        fun blockBreak(e: BlockEvent.BreakEvent) {
            if (e.player.isCreative && e.player.heldItemMainhand.item is IPreventBreakInCreative) {
                e.isCanceled = true
            }
        }
    }
}
