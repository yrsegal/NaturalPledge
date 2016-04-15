package shadowfox.botanicaladdons.common.items.bauble.faith

import net.minecraft.block.material.Material
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.MobEffects
import net.minecraft.item.ItemStack
import net.minecraft.potion.Potion
import net.minecraft.potion.PotionEffect
import net.minecraft.util.DamageSource
import net.minecraft.util.math.MathHelper
import net.minecraftforge.event.entity.living.LivingEvent
import net.minecraftforge.event.entity.player.AttackEntityEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.Botania
import vazkii.botania.common.core.helper.Vector3
import java.util.*

/**
 * @author WireSegal
 * Created at 8:09 PM on 4/14/16.
 */
class PriestlyEmblemThor : ItemFaithBauble.IFaithVariant {
    override val name: String = "thor"

    override val hasSubscriptions: Boolean = true

    override fun punishTheFaithless(stack: ItemStack, player: EntityPlayer) {
        //TODO
    }

    @SubscribeEvent
    fun updatePlayerStep(e: LivingEvent.LivingUpdateEvent) {
        val player = e.entityLiving
        if (player is EntityPlayer) {
            val emblem = ItemFaithBauble.getEmblem(player, PriestlyEmblemThor::class.java) ?: return


            if ((player.onGround || player.capabilities.isFlying) && player.moveForward > 0F && !player.isInsideOfMaterial(Material.water))
                if (ManaItemHandler.requestManaExact(emblem, player, 1, true))
                    player.moveFlying(0.0f, 1.0f, 0.035f)
        }
    }

    @SubscribeEvent
    fun onHitSomething(e: AttackEntityEvent) {
        val emblem = ItemFaithBauble.getEmblem(e.entityPlayer, PriestlyEmblemThor::class.java) ?: return
        val stackInHand = e.entityPlayer.heldItemMainhand

        if (stackInHand != null && stackInHand.item.getToolClasses(stackInHand).contains("axe") && e.target is EntityLivingBase) {
            if (ManaItemHandler.requestManaExact(emblem, e.entityPlayer, 10, true)) {
                Botania.proxy.lightningFX(e.entityPlayer.worldObj, Vector3.fromEntityCenter(e.entityPlayer), Vector3.fromEntityCenter(e.target), 1f, 0x00948B, 0x00E4D7)
                (e.target as EntityLivingBase).addPotionEffect(PotionEffect(MobEffects.moveSlowdown, 100, 3, true, false))
            }
        }
    }
}
