package shadowfox.botanicaladdons.common.items.sacred

import com.google.common.collect.Multimap
import net.minecraft.block.state.IBlockState
import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.EnumRarity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.oredict.OreDictionary
import shadowfox.botanicaladdons.api.item.IWeightEnchantable
import shadowfox.botanicaladdons.common.items.ItemResource
import shadowfox.botanicaladdons.common.items.ModItems
import shadowfox.botanicaladdons.common.items.base.IPreventBreakInCreative
import shadowfox.botanicaladdons.common.items.base.ItemMod
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.mana.IManaUsingItem
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.Botania
import vazkii.botania.common.core.helper.ItemNBTHelper
import vazkii.botania.common.core.helper.Vector3
import vazkii.botania.common.item.equipment.tool.ToolCommons

/**
 * @author WireSegal
 * Created at 9:43 PM on 4/20/16.
 */
class ItemMjolnir(name: String, val material: Item.ToolMaterial) : ItemMod(name), IWeightEnchantable, IPreventBreakInCreative, IManaUsingItem {
    private val attackDamage: Float

    init {
        setMaxStackSize(1)
        maxDamage = material.maxUses
        attackDamage = 3.0f + material.damageVsEntity
    }

    val TAG_ATTACKED = "attacked"
    val TAG_LAUNCHED = "launchedTicks"

    val MANA_PER_DAMAGE = 80

    override fun usesMana(p0: ItemStack) = true

    override fun getStrVsBlock(stack: ItemStack, state: IBlockState): Float = if (canHarvestBlock(state)) 5f else 1f

    override fun hitEntity(stack: ItemStack, target: EntityLivingBase, attacker: EntityLivingBase): Boolean {
        ToolCommons.damageItem(stack, 1, attacker, MANA_PER_DAMAGE)
        if (target.isActiveItemStackBlocking && target.activeItemStack != null)
            target.activeItemStack!!.damageItem(500, target)
        ItemNBTHelper.setBoolean(stack, TAG_ATTACKED, true)
        return true
    }

    override fun onBlockDestroyed(stack: ItemStack, world: World, blockIn: IBlockState, pos: BlockPos, entityLiving: EntityLivingBase): Boolean {
        if (blockIn.getBlockHardness(world, pos).toDouble() > 0.0) {
            ToolCommons.damageItem(stack, 1, entityLiving, MANA_PER_DAMAGE)
        }

        return true
    }

    override fun getRarity(stack: ItemStack): EnumRarity = BotaniaAPI.rarityRelic

    override fun onBlockStartBreak(itemstack: ItemStack?, pos: BlockPos?, player: EntityPlayer?): Boolean {
        return super.onBlockStartBreak(itemstack, pos, player)
    }

    override fun canHarvestBlock(state: IBlockState): Boolean = state.block.isToolEffective("pickaxe", state)

    override fun getItemEnchantability(): Int {
        return this.material.enchantability
    }

    override fun getIsRepairable(toRepair: ItemStack?, repair: ItemStack): Boolean {
        return repair.item == ModItems.resource && (repair.itemDamage % ItemResource.Variants.values().size == ItemResource.Variants.THUNDER_STEEL.ordinal)
    }

    override fun getAttributeModifiers(slot: EntityEquipmentSlot?, stack: ItemStack?): Multimap<String, AttributeModifier>? {
        val multimap = super.getAttributeModifiers(slot, stack)

        if (slot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.attributeUnlocalizedName, AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", this.attackDamage.toDouble(), 0))
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.attributeUnlocalizedName, AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -3.5, 0))
        }

        return multimap
    }

    override fun onUpdate(stack: ItemStack, world: World, player: Entity, itemSlot: Int, isSelected: Boolean) {
        if (!world.isRemote && player is EntityPlayer && stack.itemDamage > 0 && ManaItemHandler.requestManaExactForTool(stack, player, MANA_PER_DAMAGE * 2, true)) {
            stack.itemDamage = stack.itemDamage - 1
        }
        val launchedTicks = ItemNBTHelper.getInt(stack, TAG_LAUNCHED, 0)
        if (launchedTicks > 0 && player is EntityLivingBase)
            player.ticksSinceLastSwing = launchedTicks
        ItemNBTHelper.removeEntry(stack, TAG_LAUNCHED)

        ItemNBTHelper.removeEntry(stack, TAG_ATTACKED)
    }

    override fun onEntitySwing(entityLiving: EntityLivingBase, stack: ItemStack): Boolean {
        if (entityLiving is EntityPlayer) {
            if (entityLiving.cooldownTracker.hasCooldown(this)) return false
            entityLiving.cooldownTracker.setCooldown(this, 40)
        }

        if (ItemNBTHelper.getBoolean(stack, TAG_ATTACKED, false)) return false

        ItemNBTHelper.setInt(stack, TAG_LAUNCHED, entityLiving.ticksSinceLastSwing)

        ToolCommons.damageItem(stack, 1, entityLiving, MANA_PER_DAMAGE)

        entityLiving.motionX = Math.max(Math.min(entityLiving.lookVec.xCoord * 1.25 + entityLiving.motionX, 2.0), -2.0)
        entityLiving.motionY = Math.max(Math.min(entityLiving.lookVec.yCoord * 1.25 + entityLiving.motionY, 2.0), -2.0)
        entityLiving.motionZ = Math.max(Math.min(entityLiving.lookVec.zCoord * 1.25 + entityLiving.motionZ, 2.0), -2.0)
        entityLiving.fallDistance = 0f

        val targetVec = Vector3(entityLiving.motionX, entityLiving.motionY, entityLiving.motionZ).multiply(2.0).add(Vector3(entityLiving.positionVector))
        val speed = Vector3(entityLiving.motionX, entityLiving.motionY, entityLiving.motionZ).mag().toFloat()

        Botania.proxy.lightningFX(entityLiving.worldObj, Vector3(entityLiving.positionVector), targetVec, speed, 0x00948B, 0x00E4D7)
        return false
    }

    override fun canApplyWeightEnchantment(stack: ItemStack, ench: Enchantment): Boolean {
        return true
    }
}
