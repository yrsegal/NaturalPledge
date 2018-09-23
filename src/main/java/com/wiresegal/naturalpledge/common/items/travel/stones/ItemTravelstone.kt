package com.wiresegal.naturalpledge.common.items.travel.stones

import com.teamwizardry.librarianlib.features.base.item.IItemColorProvider
import com.teamwizardry.librarianlib.features.base.item.ItemMod
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent
import com.wiresegal.naturalpledge.common.NaturalPledge
import com.wiresegal.naturalpledge.common.items.ModItems
import java.util.*

/**
 * @author WireSegal
 * Created at 8:31 PM on 5/5/16.
 */
class ItemTravelstone(name: String) : ItemMod(name), IItemColorProvider {

    init {
        MinecraftForge.EVENT_BUS.register(Companion)
        setMaxStackSize(1)
    }

    override val itemColorFunction: ((ItemStack, Int) -> Int)?
        get() = { _, i ->
            if (i == 1)
                NaturalPledge.PROXY.rainbow(0.25f).rgb
            else 0xFFFFFF
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
                            player.stepHeight = 0.60001f // Not 0.5F because that is the default
                        else
                            player.stepHeight = 1f

                    } else {
                        player.stepHeight = 0.6f
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
                if (shouldPlayerHaveStepup(player) && player.isSneaking) {

                    player.motionY += 0.2f
                    player.fallDistance -= 2f
                }
            }

        }

        @SubscribeEvent
        fun playerLoggedOut(e: PlayerEvent.PlayerLoggedOutEvent) {
            val username = e.player.gameProfile.name
            playersWithStepup.remove("$username:false")
            playersWithStepup.remove("$username:true")
        }

        fun playerStr(player: EntityPlayer) = "${player.gameProfile.name}:${player.world.isRemote}"

        fun getTravelStack(player: EntityPlayer): ItemStack {
            val mainStack = player.heldItemMainhand
            val offStack = player.heldItemOffhand

            if (!offStack.isEmpty && mainStack.item == ModItems.travelStone)
                return mainStack
            if (!offStack.isEmpty && offStack.item == ModItems.travelStone)
                return offStack
            return ItemStack.EMPTY
        }

        fun shouldPlayerHaveStepup(player: EntityPlayer): Boolean {
            if (ModItems.fenrisHelm.hasFullSet(player)) return true
            return !getTravelStack(player).isEmpty
        }
    }

}
