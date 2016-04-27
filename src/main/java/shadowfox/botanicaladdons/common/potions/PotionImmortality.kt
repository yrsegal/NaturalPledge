package shadowfox.botanicaladdons.common.potions

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.DamageSource
import net.minecraft.util.math.Vec3d
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingAttackEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import shadowfox.botanicaladdons.common.lib.LibNames
import shadowfox.botanicaladdons.common.potions.base.PotionMod

/**
 * @author WireSegal
 * Created at 10:22 AM on 4/26/16.
 */
class PotionImmortality(iconIndex: Int) : PotionMod(LibNames.IMMORTALITY, false, 0xE2BD16, iconIndex, false) {

    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent
    fun onCreatureOw(e: LivingAttackEvent) {
        val creature = e.entityLiving
        val source = e.source
        if (hasEffect(creature) && !source.canHarmInCreative() && source !is DamageEffectWrapper) {
            if (source.entity == null && (e.amount > 1f || creature.health <= 1f)) {
                e.isCanceled = true
                if (creature.health > 1f)
                    creature.attackEntityFrom(DamageEffectWrapper(e.source), 1f)
            } else if (e.amount > 4f) {
                e.isCanceled = true
                creature.attackEntityFrom(DamageEffectWrapper(e.source), 4f)
            }
        }
    }

    class DamageEffectWrapper(val source: DamageSource) : DamageSource(source.damageType) {
        override fun canHarmInCreative() = source.canHarmInCreative()
        override fun getDamageLocation() = source.damageLocation
        override fun getDamageType() = source.getDamageType()
        override fun getDeathMessage(entityLivingBaseIn: EntityLivingBase?) = source.getDeathMessage(entityLivingBaseIn)
        override fun getEntity() = source.entity
        override fun getHungerDamage() = source.hungerDamage
        override fun getSourceOfDamage() = source.sourceOfDamage
        override fun isCreativePlayer() = source.isCreativePlayer
        override fun isDamageAbsolute() = source.isDamageAbsolute
        override fun isDifficultyScaled() = source.isDifficultyScaled
        override fun isExplosion() = source.isExplosion
        override fun isFireDamage() = source.isFireDamage
        override fun isMagicDamage() = source.isMagicDamage
        override fun isProjectile() = source.isProjectile
        override fun isUnblockable() = source.isUnblockable
        override fun setDamageAllowedInCreativeMode() = source.setDamageAllowedInCreativeMode()
        override fun setDamageBypassesArmor() = source.setDamageBypassesArmor()
        override fun setDamageIsAbsolute() = source.setDamageIsAbsolute()
        override fun setDifficultyScaled() = source.setDifficultyScaled()
        override fun setExplosion() = source.setExplosion()
        override fun setFireDamage() = source.setFireDamage()
        override fun setMagicDamage() = source.setMagicDamage()
        override fun setProjectile() = source.setProjectile()
        override fun toString() = source.toString()
        override fun equals(other: Any?) = source.equals(other)
        override fun hashCode() = source.hashCode()
    }
}
