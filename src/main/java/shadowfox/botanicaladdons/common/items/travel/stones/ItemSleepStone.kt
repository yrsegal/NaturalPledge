package shadowfox.botanicaladdons.common.items.travel.stones

import com.teamwizardry.librarianlib.common.base.item.IItemColorProvider
import com.teamwizardry.librarianlib.common.base.item.ItemMod
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.monster.EntityMob
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayer.SleepResult
import net.minecraft.item.EnumAction
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.world.World
import net.minecraftforge.event.ForgeEventFactory
import shadowfox.botanicaladdons.common.BotanicalAddons
import vazkii.botania.api.sound.BotaniaSoundEvents
import vazkii.botania.common.Botania

/**
 * @author WireSegal
 * Created at 9:59 PM on 1/20/17.
 */
class ItemSleepStone(name: String) : ItemMod(name), IItemColorProvider {
    override val itemColorFunction: ((ItemStack, Int) -> Int)?
        get() = { itemStack, i ->
            if (i == 1)
                BotanicalAddons.PROXY.rainbow(0.25f).rgb
            else 0xFFFFFF
        }

    init {
        setMaxStackSize(1)
    }

    override fun getItemUseAction(stack: ItemStack) = EnumAction.BOW
    override fun getMaxItemUseDuration(stack: ItemStack) = 100

    fun EntityPlayer.trySleepCustom(): SleepResult {
        val bedLocation = position
        val ret = ForgeEventFactory.onPlayerSleepInBed(this, bedLocation)
        if (ret != null) return ret

        if (isPlayerSleeping || !isEntityAlive) return SleepResult.OTHER_PROBLEM

        if (!worldObj.provider.isSurfaceWorld) return SleepResult.NOT_POSSIBLE_HERE

        if (worldObj.isDaytime) return SleepResult.NOT_POSSIBLE_NOW

        if (Math.abs(posX - bedLocation.x.toDouble()) > 3.0 || Math.abs(posY - bedLocation.y.toDouble()) > 2.0 || Math.abs(posZ - bedLocation.z.toDouble()) > 3.0) return SleepResult.TOO_FAR_AWAY

        val list = worldObj.getEntitiesWithinAABB(EntityMob::class.java, AxisAlignedBB(bedLocation.x.toDouble() - 8.0, bedLocation.y.toDouble() - 5.0, bedLocation.z.toDouble() - 8.0, bedLocation.x.toDouble() + 8.0, bedLocation.y.toDouble() + 5.0, bedLocation.z.toDouble() + 8.0))

        if (!list.isEmpty()) return SleepResult.NOT_SAFE
        return SleepResult.OK
    }

    companion object {
        val MESSAGE_ID = 3189
    }

    fun attemptSleep(player: EntityPlayer): Boolean {
        val sleepResult = player.trySleepCustom()

        if (!player.worldObj.isRemote) {
            if (sleepResult == SleepResult.NOT_POSSIBLE_NOW)
                BotanicalAddons.PROXY.sendSpamlessMessage(player, TextComponentTranslation("tile.bed.noSleep"), MESSAGE_ID)
            else if (sleepResult == SleepResult.NOT_SAFE)
                BotanicalAddons.PROXY.sendSpamlessMessage(player, TextComponentTranslation("tile.bed.notSafe"), MESSAGE_ID)
        }

        return sleepResult == SleepResult.OK
    }

    override fun onItemRightClick(itemStackIn: ItemStack, worldIn: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        if (attemptSleep(player)) {
            player.activeHand = hand
            return ActionResult(EnumActionResult.SUCCESS, itemStackIn)
        }
        return ActionResult(EnumActionResult.FAIL, itemStackIn)
    }

    override fun onUsingTick(stack: ItemStack, player: EntityLivingBase, count: Int) {
        if (player !is EntityPlayer) return
        if (!attemptSleep(player))
            player.resetActiveHand()
    }

    override fun onItemUseFinish(stack: ItemStack, worldIn: World, entityLiving: EntityLivingBase): ItemStack? {
        if (worldIn.gameRules.getBoolean("doDaylightCycle")) {
            val i = worldIn.worldInfo.worldTime + 24000L
            worldIn.worldInfo.worldTime = i - i % 24000L
        }
        entityLiving.worldObj.playSound(null, entityLiving.posX, entityLiving.posY, entityLiving.posZ, BotaniaSoundEvents.potionCreate, SoundCategory.PLAYERS, 1f, 0.5f)

        for (i in 0..20) {
            val x1 = (entityLiving.posX + Math.random()).toFloat()
            val y1 = (entityLiving.posY + 0.5 + Math.random()).toFloat()
            val z1 = (entityLiving.posZ + Math.random()).toFloat()
            Botania.proxy.wispFX(x1.toDouble(), y1.toDouble(), z1.toDouble(), Math.random().toFloat(), Math.random().toFloat(), Math.random().toFloat(), Math.random().toFloat() * 0.5f, -0.05f + Math.random().toFloat() * 0.05f)
        }

        return stack
    }
}
