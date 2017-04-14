package shadowfox.botanicaladdons.common.items.base

import com.teamwizardry.librarianlib.features.base.item.ItemModSword
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import vazkii.botania.api.mana.IManaUsingItem
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.item.equipment.tool.ToolCommons


/**
 * @author WireSegal
 * Created at 5:45 PM on 4/3/17.
 */
open class ItemBaseSword(name: String, material: ToolMaterial) : ItemModSword(name, material), IManaUsingItem {
    override fun hitEntity(par1ItemStack: ItemStack, par2EntityLivingBase: EntityLivingBase?, par3EntityLivingBase: EntityLivingBase): Boolean {
        ToolCommons.damageItem(par1ItemStack, 1, par3EntityLivingBase, 80)
        return true
    }

    override fun onBlockDestroyed(stack: ItemStack, world: World, state: IBlockState, pos: BlockPos, entity: EntityLivingBase): Boolean {
        if (state.getBlockHardness(world, pos) != 0f)
            ToolCommons.damageItem(stack, 1, entity, 80)

        return true
    }

    override fun onUpdate(stack: ItemStack, world: World, player: Entity, slot: Int, selected: Boolean) {
        if (!world.isRemote && player is EntityPlayer && stack.itemDamage > 0 && ManaItemHandler.requestManaExactForTool(stack, player, 160, true))
            stack.itemDamage = stack.itemDamage - 1
    }

    override fun usesMana(p0: ItemStack?) = true
}
