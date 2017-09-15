package shadowfox.botanicaladdons.common.items.bauble.faith

import com.google.common.base.Predicate
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper
import com.teamwizardry.librarianlib.features.helpers.vec
import com.teamwizardry.librarianlib.features.kotlin.minus
import com.teamwizardry.librarianlib.features.kotlin.motionVec
import com.teamwizardry.librarianlib.features.kotlin.plus
import com.teamwizardry.librarianlib.features.kotlin.times
import com.teamwizardry.librarianlib.features.network.PacketHandler
import com.teamwizardry.librarianlib.features.utilities.NBTTypes
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.IProjectile
import net.minecraft.entity.effect.EntityLightningBolt
import net.minecraft.entity.item.EntityArmorStand
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.entity.projectile.EntityLargeFireball
import net.minecraft.init.MobEffects
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagDouble
import net.minecraft.nbt.NBTTagInt
import net.minecraft.nbt.NBTTagList
import net.minecraft.network.play.server.SPacketEntityVelocity
import net.minecraft.potion.PotionEffect
import net.minecraft.util.*
import net.minecraft.util.math.*
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.EntityStruckByLightningEvent
import net.minecraftforge.fluids.IFluidBlock
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.oredict.OreDictionary
import shadowfox.botanicaladdons.api.item.IPriestlyEmblem
import shadowfox.botanicaladdons.api.priest.IFocusSpell
import shadowfox.botanicaladdons.common.BotanicalAddons
import shadowfox.botanicaladdons.common.block.ModBlocks
import shadowfox.botanicaladdons.common.core.BASoundEvents
import shadowfox.botanicaladdons.common.items.ItemSpellIcon.Companion.of
import shadowfox.botanicaladdons.common.items.ItemSpellIcon.Variants.*
import shadowfox.botanicaladdons.common.network.FireJetMessage
import shadowfox.botanicaladdons.common.network.FireSphereMessage
import shadowfox.botanicaladdons.common.network.LightningJetMessage
import shadowfox.botanicaladdons.common.potions.ModPotions
import vazkii.botania.api.internal.IManaBurst
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.Botania
import vazkii.botania.common.block.tile.TileBifrost
import vazkii.botania.common.core.handler.ModSounds
import vazkii.botania.common.core.helper.Vector3
import java.awt.Color
import vazkii.botania.common.block.ModBlocks as BotaniaBlocks

/**
 * @author WireSegal
 * Created at 1:05 PM on 4/19/16.
 */
object Spells {

    object Helper {
        // Copied from Psi's PieceOperatorVectorRaycast with minor changes
        fun raycast(e: Entity, len: Double, stopOnLiquid: Boolean = false): RayTraceResult? {
            val vec = Vector3.fromEntity(e).add(0.0, (e as? EntityPlayer)?.getEyeHeight()?.toDouble() ?: 0.0, 0.0)

            val look = e.lookVec
            if (look == null) {
                return null
            } else {
                return raycast(e.world, vec, Vector3(look), len, stopOnLiquid)
            }
        }

        fun raycast(world: World, origin: Vector3, ray: Vector3, len: Double, stopOnLiquid: Boolean = false): RayTraceResult? {
            val end = origin.add(ray.normalize().multiply(len))
            val pos = world.rayTraceBlocks(origin.toVec3D(), end.toVec3D(), stopOnLiquid)
            return pos
        }

        // Copied from Psi's PieceOperatorFocusedEntity

