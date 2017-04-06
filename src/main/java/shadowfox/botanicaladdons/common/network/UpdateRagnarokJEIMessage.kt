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
import shadowfox.botanicaladdons.client.integration.jei.JEIPluginBotanicalAddons
import shadowfox.botanicaladdons.common.items.bauble.faith.ItemFaithBauble
import shadowfox.botanicaladdons.common.items.bauble.faith.ItemRagnarokPendant
import shadowfox.botanicaladdons.common.items.bauble.faith.PriestlyEmblemLoki
import shadowfox.botanicaladdons.common.items.weapons.ItemFlarebringer

/**
 * @author WireSegal
 * Created at 8:15 PM on 3/25/17.
 */
@PacketRegister(Side.CLIENT)
class UpdateRagnarokJEIMessage : PacketBase() {

    companion object {
        var add: () -> Unit = {}
        var remove: () -> Unit = {}

        var lastState = false
    }

    override fun handle(ctx: MessageContext) {
        val ragnarok = ItemRagnarokPendant.hasAwakenedRagnarok()
        val state = ragnarok != lastState
        if (state) {
            if (ragnarok) add() else remove()
            lastState = ragnarok
        }
    }
}
