package com.wiresegal.naturalpledge.common.network

import com.teamwizardry.librarianlib.features.autoregister.PacketRegister
import com.teamwizardry.librarianlib.features.network.PacketBase
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side
import com.wiresegal.naturalpledge.common.items.bauble.faith.ItemRagnarokPendant

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

        fun handle() {
            val ragnarok = ItemRagnarokPendant.hasAwakenedRagnarok()
            val state = ragnarok != lastState
            if (state) {
                if (ragnarok) add() else remove()
                lastState = ragnarok
            }
        }
    }

    override fun handle(ctx: MessageContext) = handle()
}
