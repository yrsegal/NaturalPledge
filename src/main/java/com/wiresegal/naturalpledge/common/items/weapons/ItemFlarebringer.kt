package com.wiresegal.naturalpledge.common.items.weapons

import com.teamwizardry.librarianlib.features.base.item.IGlowingItem
import com.teamwizardry.librarianlib.features.network.PacketHandler
import net.minecraft.client.renderer.block.model.IBakedModel
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import com.wiresegal.naturalpledge.common.NaturalPledge
import com.wiresegal.naturalpledge.common.block.trap.BlockBaseTrap.Companion.B
import com.wiresegal.naturalpledge.common.block.trap.BlockBaseTrap.Companion.G
import com.wiresegal.naturalpledge.common.block.trap.BlockBaseTrap.Companion.R
import com.wiresegal.naturalpledge.common.items.base.ItemBaseSword
import com.wiresegal.naturalpledge.common.items.bauble.faith.ItemRagnarokPendant
import com.wiresegal.naturalpledge.common.items.bauble.faith.Spells
import com.wiresegal.naturalpledge.common.network.AttackMessage


/**
 * @author WireSegal
 * Created at 5:45 PM on 4/3/17.
 */
class ItemFlarebringer(name: String, material: Item.ToolMaterial) : ItemBaseSword(name, material), IGlowingItem {
    companion object {
        val RANGE = 16.0
    }

    @SideOnly(Side.CLIENT)
    override fun transformToGlow(itemStack: ItemStack, model: IBakedModel) = IGlowingItem.Helper.wrapperBake(model, false, 1)
    @SideOnly(Side.CLIENT)
    override fun shouldDisableLightingForGlow(itemStack: ItemStack, model: IBakedModel) = true

    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent
    fun swingTheFlarebringer(e: PlayerInteractEvent.LeftClickEmpty) {
        if (e.entityPlayer.heldItemMainhand.item != this) return
        val target = Spells.Helper.getEntityLookedAt(e.entityPlayer, RANGE) ?: return
        PacketHandler.NETWORK.sendToServer(AttackMessage(target.entityId))
    }

    override fun onUpdate(stack: ItemStack, world: World, player: Entity, slot: Int, selected: Boolean) {
        if (selected && world.isRemote && player is EntityPlayer) {
            if (player.heldItemMainhand.item != this) return
            val target = Spells.Helper.getEntityLookedAt(player, RANGE) ?: return
            NaturalPledge.PROXY.particleRing(target.posX - 0.5, target.posY + 0.5, target.posZ - 0.5, 1.0, R, G, B, 0F, 0.2F, 0.1F)
        }
    }

    override fun getSubItems(tab: CreativeTabs, subItems: NonNullList<ItemStack>) {
        if (ItemRagnarokPendant.hasAwakenedRagnarok())
            super.getSubItems(tab, subItems)
    }
}
