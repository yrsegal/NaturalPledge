package com.wiresegal.naturalpledge.common.block

import com.teamwizardry.librarianlib.features.base.block.BlockMod
import net.minecraft.block.SoundType
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.effect.EntityLightningBolt
import net.minecraft.entity.item.EntityItem
import net.minecraft.init.Blocks
import net.minecraft.init.MobEffects
import net.minecraft.potion.PotionEffect
import net.minecraft.util.DamageSource
import net.minecraft.util.EnumBlockRenderType
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.EntityStruckByLightningEvent
import com.wiresegal.naturalpledge.common.NaturalPledge
import vazkii.botania.common.Botania
import vazkii.botania.common.core.handler.ModSounds
import vazkii.botania.common.core.helper.Vector3
import java.util.*

/**
 * @author WireSegal
 * Created at 9:30 PM on 6/13/16.
 */
class BlockThunderTrap(name: String) : BlockMod(name, ModMaterials.TRANSPARENT) {
    private val AABB = AxisAlignedBB(0.25, 0.25, 0.25, 0.75, 0.75, 0.75)

    init {
        soundType = SoundType.CLOTH
    }

    override fun getBoundingBox(state: IBlockState?, world: IBlockAccess?, pos: BlockPos?) = AABB
    override fun getRenderType(state: IBlockState?) = EnumBlockRenderType.INVISIBLE
    override fun isOpaqueCube(state: IBlockState?) = false
    override fun isFullCube(state: IBlockState?) = false
    override fun isPassable(world: IBlockAccess?, pos: BlockPos?) = true
    override fun getCollisionBoundingBox(blockState: IBlockState?, worldIn: IBlockAccess?, pos: BlockPos?) = NULL_AABB
    override fun canSpawnInBlock(): Boolean = true

    override fun createItemForm() = null

    override fun randomDisplayTick(stateIn: IBlockState, world: World, pos: BlockPos, rand: Random) {
        if (NaturalPledge.PROXY.playerHasMonocle()) {
            for (i in 0..1) {
                val origVector = Vector3(pos.x + 0.5, pos.y + 0.5, pos.z + 0.5)
                val endVector = origVector.add(rand.nextDouble() * 2.0 - 1.0, rand.nextDouble() * 2.0 - 1.0, rand.nextDouble() * 2.0 - 1.0)
                Botania.proxy.lightningFX(origVector, endVector, 5.0f, 0x00948B, 0x00E4D7)
            }
        } else if (rand.nextFloat() < 0.5f) {
            Botania.proxy.sparkleFX(
                    pos.x + 0.5 + rand.nextDouble() * 0.25,
                    pos.y + 0.5 + rand.nextDouble() * 0.25,
                    pos.z + 0.5 + rand.nextDouble() * 0.25,
                    0x00 / 255f, 0xE4 / 255f, 0xD7 / 255f, 0.5f, 1)

        }
    }

    override fun onEntityCollidedWithBlock(worldIn: World, pos: BlockPos, state: IBlockState, entityIn: Entity) {
        if (entityIn is EntityLivingBase || entityIn is EntityItem) {
            if (!worldIn.isRemote) {
                worldIn.setBlockState(pos, Blocks.AIR.defaultState)

                val diffvec = Vector3.fromEntity(entityIn).subtract(Vector3.fromBlockPos(pos)).multiply(5.0)

                val fakeBolt = EntityLightningBolt(worldIn, entityIn.posX, entityIn.posY, entityIn.posZ, true)
                val event = EntityStruckByLightningEvent(entityIn, fakeBolt)
                MinecraftForge.EVENT_BUS.post(event)
                if (!event.isCanceled) {
                    entityIn.onStruckByLightning(fakeBolt)
                    entityIn.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 15f)
                    if (entityIn is EntityLivingBase) {
                        entityIn.knockBack(null, 1f, diffvec.x, diffvec.y)

                        entityIn.addPotionEffect(PotionEffect(MobEffects.SLOWNESS, 100))
                    }
                }
            } else {
                val posVec = Vector3.fromBlockPos(pos).add(0.5, 0.5, 0.5)
                for (i in 0..10) {
                    val horangle = worldIn.rand.nextInt(360) * Math.PI.toFloat() / 18
                    val verangle = worldIn.rand.nextInt(360) * Math.PI.toFloat() / 18
                    val fireVec = Vector3(MathHelper.cos(horangle).toDouble(),
                            MathHelper.sin(verangle).toDouble(),
                            MathHelper.sin(horangle).toDouble()).multiply(2.5)
                    Botania.proxy.lightningFX(posVec, posVec.add(fireVec), 0.5f, 0x00948B, 0x00E4D7)
                }
            }
            worldIn.playSound(null, pos, ModSounds.missile, SoundCategory.BLOCKS, 1f, 1f)
        }
    }
}
