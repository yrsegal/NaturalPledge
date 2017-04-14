package shadowfox.botanicaladdons.common.items.sacred

import com.google.common.collect.Multimap
import com.teamwizardry.librarianlib.features.base.item.ItemMod
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper
import net.minecraft.block.state.IBlockState
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.inventory.IInventory
import net.minecraft.item.EnumRarity
import net.minecraft.item.ItemStack
import net.minecraft.stats.Achievement
import net.minecraft.util.EntityDamageSource
import net.minecraft.util.EnumHand
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World
import net.minecraftforge.common.ForgeHooks.canHarvestBlock
import shadowfox.botanicaladdons.api.item.IWeightEnchantable
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.common.achievements.ModAchievements
import shadowfox.botanicaladdons.common.core.helper.BAMethodHandles
import shadowfox.botanicaladdons.common.enchantment.EnchantmentWeight
import shadowfox.botanicaladdons.common.enchantment.ModEnchantments
import shadowfox.botanicaladdons.common.items.ItemResource
import shadowfox.botanicaladdons.common.items.ModItems
import shadowfox.botanicaladdons.common.items.base.IPreventBreakInCreative
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.mana.IManaUsingItem
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.Botania
import vazkii.botania.common.achievement.ICraftAchievement
import vazkii.botania.common.core.helper.Vector3
import vazkii.botania.common.item.equipment.tool.ToolCommons

/**
 * @author WireSegal
 * Created at 9:43 PM on 4/20/16.
 */
class ItemMjolnir(name: String) : ItemMod(name), IWeightEnchantable, IPreventBreakInCreative, IManaUsingItem, ICraftAchievement {
    private val attackDamage: Float

    init {
        setMaxStackSize(1)
        maxDamage = 1561
        attackDamage = 15.0f
    }

    companion object {
        val TAG_LAUNCHED = "launchedTicks"
        val TAG_DIDLAUNCH = "launched"

        val MANA_PER_DAMAGE = 80

        fun hammerSource(entityLiving: EntityLivingBase) = EntityDamageSource("${LibMisc.MOD_ID}.hammer", entityLiving)
    }

    override fun usesMana(p0: ItemStack) = true

    override fun getStrVsBlock(stack: ItemStack, state: IBlockState): Float = if (canHarvestBlock(state)) 5f else 1f

    override fun hitEntity(stack: ItemStack, target: EntityLivingBase, attacker: EntityLivingBase): Boolean {
        ToolCommons.damageItem(stack, 1, attacker, MANA_PER_DAMAGE)
        if (target.isActiveItemStackBlocking && target.activeItemStack != null)
            target.activeItemStack!!.damageItem(500, target)
        return true
    }

    override fun onBlockDestroyed(stack: ItemStack, world: World, blockIn: IBlockState, pos: BlockPos, entityLiving: EntityLivingBase): Boolean {
        if (blockIn.getBlockHardness(world, pos).toDouble() > 0.0) {
            ToolCommons.damageItem(stack, 1, entityLiving, MANA_PER_DAMAGE)
        }

        return true
    }

    override fun getRarity(stack: ItemStack): EnumRarity = BotaniaAPI.rarityRelic

    override fun getItemEnchantability(): Int {
        return 26
    }

    override fun getIsRepairable(toRepair: ItemStack, repair: ItemStack): Boolean {
        return repair.item == ModItems.resource && ItemResource.variantFor(repair)?.first == ItemResource.Variants.THUNDER_STEEL
    }

