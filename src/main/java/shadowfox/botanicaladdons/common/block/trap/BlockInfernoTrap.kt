package shadowfox.botanicaladdons.common.block.trap

import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.init.SoundEvents
import net.minecraft.potion.PotionEffect
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World
import shadowfox.botanicaladdons.common.lib.LibNames
import shadowfox.botanicaladdons.common.potions.ModPotions
import vazkii.botania.common.Botania
import vazkii.botania.common.entity.EntityFlameRing
import java.util.*

/**
 * @author WireSegal
 * Created at 4:23 PM on 3/25/17.
 */
class BlockInfernoTrap : BlockBaseTrap(LibNames.INFERNO_TRAP) {
    override fun trapActivation(stateIn: IBlockState, world: World, pos: BlockPos, entityIn: EntityLivingBase) {
        val ring = EntityFlameRing(world)
        ring.setPosition(pos.x + 0.5, pos.y + 1.0, pos.z + 0.5)

        entityIn.addPotionEffect(PotionEffect(ModPotions.everburn, 200))

        world.spawnEntity(ring)
        world.playSound(null, pos, SoundEvents.ENTITY_BLAZE_AMBIENT, SoundCategory.BLOCKS, 1F, 1F)
        world.setBlockToAir(pos)
    }

    override fun particlesForSeer(stateIn: IBlockState, world: World, pos: BlockPos, rand: Random) {
        val x = pos.x + 0.5
        val y = pos.y + 0.5
        val z = pos.z + 0.5
        for (i in 0 until 360 step 10) {
            val cos = MathHelper.cos(i * Math.PI.toFloat() / 180f) * 0.5
            val sin = MathHelper.sin(i * Math.PI.toFloat() / 180f) * 0.5
            Botania.proxy.wispFX(x + cos, y, z + sin, R, G, B, 0.1f)
        }
    }
}
