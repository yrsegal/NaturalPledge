package shadowfox.botanicaladdons.common.items.sacred

import com.google.common.collect.Multimap
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.MobEffects
import net.minecraft.init.SoundEvents
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.EnumAction
import net.minecraft.item.EnumRarity
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingAttackEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.oredict.OreDictionary
import shadowfox.botanicaladdons.common.items.base.IPreventBreakInCreative
import shadowfox.botanicaladdons.common.items.base.ItemMod
import shadowfox.botanicaladdons.common.lib.LibMisc
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.mana.IManaUsingItem
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.Botania
import vazkii.botania.common.core.helper.Vector3
import vazkii.botania.common.item.equipment.tool.ToolCommons

/**
 * @author WireSegal
 * Created at 2:18 PM on 4/22/16.
 */
class ItemDagger(name: String, val toolMaterial: ToolMaterial) : ItemMod(name), IPreventBreakInCreative, IManaUsingItem {
    companion object {
        val minBlockLength = 2
        val maxBlockLength = 15

        object EventHandler {
            class DamageSourceOculus(entity: EntityLivingBase) : EntityDamageSource("${LibMisc.MOD_ID}.oculus", entity) {
                init {
                    setDamageBypassesArmor()
                    setMagicDamage()
                }
            }

            fun getHeadOrientation(entity: EntityLivingBase): Vector3 {
                val f1 = MathHelper.cos(-entity.rotationYaw * 0.017453292F - Math.PI.toFloat())
                val f2 = MathHelper.sin(-entity.rotationYaw * 0.017453292F - Math.PI.toFloat())
                val f3 = -MathHelper.cos(-(entity.rotationPitch - 90) * 0.017453292F)
                val f4 = MathHelper.sin(-(entity.rotationPitch - 90) * 0.017453292F)
                return Vector3((f2 * f3).toDouble(), f4.toDouble(), (f1 * f3).toDouble())
            }

            @SubscribeEvent
            fun onLivingAttacked(e: LivingAttackEvent) {
                val player = e.entityLiving
                val damage = e.source
                if (player is EntityPlayer && damage is EntityDamageSource && player.isHandActive && player.activeItemStack != null && player.activeItemStack.item is ItemDagger) {
                    val enemyEntity = damage.entity
                    val item = player.activeItemStack
                    val count = player.itemInUseMaxCount
                    if (enemyEntity is EntityLivingBase) {
                        if (item.item is ItemDagger && count <= maxBlockLength && count >= minBlockLength) {
                            val lookVec = Vector3(player.lookVec)
                            val targetVec = Vector3.fromEntityCenter(enemyEntity).sub(Vector3.fromEntityCenter(player))
                            val epsilon = lookVec.dotProduct(targetVec) / (lookVec.mag() * targetVec.mag())
                            if (epsilon > 0.75) {
                                e.isCanceled = true
                                player.resetActiveHand()
                                if (!player.worldObj.isRemote) {
                                    if (damage !is EntityDamageSourceIndirect) {
                                        enemyEntity.attackEntityFrom(DamageSourceOculus(player), e.amount * 2f)
                                        val xDif = enemyEntity.posX - player.posX
                                        val zDif = enemyEntity.posZ - player.posZ
                                        player.worldObj.playSound(player, enemyEntity.posX, enemyEntity.posY, enemyEntity.posZ, SoundEvents.block_anvil_land, SoundCategory.PLAYERS, 1f, 0.9f + 0.1f * Math.random().toFloat())
                                        if (enemyEntity.heldItemMainhand != null)
                                            enemyEntity.heldItemMainhand.damageItem(100, enemyEntity)
                                        if (enemyEntity.heldItemOffhand != null)
                                            enemyEntity.heldItemOffhand.damageItem(100, enemyEntity)
                                        enemyEntity.knockBack(player, 1f, -xDif, -zDif)
                                        enemyEntity.addPotionEffect(PotionEffect(MobEffects.weakness, 60, 1, true, false))
                                        enemyEntity.addPotionEffect(PotionEffect(MobEffects.moveSlowdown, 60, 2, true, false))
                                    }
                                } else {
                                    val mainVec = Vector3.fromEntityCenter(player).add(lookVec)
                                    val baseVec = getHeadOrientation(player).crossProduct(lookVec).normalize()
                                    for (i in 0..360 step 15) {
                                        val rotVec = baseVec.copy().rotate(i * 180 / Math.PI, lookVec)
                                        val endVec = mainVec.copy().add(rotVec)
                                        Botania.proxy.lightningFX(player.worldObj, mainVec, endVec, 3f, 0xFF94A1, 0xFBAAB5)
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
    }

    init {
        setMaxStackSize(1)
        maxDamage = toolMaterial.maxUses
        MinecraftForge.EVENT_BUS.register(EventHandler)
        this.addPropertyOverride(ResourceLocation("blocking")) {
            stack, worldIn, entityIn ->
            if (entityIn != null && entityIn.isHandActive && entityIn.activeItemStack == stack) 1f else 0f
        }
        this.addPropertyOverride(ResourceLocation(LibMisc.MOD_ID, "dun")) {
            stack, worldIn, entityIn ->
            if (entityIn != null && entityIn.isHandActive && entityIn.activeItemStack == stack && entityIn.itemInUseMaxCount > maxBlockLength) 1f else 0f
        }
    }

    override fun usesMana(p0: ItemStack) = true

    override fun getItemUseAction(stack: ItemStack?) = EnumAction.BLOCK

    override fun getRarity(stack: ItemStack): EnumRarity = BotaniaAPI.rarityRelic
    fun getManaPerDamage(): Int = 60

    override fun onUpdate(stack: ItemStack, world: World, player: Entity, par4: Int, par5: Boolean) {
        if (!world.isRemote && player is EntityPlayer && stack.itemDamage > 0 && ManaItemHandler.requestManaExactForTool(stack, player, getManaPerDamage() * 2, true))
            stack.itemDamage = stack.itemDamage - 1
    }

    override fun onBlockDestroyed(stack: ItemStack, worldIn: World, blockIn: IBlockState, pos: BlockPos, entityLiving: EntityLivingBase): Boolean {
        if (blockIn.getBlockHardness(worldIn, pos) > 0)
            ToolCommons.damageItem(stack, 2, entityLiving, getManaPerDamage())
        return true
    }

    override fun getMaxItemUseDuration(stack: ItemStack?): Int {
        return 72000
    }

    override fun hitEntity(stack: ItemStack?, target: EntityLivingBase?, attacker: EntityLivingBase?): Boolean {
        ToolCommons.damageItem(stack, 2, attacker, getManaPerDamage())
        return super.hitEntity(stack, target, attacker)
    }

    override fun onItemRightClick(itemStackIn: ItemStack, worldIn: World, playerIn: EntityPlayer, hand: EnumHand): ActionResult<ItemStack>? {
        playerIn.activeHand = hand
        return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand)
    }

    override fun getAttributeModifiers(slot: EntityEquipmentSlot?, stack: ItemStack?): Multimap<String, AttributeModifier>? {
        val multimap = super.getAttributeModifiers(slot, stack)
        if (slot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.attributeUnlocalizedName, AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", -0.5, 0))
        }
        return multimap
    }

    override fun getItemEnchantability(): Int = this.toolMaterial.enchantability
    override fun getIsRepairable(stack: ItemStack?, materialstack: ItemStack?): Boolean {
        val mat = this.toolMaterial.repairItemStack
        if (mat != null && OreDictionary.itemMatches(mat, materialstack, false)) return true
        return super.getIsRepairable(stack, materialstack)
    }
}
