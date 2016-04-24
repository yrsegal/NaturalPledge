package shadowfox.botanicaladdons.common.items

import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import shadowfox.botanicaladdons.client.core.ModelHandler
import shadowfox.botanicaladdons.common.achievements.ModAchievements
import shadowfox.botanicaladdons.common.items.base.ItemMod
import shadowfox.botanicaladdons.common.items.bauble.faith.ItemFaithBauble
import vazkii.botania.client.core.handler.ItemsRemainingRenderHandler
import vazkii.botania.common.Botania
import vazkii.botania.common.core.helper.ItemNBTHelper
import java.awt.Color
import java.util.*

/**
 * @author WireSegal
 * Created at 9:21 AM on 4/18/16.
 */
class ItemTerrestrialFocus(name: String) : ItemMod(name), ModelHandler.IColorProvider {

    interface IFocusSpell {
        val iconStack: ItemStack

        fun getCooldown(player: EntityPlayer, focus: ItemStack, hand: EnumHand): Int = 0

        fun onCast(player: EntityPlayer, focus: ItemStack, hand: EnumHand): Boolean
    }

    override val color: IItemColor?
        get() = IItemColor { itemStack, i ->
            if (i == 1)
                Color.HSBtoRGB(Botania.proxy.worldElapsedTicks * 2 % 360 / 360f, 0.25f, 1f)
            else 0xFFFFFF
        }

    companion object {
        fun getSpells(player: EntityPlayer): HashMap<String, out IFocusSpell> {
            val emblem = ItemFaithBauble.getEmblem(player) ?: return hashMapOf()
            return (emblem.item as ItemFaithBauble).getVariant(emblem)?.getSpells(emblem, player) ?: return hashMapOf()
        }

        val TAG_SPELL = "spell"

        fun getSpell(focus: ItemStack): String? {
            return ItemNBTHelper.getString(focus, TAG_SPELL, null)
        }

        fun setSpell(focus: ItemStack, spell: String?) {
            if (spell == null)
                ItemNBTHelper.removeEntry(focus, TAG_SPELL)
            else
                ItemNBTHelper.setString(focus, TAG_SPELL, spell)
        }
    }

    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    fun shiftSpellWithSneak(stack: ItemStack, player: EntityPlayer) {
        val spells = getSpells(player)
        val spell = getSpell(stack)

        val spellNames = spells.keys.sorted()
        val spellIndex = if (spell == null && spells.size != 0) -1 else if (spell !in spells) -2 else spellNames.indexOf(spell)
        if (spellIndex == -2) {
            setSpell(stack, null)
            player.worldObj.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.block_lever_click, SoundCategory.PLAYERS, 0.6F, (1.0F + (player.worldObj.rand.nextFloat() - player.worldObj.rand.nextFloat()) * 0.2F) * 0.7F)
        } else {
            val name = spellNames[(spellIndex + 1) % spellNames.size]
            setSpell(stack, name)
            player.worldObj.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.block_stone_button_click_on, SoundCategory.PLAYERS, 0.6F, (1.0F + (player.worldObj.rand.nextFloat() - player.worldObj.rand.nextFloat()) * 0.2F) * 0.7F)
            ItemsRemainingRenderHandler.set(spells[name]?.iconStack, -2)
        }
    }

    fun castSpell(stack: ItemStack, player: EntityPlayer, hand: EnumHand): Boolean {
        val spells = getSpells(player)
        val spellName = getSpell(stack)

        if (spellName == null || spellName !in spells)
            return false

        val spell = spells[spellName] ?: return false

        val cooldown = spell.getCooldown(player, stack, hand)
        if (cooldown > 0)
            player.cooldownTracker.setCooldown(this, cooldown)

        return spell.onCast(player, stack, hand)
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

    override fun onEntitySwing(entityLiving: EntityLivingBase?, stack: ItemStack?) = true
    override fun shouldCauseReequipAnimation(oldStack: ItemStack?, newStack: ItemStack?, slotChanged: Boolean) = slotChanged


}