        fun getEntityLookedAt(e: Entity, maxDistance: Double = 32.0): Entity? {
            var foundEntity: Entity? = null
            var distance = maxDistance
            val pos = raycast(e, maxDistance)
            var positionVector = e.positionVector
            if (e is EntityPlayer) {
                positionVector = positionVector.addVector(0.0, e.getEyeHeight().toDouble(), 0.0)
            }

            if (pos != null) {
                distance = pos.hitVec.distanceTo(positionVector)
            }

            val lookVector = e.lookVec
            val reachVector = positionVector.addVector(lookVector.x * maxDistance, lookVector.y * maxDistance, lookVector.z * maxDistance)
            var lookedEntity: Entity? = null
            val vec = vec(lookVector.x * maxDistance, lookVector.y * maxDistance, lookVector.z * maxDistance)
            val entitiesInBoundingBox = e.world.getEntitiesWithinAABBExcludingEntity(e, e.entityBoundingBox.union(AxisAlignedBB(vec, vec)).grow(1.0))
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
                            if (hitbox.contains(positionVector)) {
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

        // Copied from Psi's ItemCAD, with minor modifications
        fun craft(player: EntityPlayer, `in`: String, out: ItemStack, colorVal: Int): Boolean {
            val items = player.world.getEntitiesWithinAABB(EntityItem::class.java, AxisAlignedBB(player.posX - 8, player.posY - 8, player.posZ - 8, player.posX + 8, player.posY + 8, player.posZ + 8))

            val color = Color(colorVal)
            val r = color.red / 255f
            val g = color.green / 255f
            val b = color.blue / 255f


            var did = false
            for (item in items) {
                val stack = item.item
                if (stack != null && (stack.item != out.item || stack.itemDamage != out.itemDamage) && checkStack(stack, `in`)) {
                    val outCopy = out.copy()
                    outCopy.count = stack.count
                    item.item = outCopy
                    did = true

                    for (i in 0..4) {
                        val x = item.posX + (Math.random() - 0.5) * 2.1 * item.width.toDouble()
                        val y = item.posY - item.yOffset + 0.5
                        val z = item.posZ + (Math.random() - 0.5) * 2.1 * item.width.toDouble()
                        Botania.proxy.sparkleFX(x, y, z, r, g, b, 3.5f, 15)

                        val m = 0.01
                        val d3 = 10.0
                        for (j in 0..2) {
                            val d0 = item.world.rand.nextGaussian() * m
                            val d1 = item.world.rand.nextGaussian() * m
                            val d2 = item.world.rand.nextGaussian() * m

                            item.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, item.posX + item.world.rand.nextFloat() * item.width * 2.0f - item.width.toDouble() - d0 * d3, item.posY + item.world.rand.nextFloat() * item.height - d1 * d3, item.posZ + item.world.rand.nextFloat() * item.width * 2.0f - item.width.toDouble() - d2 * d3, d0, d1, d2)
                        }
                    }
                }
            }

            return did
        }

        fun checkStack(stack: ItemStack, key: String): Boolean {
            val ores = OreDictionary.getOres(key, false)
            return ores.any { OreDictionary.itemMatches(stack, it, false) }
        }
    }

    class ObjectInfusion(val icon: ItemStack, oreKey: String, product: ItemStack, awakenedProduct: ItemStack, manaCost: Int, color: Int, transformer: ((EntityPlayer, ObjectInfusionEntry) -> Unit)? = null) : IFocusSpell {

        object UltimateInfusion : IFocusSpell {
            override fun getIconStack() = of(SOUL_MANIFESTATION)

            override fun onCast(player: EntityPlayer, focus: ItemStack, hand: EnumHand): EnumActionResult? {
                var flag = false
                player.world.playSound(player, player.posX, player.posY, player.posZ, ModSounds.potionCreate, SoundCategory.PLAYERS, 1f, 0.5f)
                allEntries.forEach { flag = processEntry(player, focus, it) || flag }
                return if (flag) EnumActionResult.SUCCESS else EnumActionResult.FAIL
            }

            override fun getCooldown(player: EntityPlayer, focus: ItemStack, hand: EnumHand): Int {
                return 50
            }
        }

        companion object {
            val allEntries = mutableListOf<ObjectInfusionEntry>()

            fun processEntry(player: EntityPlayer, focus: ItemStack, entry: ObjectInfusionEntry): Boolean {
                if (!ManaItemHandler.requestManaExact(focus, player, entry.manaCost, false)) return false
                val emblem = ItemFaithBauble.getEmblem(player) ?: return false
                val flag =
                        if ((emblem.item as IPriestlyEmblem).isAwakened(emblem))
                            Helper.craft(player, entry.oreKey, entry.awakenedProduct, entry.color)
                        else
                            Helper.craft(player, entry.oreKey, entry.product, entry.color)
                if (flag) {
                    entry.transformer?.invoke(player, entry)
                    ManaItemHandler.requestManaExact(focus, player, entry.manaCost, true)
                }
                return true
            }
        }

