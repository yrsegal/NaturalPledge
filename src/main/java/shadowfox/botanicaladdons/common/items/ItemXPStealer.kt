package shadowfox.botanicaladdons.common.items

import com.teamwizardry.librarianlib.features.base.item.ItemMod
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.network.play.server.SPacketSetExperience
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextFormatting.*
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent
import shadowfox.botanicaladdons.client.core.ITooltipBarItem
import shadowfox.botanicaladdons.common.BotanicalAddons
import shadowfox.botanicaladdons.common.crafting.recipe.RecipeEnchantmentRemoval
import java.awt.Color

/**
 * @author WireSegal
 * Created at 9:15 PM on 1/2/17.
 */
class ItemXPStealer(name: String) : ItemMod(name), ITooltipBarItem {
    init {
        setMaxStackSize(1)
    }

    companion object {
        init {
            MinecraftForge.EVENT_BUS.register(this)
        }

        @SubscribeEvent
        fun interceptTooltip(e: ItemTooltipEvent) {
            if (e.itemStack.item is ItemXPStealer) {
                val displayName = e.toolTip[0]
                val level = e.itemStack.xpLevels
                if (level == 0) return
                e.toolTip[0] = "$RESET($GREEN$BOLD$level$RESET) $displayName"
            }
        }

        @SubscribeEvent
        fun resetSeed(e: PlayerEvent.ItemCraftedEvent) {
            val matrix = e.craftMatrix
            if (RecipeEnchantmentRemoval.matches(matrix)) {
                e.player.onEnchant(null, 0)
                (0 until matrix.sizeInventory)
                        .mapNotNull { matrix.getStackInSlot(it) }
                        .filter { it.item == ModItems.xpTome }
                        .forEach { it.xpSeed = e.player.xpSeed }
            }
        }
    }

    override fun getHighlightTip(stack: ItemStack, displayName: String): String {
        val level = stack.xpLevels
        if (level == 0) return displayName
        return "$RESET($GREEN$BOLD$level$RESET) $displayName"
    }

    override fun hasEffect(stack: ItemStack): Boolean {
        return super.hasEffect(stack) || stack.xp > 0
    }

    override fun onUpdate(stack: ItemStack, worldIn: World, entityIn: Entity, itemSlot: Int, isSelected: Boolean) {
        if (!worldIn.isRemote && entityIn is EntityPlayer)
            stack.xpSeed = entityIn.xpSeed
    }

    override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        val stack = playerIn.getHeldItem(hand)
        if (!worldIn.isRemote) {
            if ((stack.xp == 0 || playerIn.isSneaking) && (playerIn.experienceLevel > 0 || playerIn.experience > 0)) {
                val levels = playerIn.experienceLevel
                stack.xp += (playerIn.experience * getLevelCap(levels)).toInt() + (0 until levels).sumBy(::getLevelCap)
                playerIn.experienceLevel = 0
                playerIn.experience = 0.0f
                playerIn.experienceTotal = 0
                if (playerIn is EntityPlayerMP)
                    playerIn.connection.sendPacket(SPacketSetExperience(playerIn.experience, playerIn.experienceTotal, playerIn.experienceLevel))
                return ActionResult(EnumActionResult.SUCCESS, stack)
            }

            if (stack.xp == 0)
                return ActionResult(EnumActionResult.SUCCESS, stack)

            playerIn.addExperience(stack.xp)
            playerIn.addScore(-stack.xp)
            stack.xp = 0

            if (playerIn is EntityPlayerMP)
                playerIn.connection.sendPacket(SPacketSetExperience(playerIn.experience, playerIn.experienceTotal, playerIn.experienceLevel))
        }
        return ActionResult(EnumActionResult.SUCCESS, stack)
    }

    override fun onItemUse(playerIn: EntityPlayer, worldIn: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        return onItemRightClick(worldIn, playerIn, hand).type
    }

    override fun getPercentage(stack: ItemStack): Float {
        return stack.xpRemainder / getLevelCap(stack.xpLevels).toFloat()
    }

    val BASE_COLOR = Color(0xBADD08)

    override fun getColor(stack: ItemStack): Int {
        return BotanicalAddons.PROXY.pulseColor(BASE_COLOR).rgb
    }
}

private val TAG_SEED = "seed"
private val TAG_XP_AMOUNT = "xp"

var ItemStack.xpSeed: Int
    get() = ItemNBTHelper.getInt(this, TAG_SEED, 0)
    set(value) = ItemNBTHelper.setInt(this, TAG_SEED, value)

var ItemStack.xp: Int
    get() = ItemNBTHelper.getInt(this, TAG_XP_AMOUNT, 0)
    set(value) = ItemNBTHelper.setInt(this, TAG_XP_AMOUNT, value)

val ItemStack.xpLevels: Int
    get() {
        var experience = xp
        var level = 0
        while (experience >= getLevelCap(level)) {
            experience -= getLevelCap(level++)
        }
        return level
    }

val ItemStack.xpRemainder: Int
    get() {
        var experience = xp
        var level = 0
        while (experience >= getLevelCap(level)) {
            experience -= getLevelCap(level++)
        }
        return experience
    }

fun getLevelCap(level: Int)
        = if (level >= 30)
            112 + (level - 30) * 9
        else if (level >= 15)
            37 + (level - 15) * 5
        else
            7 + level * 2
