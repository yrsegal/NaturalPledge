package shadowfox.botanicaladdons.common.network

import io.netty.buffer.ByteBuf
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.network.ByteBufUtils
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext


class PlayerItemMessage(var item: ItemStack? = null) : IMessage {

    class PlayerItemMessageHandler() : IMessageHandler<PlayerItemMessage, IMessage> {

        override fun onMessage(message: PlayerItemMessage?, ctx: MessageContext?): IMessage? {
            if (ctx != null && message != null && message.item != null && ctx.side.isServer) {
                val player = ctx.serverHandler.playerEntity

                if (player.heldItemMainhand == null) {
                    player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, message.item?.copy())
                } else if (!player.inventory.addItemStackToInventory(message.item?.copy())) {
                    player.dropItem(message.item?.copy(), false)
                }
            }

            return null
        }
    }

    override fun fromBytes(buf: ByteBuf?) {
        buf?.let {
            item = ByteBufUtils.readItemStack(buf)
        }
    }

    override fun toBytes(buf: ByteBuf?) {
        buf?.let {
            ByteBufUtils.writeItemStack(buf, item)
        }
    }
}


