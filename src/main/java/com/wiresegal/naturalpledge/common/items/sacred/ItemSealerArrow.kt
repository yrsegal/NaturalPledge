package com.wiresegal.naturalpledge.common.items.sacred

import com.teamwizardry.librarianlib.features.base.item.ItemModArrow
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.projectile.EntityArrow
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import com.wiresegal.naturalpledge.common.entity.EntitySealedArrow
import vazkii.botania.api.BotaniaAPI

/**
 * @author WireSegal
 * Created at 11:49 AM on 5/22/16.
 */
class ItemSealerArrow(name: String) : ItemModArrow(name){

    override fun generateArrowEntity(worldIn: World, stack: ItemStack, position: Vec3d, shooter: EntityLivingBase?): EntityArrow {
        return if (shooter == null) EntitySealedArrow(worldIn, position.x, position.y, position.z) else EntitySealedArrow(worldIn, shooter)
    }

    override fun getRarity(stack: ItemStack) = BotaniaAPI.rarityRelic!!

    override fun isInfinite(stack: ItemStack, bow: ItemStack, player: EntityPlayer) = false


}
