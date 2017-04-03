package shadowfox.botanicaladdons.common.items.weapons

import com.teamwizardry.librarianlib.common.util.handles.MethodHandleHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.IProjectile
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.projectile.EntityArrow
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.SoundCategory
import net.minecraft.world.World
import shadowfox.botanicaladdons.common.BotanicalAddons
import shadowfox.botanicaladdons.common.core.BASoundEvents
import shadowfox.botanicaladdons.common.items.base.ItemBaseSword
import shadowfox.botanicaladdons.common.items.bauble.faith.Spells
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.entity.EntityManaBurst


/**
 * @author WireSegal
 * Created at 5:45 PM on 4/3/17.
 */
class ItemShadowbreaker(name: String, material: Item.ToolMaterial) : ItemBaseSword(name, material) {
    val getInGround = MethodHandleHelper.wrapperForGetter(EntityArrow::class.java, "a", "field_70254_i", "inGround")

    override fun onUpdate(stack: ItemStack, world: World, player: Entity, slot: Int, selected: Boolean) {
        if (selected) {
            val entitiesAround = world.getEntitiesWithinAABB(Entity::class.java, player.entityBoundingBox.expandXyz(5.0)) {
                it != null && it != player && it.positionVector.squareDistanceTo(player.positionVector) < 25.0
                        && ((it is IProjectile && it !is EntityManaBurst && !(it is EntityArrow && getInGround(it) as Boolean)) ||
                        (it is EntityLivingBase && it.positionVector.subtract(player.positionVector).dotProduct(player.lookVec) < 0))
            }

            if (entitiesAround.isNotEmpty()) {
                if (Spells.Njord.Interdict.pushEntities(player.posX, player.posY, player.posZ, 5.0, 0.6, entitiesAround)) {
                    if (!world.isRemote) {
                        if (player is EntityPlayer) ManaItemHandler.requestManaExact(stack, player, 5, true)
                        if (world.totalWorldTime % 3 == 0L)
                            world.playSound(null, player.posX, player.posY, player.posZ, BASoundEvents.woosh, SoundCategory.PLAYERS, 0.4F, 1F)
                    } else BotanicalAddons.PROXY.particleRing(player.posX, player.posY, player.posZ, 5.0, 0F, 0F, 1F)
                }
            }
        }
    }
}
