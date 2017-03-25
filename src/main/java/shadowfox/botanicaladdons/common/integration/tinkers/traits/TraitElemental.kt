//package shadowfox.botanicaladdons.common.integration.tinkers.traits
//
//import net.minecraft.entity.EntityLivingBase
//import net.minecraft.init.MobEffects
//import net.minecraft.item.ItemStack
//import net.minecraft.potion.PotionEffect
//import net.minecraft.world.biome.Biome
//import net.minecraftforge.event.entity.player.PlayerEvent
//import shadowfox.botanicaladdons.common.integration.tinkers.TinkersIntegration
//import slimeknights.tconstruct.library.traits.AbstractTrait
//import java.util.*
//
//class TraitElemental : AbstractTrait("elemental", TinkersIntegration.LIVINGROCK_COLOR) {
//
//    private fun getEnvironment(player: EntityLivingBase): Set<EnumEnvironmentType> {
//        val out = HashSet<EnumEnvironmentType>()
//        val biomeAt = player.world.getBiomeForCoordsBody(player.position)
//        if (biomeAt.tempCategory == Biome.TempCategory.COLD || biomeAt.tempCategory == Biome.TempCategory.OCEAN)
//            out.add(EnumEnvironmentType.WATER)
//        else if (biomeAt.tempCategory == Biome.TempCategory.WARM)
//            out.add(EnumEnvironmentType.FIRE)
//
//        if (player.posY < 32)
//            out.add(EnumEnvironmentType.EARTH)
//        if (player.posY > 128)
//            out.add(EnumEnvironmentType.AIR)
//
//        return out
//    }
//
//    override fun knockBack(tool: ItemStack, player: EntityLivingBase, target: EntityLivingBase?, damage: Float, knockback: Float, newKnockback: Float, isCritical: Boolean): Float {
//        return if (getEnvironment(player).contains(EnumEnvironmentType.AIR)) newKnockback * 1.5f else newKnockback
//    }
//
//    override fun miningSpeed(tool: ItemStack, event: PlayerEvent.BreakSpeed?) {
//        if (getEnvironment(event!!.entityPlayer).contains(EnumEnvironmentType.EARTH))
//            event.newSpeed = event.newSpeed * 2f
//    }
//
//    override fun damage(tool: ItemStack, player: EntityLivingBase, target: EntityLivingBase?, damage: Float, newDamage: Float, isCritical: Boolean): Float {
//        return if (getEnvironment(player).contains(EnumEnvironmentType.FIRE)) newDamage * 1.5f else newDamage
//    }
//
//    override fun onHit(tool: ItemStack, player: EntityLivingBase, target: EntityLivingBase?, damage: Float, isCritical: Boolean) {
//        if (getEnvironment(player).contains(EnumEnvironmentType.WATER)) {
//            target!!.addPotionEffect(PotionEffect(MobEffects.SLOWNESS, 40, 1))
//        }
//    }
//
//    private enum class EnumEnvironmentType {
//        FIRE, AIR,
//        WATER, EARTH
//    }
//}
