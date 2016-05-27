package shadowfox.botanicaladdons.common.items.travel

import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.client.core.ModelHandler
import shadowfox.botanicaladdons.common.BotanicalAddons
import shadowfox.botanicaladdons.common.items.base.ItemMod
import vazkii.botania.api.sound.BotaniaSoundEvents
import vazkii.botania.api.wand.ICoordBoundItem
import vazkii.botania.common.Botania
import vazkii.botania.common.core.helper.ItemNBTHelper
import vazkii.botania.common.core.helper.Vector3

/**
 * @author WireSegal
 * Created at 8:31 PM on 5/5/16.
 */
class ItemWaystone(name: String) : ItemMod(name), ICoordBoundItem, ModelHandler.IColorProvider {

    init {
        setMaxStackSize(1)
    }

    @SideOnly(Side.CLIENT)
    override fun getColor(): IItemColor? {
        return IItemColor { itemStack, i ->
            if (i == 1)
                BotanicalAddons.Companion.proxy.rainbow(0.25f).rgb
            else 0xFFFFFF
        }
    }

    companion object {
        val TAG_X = "x"
        val TAG_Y = "y"
        val TAG_Z = "z"
        val TAG_TRACK = "player"
    }

    override fun addInformation(stack: ItemStack, playerIn: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
        val track = ItemNBTHelper.getString(stack, TAG_TRACK, null)
        val dirVec = getDirVec(playerIn.worldObj, stack, playerIn)
        val distance = Math.round((dirVec ?: Vector3.zero).mag()).toInt()
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

    override fun onItemUse(stack: ItemStack?, player: EntityPlayer, world: World, pos: BlockPos, hand: EnumHand?, side: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        if (player.isSneaking && ItemNBTHelper.getString(stack, TAG_TRACK, null) == null) {
            if (world.isRemote) {
                player.swingArm(hand)
                for (i in 0..9) {
                    val x1 = (pos.x + Math.random()).toFloat()
                    val y1 = (pos.y + 1).toFloat()
                    val z1 = (pos.z + Math.random()).toFloat()
                    Botania.proxy.wispFX(player.worldObj, x1.toDouble(), y1.toDouble(), z1.toDouble(), Math.random().toFloat(), Math.random().toFloat(), Math.random().toFloat(), Math.random().toFloat() * 0.5f, -0.05f + Math.random().toFloat() * 0.05f)
                    return EnumActionResult.SUCCESS
                }
            } else {
                ItemNBTHelper.setInt(stack, TAG_X, pos.x)
                ItemNBTHelper.setInt(stack, TAG_Y, pos.y)
                ItemNBTHelper.setInt(stack, TAG_Z, pos.z)
                world.playSound(player, player.posX, player.posY, player.posZ, BotaniaSoundEvents.ding, SoundCategory.PLAYERS, 1f, 5f)
                return EnumActionResult.SUCCESS
            }
        }

        return EnumActionResult.PASS
    }

    override fun onUpdate(stack: ItemStack, worldIn: World, entityIn: Entity, itemSlot: Int, isSelected: Boolean) {

        if (stack.hasDisplayName() && stack.displayName.toLowerCase().matches("^track:?\\s+".toRegex())) {
            ItemNBTHelper.setString(stack, TAG_TRACK, stack.displayName.replace("^track:?".toRegex(), "").trim())
            stack.clearCustomName()
        }

        if (!worldIn.isRemote || entityIn !is EntityLivingBase || entityIn.heldItemMainhand != stack && entityIn.heldItemOffhand != stack) return

        val startVec = Vector3.fromEntityCenter(entityIn)
        val dirVec = getDirVec(worldIn, stack, entityIn) ?: return
        val endVec = startVec.copy().add(dirVec.copy().normalize().multiply(Math.min(dirVec.mag(), 10.0)))

        BotanicalAddons.proxy.particleStream(worldIn, startVec.copy().add(dirVec.copy().normalize()).add(0.0, 0.5, 0.0), endVec, BotanicalAddons.proxy.wireFrameRainbow().rgb)
    }

    fun getDirVec(world: World, stack: ItemStack, player: Entity): Vector3? {
        val pos = getEndVec(world, stack) ?: return null

        val entityPos = Vector3.fromEntityCenter(player).sub(Vector3(0.5, 0.5, 0.5))
        return pos.copy().sub(entityPos)
    }

    fun getEndVec(world: World, stack: ItemStack): Vector3? {
        var pos: Vector3? = null
        val track = ItemNBTHelper.getString(stack, TAG_TRACK, null)
        if (track != null) {
            for (other in world.playerEntities) {
                if (other.name == track) {
                    pos = Vector3.fromEntityCenter(other).sub(Vector3(0.5, 0.5, 0.5))
                    break
                }
            }
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
