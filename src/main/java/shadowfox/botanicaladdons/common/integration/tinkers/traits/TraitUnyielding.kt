//package shadowfox.botanicaladdons.common.integration.tinkers.traits
//
//import net.minecraft.entity.EntityLivingBase
//import net.minecraft.item.ItemStack
//import net.minecraftforge.event.entity.player.PlayerEvent
//import shadowfox.botanicaladdons.common.integration.tinkers.TinkersIntegration
//import slimeknights.tconstruct.library.traits.AbstractTrait
//import slimeknights.tconstruct.library.utils.ToolHelper
//
//class TraitUnyielding : AbstractTrait("unyielding", TinkersIntegration.TERRASTEEL_COLOR) {
//
//    override fun onToolDamage(tool: ItemStack, damage: Int, newDamage: Int, entity: EntityLivingBase?): Int {
//        if (ToolHelper.getCurrentDurability(tool) >= 5f) return newDamage
//        val chance = 0.75f
//        return if (Math.random() <= chance) 0 else newDamage
//    }
//
//    override fun damage(tool: ItemStack, player: EntityLivingBase, target: EntityLivingBase, damage: Float, newDamage: Float, isCritical: Boolean): Float {
//        if (ToolHelper.getCurrentDurability(tool) < 5f)
//            return super.damage(tool, player, target, damage, newDamage / 2.5f, isCritical)
//        return super.damage(tool, player, target, damage, newDamage, isCritical)
//    }
//
//    override fun miningSpeed(tool: ItemStack, event: PlayerEvent.BreakSpeed) {
//        if (ToolHelper.getCurrentDurability(tool) < 5f)
//            event.newSpeed = event.newSpeed / 2.5f
//    }
//}
//
