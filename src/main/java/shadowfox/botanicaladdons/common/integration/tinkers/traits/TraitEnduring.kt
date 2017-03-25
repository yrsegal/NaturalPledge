//package shadowfox.botanicaladdons.common.integration.tinkers.traits
//
//import net.minecraft.entity.EntityLivingBase
//import net.minecraft.item.ItemStack
//import shadowfox.botanicaladdons.common.integration.tinkers.TinkersIntegration
//import slimeknights.tconstruct.library.traits.AbstractTrait
//import slimeknights.tconstruct.library.utils.ToolHelper
//
//class TraitEnduring : AbstractTrait("enduring", TinkersIntegration.LIVINGROCK_COLOR) {
//
//    override fun onToolDamage(tool: ItemStack, damage: Int, newDamage: Int, entity: EntityLivingBase?): Int {
//        val chance = tool!!.itemDamage.toFloat() * MAX_CHANCE / ToolHelper.getMaxDurability(tool).toFloat()
//        return if (Math.random() <= chance) 0 else newDamage
//    }
//
//    companion object {
//
//        private val MAX_CHANCE = 0.25f
//    }
//}
