//package shadowfox.botanicaladdons.common.integration.tinkers.traits
//
//import net.minecraft.entity.EntityLivingBase
//import net.minecraft.item.ItemStack
//import net.minecraft.util.math.MathHelper
//import net.minecraftforge.event.entity.player.PlayerEvent
//import shadowfox.botanicaladdons.common.integration.tinkers.TinkersIntegration
//import slimeknights.tconstruct.library.traits.AbstractTrait
//
//class TraitStargazer : AbstractTrait("stargazer", TinkersIntegration.DREAMWOOD_COLOR) {
//
//    private fun isStargazing(entity: EntityLivingBase): Boolean {
//        val angle = MathHelper.cos(entity.world.getCelestialAngleRadians(0f))
//        val nightTime = angle < 0
//        val canSeeSky = entity.world.canBlockSeeSky(entity.position)
//        return nightTime && canSeeSky
//    }
//
//    override fun miningSpeed(tool: ItemStack?, event: PlayerEvent.BreakSpeed?) {
//        if (isStargazing(event!!.entityPlayer))
//            event.newSpeed = event.newSpeed * 1.5f
//    }
//
//    override fun damage(tool: ItemStack?, player: EntityLivingBase, target: EntityLivingBase?, damage: Float, newDamage: Float, isCritical: Boolean): Float {
//        return if (isStargazing(player)) newDamage * 1.5f else newDamage
//    }
//
//    override fun onToolDamage(tool: ItemStack?, damage: Int, newDamage: Int, entity: EntityLivingBase): Int {
//        return if (isStargazing(entity) && Math.random() < 0.5f) 0 else newDamage
//    }
//
//
//}
