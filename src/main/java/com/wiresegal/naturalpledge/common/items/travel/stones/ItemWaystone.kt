package com.wiresegal.naturalpledge.common.items.travel.stones

import com.teamwizardry.librarianlib.core.LibrarianLib
import com.teamwizardry.librarianlib.features.base.item.IItemColorProvider
import com.teamwizardry.librarianlib.features.base.item.ItemMod
import com.teamwizardry.librarianlib.features.helpers.*
import com.teamwizardry.librarianlib.features.network.PacketHandler
import com.teamwizardry.librarianlib.features.network.sendToAllAround
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper.addToTooltip
import com.wiresegal.naturalpledge.api.lib.LibMisc
import com.wiresegal.naturalpledge.common.NaturalPledge
import com.wiresegal.naturalpledge.common.items.bauble.faith.ItemFaithBauble
import com.wiresegal.naturalpledge.common.network.ParticleStreamMessage
import com.wiresegal.naturalpledge.common.network.TargetPositionPacket
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
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
                NaturalPledge.PROXY.rainbow(0.25f).rgb
            else 0xFFFFFF
        }

    companion object {
        init {
            MinecraftForge.EVENT_BUS.register(this)
        }

        val TAG_X = "x"
        val TAG_Y = "y"
        val TAG_Z = "z"
        val TAG_TRACK = "player"
        val TAG_NO_RESET = "immutable"

        val LAST_KNOWN_POSITIONS = mutableMapOf<String, Pair<Int, Vec3d>>()

        @SubscribeEvent
        fun onWorldTick(e: TickEvent.WorldTickEvent) {
            if (e.phase == TickEvent.Phase.START && e.side == Side.CLIENT) {
                LAST_KNOWN_POSITIONS.clear()
            } else if (e.phase == TickEvent.Phase.END && e.side == Side.SERVER) {
                val names = linkedMapOf<String, Pair<Int, Vec3d>>()

                for (entity in e.world.playerEntities) {
                    if (ItemFaithBauble.isFaithless(entity))
                        continue

                    names[entity.displayNameString.toLowerCase(Locale.ROOT)] = e.world.provider.dimension to Vector3.fromEntityCenter(entity).subtract(Vector3(0.5, 0.5, 0.5)).toVec3D()
                }

                val keys = names.keys.toTypedArray()
                val special = names.values.toList()
                val dims = special.map { it.first }.toTypedArray()
                val poses = special.map { it.second }.toTypedArray()
                PacketHandler.NETWORK.sendToAll(TargetPositionPacket(keys, dims, poses))
            }
        }

    }

    override fun addInformation(stack: ItemStack, worldIn: World?, tooltip: MutableList<String>, flagIn: ITooltipFlag?) {
        val track = stack.getNBTString(TAG_TRACK) ?: null
        if (Minecraft.getMinecraft().player != null) {
            val dirVec = getDirVec(stack, LibrarianLib.PROXY.getClientPlayer())
            val distance = Math.round((dirVec ?: Vector3.ZERO).mag()).toInt()
            if (track != null) {
                if (dirVec == null)
                    addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.tracking_not_here", track)
                else if (distance < 5)
                    addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.tracking_close", track)
                else
                    addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.tracking", track, distance)
            } else if (getBinding(stack) != null) {
                if (distance < 5)
                    addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.tracking_block_close")
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

        if (stack.getNBTString(TAG_TRACK) != null) {

            val target = getEndVec(entityIn, stack)
            if (target != null) {
                val targetEnd = target.add(startVec.normalize().multiply(Math.min(startVec.mag(), 10.0))).toVec3D()
                PacketHandler.NETWORK.sendToAllAround(ParticleStreamMessage(target.add(startVec.normalize()).add(0.0, 0.5, 0.0).toVec3D(), targetEnd),
                        entityIn.world, targetEnd, 64)
            }
        }

        Botania.proxy.setWispFXDepthTest(false)
        NaturalPledge.PROXY.particleStream(startVec.add(dirVec.normalize()).add(0.0, 0.5, 0.0), endVec, NaturalPledge.PROXY.wireFrameRainbow().rgb)
        Botania.proxy.setWispFXDepthTest(true)
    }

    fun getDirVec(stack: ItemStack, player: Entity): Vector3? {
        val pos = getEndVec(player, stack) ?: return null

        val entityPos = Vector3.fromEntityCenter(player).subtract(Vector3(0.5, 0.5, 0.5))
        return pos.subtract(entityPos)
    }

    fun getEndVec(player: Entity, stack: ItemStack): Vector3? {
        var pos: Vector3? = null
        val track = stack.getNBTString(TAG_TRACK)
        if (track != null) {
            if (player.world.isRemote) {
                val lastKnown = LAST_KNOWN_POSITIONS[track.toLowerCase(Locale.ROOT)] ?: return null
                if (lastKnown.first != player.world.provider.dimension) return null
                return Vector3(lastKnown.second)
            } else return null
        } else {
            if (getBinding(stack) != null)
                pos = Vector3.fromBlockPos(getBinding(stack))
        }
        return pos
    }

    override fun getBinding(stack: ItemStack): BlockPos? {
        if (stack.getNBTString(TAG_TRACK) != null) return null
        val x = stack.getNBTInt(TAG_X, 0)
        val y = stack.getNBTInt(TAG_Y, -1)
        val z = stack.getNBTInt(TAG_Z, 0)
        return if (y == -1) null else BlockPos(x, y, z)
    }
}
