package shadowfox.botanicaladdons.common.items

import com.teamwizardry.librarianlib.common.base.item.ItemMod
import com.teamwizardry.librarianlib.common.util.ItemNBTHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
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
import shadowfox.botanicaladdons.client.core.ITooltipBarItem
import shadowfox.botanicaladdons.common.BotanicalAddons
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
        if (entityIn is EntityPlayer) {
            if (stack.resetSeed) {
                stack.resetSeed = false
                entityIn.removeExperienceLevel(0)
            }
            stack.xpSeed = entityIn.xpSeed
        }
    }

    override fun onItemRightClick(itemStackIn: ItemStack, worldIn: World, playerIn: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        if ((itemStackIn.xp == 0 || playerIn.isSneaking) && playerIn.experienceTotal > 0) {
            itemStackIn.xp += playerIn.experienceTotal
            playerIn.experienceLevel = 0
            playerIn.experience = 0.0f
            playerIn.experienceTotal = 0
            return ActionResult(EnumActionResult.SUCCESS, itemStackIn)
        }

        if (itemStackIn.xp == 0)
            return ActionResult(EnumActionResult.PASS, itemStackIn)

        playerIn.addExperience(itemStackIn.xp)
        itemStackIn.xp = 0
        return ActionResult(EnumActionResult.SUCCESS, itemStackIn)
    }

    override fun onItemUse(stack: ItemStack, playerIn: EntityPlayer, worldIn: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        return onItemRightClick(stack, worldIn, playerIn, hand).type
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
private val TAG_RESET_SEED = "reset"
private val TAG_XP_AMOUNT = "xp"

var ItemStack.xpSeed: Int
    get() = ItemNBTHelper.getInt(this, TAG_SEED, 0)
    set(value) = ItemNBTHelper.setInt(this, TAG_SEED, value)

var ItemStack.resetSeed: Boolean
    get() = ItemNBTHelper.getBoolean(this, TAG_RESET_SEED, false)
    set(value) = ItemNBTHelper.setBoolean(this, TAG_RESET_SEED, value)

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
