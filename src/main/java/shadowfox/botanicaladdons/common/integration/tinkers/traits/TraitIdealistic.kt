//package shadowfox.botanicaladdons.common.integration.tinkers.traits
//
//import net.minecraft.entity.EntityLivingBase
//import net.minecraft.item.ItemStack
//import net.minecraftforge.event.entity.player.PlayerEvent
//import shadowfox.botanicaladdons.common.integration.tinkers.TinkersIntegration
//import slimeknights.tconstruct.library.traits.AbstractTrait
//
//class TraitIdealistic : AbstractTrait("idealistic", TinkersIntegration.DREAMWOOD_COLOR) {
//
//    override fun damage(tool: ItemStack, player: EntityLivingBase?, target: EntityLivingBase?, damage: Float, newDamage: Float, isCritical: Boolean): Float {
//        if (target!!.health <= 10) return newDamage
//
//        return newDamage + (target.health - 10) / 5
//    }
//
//    override fun miningSpeed(tool: ItemStack, event: PlayerEvent.BreakSpeed?) {
//        val hardness = event!!.state.getBlockHardness(event.entityPlayer.world, event.pos)
//        if (hardness == 0.0f)
//            event.newSpeed = 1.0f
//        else if (hardness < 5.0f)
//            event.newSpeed = 0.075f * event.newSpeed
//        else if (hardness < 20.0f)
//            event.newSpeed = 0.375f * event.newSpeed + hardness
//        else
//            event.newSpeed = 3.375f * event.newSpeed + hardness
//    }
//}
