package com.wiresegal.naturalpledge.common.items.travel.stones

import com.teamwizardry.librarianlib.core.LibrarianLib
import com.teamwizardry.librarianlib.features.base.item.IItemColorProvider
import com.teamwizardry.librarianlib.features.base.item.ItemMod
import com.teamwizardry.librarianlib.features.helpers.*
import com.teamwizardry.librarianlib.features.network.PacketHandler
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper.addToTooltip
import com.wiresegal.naturalpledge.api.lib.LibMisc
import com.wiresegal.naturalpledge.common.NaturalPledge
import com.wiresegal.naturalpledge.common.items.bauble.faith.ItemFaithBauble
import com.wiresegal.naturalpledge.common.network.ParticleStreamPacket
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.relauncher.Side
import vazkii.botania.api.wand.ICoordBoundItem
import vazkii.botania.common.Botania
import vazkii.botania.common.core.handler.ModSounds
import vazkii.botania.common.core.helper.Vector3
import java.util.*

/**
 * @author WireSegal
 * Created at 8:31 PM on 5/5/16.
 */
class ItemWaystone(name: String) : ItemMod(name), ICoordBoundItem, IItemColorProvider {

    init {
        setMaxStackSize(1)
    }

    override val itemColorFunction: ((ItemStack, Int) -> Int)?
        get() = { _, i ->
            if (i == 1)
                NaturalPledge.PROXY.rainbow(0.25f)
            else 0xFFFFFF
        }

    companion object {
        init {
            MinecraftForge.EVENT_BUS.register(this)
        }

        const val TAG_X = "x"
        const val TAG_Y = "y"
        const val TAG_Z = "z"
        const val TAG_TRACK = "player"
        const val TAG_NO_RESET = "immutable"

        @SubscribeEvent
        fun onWorldTick(e: TickEvent.WorldTickEvent) {
            if (e.phase == TickEvent.Phase.END && e.side == Side.SERVER) {
                val names = linkedMapOf<String, EntityPlayer>()

                for (entity in e.world.playerEntities) {
                    if (ItemFaithBauble.isFaithless(entity))
                        continue

                    names[entity.name.toLowerCase(Locale.ROOT)] = entity
                }

                for (entity in e.world.playerEntities) {
                    val mainHand = entity.heldItemMainhand
                    val offHand = entity.heldItemOffhand
                    streamForStack(mainHand, names, entity)
                    streamForStack(offHand, names, entity)

                }
            }
        }

        fun streamForStack(stack: ItemStack, names: Map<String, EntityPlayer>, player: EntityPlayer) {
            if (stack.isEmpty || stack.item !is ItemWaystone)
                return
            val track = stack.getNBTString(TAG_TRACK) ?: return

            val other = names[track] ?: return
            val dist = other.getDistance(player).toDouble()

            if (dist > 1000)
                return

            val positionPlayer = player.positionVector
            val positionTarget = other.positionVector

            val direction = positionTarget.subtract(positionPlayer).normalize()

            val targetPlayer = positionPlayer.add(direction.scale(Math.max(1.0, Math.min(dist, 10.0))))
            val returnTarget = positionPlayer.add(direction.scale(-Math.max(1.0, Math.min(dist, 10.0))))

            if (player is EntityPlayerMP)
                PacketHandler.NETWORK.sendTo(ParticleStreamPacket(positionPlayer.add(direction).add(0.0, 0.5, 0.0), targetPlayer),
                        player)

            if (other is EntityPlayerMP)
                PacketHandler.NETWORK.sendTo(ParticleStreamPacket(positionTarget.subtract(direction).add(0.0, 0.5, 0.0), returnTarget),
                        other)

        }

    }

    override fun addInformation(stack: ItemStack, worldIn: World?, tooltip: MutableList<String>, flagIn: ITooltipFlag?) {
        val track = stack.getNBTString(TAG_TRACK)
        if (Minecraft.getMinecraft().player != null) {
            val dirVec = getDirVec(stack, LibrarianLib.PROXY.getClientPlayer())
            val distance = Math.round((dirVec ?: Vector3.ZERO).mag()).toInt()
            if (track != null) {
                if (dirVec == null)
                    addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.tracking_not_here", track)
                else if (distance < 5)
                    addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.tracking_close", track)
                else if (distance > 1000)
                    addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.tracking_far", track)
                else
                    addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.tracking", track, distance)
            } else if (getBinding(stack) != null) {
                if (distance < 5)
                    addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.tracking_block_close")
                else if (distance > 1000)
                    addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.tracking_block_far", distance)
                else
                    addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.tracking_block", distance)
            }
        }
    }

