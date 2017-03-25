package shadowfox.botanicaladdons.common.network

import com.teamwizardry.librarianlib.common.network.PacketBase
import com.teamwizardry.librarianlib.common.util.autoregister.PacketRegister
import com.teamwizardry.librarianlib.common.util.saving.Save
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side

@PacketRegister(Side.SERVER)
class PlayerItemMessage(@Save var item: ItemStack = ItemStack.EMPTY) : PacketBase() {

    override fun handle(ctx: MessageContext) {
        val player = ctx.serverHandler.player

        val copy = item.copy()

        if (player.heldItemMainhand.isEmpty) {
            player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, copy)
        } else if (!player.inventory.addItemStackToInventory(copy)) {
            player.dropItem(item.copy(), false)
        }
    }
}


