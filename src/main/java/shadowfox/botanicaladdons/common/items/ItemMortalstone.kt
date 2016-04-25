package shadowfox.botanicaladdons.common.items

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import shadowfox.botanicaladdons.api.IDiscordantItem
import shadowfox.botanicaladdons.common.items.base.ItemMod
import shadowfox.botanicaladdons.common.potions.ModPotions
import shadowfox.botanicaladdons.common.potions.base.ModPotionEffect
import vazkii.botania.api.mana.IManaUsingItem
import vazkii.botania.api.mana.ManaItemHandler

/**
 * @author WireSegal
 * Created at 12:15 PM on 4/25/16.
 */
class ItemMortalstone(name: String) : ItemMod(name), IManaUsingItem, IDiscordantItem {

    val RANGE = 5.0

    val MANA_PER_TICK = 1

    override fun onUpdate(stack: ItemStack, worldIn: World, entityIn: Entity, itemSlot: Int, isSelected: Boolean) {
        var flag = false
        if (isSelected && (entityIn !is EntityPlayer || ManaItemHandler.requestManaExact(stack, entityIn, MANA_PER_TICK, false))) {
            val entities = worldIn.getEntitiesWithinAABBExcludingEntity(entityIn as EntityLivingBase, entityIn.entityBoundingBox.expandXyz(RANGE))
            for (entity in entities)
                if (entity is EntityLivingBase && entity.positionVector.subtract(entityIn.positionVector).lengthVector() <= RANGE) {
                    entity.addPotionEffect(ModPotionEffect(ModPotions.faithlessness, 5, 0, true, true))
                    if (!entity.equals(entityIn)) flag = true
                }
        }
        if (entityIn is EntityPlayer && isSelected) {
            if (flag)
                ManaItemHandler.requestManaExact(stack, entityIn, MANA_PER_TICK, true)
            entityIn.addPotionEffect(ModPotionEffect(ModPotions.faithlessness, 5, 0, true, true))
        }
    }

    override fun usesMana(p0: ItemStack) = true
    override fun isDiscordant(stack: ItemStack) = true
}
