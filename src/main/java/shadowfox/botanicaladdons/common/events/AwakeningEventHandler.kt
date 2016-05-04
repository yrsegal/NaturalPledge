package shadowfox.botanicaladdons.common.events

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.SoundEvents
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.Style
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.util.text.TextFormatting
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.event.entity.living.LivingEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.ReflectionHelper
import shadowfox.botanicaladdons.api.IPriestlyEmblem
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.common.BotanicalAddons
import shadowfox.botanicaladdons.common.achievements.ModAchievements
import shadowfox.botanicaladdons.common.block.ModBlocks
import shadowfox.botanicaladdons.common.items.bauble.faith.ItemFaithBauble
import vazkii.botania.common.Botania
import vazkii.botania.common.core.helper.Vector3
import vazkii.botania.common.entity.EntityDoppleganger
import java.awt.Color
import java.util.*

/**
 * @author WireSegal
 * Created at 2:18 PM on 4/17/16.
 */
class AwakeningEventHandler {

    var registered = false

    companion object {
        private val INSTANCE = AwakeningEventHandler()

        fun register() {
            if (!INSTANCE.registered)
                MinecraftForge.EVENT_BUS.register(INSTANCE)
            INSTANCE.registered = true
        }

        val CORE_LOCATIONS = arrayOf(
                BlockPos(1, 2, 1),
                BlockPos(1, 2, -1),
                BlockPos(-1, 2, 1),
                BlockPos(-1, 2, -1)
        )
    }

    private fun getPlayersAround(entityDoppleganger: EntityDoppleganger): MutableList<EntityPlayer> {
        val source = entityDoppleganger.source
        val range = 15F
        return entityDoppleganger.worldObj.getEntitiesWithinAABB(EntityPlayer::class.java, AxisAlignedBB(source.x + 0.5 - range, source.y + 0.5 - range, source.z + 0.5 - range, source.x + 0.5 + range, source.y + 0.5 + range, source.z + 0.5 + range))
    }

    private fun fitsLocation(entityDoppleganger: EntityDoppleganger): Boolean {
        for (coords in CORE_LOCATIONS) {
            val pos = entityDoppleganger.source.add(coords)

            val state = entityDoppleganger.worldObj.getBlockState(pos)
            val block = state.block
            if (block != ModBlocks.awakenerCore) {
                return false;
            }
        }
        return true
    }

    @SubscribeEvent
    fun itBegins(e: EntityJoinWorldEvent) {
        val entity = e.entity

        if (entity is EntityDoppleganger && entity.isHardMode) {
            val fits = fitsLocation(entity)
            if (fits) {

                entity.entityData.setBoolean("divineBattle", true)

                val players = getPlayersAround(entity)

                if (entity.worldObj.isRemote)
                    for (player in players) {
                        val emblem = ItemFaithBauble.getEmblem(player)
                        if (emblem != null) {
                            val variant = (emblem.item as IPriestlyEmblem).getVariant(emblem)
                            if (variant != null)
                                player.addChatComponentMessage(TextComponentTranslation("misc.${LibMisc.MOD_ID}.${variant.name}Watches").setStyle(Style().setColor(TextFormatting.DARK_AQUA)))
                        }
                    }
            }
        }
    }

    @SubscribeEvent
    fun itContinues(e: LivingEvent.LivingUpdateEvent) {
        val entity = e.entityLiving

        if (entity is EntityDoppleganger && entity.isHardMode) {
            if (entity.entityData.hasKey("divineBattle") && entity.entityData.getBoolean("divineBattle")) {
                e.entityLiving.heal(0.02f) // 1 heart every five seconds, making it far harder to fight the Guardian. Consider it GGIII.
                if (entity.worldObj.isRemote) {
                    val pos = Vector3.fromEntityCenter(entity).subtract(Vector3(0.0, 0.2, 0.0))

                    val loc = BlockPos(0, 2, 0)

                    val pylonPos = Vector3(entity.source.x + loc.x.toDouble(), entity.source.y + loc.y.toDouble(), entity.source.z + loc.z.toDouble())
                    var worldTime = entity.ticksExisted / 5.0

                    val rad = 1.5f + Math.random().toFloat() * 0.1f
                    val xp = pylonPos.x + 0.5 + Math.cos(worldTime) * rad
                    val zp = pylonPos.z + 0.5 + Math.sin(worldTime) * rad

                    val partPos = Vector3(xp, pylonPos.y, zp)
                    val pyPos = Vector3(pylonPos.x + 0.5, pylonPos.y + 1, pylonPos.z + 0.5)
                    val pymot = pos.copy().sub(pyPos).multiply(0.04)

                    val color = BotanicalAddons.proxy.rainbow()
                    val r = color.red / 255f
                    val g = color.green / 255f
                    val b = color.blue / 255f

                    Botania.proxy.wispFX(entity.worldObj, partPos.x, partPos.y, partPos.z, r, g, b, 0.25f + Math.random().toFloat() * 0.1f, -0.075f - Math.random().toFloat() * 0.015f)
                    Botania.proxy.wispFX(entity.worldObj, pyPos.x, pyPos.y, pyPos.z, r, g, b, 0.4f, pymot.x.toFloat(), pymot.y.toFloat(), pymot.z.toFloat())
                }
            }
        }
    }

    @SubscribeEvent
    fun itEnds(e: LivingDeathEvent) {
        val entity = e.entityLiving

        if (entity is EntityDoppleganger && entity.isHardMode) {
            val fits = fitsLocation(entity)
            if (fits && entity.entityData.hasKey("divineBattle") && entity.entityData.getBoolean("divineBattle")) {
                val players = getPlayersAround(entity)

                val playersWhoAttacked = ReflectionHelper.getPrivateValue<List<UUID>, EntityDoppleganger>(EntityDoppleganger::class.java, entity, "playersWhoAttacked")

                entity.worldObj.playSound(entity.source.x.toDouble(), entity.source.y.toDouble(), entity.source.z.toDouble(), SoundEvents.entity_enderdragon_growl, SoundCategory.HOSTILE, 1.0f, 1.0f, false)

                for (player in players) {
                    if (player.uniqueID in playersWhoAttacked) {
                        val emblem = ItemFaithBauble.getEmblem(player)
                        if (emblem != null) {
                            val variant = (emblem.item as IPriestlyEmblem).getVariant(emblem)
                            if (variant != null) {
                                player.addStat(ModAchievements.awakening)
                                if (entity.worldObj.isRemote)
                                    player.addChatComponentMessage(TextComponentTranslation("misc.${LibMisc.MOD_ID}.${variant.name}Smiles").setStyle(Style().setColor(TextFormatting.DARK_AQUA)))
                                (emblem.item as IPriestlyEmblem).setAwakened(emblem, true)
                            }
                        }
                    }
                }
            }
        }
    }

}