        val objectInfusionEntries = mutableListOf<ObjectInfusionEntry>()

        fun addEntry(oreKey: String, product: ItemStack, awakenedProduct: ItemStack, manaCost: Int, color: Int, transformer: ((EntityPlayer, ObjectInfusionEntry) -> Unit)? = null): ObjectInfusion {
            val entry = ObjectInfusionEntry(oreKey, product, awakenedProduct, manaCost, color, transformer)
            objectInfusionEntries.add(entry)
            allEntries.add(entry)
            return this
        }

        init {
            addEntry(oreKey, product, awakenedProduct, manaCost, color, transformer)
        }

        override fun getIconStack() = icon

        override fun onCast(player: EntityPlayer, focus: ItemStack, hand: EnumHand): EnumActionResult? {
            var flag = false
            player.world.playSound(player, player.posX, player.posY, player.posZ, ModSounds.potionCreate, SoundCategory.PLAYERS, 1f, 0.5f)
            allEntries.forEach { flag = processEntry(player, focus, it) || flag }
            return if (flag) EnumActionResult.SUCCESS else EnumActionResult.FAIL
        }

        override fun getCooldown(player: EntityPlayer, focus: ItemStack, hand: EnumHand): Int {
            return 50
        }

        data class ObjectInfusionEntry(val oreKey: String, val product: ItemStack, val awakenedProduct: ItemStack, val manaCost: Int, val color: Int, val transformer: ((EntityPlayer, ObjectInfusionEntry) -> Unit)? = null)
    }

    object Njord {
        object Leap : IFocusSpell {
            override fun getIconStack() = of(LEAP)

            override fun onCast(player: EntityPlayer, focus: ItemStack, hand: EnumHand): EnumActionResult {
                if (ManaItemHandler.requestManaExact(focus, player, 20, true)) {
                    val look = player.lookVec
                    val speedVec = Vector3(look).multiply(0.75).add(player.motionX, player.motionY, player.motionZ)
                    if (speedVec.magSquared() > 9)
                        return EnumActionResult.FAIL

                    player.motionX = speedVec.x
                    player.motionY = speedVec.y
                    player.motionZ = speedVec.z

                    player.fallDistance = 0f
                    if (player.world.totalWorldTime % 5 == 0L)
                        player.world.playSound(player, player.posX + player.motionX, player.posY + player.motionY, player.posZ + player.motionZ, BASoundEvents.woosh, SoundCategory.PLAYERS, 0.4F, 1F)

                    return EnumActionResult.SUCCESS
                }
                return EnumActionResult.FAIL
            }
        }

        //////////

        object Interdict : IFocusSpell {
            override fun getIconStack() = of(INTERDICT)

            val RANGE = 6.0
            val VELOCITY = 0.4

            val SELECTOR = Predicate<Entity> {
                (it is EntityLivingBase && it.isNonBoss && it !is EntityArmorStand) || (it is IProjectile && it !is IManaBurst)
            }

            fun pushEntities(x: Double, y: Double, z: Double, range: Double, velocity: Double, entities: List<Entity>): Boolean {
                var flag = false
                for (entity in entities) {
                    val xDif = entity.posX - x
                    val yDif = entity.posY - (y + 1)
                    val zDif = entity.posZ - z
                    val vec = Vector3(xDif, yDif, zDif).normalize()
                    val dist = Math.sqrt(xDif * xDif + yDif * yDif + zDif * zDif)
                    if (dist <= range) {
                        entity.motionX = velocity * vec.x
                        entity.motionY = velocity * vec.y
                        entity.motionZ = velocity * vec.z
                        entity.fallDistance = 0f
                        if (entity is EntityPlayerMP)
                            entity.connection.sendPacket(SPacketEntityVelocity(entity))
                        if (entity.motionVec.lengthSquared() != 0.0)
                            flag = true
                    }
                }
                return flag
            }

