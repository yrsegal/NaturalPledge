package shadowfox.botanicaladdons.common.items.travel.stones

import com.teamwizardry.librarianlib.client.util.TooltipHelper.addToTooltip
import com.teamwizardry.librarianlib.common.base.item.IItemColorProvider
import com.teamwizardry.librarianlib.common.base.item.ItemMod
import com.teamwizardry.librarianlib.common.network.PacketHandler
import com.teamwizardry.librarianlib.common.util.ItemNBTHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.relauncher.Side
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.common.BotanicalAddons
import shadowfox.botanicaladdons.common.network.TargetPositionPacket
import vazkii.botania.api.sound.BotaniaSoundEvents
import vazkii.botania.api.wand.ICoordBoundItem
import vazkii.botania.common.Botania
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
        get() = { itemStack, i ->
            if (i == 1)
                BotanicalAddons.PROXY.rainbow(0.25f).rgb
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

        val LAST_KNOWN_POSITIONS = mutableMapOf<String, Pair<Int, Vec3d>>()

        @SubscribeEvent
        fun onWorldTick(e: TickEvent.WorldTickEvent) {
            if (e.phase == TickEvent.Phase.START && e.side == Side.CLIENT) {
                LAST_KNOWN_POSITIONS
            } else if (e.phase == TickEvent.Phase.END && e.side == Side.SERVER) {
                val names = linkedMapOf<String, Pair<Int, Vec3d>>()


                for (entity in e.world.playerEntities)
                    names.put(entity.displayNameString.toLowerCase(Locale.ROOT), e.world.provider.dimension to Vector3.fromEntityCenter(entity).subtract(Vector3(0.5, 0.5, 0.5)).toVec3D())

                val keys = names.keys.toTypedArray()
                val special = names.values.toList()
                val dims = special.map { it.first }.toTypedArray()
                val poses = special.map { it.second }.toTypedArray()
                PacketHandler.NETWORK.sendToAll(TargetPositionPacket(keys, dims, poses))
            }
        }

    }

    override fun addInformation(stack: ItemStack, playerIn: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
        val track = ItemNBTHelper.getString(stack, TAG_TRACK, null)
        val dirVec = getDirVec(stack, playerIn)
        val distance = Math.round((dirVec ?: Vector3.ZERO).mag()).toInt()
        if (track != null) {
            if (dirVec == null)
                addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.trackingNotHere", track)
            else if (distance < 5)
                addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.trackingClose", track)
            else
                addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.tracking", track, distance)
        } else if (getBinding(stack) != null) {
            if (distance < 5)
                addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.trackingBlockClose")
            else
                addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.trackingBlock", distance)
        }
    }

    override fun onItemUse(player: EntityPlayer, world: World, pos: BlockPos, hand: EnumHand?, side: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        val stack = player.getHeldItem(hand)
        if (player.isSneaking && hand == EnumHand.MAIN_HAND) {
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
                ItemNBTHelper.setInt(stack, TAG_X, pos.x)
                ItemNBTHelper.setInt(stack, TAG_Y, pos.y)
                ItemNBTHelper.setInt(stack, TAG_Z, pos.z)
                ItemNBTHelper.removeEntry(stack, TAG_TRACK)
                world.playSound(player, player.posX, player.posY, player.posZ, BotaniaSoundEvents.ding, SoundCategory.PLAYERS, 1f, 5f)
                return EnumActionResult.SUCCESS
            }
        }

        return EnumActionResult.PASS
    }

    override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        val stack = player.getHeldItem(hand)
        if (player.isSneaking && hand == EnumHand.MAIN_HAND) {
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
                ItemNBTHelper.removeEntry(stack, TAG_X)
                ItemNBTHelper.removeEntry(stack, TAG_Y)
                ItemNBTHelper.removeEntry(stack, TAG_Z)
                ItemNBTHelper.removeEntry(stack, TAG_TRACK)
                world.playSound(player, player.posX, player.posY, player.posZ, BotaniaSoundEvents.ding, SoundCategory.PLAYERS, 1f, 5f)
                return ActionResult(EnumActionResult.SUCCESS, stack)
            }
        }
        return ActionResult(EnumActionResult.PASS, stack)
    }

    override fun onUpdate(stack: ItemStack, worldIn: World, entityIn: Entity, itemSlot: Int, isSelected: Boolean) {

        if (stack.hasDisplayName() && stack.displayName.toLowerCase(Locale.ROOT).trim().matches("^track[:\\s]\\s*.+$".toRegex())) {
            ItemNBTHelper.setString(stack, TAG_TRACK, stack.displayName.trim().replace("^track:?".toRegex(), "").trim())
            stack.clearCustomName()
        }

        if (entityIn !is EntityPlayer || entityIn.heldItemMainhand != stack && entityIn.heldItemOffhand != stack) return

        val startVec = Vector3.fromEntityCenter(entityIn)
        val dirVec = getDirVec(stack, entityIn) ?: return
        val endVec = startVec.add(dirVec.normalize().multiply(Math.min(dirVec.mag(), 10.0)))

        Botania.proxy.setWispFXDepthTest(false)
        BotanicalAddons.PROXY.particleStream(startVec.add(dirVec.normalize()).add(0.0, 0.5, 0.0), endVec, BotanicalAddons.PROXY.wireFrameRainbow().rgb)
        Botania.proxy.setWispFXDepthTest(true)
    }

    fun getDirVec(stack: ItemStack, player: Entity): Vector3? {
        val pos = getEndVec(player, stack) ?: return null

        val entityPos = Vector3.fromEntityCenter(player).subtract(Vector3(0.5, 0.5, 0.5))
        return pos.subtract(entityPos)
    }

    fun getEndVec(player: Entity, stack: ItemStack): Vector3? {
        var pos: Vector3? = null
        val track = ItemNBTHelper.getString(stack, TAG_TRACK, null)
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
        if (ItemNBTHelper.getString(stack, TAG_TRACK, null) != null) return null
        val x = ItemNBTHelper.getInt(stack, TAG_X, 0)
        val y = ItemNBTHelper.getInt(stack, TAG_Y, -1)
        val z = ItemNBTHelper.getInt(stack, TAG_Z, 0)
        return if (y == -1) null else BlockPos(x, y, z)
    }
}
