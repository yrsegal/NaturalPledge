package shadowfox.botanicaladdons.common.items

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiIngame
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.SoundEvents
import net.minecraft.item.EnumRarity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextFormatting.GREEN
import net.minecraft.util.text.TextFormatting.WHITE
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.relauncher.FMLLaunchHandler
import net.minecraftforge.fml.relauncher.ReflectionHelper
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import shadowfox.botanicaladdons.api.IFocusSpell
import shadowfox.botanicaladdons.api.IPriestlyEmblem
import shadowfox.botanicaladdons.api.SpellRegistry
import shadowfox.botanicaladdons.client.core.ModelHandler
import shadowfox.botanicaladdons.common.achievements.ModAchievements
import shadowfox.botanicaladdons.common.core.helper.CooldownHelper
import shadowfox.botanicaladdons.common.items.base.ItemMod
import shadowfox.botanicaladdons.common.items.bauble.faith.ItemFaithBauble
import vazkii.botania.api.mana.IManaUsingItem
import vazkii.botania.client.core.handler.ItemsRemainingRenderHandler
import vazkii.botania.common.Botania
import vazkii.botania.common.core.helper.ItemNBTHelper
import java.awt.Color

/**
 * @author WireSegal
 * Created at 9:21 AM on 4/18/16.
 */
class ItemTerrestrialFocus(name: String) : ItemMod(name), ModelHandler.IColorProvider, IManaUsingItem {

    @SideOnly(Side.CLIENT)
    override fun getColor() = IItemColor { itemStack, i ->
            if (i == 1)
                Color.HSBtoRGB(Botania.proxy.worldElapsedTicks * 2 % 360 / 360f, 0.25f, 1f)
            else 0xFFFFFF
        }

    override fun usesMana(p0: ItemStack) = getSpell(p0) != null

    companion object {
        fun getSpells(player: EntityPlayer): MutableList<String> {
            val emblem = ItemFaithBauble.getEmblem(player) ?: return mutableListOf()
            return (emblem.item as IPriestlyEmblem).getVariant(emblem)?.getSpells(emblem, player) ?: return mutableListOf()
        }

        val TAG_SPELL = "spell"
        val TAG_CAST = "cast"
        val TAG_COOLDOWN_EXPIRE = "cooldownExpire"
        val TAG_USED_TIME = "time"


        fun getSpellName(focus: ItemStack): String? {
            return ItemNBTHelper.getString(focus, TAG_SPELL, null)
        }

        fun getSpell(focus: ItemStack): IFocusSpell? {
            val spellName = getSpellName(focus) ?: return null
            return SpellRegistry.getSpell(spellName)
        }

        fun setSpellByName(focus: ItemStack, spell: String?) {
            if (spell == null)
                ItemNBTHelper.removeEntry(focus, TAG_SPELL)
            else
                ItemNBTHelper.setString(focus, TAG_SPELL, spell)
        }

        fun setSpell(focus: ItemStack, spell: IFocusSpell?) {
            if (spell == null)
                ItemNBTHelper.removeEntry(focus, TAG_SPELL)
            else
                ItemNBTHelper.setString(focus, TAG_SPELL, SpellRegistry.getSpellName(spell))
        }
    }