            override fun onCast(player: EntityPlayer, focus: ItemStack, hand: EnumHand): EnumActionResult {
                if (ManaItemHandler.requestManaExact(focus, player, 5, false)) {

                    BotanicalAddons.PROXY.particleRing(player.posX, player.posY, player.posZ, RANGE, 0F, 0F, 1F)

                    val exclude: EntityLivingBase = player
                    val entities = player.world.getEntitiesInAABBexcluding(exclude,
                            player.entityBoundingBox.expand(RANGE, RANGE, RANGE), SELECTOR)

                    if (pushEntities(player.posX, player.posY, player.posZ, RANGE, VELOCITY, entities)) {
                        if (player.world.totalWorldTime % 3 == 0L)
                            player.world.playSound(player, player.posX, player.posY, player.posZ, BASoundEvents.woosh, SoundCategory.PLAYERS, 0.4F, 1F)
                        ManaItemHandler.requestManaExact(focus, player, 5, true)
                    }
                    return EnumActionResult.SUCCESS
                }
                return EnumActionResult.FAIL
            }
        }

        //////////

        object PushAway : IFocusSpell {

            override fun getIconStack() = of(PUSH_AWAY)

            override fun onCast(player: EntityPlayer, focus: ItemStack, hand: EnumHand): EnumActionResult {
                val focused = Helper.getEntityLookedAt(player)

                if (focused != null && focused is EntityLivingBase) {
                    if (ManaItemHandler.requestManaExact(focus, player, 20, true)) {
                        focused.knockBack(player, 1.5f,
                                MathHelper.sin(player.rotationYaw * Math.PI.toFloat() / 180).toDouble(),
                                -MathHelper.cos(player.rotationYaw * Math.PI.toFloat() / 180).toDouble())
                        player.world.playSound(player, focused.posX, focused.posY, focused.posZ, BASoundEvents.woosh, SoundCategory.PLAYERS, 0.4F, 1F)
                        return EnumActionResult.SUCCESS
                    }
                    return EnumActionResult.FAIL
                }
                return EnumActionResult.PASS
            }

            override fun getCooldown(player: EntityPlayer, focus: ItemStack, hand: EnumHand): Int {
                return 20
            }
        }
    }

    object Thor {
        object Lightning : IFocusSpell {
            fun bolt(to: Vector3, player: EntityPlayer) {
                val from = Vector3.fromEntityCenter(player)
                PacketHandler.NETWORK.sendToAllAround(LightningJetMessage(from.toVec3D(), to.toVec3D()),
                        NetworkRegistry.TargetPoint(player.world.provider.dimension, player.posX, player.posY, player.posZ, 30.0))
            }

            override fun getIconStack() = of(LIGHTNING)

            override fun getCooldown(player: EntityPlayer, focus: ItemStack, hand: EnumHand): Int {
                return 60
            }

            override fun onCast(player: EntityPlayer, focus: ItemStack, hand: EnumHand): EnumActionResult {
                val cast = Helper.raycast(player, 16.0)
                val focused = Helper.getEntityLookedAt(player, 16.0)

                val emblem = ItemFaithBauble.getEmblem(player)

                if (focused != null && focused is EntityLivingBase) {
                    if (ManaItemHandler.requestManaExact(focus, player, 20, true)) {
                        focused.attackEntityFrom(DamageSource.causePlayerDamage(player), if (emblem != null && (emblem.item as IPriestlyEmblem).isAwakened(emblem)) 10f else 5f)
                        val fakeBolt = EntityLightningBolt(player.world, focused.posX, focused.posY, focused.posZ, true)
                        val event = EntityStruckByLightningEvent(focused, fakeBolt)
                        MinecraftForge.EVENT_BUS.post(event)
                        if (!event.isCanceled)
                            focused.onStruckByLightning(fakeBolt)
                        bolt(Vector3.fromEntityCenter(focused), player)
                        player.world.playSound(player, player.position, ModSounds.missile, SoundCategory.PLAYERS, 1f, 1f)
                        return EnumActionResult.SUCCESS
                    }
                } else if (cast != null && cast.typeOfHit == RayTraceResult.Type.BLOCK) {
                    bolt(Vector3(cast.hitVec), player)
                    player.world.playSound(player, player.position, ModSounds.missile, SoundCategory.PLAYERS, 1f, 1f)
                    return EnumActionResult.SUCCESS
                } else if (cast == null || cast.typeOfHit == RayTraceResult.Type.MISS) {
                    bolt(Vector3.fromEntityCenter(player).add(Vector3(player.lookVec).multiply(10.0)), player)
                    player.world.playSound(player, player.position, ModSounds.missile, SoundCategory.PLAYERS, 1f, 1f)
                    return EnumActionResult.SUCCESS
                }
                return EnumActionResult.FAIL
            }
        }

