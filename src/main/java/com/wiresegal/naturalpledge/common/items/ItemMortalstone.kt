package com.wiresegal.naturalpledge.common.items

import com.teamwizardry.librarianlib.features.base.item.IItemColorProvider
import com.teamwizardry.librarianlib.features.base.item.ItemMod
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper
import com.teamwizardry.librarianlib.features.helpers.getNBTInt
import com.teamwizardry.librarianlib.features.helpers.setNBTInt
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import com.wiresegal.naturalpledge.api.item.IDiscordantItem
import com.wiresegal.naturalpledge.common.NaturalPledge
import com.wiresegal.naturalpledge.common.items.bauble.faith.ItemFaithBauble
import com.wiresegal.naturalpledge.common.potions.ModPotions
import vazkii.botania.api.mana.IManaItem
import vazkii.botania.api.mana.IManaTooltipDisplay
import vazkii.botania.api.mana.IManaUsingItem
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.core.helper.Vector3
import java.awt.Color

/**
 * @author WireSegal
 * Created at 12:15 PM on 4/25/16.
 */
class ItemMortalstone(name: String) : ItemMod(name), IManaUsingItem, IDiscordantItem, IManaItem, IManaTooltipDisplay, IItemColorProvider {

    val RANGE = 5.0

    val MANA_PER_TICK = 1

    val TAG_MANA = "mana"
    val MAX_MANA = 600 * MANA_PER_TICK

    val PARTICLE_COLOR = 0xff0000

    init {
        setMaxStackSize(1)
    }

    override val itemColorFunction: ((ItemStack, Int) -> Int)?
        get() = { itemStack, i ->
            if (i == 1)
                (if (getMana(itemStack) == 0)
                    0x600000
                else
                    NaturalPledge.PROXY.pulseColor(Color(0xB71010)).rgb)
            else 0xFFFFFF
        }

    override fun onUpdate(stack: ItemStack, worldIn: World, entityIn: Entity, itemSlot: Int, isSelected: Boolean) {
        var flag = false

        if (entityIn is EntityPlayer)
            addMana(stack, ManaItemHandler.requestMana(stack, entityIn, MANA_PER_TICK * 3, true))

        if (isSelected && !entityIn.world.isRemote && (entityIn !is EntityPlayer || ManaItemHandler.requestManaExact(stack, entityIn, MANA_PER_TICK, false))) {
            val entities = worldIn.getEntitiesWithinAABB(EntityPlayer::class.java, entityIn.entityBoundingBox.grow(RANGE))
            for (entity in entities)
                if (entity is EntityPlayer && entity.positionVector.subtract(entityIn.positionVector).length() <= RANGE && ItemFaithBauble.getEmblem(entity) != null) {
                    entity.addPotionEffect(PotionEffect(ModPotions.faithlessness, 5, 0, true, true))
                    if (entity != entityIn && !ModPotions.faithlessness.hasEffect(entity)) flag = true
                    NaturalPledge.PROXY.particleEmission(Vector3.fromEntityCenter(entity).add(-0.5, 0.0, -0.5), PARTICLE_COLOR, 0.7F)
                }
        }
        if (entityIn is EntityPlayer && isSelected && !entityIn.world.isRemote) {
            if (flag)
                addMana(stack, -MANA_PER_TICK)
            entityIn.addPotionEffect(PotionEffect(ModPotions.faithlessness, 5, 0, true, true))
        }
    }

    override fun addMana(stack: ItemStack, mana: Int) = stack.setNBTInt(TAG_MANA, Math.max(0, Math.min(mana + getMana(stack), getMaxMana(stack))))
    override fun canExportManaToItem(stack: ItemStack, p1: ItemStack): Boolean = false
    override fun canExportManaToPool(stack: ItemStack, p1: TileEntity?): Boolean = false
    override fun canReceiveManaFromItem(stack: ItemStack, p1: ItemStack) = true
    override fun canReceiveManaFromPool(stack: ItemStack, p1: TileEntity?) = false
    override fun getMana(stack: ItemStack) = stack.getNBTInt(TAG_MANA, getMaxMana(stack))
    override fun getMaxMana(stack: ItemStack) = MAX_MANA
    override fun isNoExport(stack: ItemStack) = true

    override fun getEntityLifespan(itemStack: ItemStack, world: World?) = Int.MAX_VALUE
    override fun onEntityItemUpdate(entityItem: EntityItem): Boolean {
        var flag = 0
        if (getMana(entityItem.item) > 0 && !entityItem.world.isRemote) {

            val entities = entityItem.world.getEntitiesWithinAABB(EntityPlayer::class.java, entityItem.entityBoundingBox.grow(RANGE))
            for (entity in entities)
                if (entity is EntityPlayer && entity.positionVector.subtract(entityItem.positionVector).length() <= RANGE && ItemFaithBauble.getEmblem(entity) != null) {
                    if ((ModPotions.faithlessness.getEffect(entity) ?: PotionEffect(ModPotions.faithlessness)).duration <= 5) {
                        flag = flag or 1
                    }
                    entity.addPotionEffect(PotionEffect(ModPotions.faithlessness, 5, 0, true, true))
                    NaturalPledge.PROXY.particleEmission(Vector3.fromEntityCenter(entity).add(-0.5, 0.0, -0.5), 0x5e0a02, 0.7F)
                    flag = flag or 2
                }

            NaturalPledge.PROXY.particleEmission(Vector3.fromEntity(entityItem).add(-0.5, 0.0, -0.5), 0x5e0a02, if (flag and 2 == 0) 0.1F else 0.9F)
        }
        if (flag and 1 != 0)
            addMana(entityItem.item, -MANA_PER_TICK)

        return false
    }

    override fun showDurabilityBar(stack: ItemStack) = getMana(stack) < getMaxMana(stack) - MANA_PER_TICK
    override fun getDurabilityForDisplay(stack: ItemStack) = 1 - getManaFractionForDisplay(stack).toDouble()
    override fun getManaFractionForDisplay(stack: ItemStack) = getMana(stack) / getMaxMana(stack).toFloat()

    override fun usesMana(p0: ItemStack) = true
    override fun isDiscordant(stack: ItemStack) = true
}
