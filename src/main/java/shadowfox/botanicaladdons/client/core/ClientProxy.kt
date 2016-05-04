package shadowfox.botanicaladdons.client.core

import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import shadowfox.botanicaladdons.client.render.tile.RenderTileFrozenStar
import shadowfox.botanicaladdons.common.block.tile.TileStar
import shadowfox.botanicaladdons.common.core.CommonProxy
import vazkii.botania.client.core.handler.ClientTickHandler
import vazkii.botania.common.Botania
import java.awt.Color
import java.util.*

/**
 * @author WireSegal
 * Created at 8:37 AM on 3/20/16.
 */
class ClientProxy : CommonProxy() {
    override fun pre(e: FMLPreInitializationEvent) {
        super.pre(e)
        ModelHandler.preInit()
    }

    override fun init(e: FMLInitializationEvent) {
        super.init(e)
        ModelHandler.init()

        ClientRegistry.bindTileEntitySpecialRenderer(TileStar::class.java, RenderTileFrozenStar())
    }

    override fun rainbow(saturation: Float) = Color(Color.HSBtoRGB((Botania.proxy.worldElapsedTicks * 2L % 360L).toFloat() / 360.0f, saturation, 1.0f))

    override fun rainbow(pos: BlockPos, saturation: Float): Color {
        val ticks = ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks
        val seed = (pos.x xor pos.y xor pos.z).toLong()
        val index = Random(seed).nextInt(100000)
        return Color(Color.HSBtoRGB((index + ticks) * 0.005F, saturation, 1F))
    }
}
