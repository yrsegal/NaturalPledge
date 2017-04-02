package shadowfox.botanicaladdons.common.items.sacred

import com.teamwizardry.librarianlib.common.base.item.ItemMod
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.projectile.EntityLargeFireball
import net.minecraft.init.Items
import net.minecraft.init.SoundEvents
import net.minecraft.inventory.IInventory
import net.minecraft.item.EnumAction
import net.minecraft.item.EnumRarity
import net.minecraft.item.ItemStack
import net.minecraft.stats.Achievement
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.world.World
import shadowfox.botanicaladdons.common.achievements.ModAchievements
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.mana.IManaUsingItem
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.achievement.ICraftAchievement

/**
 * @author WireSegal
 * Created at 1:47 AM on 3/26/17.
 */
class ItemPerditionFist(name: String) : ItemMod(name), IManaUsingItem, ICraftAchievement {
    override fun getRarity(stack: ItemStack?) = BotaniaAPI.rarityRelic

    override fun getMaxItemUseDuration(stack: ItemStack) = 72000

    override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, handIn: EnumHand): ActionResult<ItemStack> {
        playerIn.activeHand = handIn
        return super.onItemRightClick(worldIn, playerIn, handIn)
    }

    override fun getAchievementOnCraft(p0: ItemStack?, p1: EntityPlayer?, p2: IInventory?): Achievement {
        return ModAchievements.sacredFlame
    }

    override fun usesMana(p0: ItemStack?) = true

    override fun getItemUseAction(stack: ItemStack?): EnumAction {
        return EnumAction.BOW
    }

    private fun findAmmo(player: EntityPlayer): ItemStack {
        if (player.getHeldItem(EnumHand.OFF_HAND).item == Items.FIRE_CHARGE)
            return player.getHeldItem(EnumHand.OFF_HAND)
        else if (player.getHeldItem(EnumHand.MAIN_HAND).item == Items.FIRE_CHARGE)
            return player.getHeldItem(EnumHand.MAIN_HAND)
        else return (0..player.inventory.sizeInventory - 1)
                .map { player.inventory.getStackInSlot(it) }
                .filter { it.item == Items.FIRE_CHARGE }
                .firstOrNull() ?: ItemStack.EMPTY
    }

    override fun onPlayerStoppedUsing(stack: ItemStack, worldIn: World, player: EntityLivingBase, timeLeft: Int) {
        if (worldIn.isRemote) return
        val time = (getMaxItemUseDuration(stack) - timeLeft) / 20.0
        val power = if (time < 1)
            -1.0
        else if (time <= 21)
            -time * time * 0.0180952381 + time * 0.7933333333 - 0.5142857143
        else
            7.45264 + time * 0.0332076
        if (power > 0) {
            if (player is EntityPlayer && !player.isCreative) {
                if (!ManaItemHandler.requestManaExact(stack, player, 200, true)) return
                val fireCharge = findAmmo(player)
                if (fireCharge.isEmpty) return
                fireCharge.count--
            }

            val look = player.lookVec
            val ghastBall = EntityLargeFireball(worldIn, player, look.xCoord, look.yCoord, look.zCoord)
            ghastBall.explosionPower = (power + 0.5).toInt()
            ghastBall.posX = player.posX + look.xCoord
            ghastBall.posY = player.posY + (player.height / 2.0f).toDouble() + 0.5
            ghastBall.posZ = player.posZ + look.zCoord
            ghastBall.motionX = look.xCoord * 2
            ghastBall.motionY = look.yCoord * 2
            ghastBall.motionZ = look.zCoord * 2
            ghastBall.accelerationX = ghastBall.motionX * 0.1
            ghastBall.accelerationY = ghastBall.motionY * 0.1
            ghastBall.accelerationZ = ghastBall.motionZ * 0.1
            player.world.spawnEntity(ghastBall)
            player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.PLAYERS, 1f, 1f)

            if (player is EntityPlayer)
                player.cooldownTracker.setCooldown(this, 20)
        }

    }

    override fun onItemUseFinish(stack: ItemStack, worldIn: World, entityLiving: EntityLivingBase): ItemStack {
        onPlayerStoppedUsing(stack, worldIn, entityLiving, 0)
        return stack
    }
}
