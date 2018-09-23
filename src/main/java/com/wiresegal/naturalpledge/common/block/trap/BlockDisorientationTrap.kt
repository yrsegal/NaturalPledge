package com.wiresegal.naturalpledge.common.block.trap

import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import com.wiresegal.naturalpledge.common.NaturalPledge
import com.wiresegal.naturalpledge.common.lib.LibNames
import vazkii.botania.common.core.helper.Vector3
import java.util.*

/**
 * @author WireSegal
 * Created at 4:23 PM on 3/25/17.
 */
class BlockDisorientationTrap : BlockBaseTrap(LibNames.DISORIENT_TRAP) {
    override fun trapActivation(stateIn: IBlockState, world: World, pos: BlockPos, entityIn: EntityLivingBase) {
        val direction = if (world.rand.nextBoolean()) 1 else -1
        entityIn.rotationYaw += 90 * direction
        if (entityIn is EntityPlayerMP)
            entityIn.connection.setPlayerLocation(entityIn.posX, entityIn.posY, entityIn.posZ, entityIn.rotationYaw, entityIn.rotationPitch)
    }

    override fun particlesForSeer(stateIn: IBlockState, world: World, pos: BlockPos, rand: Random) {
        val minX = pos.x + 0.25
        val maxX = minX + 0.5
        val minZ = pos.z + 0.25
        val maxZ = minZ + 0.5
        val y = pos.y + 0.5
        val cornerMin = Vector3(minX, y, minZ)
        val cornerX = Vector3(maxX, y, minZ)
        val cornerZ = Vector3(minX, y, maxZ)
        val cornerMax = Vector3(maxX, y, maxZ)
        NaturalPledge.PROXY.particleStream(cornerMin, cornerX, COLOR)
        NaturalPledge.PROXY.particleStream(cornerX, cornerMax, COLOR)
        NaturalPledge.PROXY.particleStream(cornerMax, cornerZ, COLOR)
        NaturalPledge.PROXY.particleStream(cornerZ, cornerMin, COLOR)
    }
}
