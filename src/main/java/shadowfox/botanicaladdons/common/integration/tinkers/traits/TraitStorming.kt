//package shadowfox.botanicaladdons.common.integration.tinkers.traits
//
//import net.minecraft.entity.EntityLivingBase
//import net.minecraft.item.ItemStack
//import net.minecraft.util.math.MathHelper
//import shadowfox.botanicaladdons.common.integration.tinkers.TinkersIntegration
//import slimeknights.tconstruct.library.traits.AbstractTrait
//
///**
// * @author WireSegal
// * Created at 5:00 PM on 6/25/16.
// */
//class TraitStorming : AbstractTrait("storming", TinkersIntegration.AQUAMARINE_COLORS[0]) {
//    override fun knockBack(tool: ItemStack, player: EntityLivingBase?, target: EntityLivingBase, damage: Float, knockback: Float, newKnockback: Float, isCritical: Boolean): Float {
//        return Math.min(newKnockback - 2, 0f)
//    }
//
//    override fun onHit(tool: ItemStack, player: EntityLivingBase?, target: EntityLivingBase, damage: Float, isCritical: Boolean) {
//        val angle = Math.random().toFloat() * 2 * Math.PI.toFloat()
//        target.knockBack(player, 2f, MathHelper.sin(angle).toDouble(), (-MathHelper.cos(angle)).toDouble())
//    }
//}
