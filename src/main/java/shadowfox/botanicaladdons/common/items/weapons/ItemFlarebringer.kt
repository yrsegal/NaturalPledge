package shadowfox.botanicaladdons.common.items.weapons

import com.teamwizardry.librarianlib.LibrarianLib
import com.teamwizardry.librarianlib.common.network.PacketHandler
import net.minecraft.client.Minecraft
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
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import shadowfox.botanicaladdons.common.BotanicalAddons
import shadowfox.botanicaladdons.common.block.trap.BlockBaseTrap.Companion.R
import shadowfox.botanicaladdons.common.block.trap.BlockBaseTrap.Companion.G
import shadowfox.botanicaladdons.common.block.trap.BlockBaseTrap.Companion.B
import shadowfox.botanicaladdons.common.items.base.ItemBaseSword
import shadowfox.botanicaladdons.common.items.bauble.faith.ItemRagnarokPendant
import shadowfox.botanicaladdons.common.items.bauble.faith.Spells
import shadowfox.botanicaladdons.common.network.AttackMessage
import sun.audio.AudioPlayer.player


/**
 * @author WireSegal
 * Created at 5:45 PM on 4/3/17.
 */
class ItemFlarebringer(name: String, material: Item.ToolMaterial) : ItemBaseSword(name, material) {
    companion object {
        val RANGE = 16.0
    }

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
            BotanicalAddons.PROXY.particleRing(target.posX - 0.5, target.posY + 0.5, target.posZ - 0.5, 1.0, R, G, B, 0F, 0.2F, 0.1F)
        }
    }

    override fun getSubItems(itemIn: Item, tab: CreativeTabs?, subItems: NonNullList<ItemStack>) {
        val ragnarokRises = try {
            ItemRagnarokPendant.hasAwakenedRagnarok(LibrarianLib.PROXY.getClientPlayer())
        } catch (e: IllegalStateException) {
            false
        }
        if (ragnarokRises)
            super.getSubItems(itemIn, tab, subItems)
    }
}