        //////////

        object Strength : IFocusSpell {
            override fun getIconStack() = of(STRENGTH)

            override fun onCast(player: EntityPlayer, focus: ItemStack, hand: EnumHand): EnumActionResult {
                return if (ManaItemHandler.requestManaExact(focus, player, 100, true)) EnumActionResult.SUCCESS else EnumActionResult.FAIL
            }

            override fun getCooldown(player: EntityPlayer, focus: ItemStack, hand: EnumHand): Int {
                return 900
            }

            override fun onCooldownTick(player: EntityPlayer, focus: ItemStack, slot: Int, selected: Boolean, cooldownRemaining: Int) {
                if (!player.world.isRemote && cooldownRemaining > 300)
                    player.addPotionEffect(PotionEffect(MobEffects.STRENGTH, 5, 0, true, true))
            }
        }

        //////////

        object Pull : IFocusSpell {
            override fun getIconStack() = of(PULL)

            override fun onCast(player: EntityPlayer, focus: ItemStack, hand: EnumHand): EnumActionResult {
                val focused = Helper.getEntityLookedAt(player, 16.0)

                if (focused != null && focused is EntityLivingBase)
                    if (ManaItemHandler.requestManaExact(focus, player, 20, true)) {
                        val diff = Vector3.fromEntityCenter(player).subtract(Vector3.fromEntityCenter(focused))
                        focused.motionX += diff.x * 0.25
                        focused.motionY += diff.y * 0.25
                        focused.motionZ += diff.z * 0.25
                        focused.addPotionEffect(PotionEffect(MobEffects.SLOWNESS, 100, 1))
                        if (focused is EntityPlayerMP)
                            focused.connection.sendPacket(SPacketEntityVelocity(focused))
                        return EnumActionResult.SUCCESS
                    }
                return EnumActionResult.FAIL
            }

            override fun getCooldown(player: EntityPlayer, focus: ItemStack, hand: EnumHand): Int {
                return 100
            }
        }

        object LightningTrap : IFocusSpell {
            override fun getIconStack() = of(HYPERCHARGE)

            override fun getCooldown(player: EntityPlayer, focus: ItemStack, hand: EnumHand): Int {
                return 400
            }

            override fun onCast(player: EntityPlayer, focus: ItemStack, hand: EnumHand): EnumActionResult {
                if (!ManaItemHandler.requestManaExact(focus, player, 1500, false)) return EnumActionResult.FAIL

                val ray = Helper.raycast(player, 32.0)

                if (ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK) {
                    var pos = ray.blockPos
                    if (!player.world.getBlockState(pos).block.isReplaceable(player.world, pos)) pos = pos.offset(ray.sideHit)

                    if (player.canPlayerEdit(pos, ray.sideHit, null)) {
                        ManaItemHandler.requestManaExact(focus, player, 1500, true)
                        player.world.setBlockState(pos, ModBlocks.thunderTrap.defaultState)
                        Botania.proxy.lightningFX(Vector3.fromEntityCenter(player), Vector3.fromBlockPos(pos).add(0.5, 0.5, 0.5), 1f, 0x00948B, 0x00E4D7)
                        player.world.playSound(player, player.position, ModSounds.missile, SoundCategory.PLAYERS, 1f, 1f)
                        return EnumActionResult.SUCCESS
                    }
                }
                return EnumActionResult.FAIL
            }
        }
    }

    object Heimdall {
        object BifrostWave : IFocusSpell {
            override fun getIconStack() = of(BIFROST_SPHERE)

            val TAG_SOURCE = "source"

