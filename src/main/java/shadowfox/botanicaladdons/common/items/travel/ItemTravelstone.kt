package shadowfox.botanicaladdons.common.items.travel

import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import shadowfox.botanicaladdons.client.core.ModelHandler
import shadowfox.botanicaladdons.common.BotanicalAddons
import shadowfox.botanicaladdons.common.items.ModItems
import shadowfox.botanicaladdons.common.items.base.ItemMod
import java.util.*

/**
 * @author WireSegal
 * Created at 8:31 PM on 5/5/16.
 */
class ItemTravelstone(name: String) : ItemMod(name), ModelHandler.IItemColorProvider {

    init {
        MinecraftForge.EVENT_BUS.register(Companion)
        setMaxStackSize(1)
    }

    @SideOnly(Side.CLIENT)
    override fun getItemColor(): IItemColor? {
        return IItemColor { itemStack, i ->
            if (i == 1)
                BotanicalAddons.PROXY.rainbow(0.25f).rgb
            else 0xFFFFFF
        }
    }

    companion object {
        val playersWithStepup: MutableList<String> = ArrayList()

        @SubscribeEvent
        fun updatePlayerStepStatus(e: LivingEvent.LivingUpdateEvent) {
            if (e.entityLiving is EntityPlayer) {
                val player = e.entityLiving as EntityPlayer
                val s = playerStr(player)
                if (playersWithStepup.contains(s)) {
                    if (shouldPlayerHaveStepup(player)) {
                        if (player.isSneaking)
                            player.stepHeight = 0.50001f // Not 0.5F because that is the default
                        else
                            player.stepHeight = 1f

                    } else {
                        player.stepHeight = 0.5f
                        playersWithStepup.remove(s)
                    }
                } else if (shouldPlayerHaveStepup(player)) {
                    playersWithStepup.add(s)
                    player.stepHeight = 1f
                }
            }
        }

        @SubscribeEvent(priority = EventPriority.LOW)
        fun onPlayerJump(e: LivingEvent.LivingJumpEvent) {
            if (e.entityLiving is EntityPlayer) {
                val player = e.entityLiving as EntityPlayer
                getTravelStack(player) ?: return

                player.motionY += 0.2f
                player.fallDistance -= 2f
            }

        }

        @SubscribeEvent
        fun playerLoggedOut(e: PlayerEvent.PlayerLoggedOutEvent) {
            val username = e.player.gameProfile.name
            playersWithStepup.remove("$username:false")
            playersWithStepup.remove("$username:true")
        }

        fun playerStr(player: EntityPlayer) = "${player.gameProfile.name}:${player.worldObj.isRemote}"

        fun getTravelStack(player: EntityPlayer): ItemStack? {
            val mainStack = player.heldItemMainhand
            val offStack = player.heldItemOffhand

            if (mainStack != null && mainStack.item == ModItems.travelStone)
                return mainStack
            if (offStack != null && offStack.item == ModItems.travelStone)
                return offStack
            return null
        }

        fun shouldPlayerHaveStepup(player: EntityPlayer): Boolean {
            return getTravelStack(player) != null
        }
    }

}
