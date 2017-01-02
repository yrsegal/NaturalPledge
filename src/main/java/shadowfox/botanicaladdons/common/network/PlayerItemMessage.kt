package shadowfox.botanicaladdons.common.network

import com.teamwizardry.librarianlib.common.network.PacketBase
import com.teamwizardry.librarianlib.common.util.saving.Save
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext


class PlayerItemMessage(@Save var item: ItemStack? = null) : PacketBase() {

    override fun handle(ctx: MessageContext) {
        val player = ctx.serverHandler.playerEntity

        if (player.heldItemMainhand == null) {
            player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, item?.copy())
        } else if (!player.inventory.addItemStackToInventory(item?.copy())) {
            player.dropItem(item?.copy(), false)
        }
    }
}


