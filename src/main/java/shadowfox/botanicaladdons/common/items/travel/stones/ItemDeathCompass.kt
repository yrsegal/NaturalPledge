package shadowfox.botanicaladdons.common.items.travel.stones

import com.teamwizardry.librarianlib.client.util.TooltipHelper.addToTooltip
import com.teamwizardry.librarianlib.common.base.item.IItemColorProvider
import com.teamwizardry.librarianlib.common.base.item.ItemMod
import com.teamwizardry.librarianlib.common.util.ItemNBTHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.event.entity.player.PlayerDropsEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.common.BotanicalAddons
import vazkii.botania.api.sound.BotaniaSoundEvents
import vazkii.botania.api.wand.ICoordBoundItem
import vazkii.botania.common.Botania
import vazkii.botania.common.core.helper.Vector3
import java.util.*

/**
 * @author WireSegal
 * Created at 10:02 PM on 6/8/16.
 */
class ItemDeathCompass(name: String) : ItemMod(name), ICoordBoundItem, IItemColorProvider {

    init {
        setMaxStackSize(1)
        MinecraftForge.EVENT_BUS.register(this)
    }

    override val itemColorFunction: ((ItemStack, Int) -> Int)?
        get() = { itemStack, i ->
            if (i == 1)
                BotanicalAddons.PROXY.rainbow(0.25f).rgb
            else 0xFFFFFF
        }

    override fun addInformation(stack: ItemStack, playerIn: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
        val dirVec = getDirVec(stack, playerIn)
        val distance = Math.round((dirVec ?: Vector3.ZERO).mag()).toInt()
        if (getBinding(stack) != null) {
            if (distance < 5)
                addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.trackingBlockClose")
            else
                addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.trackingBlock", distance)
        }
    }

    override fun onUpdate(stack: ItemStack, worldIn: World, entityIn: Entity, itemSlot: Int, isSelected: Boolean) {
        if (!worldIn.isRemote || entityIn !is EntityLivingBase || entityIn.heldItemMainhand != stack && entityIn.heldItemOffhand != stack) return

        val startVec = Vector3.fromEntityCenter(entityIn)
        val dirVec = getDirVec(stack, entityIn) ?: return
        val endVec = startVec.add(dirVec.normalize().multiply(Math.min(dirVec.mag(), 10.0)))

        Botania.proxy.setWispFXDepthTest(false)
        BotanicalAddons.PROXY.particleStream(startVec.add(dirVec.normalize()).add(0.0, 0.5, 0.0), endVec, BotanicalAddons.PROXY.wireFrameRainbow().rgb)
        Botania.proxy.setWispFXDepthTest(true)
    }

    override fun onItemRightClick(stack: ItemStack, worldIn: World, player: EntityPlayer, hand: EnumHand?): ActionResult<ItemStack>? {
        if (player.isSneaking && getBinding(stack) != null && hand == EnumHand.MAIN_HAND) {
            ItemNBTHelper.removeEntry(stack, TAG_X)
            ItemNBTHelper.removeEntry(stack, TAG_Y)
            ItemNBTHelper.removeEntry(stack, TAG_Z)
            worldIn.playSound(player, player.posX, player.posY, player.posZ, BotaniaSoundEvents.ding, SoundCategory.PLAYERS, 1f, 5f)
        }

        return super.onItemRightClick(stack, worldIn, player, hand)
    }

    fun getDirVec(stack: ItemStack, player: Entity): Vector3? {
        val pos = getEndVec(stack) ?: return null

        val entityPos = Vector3.fromEntityCenter(player).subtract(Vector3(0.5, 0.5, 0.5))
        return pos.subtract(entityPos)
    }

    fun getEndVec(stack: ItemStack): Vector3? {
        return Vector3.fromBlockPos(getBinding(stack) ?: return null)
    }

    override fun getBinding(stack: ItemStack): BlockPos? {
        val x = ItemNBTHelper.getInt(stack, TAG_X, 0)
        val y = ItemNBTHelper.getInt(stack, TAG_Y, Int.MIN_VALUE)
        val z = ItemNBTHelper.getInt(stack, TAG_Z, 0)
        return if (y == Int.MIN_VALUE) null else BlockPos(x, y, z)
    }

    @SubscribeEvent
    fun onPlayerDeath(event: LivingDeathEvent) {
        val entity = event.entityLiving
        if (entity is EntityPlayer && entity.worldObj.gameRules.getBoolean("keepInventory")) {
            for (i in 0..entity.inventory.sizeInventory - 1) {
                val stack = entity.inventory.getStackInSlot(i)
                if (stack != null && stack.item == this) {
                    ItemNBTHelper.setInt(stack, TAG_X, (entity.posX - 0.5).toInt())
                    ItemNBTHelper.setInt(stack, TAG_Y, (entity.posY - 0.5).toInt())
                    ItemNBTHelper.setInt(stack, TAG_Z, (entity.posZ - 0.5).toInt())
                }
            }
        }
    }

    @SubscribeEvent
    fun onPlayerDrops(event: PlayerDropsEvent) {
        val keeps = ArrayList<EntityItem>()
        for (item in event.drops) {
            val stack = item.entityItem
            if (stack != null && stack.item == this) {
                keeps.add(item)
                ItemNBTHelper.setInt(stack, TAG_X, (event.entityPlayer.posX - 0.5).toInt())
                ItemNBTHelper.setInt(stack, TAG_Y, (event.entityPlayer.posY - 0.5).toInt())
                ItemNBTHelper.setInt(stack, TAG_Z, (event.entityPlayer.posZ - 0.5).toInt())
            }
        }

        if (event.entityPlayer.worldObj.gameRules.getBoolean("keepInventory"))
            return

        if (keeps.size > 0) {
            event.drops.removeAll(keeps)

            val cmp = NBTTagCompound()
            cmp.setInteger(TAG_DROP_COUNT, keeps.size)

            for ((i, keep) in keeps.withIndex()) {
                val stack = keep.entityItem
                val cmp1 = NBTTagCompound()
                stack.writeToNBT(cmp1)
                cmp.setTag(TAG_DROP_PREFIX + i, cmp1)
            }

            val data = event.entityPlayer.entityData
            if (!data.hasKey(EntityPlayer.PERSISTED_NBT_TAG))
                data.setTag(EntityPlayer.PERSISTED_NBT_TAG, NBTTagCompound())

            val persist = data.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG)
            persist.setTag(TAG_PLAYER_KEPT_DROPS, cmp)
        }
    }

    @SubscribeEvent
    fun onPlayerRespawn(event: PlayerEvent.PlayerRespawnEvent) {
        val data = event.player.entityData
        if (data.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
            val cmp = data.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG)
            val cmp1 = cmp.getCompoundTag(TAG_PLAYER_KEPT_DROPS)

            val count = cmp1.getInteger(TAG_DROP_COUNT)
            (0..count - 1)
                    .map { cmp1.getCompoundTag(TAG_DROP_PREFIX + it) }
                    .mapNotNull { ItemStack.loadItemStackFromNBT(it)?.copy() }
                    .forEach { event.player.inventory.addItemStackToInventory(it) }

            cmp.setTag(TAG_PLAYER_KEPT_DROPS, NBTTagCompound())
        }
    }

    companion object {
        val TAG_PLAYER_KEPT_DROPS = "${LibMisc.MOD_ID}_playerKeptDrops"
        val TAG_DROP_COUNT = "dropCount"
        val TAG_DROP_PREFIX = "dropPrefix"

        val TAG_X = "x"
        val TAG_Y = "y"
        val TAG_Z = "z"
    }
}
