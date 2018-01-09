package shadowfox.botanicaladdons.common.events

import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.init.SoundEvents
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.text.Style
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.util.text.TextFormatting
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.event.entity.living.LivingEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import shadowfox.botanicaladdons.api.item.IPriestlyEmblem
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.common.BotanicalAddons
import shadowfox.botanicaladdons.common.block.ModBlocks
import shadowfox.botanicaladdons.common.core.helper.BAMethodHandles
import shadowfox.botanicaladdons.common.items.ItemResource
import shadowfox.botanicaladdons.common.items.bauble.faith.ItemFaithBauble
import shadowfox.botanicaladdons.common.items.bauble.faith.ItemRagnarokPendant
import vazkii.botania.common.Botania
import vazkii.botania.common.core.helper.PlayerHelper
import vazkii.botania.common.core.helper.Vector3
import vazkii.botania.common.entity.EntityDoppleganger

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
        return entityDoppleganger.world.getEntitiesWithinAABB(EntityPlayer::class.java, AxisAlignedBB(source.x + 0.5 - range, source.y + 0.5 - range, source.z + 0.5 - range, source.x + 0.5 + range, source.y + 0.5 + range, source.z + 0.5 + range))
    }

    private fun fitsLocation(entityDoppleganger: EntityDoppleganger): Boolean {
        return CORE_LOCATIONS
                .map { entityDoppleganger.source.add(it) }
                .map { entityDoppleganger.world.getBlockState(it) }
                .map { it.block }
                .none { it != ModBlocks.awakenerCore }
    }

    @SubscribeEvent
    fun itBegins(e: EntityJoinWorldEvent) {
        val entity = e.entity

        if (entity is EntityDoppleganger && entity.isHardMode) {
            val fits = fitsLocation(entity)
            if (fits) {

                entity.entityData.setByte("divineBattle", 1)

                val players = getPlayersAround(entity)

                if (!entity.world.isRemote)
                    for (player in players) {
                        val emblem = ItemFaithBauble.getEmblem(player)
                        if (emblem != null) {
                            val variant = (emblem.item as IPriestlyEmblem).getVariant(emblem)
                            if (variant != null) {
                                player.sendMessage(TextComponentTranslation("misc.${LibMisc.MOD_ID}.${variant.name}_watches").setStyle(Style().setColor(TextFormatting.DARK_AQUA)))
                                if (variant.name == "ragnarok")
                                    entity.entityData.setByte("divineBattle", 2)
                            }
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
                val divineBattle = entity.entityData.getInteger("divineBattle")
                e.entityLiving.heal(if (divineBattle == 1) 0.02f else 0.04f) // 1 heart every five seconds, making it far harder to fight the Guardian. Consider it GGIII.
                if (entity.world.isRemote) {
                    val pos = Vector3.fromEntityCenter(entity).subtract(Vector3(0.0, 0.2, 0.0))

                    val loc = BlockPos(0, 2, 0)

                    val pylonPos = Vector3(entity.source.x + loc.x.toDouble(), entity.source.y + loc.y.toDouble(), entity.source.z + loc.z.toDouble())
                    val worldTime = entity.ticksExisted / 5.0

                    val rad = 1.5f + Math.random().toFloat() * 0.1f
                    val xp = pylonPos.x + 0.5 + MathHelper.cos(worldTime.toFloat()) * rad
                    val zp = pylonPos.z + 0.5 + MathHelper.sin(worldTime.toFloat()) * rad

                    val partPos = Vector3(xp, pylonPos.y, zp)
                    val pyPos = Vector3(pylonPos.x + 0.5, pylonPos.y + 1, pylonPos.z + 0.5)

                    val color = BotanicalAddons.PROXY.rainbow(entity.source)
                    val r = color.red / 255f
                    val g = color.green / 255f
                    val b = color.blue / 255f

                    BotanicalAddons.PROXY.particleStream(pyPos, pos, color.rgb)
                    Botania.proxy.wispFX(partPos.x, partPos.y, partPos.z, r, g, b, 0.25f + Math.random().toFloat() * 0.1f, -0.075f - Math.random().toFloat() * 0.015f)
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

                val playersWhoAttacked = BAMethodHandles.getPlayersWhoAttacked(entity)

                entity.world.playSound(entity.source.x.toDouble(), entity.source.y.toDouble(), entity.source.z.toDouble(), SoundEvents.ENTITY_ENDERDRAGON_GROWL, SoundCategory.HOSTILE, 1.0f, 1.0f, false)

                for (player in players) {
                    if (player.uniqueID in playersWhoAttacked) {
                        val emblem = ItemFaithBauble.getEmblem(player)
                        if (emblem != null) {
                            val variant = (emblem.item as IPriestlyEmblem).getVariant(emblem)
                            if (variant != null) {
                                if (!entity.world.isRemote)
                                PlayerHelper.grantCriterion(player as EntityPlayerMP?, ResourceLocation(LibMisc.MOD_ID, "botanicaladdons/awakening"), "code_triggered")
                                if (entity.world.isRemote)
                                    player.sendMessage(TextComponentTranslation("misc.${LibMisc.MOD_ID}.${variant.name}_smiles").setStyle(Style().setColor(TextFormatting.DARK_AQUA)))
                                (emblem.item as IPriestlyEmblem).setAwakened(emblem, true)
                                if (variant == ItemRagnarokPendant.Ragnarok)
                                    if (!entity.world.isRemote)
                                        PlayerHelper.grantCriterion(player as EntityPlayerMP?, ResourceLocation(LibMisc.MOD_ID, "botanicaladdons/begin_ragnarok"), "code_triggered")
                            }
                        }
                    }
                }

                if (!entity.world.isRemote) {
                    val divineBattle = entity.entityData.getInteger("divineBattle")
                    if (divineBattle > 1) {
                        var amount = playersWhoAttacked.size
                        for (i in 0..amount / 64) {
                            val stack = ItemResource.of(ItemResource.Variants.GOD_SOUL, true, if (amount > 64) 64 else amount)
                            val item = EntityItem(entity.world, entity.posX, entity.posY, entity.posZ, stack)
                            entity.world.spawnEntity(item)
                            amount -= 64
                        }
                    }
                }
            }
        }
    }

}
