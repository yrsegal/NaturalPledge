package shadowfox.botanicaladdons.common.items.bauble.faith

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.IProjectile
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.world.World
import shadowfox.botanicaladdons.common.core.BASoundEvents
import shadowfox.botanicaladdons.common.items.ItemSpellIcon
import shadowfox.botanicaladdons.common.items.ItemTerrestrialFocus
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
    object Njord {
        class Leap : ItemTerrestrialFocus.IFocusSpell {
            override val iconStack: ItemStack
                get() = ItemSpellIcon.of(ItemSpellIcon.Variants.LEAP)

            override fun onCast(player: EntityPlayer, focus: ItemStack, hand: EnumHand): Boolean {
                if (ManaItemHandler.requestManaExact(focus, player, 20, true)) {
                    val look = player.lookVec
                    player.motionX = Math.max(Math.min(look.xCoord * 0.75 + player.motionX, 2.0), -2.0)
                    player.motionY = Math.max(Math.min(look.yCoord * 0.75 + player.motionY, 2.0), -2.0)
                    player.motionZ = Math.max(Math.min(look.zCoord * 0.75 + player.motionZ, 2.0), -2.0)
                    player.fallDistance = 0f

                    return true
                }
                return false
            }
        }

        //////////

        class Interdict : ItemTerrestrialFocus.IFocusSpell {
            override val iconStack: ItemStack
                get() = ItemSpellIcon.of(ItemSpellIcon.Variants.INTERDICT)

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
    }
}
