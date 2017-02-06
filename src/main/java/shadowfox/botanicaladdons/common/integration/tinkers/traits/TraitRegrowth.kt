package shadowfox.botanicaladdons.common.integration.tinkers.traits

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import shadowfox.botanicaladdons.common.integration.tinkers.TinkersIntegration
import slimeknights.tconstruct.library.modifiers.Modifier
import slimeknights.tconstruct.library.traits.AbstractTrait
import slimeknights.tconstruct.library.utils.ToolHelper

class TraitRegrowth : AbstractTrait("regrowth", TinkersIntegration.LIVINGWOOD_COLOR) {

    override fun onUpdate(tool: ItemStack?, world: World?, entity: Entity?, itemSlot: Int, isSelected: Boolean) {
        val chance = 30
        if (!world!!.isRemote && entity is EntityLivingBase && Modifier.random.nextInt(20 * chance) == 0) {
            ToolHelper.healTool(tool, 1, entity as EntityLivingBase?)
        }
    }
}

