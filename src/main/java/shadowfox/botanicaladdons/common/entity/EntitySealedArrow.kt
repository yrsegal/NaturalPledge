package shadowfox.botanicaladdons.common.entity

import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.projectile.EntityArrow
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import shadowfox.botanicaladdons.common.items.ModItems
import shadowfox.botanicaladdons.common.potions.ModPotions
import shadowfox.botanicaladdons.common.potions.base.ModPotionEffect

/**
 * @author WireSegal
 * Created at 9:17 PM on 5/25/16.
 */
class EntitySealedArrow : EntityArrow {

    constructor(world: World, player: EntityLivingBase?) : super(world, player)

    constructor(worldIn: World) : super(worldIn)

    constructor(worldIn: World, x: Double, y: Double, z: Double) : super(worldIn, x, y, z)

    override fun getArrowStack(): ItemStack? {
        return ItemStack(ModItems.sealArrow)
    }

    override fun arrowHit(living: EntityLivingBase?) {
        super.arrowHit(living)
        living?.addPotionEffect(ModPotionEffect(ModPotions.featherweight, 900))
    }
}
