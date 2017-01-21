package shadowfox.botanicaladdons.common.network

import com.teamwizardry.librarianlib.common.network.PacketBase
import com.teamwizardry.librarianlib.common.util.saving.Save
import io.netty.buffer.ByteBuf
import net.minecraft.network.PacketBuffer
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import shadowfox.botanicaladdons.common.BotanicalAddons

/**
 * @author WireSegal
 * Created at 9:18 PM on 1/2/17.
 */
class SpamlessMessagePacket(var msg: ITextComponent? = null, @Save var id: Int = 0) : PacketBase() {
    override fun handle(ctx: MessageContext) {
        val message = msg ?: return
        BotanicalAddons.PROXY.sendSpamlessMessage(BotanicalAddons.PROXY.getClientPlayer(), message, id)
    }

    override fun writeCustomBytes(buf: ByteBuf) {
        PacketBuffer(buf).writeTextComponent(msg)
    }

    override fun readCustomBytes(buf: ByteBuf) {
        msg = PacketBuffer(buf).readTextComponent()
    }
}
