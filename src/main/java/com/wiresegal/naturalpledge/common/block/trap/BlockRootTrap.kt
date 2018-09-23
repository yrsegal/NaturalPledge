package com.wiresegal.naturalpledge.common.block.trap

import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.potion.PotionEffect
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World
import com.wiresegal.naturalpledge.common.lib.LibNames
import com.wiresegal.naturalpledge.common.potions.ModPotions
import vazkii.botania.common.Botania
import java.util.*

/**
 * @author WireSegal
 * Created at 4:23 PM on 3/25/17.
 */
class BlockRootTrap : BlockBaseTrap(LibNames.ROOT_TRAP) {
    override fun trapActivation(stateIn: IBlockState, world: World, pos: BlockPos, entityIn: EntityLivingBase) {
        entityIn.addPotionEffect(PotionEffect(ModPotions.rooted, 200))

        world.setBlockToAir(pos)
    }

    override fun particlesForSeer(stateIn: IBlockState, world: World, pos: BlockPos, rand: Random) {
        val x = pos.x + 0.5
        val y = pos.y + 0.1
        val z = pos.z + 0.5
        for (i in 0 until 360 step 10) {
            val cos = MathHelper.cos(i * Math.PI.toFloat() / 180f) * rand.nextDouble() * 0.5
            val sin = MathHelper.sin(i * Math.PI.toFloat() / 180f) * rand.nextDouble() * 0.5
            Botania.proxy.wispFX(x + cos, y, z + sin, R, G, B, 0.1f)
        }
    }
}