            override fun onCast(player: EntityPlayer, focus: ItemStack, hand: EnumHand): EnumActionResult {
                if (ManaItemHandler.requestManaExact(focus, player, 100, true)) {
                    val pos = NBTTagList()
                    pos.appendTag(NBTTagInt(player.position.x))
                    pos.appendTag(NBTTagInt(player.position.y))
                    pos.appendTag(NBTTagInt(player.position.z))
                    ItemNBTHelper.setList(focus, TAG_SOURCE, pos)

                    player.fallDistance = 0f
                    player.motionX = 0.0
                    player.motionY = 0.0
                    player.motionZ = 0.0
                    return EnumActionResult.SUCCESS
                }
                return EnumActionResult.FAIL
            }

            override fun getCooldown(player: EntityPlayer, focus: ItemStack, hand: EnumHand): Int {
                return 400
            }

            override fun onCooldownTick(player: EntityPlayer, focus: ItemStack, slot: Int, selected: Boolean, cooldownRemaining: Int) {
                val timeElapsed = 400 - cooldownRemaining
                val stage = timeElapsed / 5

                if (player.world.isRemote) return

                if (stage * 5 != timeElapsed) return

                val positionTag = ItemNBTHelper.getList(focus, TAG_SOURCE, 3)

                if (stage >= 5 || positionTag == null) {
                    if (positionTag != null)
                        ItemNBTHelper.removeEntry(focus, TAG_SOURCE)
                    return
                }

                val pos = BlockPos(positionTag.getIntAt(0).toDouble(), positionTag.getIntAt(1) + stage - 1.5, positionTag.getIntAt(2).toDouble())

                if (stage == 0 || stage == 4) for (xShift in -1..1) for (zShift in -1..1)
                    makeBifrost(player.world, pos.add(xShift, 0, zShift), cooldownRemaining - 200)
                else for (rot in EnumFacing.HORIZONTALS) for (perpShift in -1..1)
                    makeBifrost(player.world, pos.offset(rot, 2).offset(rot.rotateY(), perpShift), cooldownRemaining - 200)

            }

            fun makeBifrost(world: World, pos: BlockPos, time: Int) {
                val state = world.getBlockState(pos)
                val block = state.block
                if (block.isAir(state, world, pos) || block.isReplaceable(world, pos) || block is IFluidBlock) {
                    world.setBlockState(pos, BotaniaBlocks.bifrost.defaultState)
                    val tileBifrost = world.getTileEntity(pos) as TileBifrost
                    tileBifrost.ticks = time
                } else if (block == BotaniaBlocks.bifrost) {
                    val tileBifrost = world.getTileEntity(pos) as TileBifrost
                    if (tileBifrost.ticks < 2) {
                        tileBifrost.ticks = time
                    }
                }
            }
        }
    }

    object Idunn {
        object Ironroot : IFocusSpell {
            override fun getIconStack() = of(IRONROOT)

            override fun onCast(player: EntityPlayer, focus: ItemStack, hand: EnumHand): EnumActionResult {
                return if (ManaItemHandler.requestManaExact(focus, player, 100, true)) EnumActionResult.SUCCESS else EnumActionResult.FAIL
            }

            override fun getCooldown(player: EntityPlayer, focus: ItemStack, hand: EnumHand): Int {
                return 900
            }

            override fun onCooldownTick(player: EntityPlayer, focus: ItemStack, slot: Int, selected: Boolean, cooldownRemaining: Int) {
                if (!player.world.isRemote && cooldownRemaining > 300) {
                    player.addPotionEffect(PotionEffect(MobEffects.RESISTANCE, 5, 4, true, true))
                    player.addPotionEffect(PotionEffect(MobEffects.WEAKNESS, 5, 4, true, true))
                    player.addPotionEffect(PotionEffect(ModPotions.rooted, 5, 0, true, true))
                }
            }
        }
    }

    object Loki {
        object Truesight : IFocusSpell {
            override fun getIconStack() = of(TRUESIGHT)

            override fun onCast(player: EntityPlayer, focus: ItemStack, hand: EnumHand): EnumActionResult {
                return if (ManaItemHandler.requestManaExact(focus, player, 100, true)) EnumActionResult.SUCCESS else EnumActionResult.FAIL
            }

