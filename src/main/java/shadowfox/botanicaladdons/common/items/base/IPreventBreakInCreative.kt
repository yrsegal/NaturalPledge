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
        fun onTryToGetAbilityToCheckIfCanGetAbilityToBreakBlock(e: PlayerEvent.HarvestCheck) { // I was tired when I wrote this name
            if (e.entityPlayer.isCreative && e.entityPlayer.heldItemMainhand?.item is IPreventBreakInCreative) {
                e.isCanceled = true
            }
        }

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        fun onTryToGetAbilityToBreakBlock(e: PlayerEvent.BreakSpeed) {
            if (e.entityPlayer.isCreative && e.entityPlayer.heldItemMainhand?.item is IPreventBreakInCreative) {
                e.isCanceled = true
            }
        }

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        fun onTryToBreakBlock(e: PlayerInteractEvent.LeftClickBlock) {
            if (e.entityPlayer.isCreative && e.itemStack?.item is IPreventBreakInCreative) {
                e.isCanceled = true
            }
        }

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        fun onBlockActuallyBreak(e: BlockEvent.BreakEvent) {
            if (e.player.isCreative && e.player.heldItemMainhand?.item is IPreventBreakInCreative) {
                e.isCanceled = true
            }
        }
    }
}
