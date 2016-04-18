package shadowfox.botanicaladdons.common.items

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import shadowfox.botanicaladdons.common.items.base.ItemMod
import shadowfox.botanicaladdons.common.items.bauble.faith.ItemFaithBauble
import vazkii.botania.client.core.handler.ItemsRemainingRenderHandler
import vazkii.botania.common.core.helper.ItemNBTHelper
import java.util.*

/**
 * @author WireSegal
 * Created at 9:21 AM on 4/18/16.
 */
class ItemTerrestrialFocus(name: String) : ItemMod(name) {

    interface IFocusSpell {
        val iconStack: ItemStack

        fun onCast(player: EntityPlayer, focus: ItemStack, hand: EnumHand): Boolean
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
        val spellIndex = if (spell == null || spell !in spells) -1 else spellNames.indexOf(spell)
        val name = spellNames[(spellIndex + 1) % spellNames.size]
        setSpell(stack, name)
        ItemsRemainingRenderHandler.set(spells[name]?.iconStack, -2)
    }

    fun castSpell(stack: ItemStack, player: EntityPlayer, hand: EnumHand): Boolean {
        val spells = getSpells(player)
        val spellName = getSpell(stack)

        if (spellName == null || spellName !in spells)
            return false

        val spell = spells[spellName]?: return false

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
        } else if (castSpell(stack, playerIn, hand))
            return ActionResult(EnumActionResult.SUCCESS, stack)
        return ActionResult(EnumActionResult.PASS, stack)
    }
}
