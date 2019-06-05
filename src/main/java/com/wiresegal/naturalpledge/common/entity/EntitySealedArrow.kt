package com.wiresegal.naturalpledge.common.entity

import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.projectile.EntityArrow
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect
import net.minecraft.world.World
import com.wiresegal.naturalpledge.common.items.ModItems
import com.wiresegal.naturalpledge.common.potions.ModPotions

/**
 * @author WireSegal
 * Created at 9:17 PM on 5/25/16.
 */
class EntitySealedArrow : EntityArrow {

    constructor(world: World, player: EntityLivingBase?) : super(world, player)

    constructor(worldIn: World) : super(worldIn)

    constructor(worldIn: World, x: Double, y: Double, z: Double) : super(worldIn, x, y, z)

    override fun getArrowStack(): ItemStack {
        return ItemStack(ModItems.sealArrow)
    }

    override fun arrowHit(living: EntityLivingBase) {
        super.arrowHit(living)
        if (!living.world.isRemote)
            living.addPotionEffect(PotionEffect(ModPotions.featherweight, 900))
    }
}
