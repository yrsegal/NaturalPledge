package shadowfox.botanicaladdons.common.items.base

import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.world.BlockEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

/**
 * @author WireSegal
 * Created at 6:28 PM on 4/23/16.
 */
interface IPreventBreakInCreative {
    companion object {
        private object EventHandler {

            var registered = false

            @SubscribeEvent(priority = EventPriority.HIGHEST)
            fun onBlockBreak(e: BlockEvent.BreakEvent) {
                if (e.player.isCreative && e.player.heldItemMainhand != null && e.player.heldItemMainhand!!.item is IPreventBreakInCreative) {
                    e.isCanceled = true
                }
            }
        }

        fun register() {
            if (!EventHandler.registered) {
                MinecraftForge.EVENT_BUS.register(EventHandler)
                EventHandler.registered = true
            }
        }
    }
}
