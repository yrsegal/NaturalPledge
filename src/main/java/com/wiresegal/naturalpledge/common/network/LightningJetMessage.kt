package com.wiresegal.naturalpledge.common.network

import com.teamwizardry.librarianlib.features.autoregister.PacketRegister
import com.teamwizardry.librarianlib.features.network.PacketBase
import com.teamwizardry.librarianlib.features.saving.Save
import net.minecraft.util.math.Vec3d
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side
import vazkii.botania.common.Botania
import vazkii.botania.common.core.helper.Vector3

/**
 * @author WireSegal
 * Created at 8:15 PM on 3/25/17.
 */
@PacketRegister(Side.CLIENT)
class LightningJetMessage(@Save var pos: Vec3d = Vec3d.ZERO, @Save var vecTo: Vec3d = Vec3d.ZERO) : PacketBase() {
    override fun handle(ctx: MessageContext) = Botania.proxy.lightningFX(Vector3(pos), Vector3(vecTo), 1f, 0x00948B, 0x00E4D7)
}
