package shadowfox.botanicaladdons.common.items.bauble.faith

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.IProjectile
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.World
import shadowfox.botanicaladdons.api.IFocusSpell
import shadowfox.botanicaladdons.common.core.BASoundEvents
import shadowfox.botanicaladdons.common.items.ItemSpellIcon
import vazkii.botania.api.internal.IManaBurst
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.Botania
import vazkii.botania.common.core.helper.Vector3
import vazkii.botania.common.entity.EntityDoppleganger

/**
 * @author WireSegal
 * Created at 1:05 PM on 4/19/16.
 */
object Spells {

    // Copied from Psi's PieceOperatorVectorRaycast
    fun raycast(e: Entity, len: Double): RayTraceResult? {
        val vec = Vector3.fromEntity(e)
        if (e is EntityPlayer) {
            vec.add(0.0, e.getEyeHeight().toDouble(), 0.0)
        }

        val look = e.lookVec
        if (look == null) {
            return null
        } else {
            return raycast(e.worldObj, vec, Vector3(look), len)
        }
    }

    fun raycast(world: World, origin: Vector3, ray: Vector3, len: Double): RayTraceResult? {
        val end = origin.copy().add(ray.copy().normalize().multiply(len))
        val pos = world.rayTraceBlocks(origin.toVec3D(), end.toVec3D())
        return pos
    }

    class Infuse : IFocusSpell {
        override fun getIconStack(): ItemStack {
            return ItemSpellIcon.of(ItemSpellIcon.Variants.SUFFUSION)
        }

        override fun onCast(player: EntityPlayer, focus: ItemStack, hand: EnumHand): Boolean {
            var range = 5.0
            if (player is EntityPlayerMP)
                range = player.interactionManager.blockReachDistance
            val ray = raycast(player, range)
            if (ray != null) {
                //todo
                return true
            }
            return false
        }
    }

    object Njord {
        class Leap : IFocusSpell {
            override fun getIconStack(): ItemStack {
                return ItemSpellIcon.of(ItemSpellIcon.Variants.LEAP)
            }

            override fun onCast(player: EntityPlayer, focus: ItemStack, hand: EnumHand): Boolean {
                if (ManaItemHandler.requestManaExact(focus, player, 20, true)) {
                    val look = player.lookVec
                    player.motionX = Math.max(Math.min(look.xCoord * 0.75 + player.motionX, 2.0), -2.0)
                    player.motionY = Math.max(Math.min(look.yCoord * 0.75 + player.motionY, 2.0), -2.0)
                    player.motionZ = Math.max(Math.min(look.zCoord * 0.75 + player.motionZ, 2.0), -2.0)
                    player.fallDistance = 0f
                    if (player.worldObj.totalWorldTime % 5 == 0L)
                        player.worldObj.playSound(player, player.posX + player.motionX, player.posY + player.motionY, player.posZ + player.motionZ, BASoundEvents.woosh, SoundCategory.PLAYERS, 0.4F, 1F)

                    return true
                }
                return false
            }
        }

        //////////

        class Interdict : IFocusSpell {
            override fun getIconStack(): ItemStack {
                return ItemSpellIcon.of(ItemSpellIcon.Variants.INTERDICT)
            }

            val RANGE = 6.0
            val VELOCITY = 0.4

            val SELECTOR: (Entity) -> Boolean = {
                (it is EntityLivingBase && it !is EntityDoppleganger) || (it is IProjectile && it !is IManaBurst)
            }

            fun pushEntities(x: Double, y: Double, z: Double, range: Double, velocity: Double, entities: List<Entity>): Boolean {
                var flag = false
                for (entityLiving in entities) {
                    var xDif = entityLiving.posX - x
                    var yDif = entityLiving.posY - (y + 1)
                    var zDif = entityLiving.posZ - z
                    val vec = Vector3(xDif, yDif, zDif).normalize()
                    var dist = Math.sqrt(xDif * xDif + yDif * yDif + zDif * zDif)
                    if (dist <= range) {
                        entityLiving.motionX = velocity * vec.x
                        entityLiving.motionY = velocity * vec.y
                        entityLiving.motionZ = velocity * vec.z
                        entityLiving.fallDistance = 0f
                        flag = true
                    }
                }
                return flag
            }

            fun particleRing(world: World, x: Double, y: Double, z: Double, range: Double, r: Float, g: Float, b: Float) {
                val m = 0.15F
                val mv = 0.35F
                for (i in 0..359 step 8) {
                    val rad = i.toDouble() * Math.PI / 180.0
                    val dispx = x + 0.5 - Math.cos(rad) * range.toFloat()
                    val dispy = y + 0.5
                    val dispz = z + 0.5 - Math.sin(rad) * range.toFloat()

                    Botania.proxy.wispFX(world, dispx, dispy, dispz, r, g, b, 0.2F, (Math.random() - 0.5).toFloat() * m, (Math.random() - 0.5).toFloat() * mv, (Math.random() - 0.5F).toFloat() * m)
                }
            }

