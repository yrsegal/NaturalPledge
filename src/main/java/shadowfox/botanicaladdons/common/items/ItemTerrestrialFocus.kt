package shadowfox.botanicaladdons.common.items

import com.teamwizardry.librarianlib.features.base.item.IItemColorProvider
import com.teamwizardry.librarianlib.features.base.item.ItemMod
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper
import net.minecraft.client.Minecraft
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.init.SoundEvents
import net.minecraft.item.EnumRarity
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextFormatting.GREEN
import net.minecraft.util.text.TextFormatting.WHITE
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.fml.common.eventhandler.Event
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import shadowfox.botanicaladdons.api.SpellRegistry
import shadowfox.botanicaladdons.api.item.IPriestlyEmblem
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.api.priest.IFocusSpell
import shadowfox.botanicaladdons.client.core.BAClientMethodHandles
import shadowfox.botanicaladdons.common.BotanicalAddons
import shadowfox.botanicaladdons.common.core.helper.BAMethodHandles
import shadowfox.botanicaladdons.common.core.helper.CooldownHelper
import shadowfox.botanicaladdons.common.items.bauble.faith.ItemFaithBauble
import vazkii.botania.api.mana.IManaUsingItem
import vazkii.botania.client.core.handler.ItemsRemainingRenderHandler
import vazkii.botania.common.core.helper.PlayerHelper


/**
 * @author WireSegal
 * Created at 9:21 AM on 4/18/16.
 */
class ItemTerrestrialFocus(name: String) : ItemMod(name), IItemColorProvider, IManaUsingItem {

    override val itemColorFunction: ((ItemStack, Int) -> Int)?
        get() = { _, i ->
            if (i == 1)
                BotanicalAddons.PROXY.rainbow(0.25f).rgb
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
                ItemNBTHelper.setString(focus, TAG_SPELL, SpellRegistry.getSpellName(spell) ?: "")
        }

        object EventHandler {
            @SubscribeEvent
            fun handleRightClick(e: PlayerInteractEvent.RightClickBlock) {
                if (e.itemStack.item is ItemTerrestrialFocus) {
                    e.useBlock = Event.Result.DENY
                    e.useItem = Event.Result.ALLOW
                }
            }
        }
    }

    init {
        MinecraftForge.EVENT_BUS.register(EventHandler)
        setMaxStackSize(1)
    }

    fun shiftSpellWithSneak(stack: ItemStack, player: EntityPlayer) {
        val spells = getSpells(player)
        val spellName = getSpellName(stack)

        val spellIndex = if (spellName == null && spells.size != 0) -1 else if (spellName !in spells) -2 else spells.indexOf(spellName)
        if (spellIndex == -2) {
            setSpell(stack, null)
            player.world.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.PLAYERS, 0.6F, (1.0F + (player.world.rand.nextFloat() - player.world.rand.nextFloat()) * 0.2F) * 0.7F)
        } else {
            val name = spells[(spellIndex + 1) % spells.size]
            setSpellByName(stack, name)
            player.world.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON, SoundCategory.PLAYERS, 0.6F, (1.0F + (player.world.rand.nextFloat() - player.world.rand.nextFloat()) * 0.2F) * 0.7F)
            ItemsRemainingRenderHandler.set(SpellRegistry.getSpell(name)?.iconStack, -2)
            if (player.world.isRemote) {
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
        BAClientMethodHandles.setRemainingHighlight(gui, ticks)
    }

    fun castSpell(stack: ItemStack, player: EntityPlayer, hand: EnumHand): EnumActionResult {
        val spell = getSpell(stack) ?: return EnumActionResult.PASS

        val spells = getSpells(player)
        if (SpellRegistry.getSpellName(spell) !in spells) return EnumActionResult.FAIL

        val ret = spell.onCast(player, stack, hand)

        val cooldown = spell.getCooldown(player, stack, hand)
        if (ret == EnumActionResult.SUCCESS && cooldown > 0) {
            player.cooldownTracker.setCooldown(this, cooldown)
            ItemNBTHelper.setBoolean(stack, TAG_CAST, true)

            val ticks = BAMethodHandles.getCooldownTicks(player.cooldownTracker)

            ItemNBTHelper.setInt(stack, TAG_COOLDOWN_EXPIRE, cooldown + ticks)
            ItemNBTHelper.setInt(stack, TAG_USED_TIME, ticks)
        }

        return ret
    }

    override fun onItemUse(playerIn: EntityPlayer, worldIn: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult? {
        val stack = playerIn.getHeldItem(hand)
        if (playerIn.isSneaking) {
            shiftSpellWithSneak(stack, playerIn)
            return EnumActionResult.SUCCESS
        }
        val result = castSpell(stack, playerIn, hand)
        if (result == EnumActionResult.SUCCESS)
            if (worldIn != null)
                if (result == EnumActionResult.SUCCESS && !worldIn.isRemote)
                    PlayerHelper.grantCriterion(playerIn as EntityPlayerMP?, ResourceLocation(LibMisc.MOD_ID, "botanicaladdons/focus"), "code_triggered")
        return result
    }

    override fun onItemRightClick(worldIn: World?, playerIn: EntityPlayer, hand: EnumHand): ActionResult<ItemStack>? {
        val stack = playerIn.getHeldItem(hand)
        if (playerIn.isSneaking) {
            shiftSpellWithSneak(stack, playerIn)
            return ActionResult(EnumActionResult.SUCCESS, stack)
        }
        val result = castSpell(stack, playerIn, hand)
        if (worldIn != null)
            if (result == EnumActionResult.SUCCESS && !worldIn.isRemote)
                PlayerHelper.grantCriterion(playerIn as EntityPlayerMP?, ResourceLocation(LibMisc.MOD_ID, "botanicaladdons/focus"), "code_triggered")
        return ActionResult(result, stack)
    }

    override fun getItemStackDisplayName(stack: ItemStack): String? {
        var name = super.getItemStackDisplayName(stack)
        val spell = getSpell(stack)
        if (spell != null) name += " $WHITE($GREEN${spell.iconStack.displayName}$WHITE)"
        return name
    }

    override fun onUpdate(stack: ItemStack, worldIn: World, entityIn: Entity, itemSlot: Int, isSelected: Boolean) {
        if (entityIn is EntityPlayer) {

            val ticks = BAMethodHandles.getCooldownTicks(entityIn.cooldownTracker)

            val usedTime = ItemNBTHelper.getInt(stack, TAG_USED_TIME, ticks)
            val expireTime = ItemNBTHelper.getInt(stack, TAG_COOLDOWN_EXPIRE, ticks)
            val cooldown = CooldownHelper.getCooldown(entityIn.cooldownTracker, this)
            if (cooldown == null && !worldIn.isRemote) {
                CooldownHelper.setCooldown(entityIn.cooldownTracker, this, usedTime, expireTime)
            }

            if (entityIn.cooldownTracker.hasCooldown(this) && ItemNBTHelper.getBoolean(stack, TAG_CAST, false) && !ItemFaithBauble.isFaithless(entityIn)) {
                val spell = getSpell(stack) ?: return
                spell.onCooldownTick(entityIn, stack, itemSlot, isSelected,
                        (CooldownHelper.getCooldown(entityIn.cooldownTracker, this)?.expireTicks ?: ticks) - ticks)
            } else {
                ItemNBTHelper.removeEntry(stack, TAG_CAST)
            }
        }
    }

}