            override fun getCooldown(player: EntityPlayer, focus: ItemStack, hand: EnumHand): Int {
                return 400
            }

            override fun onCooldownTick(player: EntityPlayer, focus: ItemStack, slot: Int, selected: Boolean, cooldownRemaining: Int) {
                if (!player.world.isRemote) player.addPotionEffect(PotionEffect(ModPotions.trapSeer, 5, 0, true, true))
            }
        }

        object Disdain : IFocusSpell {
            override fun getIconStack(): ItemStack {
                return of(DISDAIN)
            }

            override fun onCast(player: EntityPlayer, focus: ItemStack, hand: EnumHand): EnumActionResult {
                return EnumActionResult.SUCCESS
            }

            override fun getCooldown(player: EntityPlayer, focus: ItemStack, hand: EnumHand): Int {
                return 400
            }

            override fun onCooldownTick(player: EntityPlayer, focus: ItemStack, slot: Int, selected: Boolean, cooldownRemaining: Int) {
                if (!player.world.isRemote && ManaItemHandler.requestManaExact(focus, player, 10, true)) {
                    if (cooldownRemaining % 5 == 0)
                        PacketHandler.NETWORK.sendToAllAround(FireSphereMessage(player.positionVector.addVector(0.0, 0.5, 0.0)),
                                NetworkRegistry.TargetPoint(player.world.provider.dimension, player.posX, player.posY, player.posZ, 30.0))
                    player.world.getEntitiesWithinAABB(EntityLivingBase::class.java, player.entityBoundingBox.grow(5.0)) {
                        player != it && player.positionVector.squareDistanceTo(it?.positionVector ?: Vec3d.ZERO) < 25.0
                    }.forEach {
                        it.addPotionEffect(PotionEffect(ModPotions.everburn, 100))
                    }
                }
            }
        }

        object FlameJet : IFocusSpell {

            val TAG_SOURCE = "source"
            val TAG_TARGET = "target"

            var hit: Vec3d = Vec3d.ZERO

            fun getIntersection(fDst1: Double, fDst2: Double, l1: Vec3d, l2: Vec3d): Boolean {
                if ((fDst1 * fDst2) >= 0.0f) return false
                if (fDst1 == fDst2) return false
                hit = l1 + (l2 - l1) * (-fDst1 / (fDst2 - fDst1))
                return true
            }

            fun inBox(b1: Vec3d, b2: Vec3d, axis: EnumFacing.Axis): Boolean {
                return when (axis) {
                    EnumFacing.Axis.X -> hit.z > b1.z &&
                            hit.z < b2.z &&
                            hit.y > b1.y &&
                            hit.y < b2.y
                    EnumFacing.Axis.Y -> hit.z > b1.z &&
                            hit.z < b2.z &&
                            hit.x > b1.x &&
                            hit.x < b2.x
                    EnumFacing.Axis.Z -> hit.x > b1.x &&
                            hit.x < b2.x &&
                            hit.y > b1.y &&
                            hit.y < b2.y
                    else -> false
                }
            }

            fun intersectsBox(l1: Vec3d, l2: Vec3d, aabb: AxisAlignedBB): Boolean {
                val b1 = Vec3d(aabb.minX, aabb.minY, aabb.minZ)
                val b2 = Vec3d(aabb.maxX, aabb.maxY, aabb.maxZ)
                if (l2.x < b1.x && l1.x < b1.x) return false
                if (l2.x > b2.x && l1.x > b2.x) return false
                if (l2.y < b1.y && l1.y < b1.y) return false
                if (l2.y > b2.y && l1.y > b2.y) return false
                if (l2.z < b1.z && l1.z < b1.z) return false
                if (l2.z > b2.z && l1.z > b2.z) return false
                if (l1.x > b1.x && l1.x < b2.x &&
                        l1.y > b1.y && l1.y < b2.y &&
                        l1.z > b1.z && l1.z < b2.z) return true
                return getIntersection(l1.x - b1.x, l2.x - b1.x, l1, l2) && inBox(b1, b2, EnumFacing.Axis.X)
                        || getIntersection(l1.y - b1.y, l2.y - b1.y, l1, l2) && inBox(b1, b2, EnumFacing.Axis.Y)
                        || getIntersection(l1.z - b1.z, l2.z - b1.z, l1, l2) && inBox(b1, b2, EnumFacing.Axis.Z)
                        || getIntersection(l1.x - b2.x, l2.x - b2.x, l1, l2) && inBox(b1, b2, EnumFacing.Axis.X)
                        || getIntersection(l1.y - b2.y, l2.y - b2.y, l1, l2) && inBox(b1, b2, EnumFacing.Axis.Y)
                        || getIntersection(l1.z - b2.z, l2.z - b2.z, l1, l2) && inBox(b1, b2, EnumFacing.Axis.Z)
            }