            override fun onCast(player: EntityPlayer, focus: ItemStack, hand: EnumHand): Boolean {
                if (ManaItemHandler.requestManaExact(focus, player, 5, false)) {

                    particleRing(player.worldObj, player.posX, player.posY, player.posZ, RANGE, 0F, 0F, 1F)

                    val exclude: EntityLivingBase = player
                    val entities = player.worldObj.getEntitiesInAABBexcluding(exclude,
                            player.entityBoundingBox.expand(RANGE, RANGE, RANGE), SELECTOR)

                    if (pushEntities(player.posX, player.posY, player.posZ, RANGE, VELOCITY, entities)) {
                        if (player.worldObj.totalWorldTime % 3 == 0L)
                            player.worldObj.playSound(player, player.posX, player.posY, player.posZ, BASoundEvents.woosh, SoundCategory.PLAYERS, 0.4F, 1F)
                        ManaItemHandler.requestManaExact(focus, player, 5, true)
                    }
                    return true
                }
                return false
            }
        }

        //////////

        class PushAway : IFocusSpell {

            override fun getIconStack(): ItemStack {
                return ItemSpellIcon.of(ItemSpellIcon.Variants.PUSH_AWAY)
            }

            override fun onCast(player: EntityPlayer, focus: ItemStack, hand: EnumHand): Boolean {
                val focused = getEntityLookedAt(player)

                if (focused != null && focused is EntityLivingBase)
                    if (ManaItemHandler.requestManaExact(focus, player, 20, true)) {
                        focused.knockBack(player, 1.5f,
                                MathHelper.sin(player.rotationYaw * Math.PI.toFloat() / 180).toDouble(),
                                -MathHelper.cos(player.rotationYaw * Math.PI.toFloat() / 180).toDouble())
                        player.worldObj.playSound(player, focused.posX, focused.posY, focused.posZ, BASoundEvents.woosh, SoundCategory.PLAYERS, 0.4F, 1F)
                        return true
                    }
                return false
            }

            override fun getCooldown(player: EntityPlayer, focus: ItemStack, hand: EnumHand): Int {
                return 20
            }

            // Copied from Psi's PieceOperatorFocusedEntity

            fun getEntityLookedAt(e: Entity): Entity? {
                var foundEntity: Entity? = null
                var distance = 32.0
                val pos = raycast(e, 32.0)
                var positionVector = e.positionVector
                if (e is EntityPlayer) {
                    positionVector = positionVector.addVector(0.0, e.getEyeHeight().toDouble(), 0.0)
                }

                if (pos != null) {
                    distance = pos.hitVec.distanceTo(positionVector)
                }

                val lookVector = e.lookVec
                val reachVector = positionVector.addVector(lookVector.xCoord * 32.0, lookVector.yCoord * 32.0, lookVector.zCoord * 32.0)
                var lookedEntity: Entity? = null
                val entitiesInBoundingBox = e.worldObj.getEntitiesWithinAABBExcludingEntity(e, e.entityBoundingBox.addCoord(lookVector.xCoord * 32.0, lookVector.yCoord * 32.0, lookVector.zCoord * 32.0).expand(1.0, 1.0, 1.0))
                var minDistance = distance
                val var14 = entitiesInBoundingBox.iterator()

                while (true) {
                    do {
                        do {
                            if (!var14.hasNext()) {
                                return foundEntity
                            }
                            val next = var14.next()
                            if (next.canBeCollidedWith()) {
                                val collisionBorderSize = next.collisionBorderSize
                                val hitbox = next.entityBoundingBox.expand(collisionBorderSize.toDouble(), collisionBorderSize.toDouble(), collisionBorderSize.toDouble())
                                val interceptPosition = hitbox.calculateIntercept(positionVector, reachVector)
                                if (hitbox.isVecInside(positionVector)) {
                                    if (0.0 < minDistance || minDistance == 0.0) {
                                        lookedEntity = next
                                        minDistance = 0.0
                                    }
                                } else if (interceptPosition != null) {
                                    val distanceToEntity = positionVector.distanceTo(interceptPosition.hitVec)
                                    if (distanceToEntity < minDistance || minDistance == 0.0) {
                                        lookedEntity = next
                                        minDistance = distanceToEntity
                                    }
                                }
                            }
                        } while (lookedEntity == null)
                    } while (minDistance >= distance && pos != null)

                    foundEntity = lookedEntity
                }
            }
        }
    }
}