    fun shiftSpellWithSneak(stack: ItemStack, player: EntityPlayer) {
        val spells = getSpells(player)
        val spellName = getSpellName(stack)

        val spellIndex = if (spellName == null && spells.size != 0) -1 else if (spellName !in spells) -2 else spells.indexOf(spellName)
        if (spellIndex == -2) {
            setSpell(stack, null)
            player.worldObj.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.block_lever_click, SoundCategory.PLAYERS, 0.6F, (1.0F + (player.worldObj.rand.nextFloat() - player.worldObj.rand.nextFloat()) * 0.2F) * 0.7F)
        } else {
            val name = spells[(spellIndex + 1) % spells.size]
            setSpellByName(stack, name)
            player.worldObj.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.block_stone_button_click_on, SoundCategory.PLAYERS, 0.6F, (1.0F + (player.worldObj.rand.nextFloat() - player.worldObj.rand.nextFloat()) * 0.2F) * 0.7F)
            ItemsRemainingRenderHandler.set(SpellRegistry.getSpell(name)?.iconStack, -2)
            if (player.worldObj.isRemote) {
                displayItemName(30)
            }

        }
    }

    override fun getRarity(stack: ItemStack): EnumRarity {
        return if (ItemNBTHelper.getBoolean(stack, TAG_CAST, false)) EnumRarity.UNCOMMON else super.getRarity(stack)
    }

    @SideOnly(Side.CLIENT)
    fun displayItemName(ticks: Int) {
        val gui = Minecraft.getMinecraft().ingameGUI
        gui.remainingHighlightTicks = ticks
    }

    fun castSpell(stack: ItemStack, player: EntityPlayer, hand: EnumHand): Boolean {
        val spell = getSpell(stack) ?: return false

        val spells = getSpells(player)
        if (SpellRegistry.getSpellName(spell) !in spells) return false

        val ret = spell.onCast(player, stack, hand)

        val cooldown = spell.getCooldown(player, stack, hand)
        if (ret && cooldown > 0) {
            player.cooldownTracker.setCooldown(this, cooldown)
            ItemNBTHelper.setBoolean(stack, TAG_CAST, true)

            val ticks = player.cooldownTracker.ticks

            ItemNBTHelper.setInt(stack, TAG_COOLDOWN_EXPIRE, cooldown + ticks)
            ItemNBTHelper.setInt(stack, TAG_USED_TIME, ticks)
        }

        return ret
    }

    override fun onItemUse(stack: ItemStack, playerIn: EntityPlayer, worldIn: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult? {
        if (playerIn.isSneaking) {
            shiftSpellWithSneak(stack, playerIn)
            return EnumActionResult.SUCCESS
        } else if (castSpell(stack, playerIn, hand))
            return EnumActionResult.SUCCESS
        return EnumActionResult.PASS
    }

    override fun onItemRightClick(stack: ItemStack, worldIn: World?, playerIn: EntityPlayer, hand: EnumHand): ActionResult<ItemStack>? {
        if (playerIn.isSneaking) {
            shiftSpellWithSneak(stack, playerIn)
            return ActionResult(EnumActionResult.SUCCESS, stack)
        } else if (castSpell(stack, playerIn, hand)) {
            playerIn.addStat(ModAchievements.focus)
            return ActionResult(EnumActionResult.SUCCESS, stack)
        }
        return ActionResult(EnumActionResult.PASS, stack)
    }

    override fun getItemStackDisplayName(stack: ItemStack): String? {
        var name = super.getItemStackDisplayName(stack)
        val spell = getSpell(stack)
        if (spell != null) name += " $WHITE($GREEN${spell.iconStack.displayName}$WHITE)"
        return name
    }

    override fun onUpdate(stack: ItemStack, worldIn: World, entityIn: Entity, itemSlot: Int, isSelected: Boolean) {
        if (entityIn is EntityPlayer) {

            val usedTime = ItemNBTHelper.getInt(stack, TAG_USED_TIME, entityIn.cooldownTracker.ticks)
            val expireTime = ItemNBTHelper.getInt(stack, TAG_COOLDOWN_EXPIRE, entityIn.cooldownTracker.ticks)
            val cooldown = CooldownHelper.getCooldown(entityIn.cooldownTracker, this)
            if (cooldown == null || expireTime - entityIn.cooldownTracker.ticks > cooldown.expireTicks - entityIn.cooldownTracker.ticks)
                CooldownHelper.setCooldown(entityIn.cooldownTracker, this, usedTime, expireTime)

            if (entityIn.cooldownTracker.hasCooldown(this) && ItemNBTHelper.getBoolean(stack, TAG_CAST, false)) {
                val spell = getSpell(stack) ?: return
                spell.onCooldownTick(entityIn, stack, itemSlot, isSelected,
                        (CooldownHelper.getCooldown(entityIn.cooldownTracker, this)?.expireTicks ?: entityIn.cooldownTracker.ticks) - entityIn.cooldownTracker.ticks)
            } else {
                ItemNBTHelper.removeEntry(stack, TAG_CAST)
            }
        }
    }

    override fun onEntitySwing(entityLiving: EntityLivingBase?, stack: ItemStack?) = true
    override fun shouldCauseReequipAnimation(oldStack: ItemStack, newStack: ItemStack, slotChanged: Boolean) = slotChanged || getSpell(oldStack.copy()) != getSpell(newStack.copy())


}
