package shadowfox.botanicaladdons.common.network

import com.teamwizardry.librarianlib.common.network.PacketBase
import com.teamwizardry.librarianlib.common.util.autoregister.PacketRegister
import com.teamwizardry.librarianlib.common.util.saving.Save
import io.netty.buffer.ByteBuf
import net.minecraft.entity.projectile.EntityLargeFireball
import net.minecraft.init.Items
import net.minecraft.init.SoundEvents
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side
import shadowfox.botanicaladdons.api.item.IPriestlyEmblem
import shadowfox.botanicaladdons.common.items.bauble.faith.ItemFaithBauble
import shadowfox.botanicaladdons.common.items.bauble.faith.PriestlyEmblemLoki
import shadowfox.botanicaladdons.common.items.weapons.ItemFlarebringer

/**
 * @author WireSegal
 * Created at 8:15 PM on 3/25/17.
 */
@PacketRegister(Side.SERVER)
class AttackMessage(@Save var eID: Int = 0) : PacketBase() {

    override fun handle(ctx: MessageContext) {
        val player = ctx.serverHandler.player
        if (player.heldItemMainhand.item !is ItemFlarebringer) return
        val entity = player.world.getEntityByID(eID) ?: return
        if (player.positionVector.squareDistanceTo(entity.positionVector) > ItemFlarebringer.RANGE * ItemFlarebringer.RANGE) return
        player.attackTargetEntityWithCurrentItem(entity)
    }
}
