package com.wiresegal.naturalpledge.common.items.weapons

import com.teamwizardry.librarianlib.features.base.item.IGlowingItem
import com.teamwizardry.librarianlib.features.network.PacketHandler
import com.teamwizardry.librarianlib.features.utilities.RaycastUtils
import com.wiresegal.naturalpledge.common.items.base.ItemBaseSword
import com.wiresegal.naturalpledge.common.network.AttackMessage
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.block.model.IBakedModel
import net.minecraft.entity.Entity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly


/**
 * @author WireSegal
 * Created at 5:45 PM on 4/3/17.
 */
class ItemFlarebringer(name: String, material: Item.ToolMaterial) : ItemBaseSword(name, material), IGlowingItem {
    companion object {
        val RANGE = 16.0

        init {
            MinecraftForge.EVENT_BUS.register(this)
        }

        private var wasGlowing = false
        private var entityTarget: Entity? = null

        @SubscribeEvent
        @SideOnly(Side.CLIENT)
        fun startRenderTick(e: TickEvent.RenderTickEvent) {
            if (e.phase == TickEvent.Phase.START) {

                val player = Minecraft.getMinecraft().player

                entityTarget = if (player == null || player.heldItemMainhand.item !is ItemFlarebringer)
                    null
                else
                    RaycastUtils.getEntityLookedAt(player, RANGE)

                entityTarget?.let {
                    wasGlowing = it.isGlowing
                    it.isGlowing = true
                }
            } else if (e.phase == TickEvent.Phase.END) {
                entityTarget?.let {
                    it.isGlowing = wasGlowing
                }
            }
        }

        @SubscribeEvent
        fun swingTheFlarebringer(e: PlayerInteractEvent.LeftClickEmpty) {
            if (e.entityPlayer.heldItemMainhand.item !is ItemFlarebringer) return
            val target = RaycastUtils.getEntityLookedAt(e.entityPlayer, RANGE) ?: return
            PacketHandler.NETWORK.sendToServer(AttackMessage(target.entityId))
        }
    }

    @SideOnly(Side.CLIENT)
    override fun transformToGlow(itemStack: ItemStack, model: IBakedModel) = IGlowingItem.Helper.wrapperBake(model, false, 1)
    @SideOnly(Side.CLIENT)
    override fun shouldDisableLightingForGlow(itemStack: ItemStack, model: IBakedModel) = true

    init {
        MinecraftForge.EVENT_BUS.register(this)
    }
}