    override fun getAttributeModifiers(slot: EntityEquipmentSlot?, stack: ItemStack): Multimap<String, AttributeModifier>? {
        val multimap = super.getAttributeModifiers(slot, stack)

        if (slot == EntityEquipmentSlot.MAINHAND) {
            val lightweight = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.lightweight, stack)
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.name, AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", this.attackDamage.toDouble(), 0))
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.name, AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -3.5 + lightweight * .25, 0))
        }

        return multimap
    }

    override fun onUpdate(stack: ItemStack, world: World, player: Entity, itemSlot: Int, isSelected: Boolean) {
        if (!world.isRemote && player is EntityPlayer && stack.itemDamage > 0 && ManaItemHandler.requestManaExactForTool(stack, player, MANA_PER_DAMAGE * 2, true)) {
            stack.itemDamage = stack.itemDamage - 1
        }
        val launchedTicks = ItemNBTHelper.getInt(stack, TAG_LAUNCHED, 0)
        if (launchedTicks > 0 && player is EntityLivingBase)
            BAMethodHandles.setSwingTicks(player, launchedTicks)
        ItemNBTHelper.removeEntry(stack, TAG_LAUNCHED)

        if (player is EntityLivingBase && player.heldItemMainhand == stack) {
            if (player.health > 0.0f && !(player is EntityPlayer && player.isSpectator) && ItemNBTHelper.getBoolean(stack, TAG_DIDLAUNCH, false)) {
                val axisalignedbb: AxisAlignedBB

                if (player.isRiding() && !player.getRidingEntity()!!.isDead)
                    axisalignedbb = player.getEntityBoundingBox().union(player.getRidingEntity()!!.entityBoundingBox).expand(1.0, 0.0, 1.0)
                else
                    axisalignedbb = player.getEntityBoundingBox().expand(1.0, 0.5, 1.0)

                val list = world.getEntitiesWithinAABBExcludingEntity(player, axisalignedbb)

                var flag = false

                if (!world.isRemote) for (i in list) if (!i.isDead && i is EntityLivingBase) {
                    val motVec = Vector3(player.motionX, player.motionY, player.motionZ)
                    val diffVec = Vector3.fromEntity(i).subtract(Vector3.fromEntity(player))
                    val diff = motVec.dotProduct(diffVec) / (diffVec.mag() * motVec.mag())
                    if (diff > 0.75) {
                        if (!i.isActiveItemStackBlocking) {
                            i.attackEntityFrom(hammerSource(player), motVec.mag().toFloat() * 5f + 2f)
                        } else {
                            player.attackEntityFrom(hammerSource(i), 5f)
                        }
                        i.knockBack(player, 3f,
                                MathHelper.sin(player.rotationYaw * Math.PI.toFloat() / 180).toDouble(),
                                -MathHelper.cos(player.rotationYaw * Math.PI.toFloat() / 180).toDouble())
                        player.knockBack(player, 1.5f,
                                -MathHelper.sin(player.rotationYaw * Math.PI.toFloat() / 180).toDouble(),
                                MathHelper.cos(player.rotationYaw * Math.PI.toFloat() / 180).toDouble())
                        flag = true
                    }
                }

                if (flag) {
                    player.swingArm(EnumHand.MAIN_HAND)
                    ItemNBTHelper.removeEntry(stack, TAG_DIDLAUNCH)
                }
            }
        }

        val motVec = Vector3(player.motionX, player.motionY, player.motionZ)
        if (motVec.magSquared() < 0.01 || (player is EntityPlayer && player.moveForward > 0)) ItemNBTHelper.removeEntry(stack, TAG_DIDLAUNCH)
    }

    override fun onEntitySwing(entityLiving: EntityLivingBase, stack: ItemStack): Boolean {
        val weight = EnchantmentWeight.getWeight(stack)

        val speedVec = Vector3(entityLiving.lookVec).multiply(weight * .15 + 1.75).add(entityLiving.motionX, entityLiving.motionY, entityLiving.motionZ)
        if (speedVec.magSquared() > 16) return false

        if (entityLiving is EntityPlayer) {
            if (entityLiving.cooldownTracker.hasCooldown(this)) return false
            entityLiving.cooldownTracker.setCooldown(this, entityLiving.cooldownPeriod.toInt())
        }

        ItemNBTHelper.setInt(stack, TAG_LAUNCHED, BAMethodHandles.getSwingTicks(entityLiving))

        ToolCommons.damageItem(stack, 1, entityLiving, MANA_PER_DAMAGE)
        ItemNBTHelper.setBoolean(stack, TAG_DIDLAUNCH, true)

        entityLiving.motionX = speedVec.x
        entityLiving.motionY = speedVec.y
        entityLiving.motionZ = speedVec.z
        entityLiving.fallDistance = 0f

        val targetVec = speedVec.multiply(2.0).add(Vector3(entityLiving.positionVector))

        if (entityLiving.world.isRemote) Botania.proxy.lightningFX(Vector3(entityLiving.positionVector), targetVec, speedVec.mag().toFloat(), 0x00948B, 0x00E4D7)
        return false
    }

    override fun canApplyWeightEnchantment(stack: ItemStack, ench: Enchantment): Boolean {
        return true
    }

    override fun getAchievementOnCraft(p0: ItemStack, p1: EntityPlayer?, p2: IInventory?): Achievement? {
        return ModAchievements.sacredThunder
    }
}