    override fun onItemUse(player: EntityPlayer, world: World, pos: BlockPos, hand: EnumHand?, side: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        val stack = player.getHeldItem(hand)
        if (player.isSneaking && hand == EnumHand.MAIN_HAND) {
            if (stack.getNBTBoolean(TAG_NO_RESET, false))
                return EnumActionResult.PASS

            if (world.isRemote) {
                player.swingArm(hand)
                for (i in 0..9) {
                    val x1 = (pos.x + Math.random()).toFloat()
                    val y1 = (pos.y + 1).toFloat()
                    val z1 = (pos.z + Math.random()).toFloat()
                    Botania.proxy.wispFX(x1.toDouble(), y1.toDouble(), z1.toDouble(), Math.random().toFloat(), Math.random().toFloat(), Math.random().toFloat(), Math.random().toFloat() * 0.5f, -0.05f + Math.random().toFloat() * 0.05f)
                }
                return EnumActionResult.SUCCESS
            } else {
                stack.setNBTInt(TAG_X, pos.x)
                stack.setNBTInt(TAG_Y, pos.y)
                stack.setNBTInt(TAG_Z, pos.z)
                stack.removeNBTEntry(TAG_TRACK)
                world.playSound(player, player.posX, player.posY, player.posZ, ModSounds.ding, SoundCategory.PLAYERS, 1f, 5f)
                return EnumActionResult.SUCCESS
            }
        }

        return EnumActionResult.PASS
    }

    override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        val stack = player.getHeldItem(hand)
        if (player.isSneaking && hand == EnumHand.MAIN_HAND) {
            if (stack.getNBTBoolean(TAG_NO_RESET, false))
                return ActionResult(EnumActionResult.PASS, stack)

            if (world.isRemote) {
                player.swingArm(hand)
                for (i in 0..9) {
                    val x1 = (player.posX + Math.random()).toFloat()
                    val y1 = (player.posY + 1).toFloat()
                    val z1 = (player.posZ + Math.random()).toFloat()
                    Botania.proxy.wispFX(x1.toDouble(), y1.toDouble(), z1.toDouble(), Math.random().toFloat(), Math.random().toFloat(), Math.random().toFloat(), Math.random().toFloat() * 0.5f, -0.05f + Math.random().toFloat() * 0.05f)
                    return ActionResult(EnumActionResult.SUCCESS, stack)
                }
            } else {
                stack.removeNBTEntry(TAG_X)
                stack.removeNBTEntry(TAG_Y)
                stack.removeNBTEntry(TAG_Z)
                stack.removeNBTEntry(TAG_TRACK)
                world.playSound(player, player.posX, player.posY, player.posZ, ModSounds.ding, SoundCategory.PLAYERS, 1f, 5f)
                return ActionResult(EnumActionResult.SUCCESS, stack)
            }
        }
        return ActionResult(EnumActionResult.PASS, stack)
    }

    override fun onUpdate(stack: ItemStack, worldIn: World, entityIn: Entity, itemSlot: Int, isSelected: Boolean) {

        val noReset = stack.getNBTBoolean(TAG_NO_RESET, false)
        if (!noReset && stack.hasDisplayName() && stack.displayName.toLowerCase(Locale.ROOT).trim().matches("^track[:\\s]\\s*.+$".toRegex())) {
            val name = stack.displayName.trim().replace("^track:?".toRegex(), "").trim()
            val player = worldIn.playerEntities.firstOrNull { name == it.name }
            if (player != null) {
                stack.setNBTString(TAG_TRACK, name)
                player.sendMessage(TextComponentTranslation("naturalpledge.you_are_tracked"))
                stack.clearCustomName()
            }
        }

        if (entityIn !is EntityPlayer || entityIn.heldItemMainhand != stack && entityIn.heldItemOffhand != stack) return

        val startVec = Vector3.fromEntityCenter(entityIn)
        val dirVec = getDirVec(stack, entityIn) ?: return
        val endVec = startVec.add(dirVec.normalize().multiply(Math.min(dirVec.mag(), 10.0)))

        Botania.proxy.setWispFXDepthTest(false)
        NaturalPledge.PROXY.particleStream(startVec.add(dirVec.normalize()).add(0.0, 0.5, 0.0), endVec, NaturalPledge.PROXY.wireFrameRainbow())
        Botania.proxy.setWispFXDepthTest(true)
    }

    fun getDirVec(stack: ItemStack, player: Entity): Vector3? {
        val pos = getEndVec(stack) ?: return null

        val entityPos = Vector3.fromEntityCenter(player).subtract(Vector3(0.5, 0.5, 0.5))
        return pos.subtract(entityPos)
    }

    fun getEndVec(stack: ItemStack): Vector3? {
        val track = stack.getNBTString(TAG_TRACK)
        if (track == null && getBinding(stack) != null)
            return Vector3.fromBlockPos(getBinding(stack))
        return null
    }

    override fun getBinding(stack: ItemStack): BlockPos? {
        if (stack.getNBTString(TAG_TRACK) != null) return null
        val x = stack.getNBTInt(TAG_X, 0)
        val y = stack.getNBTInt(TAG_Y, -1)
        val z = stack.getNBTInt(TAG_Z, 0)
        return if (y == -1) null else BlockPos(x, y, z)
    }
}