            fun jet(to: Vector3, player: EntityPlayer, from: Vector3) {
                val fakeFireball = EntityLargeFireball(player.world, player, 0.0, 0.0, 0.0)

                val f = from.toVec3D()
                val t = to.toVec3D()
                val aabb = AxisAlignedBB(f.x, f.y, f.z, t.x, t.y, t.z)

                player.world.getEntitiesWithinAABB(EntityLivingBase::class.java, aabb.grow(1.0)) {
                    val bb = it?.entityBoundingBox
                    bb != null && it != player && intersectsBox(f, t, bb.grow(1.0))
                }.forEach {
                    it.attackEntityFrom(DamageSource.causeFireballDamage(fakeFireball, player), 2f)
                    it.addPotionEffect(PotionEffect(ModPotions.everburn, 300))
                }
            }

            override fun getIconStack() = of(FIRE_JET)

            override fun getCooldown(player: EntityPlayer, focus: ItemStack, hand: EnumHand): Int {
                return 60
            }

            override fun onCast(player: EntityPlayer, focus: ItemStack, hand: EnumHand): EnumActionResult {
                if (!ManaItemHandler.requestManaExact(focus, player, 1000, true)) return EnumActionResult.FAIL
                val cast = Helper.raycast(player, 16.0)

                val vec = Vector3.fromEntityCenter(player).toVec3D()
                val dist = if (cast == null || cast.typeOfHit == RayTraceResult.Type.MISS) 16.0 else cast.hitVec.distanceTo(vec)

                val to = Vector3(player.lookVec.scale(dist).add(vec))
                PacketHandler.NETWORK.sendToAllAround(FireJetMessage(Vector3.fromEntityCenter(player).toVec3D(), to.toVec3D()),
                        NetworkRegistry.TargetPoint(player.world.provider.dimension, player.posX, player.posY, player.posZ, 30.0))
                player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.PLAYERS, 1f, 1f)

                val from = Vector3.fromEntityCenter(player)

                val pos = NBTTagList()
                pos.appendTag(NBTTagDouble(from.x))
                pos.appendTag(NBTTagDouble(from.y))
                pos.appendTag(NBTTagDouble(from.z))
                ItemNBTHelper.setList(focus, TAG_SOURCE, pos)

                val target = NBTTagList()
                target.appendTag(NBTTagDouble(to.x))
                target.appendTag(NBTTagDouble(to.y))
                target.appendTag(NBTTagDouble(to.z))
                ItemNBTHelper.setList(focus, TAG_TARGET, target)
                return EnumActionResult.SUCCESS
            }

            override fun onCooldownTick(player: EntityPlayer, focus: ItemStack, slot: Int, selected: Boolean, cooldownRemaining: Int) {
                if (cooldownRemaining in 31 until 58) {
                    val source = ItemNBTHelper.getList(focus, TAG_SOURCE, NBTTypes.DOUBLE)?.run {
                        Vector3(getDoubleAt(0), getDoubleAt(1), getDoubleAt(2))
                    } ?: Vector3.ZERO
                    val target = ItemNBTHelper.getList(focus, TAG_TARGET, NBTTypes.DOUBLE)?.run {
                        Vector3(getDoubleAt(0), getDoubleAt(1), getDoubleAt(2))
                    } ?: Vector3.ZERO
                    jet(target, player, source)
                }
            }
        }
    }
}
