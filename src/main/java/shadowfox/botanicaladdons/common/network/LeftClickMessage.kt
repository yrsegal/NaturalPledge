package shadowfox.botanicaladdons.common.network

import io.netty.buffer.ByteBuf
import net.minecraft.entity.projectile.EntityLargeFireball
import net.minecraft.init.Items
import net.minecraft.init.SoundEvents
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraftforge.fml.common.network.ByteBufUtils
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import shadowfox.botanicaladdons.api.item.IPriestlyEmblem
import shadowfox.botanicaladdons.common.items.bauble.faith.ItemFaithBauble
import shadowfox.botanicaladdons.common.items.bauble.faith.PriestlyEmblemLoki


class LeftClickMessage() : IMessage {

    class LeftClickMessageHandler() : IMessageHandler<LeftClickMessage, IMessage> {

        override fun onMessage(message: LeftClickMessage?, ctx: MessageContext?): IMessage? {
            if (ctx != null && message != null && ctx.side.isServer) {
                val player = ctx.serverHandler.playerEntity
                val loki = ItemFaithBauble.getEmblem(player, PriestlyEmblemLoki::class.java)
                val mainhand = player.heldItemMainhand
                if (loki != null && mainhand != null && mainhand.item == Items.FIRE_CHARGE) {
                    val awakened = (loki.item as IPriestlyEmblem).isAwakened(loki)
                    val c = if (awakened) 1.5 else 1.0
                    val look = player.lookVec
                    val ghastBall = EntityLargeFireball(player.worldObj, player, look.xCoord, look.yCoord, look.zCoord)
                    ghastBall.explosionPower = if (awakened) 2 else 1
                    ghastBall.posX = player.posX + look.xCoord
                    ghastBall.posY = player.posY + (player.height / 2.0f).toDouble() + 0.5
                    ghastBall.posZ = player.posZ + look.zCoord
                    ghastBall.motionX = look.xCoord * c
                    ghastBall.motionY = look.yCoord * c
                    ghastBall.motionZ = look.zCoord * c
                    ghastBall.accelerationX = ghastBall.motionX * 0.1
                    ghastBall.accelerationY = ghastBall.motionY * 0.1
                    ghastBall.accelerationZ = ghastBall.motionZ * 0.1
                    player.worldObj.spawnEntityInWorld(ghastBall)
                    player.worldObj.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.PLAYERS, 1f, 1f)
                    if (!player.capabilities.isCreativeMode) {
                        player.cooldownTracker.setCooldown(Items.FIRE_CHARGE, 100)
                        mainhand.stackSize--
                        if (mainhand.stackSize == 0) player.setHeldItem(EnumHand.MAIN_HAND, null)
                    }
                }
            }

            return null
        }
    }

    override fun fromBytes(buf: ByteBuf?) {
        //NO-OP
    }

    override fun toBytes(buf: ByteBuf?) {
        //NO-OP
    }
}


