package shadowfox.botanicaladdons.common.potions

import com.teamwizardry.librarianlib.features.base.PotionMod
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.attributes.AbstractAttributeMap
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import shadowfox.botanicaladdons.common.lib.LibNames

/**
 * @author WireSegal
 * Created at 2:47 PM on 4/15/16.
 */
class PotionRooted : PotionMod(LibNames.ROOTED, true, 0x634D05) {
    init {
        MinecraftForge.EVENT_BUS.register(this)
        registerPotionAttributeModifier(SharedMonsterAttributes.KNOCKBACK_RESISTANCE, "F412C29C-0DB3-11E6-B4DD-7CEA70D5A8C7", 1.0, 0)
    }

    // begin hacky nonsense to make mobs have no ai
    override fun applyAttributesModifiersToEntity(entityLivingBaseIn: EntityLivingBase, map: AbstractAttributeMap, amplifier: Int) {
        if (entityLivingBaseIn is EntityLiving)
            entityLivingBaseIn.setNoAI(true)
        super.applyAttributesModifiersToEntity(entityLivingBaseIn, map, amplifier)
    }

    override fun removeAttributesModifiersFromEntity(entityLivingBaseIn: EntityLivingBase, map: AbstractAttributeMap, amplifier: Int) {
        if (entityLivingBaseIn is EntityLiving)
            entityLivingBaseIn.setNoAI(false)
        super.removeAttributesModifiersFromEntity(entityLivingBaseIn, map, amplifier)
    }
    // end hacky nonsense to make mobs have no ai

    @SubscribeEvent
    fun onLivingTick(e: LivingEvent.LivingUpdateEvent) {
        val entity = e.entityLiving
        if (entity !is EntityLiving && this.hasEffect(entity)) {
            if (entity.onGround)
                entity.setPosition(entity.prevPosX, entity.prevPosY, entity.prevPosZ)
            if (entity is EntityPlayer && entity.capabilities.isFlying && !entity.isCreative) {
                entity.capabilities.isFlying = false
            }
        } else if (entity is EntityLiving && this.hasEffect(entity)) {
            entity.setNoAI(true)
            if (entity.onGround) {
                entity.motionX = 0.0
                entity.motionY = 0.0
                entity.motionZ = 0.0
                entity.onGround = false
            }
        }
    }

    @SubscribeEvent
    fun onJump(e: LivingEvent.LivingJumpEvent) {
        val entity = e.entityLiving
        if (entity !is EntityLiving && this.hasEffect(entity)) {
            entity.motionY = -1.0
            entity.setPosition(entity.prevPosX, entity.prevPosY, entity.prevPosZ)
        }
    }

    override fun getCurativeItems(): MutableList<ItemStack> = mutableListOf()
}
