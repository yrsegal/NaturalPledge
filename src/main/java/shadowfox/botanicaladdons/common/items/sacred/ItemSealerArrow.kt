package shadowfox.botanicaladdons.common.items.sacred

import com.teamwizardry.librarianlib.features.base.item.ItemModArrow
import net.minecraft.block.BlockDispenser
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.dispenser.BehaviorProjectileDispense
import net.minecraft.dispenser.IPosition
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.IProjectile
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.projectile.EntityArrow
import net.minecraft.inventory.IInventory
import net.minecraft.item.EnumRarity
import net.minecraft.item.Item
import net.minecraft.item.ItemArrow
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import shadowfox.botanicaladdons.common.entity.EntitySealedArrow
import sun.audio.AudioPlayer.player
import vazkii.botania.api.BotaniaAPI

/**
 * @author WireSegal
 * Created at 11:49 AM on 5/22/16.
 */
class ItemSealerArrow(name: String) : ItemModArrow(name){

    override fun generateArrowEntity(worldIn: World, stack: ItemStack, position: Vec3d, shooter: EntityLivingBase?): EntityArrow {
        return if (shooter == null) EntitySealedArrow(worldIn, position.x, position.y, position.z) else EntitySealedArrow(worldIn, shooter)
    }

    override fun getRarity(stack: ItemStack): EnumRarity? {
        return BotaniaAPI.rarityRelic
    }

    override fun isInfinite(stack: ItemStack, bow: ItemStack, player: EntityPlayer) = false


}
