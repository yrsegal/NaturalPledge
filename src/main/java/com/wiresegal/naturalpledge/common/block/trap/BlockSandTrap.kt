package com.wiresegal.naturalpledge.common.block.trap

import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.init.MobEffects
import net.minecraft.potion.PotionEffect
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World
import com.wiresegal.naturalpledge.common.lib.LibNames
import vazkii.botania.common.Botania
import java.util.*

/**
 * @author WireSegal
 * Created at 4:23 PM on 3/25/17.
 */
class BlockSandTrap : BlockBaseTrap(LibNames.SAND_TRAP) {
    override fun trapActivation(stateIn: IBlockState, world: World, pos: BlockPos, entityIn: EntityLivingBase) {
        entityIn.addPotionEffect(PotionEffect(MobEffects.BLINDNESS, 200))
        entityIn.addPotionEffect(PotionEffect(MobEffects.SLOWNESS, 200, 1))

        world.setBlockToAir(pos)
    }

    override fun particlesForSeer(stateIn: IBlockState, world: World, pos: BlockPos, rand: Random) {
        val x = pos.x + 0.5
        val y = pos.y + 0.5
        val z = pos.z + 0.5
        for (i in 0 until 360 step 10) {
            val dist = rand.nextDouble() * 0.25 + 0.125
            val azimuth = rand.nextInt(181) - 90
            val particleX = MathHelper.cos(i * Math.PI.toFloat() / 180) * MathHelper.cos(azimuth * Math.PI.toFloat() / 180) * dist + x
            val particleZ = MathHelper.sin(i * Math.PI.toFloat() / 180) * MathHelper.cos(azimuth * Math.PI.toFloat() / 180) * dist + z
            val particleY = MathHelper.sin(azimuth * Math.PI.toFloat() / 180) * dist + y
            Botania.proxy.wispFX(particleX, particleY, particleZ, R, G, B, 0.1f)
        }
    }
}
