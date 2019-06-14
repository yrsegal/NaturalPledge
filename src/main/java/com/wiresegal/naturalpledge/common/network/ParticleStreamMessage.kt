package com.wiresegal.naturalpledge.common.network

import com.teamwizardry.librarianlib.features.autoregister.PacketRegister
import com.teamwizardry.librarianlib.features.network.PacketBase
import com.teamwizardry.librarianlib.features.saving.Save
import com.wiresegal.naturalpledge.common.NaturalPledge
import net.minecraft.util.math.Vec3d
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side
import vazkii.botania.common.Botania
import vazkii.botania.common.core.helper.Vector3

/**
 * @author WireSegal
 * Created at 11:35 AM on 6/14/19.
 */
@PacketRegister(Side.CLIENT)
class ParticleStreamMessage(@Save var from: Vec3d = Vec3d.ZERO, @Save var to: Vec3d = Vec3d.ZERO) : PacketBase() {

    override fun handle(ctx: MessageContext) {
        Botania.proxy.setWispFXDepthTest(false)
        NaturalPledge.PROXY.particleStream(Vector3(from), Vector3(to), NaturalPledge.PROXY.wireFrameRainbow().rgb)
        Botania.proxy.setWispFXDepthTest(true)
    }
}
