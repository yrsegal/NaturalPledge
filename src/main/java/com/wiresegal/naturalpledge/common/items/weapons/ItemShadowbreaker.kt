package com.wiresegal.naturalpledge.common.items.weapons

import com.teamwizardry.librarianlib.features.base.item.IGlowingItem
import com.teamwizardry.librarianlib.features.methodhandles.MethodHandleHelper
import com.wiresegal.naturalpledge.common.NaturalPledge
import com.wiresegal.naturalpledge.common.core.NPSoundEvents
import com.wiresegal.naturalpledge.common.items.base.ItemBaseSword
import com.wiresegal.naturalpledge.common.items.bauble.faith.Spells
import net.minecraft.client.renderer.block.model.IBakedModel
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.IProjectile
import net.minecraft.entity.item.EntityArmorStand
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.projectile.EntityArrow
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.SoundCategory
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingAttackEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.entity.EntityMagicMissile
import vazkii.botania.common.entity.EntityManaBurst
import vazkii.botania.common.entity.EntityPixie


/**
 * @author WireSegal
 * Created at 5:45 PM on 4/3/17.
 */
class ItemShadowbreaker(name: String, material: Item.ToolMaterial) : ItemBaseSword(name, material), IGlowingItem {

    companion object {
        init {
            MinecraftForge.EVENT_BUS.register(this)
        }

        private var no = false

        @SubscribeEvent
        fun onDamage(e: LivingAttackEvent) {
            if (no)
                return

            val held = e.entityLiving.heldItemMainhand
            if (held.isEmpty || held.item !is ItemShadowbreaker) {
                val offhand = e.entityLiving.heldItemOffhand
                if (offhand.isEmpty || offhand.item !is ItemShadowbreaker)
                    return
            }

            val attacker = e.source.immediateSource
            if (attacker is EntityMagicMissile || attacker is EntityPixie)
                e.isCanceled = true
            else if (attacker is IProjectile || attacker != e.source.trueSource) {
                e.isCanceled = true
                no = true
                e.entity.attackEntityFrom(e.source, e.amount / 2)
                no = false
            }
        }
    }

    @SideOnly(Side.CLIENT)
    override fun transformToGlow(itemStack: ItemStack, model: IBakedModel) = IGlowingItem.Helper.wrapperBake(model, false, 1)
    @SideOnly(Side.CLIENT)
    override fun shouldDisableLightingForGlow(itemStack: ItemStack, model: IBakedModel) = true

    val getInGround = MethodHandleHelper.wrapperForGetter(EntityArrow::class.java, "a", "field_70254_i", "inGround")

    override fun onUpdate(stack: ItemStack, world: World, player: Entity, slot: Int, selected: Boolean) {
        if (selected) {
            val entitiesAround = world.getEntitiesWithinAABB(Entity::class.java, player.entityBoundingBox.grow(4.0)) {
                it != null && it != player && it.positionVector.squareDistanceTo(player.positionVector) < 16.0
                        && ((it is EntityPixie) ||
                        (it is IProjectile && it !is EntityManaBurst && (it !is EntityMagicMissile || !it.isEvil) && !(it is EntityArrow && getInGround(it) as Boolean)) ||
                        (it is EntityLivingBase && it !is EntityArmorStand && it.positionVector.subtract(player.positionVector).dotProduct(player.lookVec) < 0))
            }

            if (entitiesAround.isNotEmpty()) {
                if (Spells.Njord.Interdict.pushEntities(player, player.posX, player.posY, player.posZ, 4.0, 0.3, entitiesAround)) {
                    if (!world.isRemote) {
                        if (player is EntityPlayer) ManaItemHandler.requestManaExact(stack, player, 5, true)
                        if (world.totalWorldTime % 3 == 0L)
                            world.playSound(null, player.posX, player.posY, player.posZ, NPSoundEvents.woosh, SoundCategory.PLAYERS, 0.4F, 1F)
                    } else NaturalPledge.PROXY.particleRing(player.posX, player.posY, player.posZ, 5.0, 0F, 0F, 1F)
                }
            }
        }
    }
}
