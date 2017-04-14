package shadowfox.botanicaladdons.common.items.weapons

import com.google.common.collect.Multimap
import com.teamwizardry.librarianlib.features.base.item.IShieldItem
import com.teamwizardry.librarianlib.features.base.item.ItemMod
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.EnumAction
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import shadowfox.botanicaladdons.api.item.IWeightEnchantable
import shadowfox.botanicaladdons.common.enchantment.EnchantmentWeight
import shadowfox.botanicaladdons.common.items.base.IPreventBreakInCreative
import shadowfox.botanicaladdons.common.items.bauble.faith.ItemRagnarokPendant
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.item.equipment.tool.ToolCommons

/**
 * @author WireSegal
 * Created at 9:20 PM on 5/18/16.
 */
class ItemNightscourge(val name: String) : ItemMod(name), IWeightEnchantable, IPreventBreakInCreative, IShieldItem {

    val MANA_PER_DAMAGE = 40

    init {
        setMaxStackSize(1)
        maxDamage = 1561
        addPropertyOverride(ResourceLocation("blocking")) {
            stack, _, entityIn ->
            if (entityIn != null && entityIn.isHandActive && (entityIn.heldItemMainhand == stack || entityIn.heldItemOffhand == stack)) 1f else 0f
        }
    }

    override fun damageItem(stack: ItemStack, player: EntityPlayer, indirectSource: Entity?, directSource: Entity?, amount: Float, source: DamageSource, damageAmount: Int): Boolean {
        ToolCommons.damageItem(stack, damageAmount, player, MANA_PER_DAMAGE)
        return true
    }

    override fun onAxeBlocked(stack: ItemStack, player: EntityPlayer, attacker: EntityLivingBase, amount: Float, source: DamageSource)
            = false

    override fun onDamageBlocked(stack: ItemStack, player: EntityPlayer, indirectSource: Entity?, directSource: Entity?, amount: Float, source: DamageSource) {
        // NO-OP
    }

    private val attackDamage = 1.5f

    override fun getItemUseAction(stack: ItemStack) = EnumAction.BLOCK

    override fun onUpdate(stack: ItemStack, world: World, player: Entity, itemSlot: Int, isSelected: Boolean) {
        if (!world.isRemote && player is EntityPlayer && stack.itemDamage > 0 && ManaItemHandler.requestManaExactForTool(stack, player, MANA_PER_DAMAGE * 2, true))
            stack.itemDamage = stack.itemDamage - 1
    }

    override fun onBlockDestroyed(stack: ItemStack, worldIn: World, blockIn: IBlockState, pos: BlockPos, entityLiving: EntityLivingBase): Boolean {
        if (blockIn.getBlockHardness(worldIn, pos) > 0)
            ToolCommons.damageItem(stack, 2, entityLiving, MANA_PER_DAMAGE)
        return true
    }

    override fun getMaxItemUseDuration(stack: ItemStack): Int {
        return 72000
    }

    override fun onUsingTick(stack: ItemStack, player: EntityLivingBase, count: Int) {
        if (player.heldItemMainhand?.item != this)
            player.stopActiveHand()
    }

    override fun hitEntity(stack: ItemStack, target: EntityLivingBase?, attacker: EntityLivingBase?): Boolean {
        ToolCommons.damageItem(stack, 1, attacker, MANA_PER_DAMAGE)
        return super.hitEntity(stack, target, attacker)
    }

    override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, hand: EnumHand): ActionResult<ItemStack>? {
        if (hand == EnumHand.OFF_HAND && playerIn.heldItemMainhand?.item == this)
            playerIn.activeHand = hand
        return super.onItemRightClick(worldIn, playerIn, hand)
    }

    override fun getAttributeModifiers(slot: EntityEquipmentSlot?, stack: ItemStack): Multimap<String, AttributeModifier>? {
        val multimap = super.getAttributeModifiers(slot, stack)
        if (slot == EntityEquipmentSlot.MAINHAND) {
            val offset = EnchantmentWeight.getWeight(stack)
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.name, AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", this.attackDamage.toDouble(), 0))
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.name, AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.0 + offset * -.3, 0))
        }
        return multimap
    }

    override fun getSubItems(itemIn: Item, tab: CreativeTabs?, subItems: NonNullList<ItemStack>) {
                 if (ItemRagnarokPendant.hasAwakenedRagnarok())
            super.getSubItems(itemIn, tab, subItems)
    }

    override fun getItemEnchantability(): Int = 14

    override fun canApplyWeightEnchantment(stack: ItemStack, ench: Enchantment) = true
}
