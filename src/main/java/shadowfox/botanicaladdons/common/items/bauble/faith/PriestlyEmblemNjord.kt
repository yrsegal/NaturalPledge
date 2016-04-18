package shadowfox.botanicaladdons.common.items.bauble.faith

import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.math.MathHelper
import net.minecraftforge.event.entity.player.AttackEntityEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import shadowfox.botanicaladdons.common.items.ItemTerrestrialFocus
import shadowfox.botanicaladdons.common.potions.ModPotions
import shadowfox.botanicaladdons.common.potions.base.ModPotionEffect
import vazkii.botania.api.item.IBaubleRender
import vazkii.botania.api.mana.ManaItemHandler
import java.util.*

/**
 * @author WireSegal
 * Created at 4:38 PM on 4/13/16.
 */
class PriestlyEmblemNjord : ItemFaithBauble.IFaithVariant {
    override val hasSubscriptions = true

    override val name: String = "njord"

    override fun getSpells(stack: ItemStack, player: EntityPlayer): HashMap<String, out ItemTerrestrialFocus.IFocusSpell> {
        return hashMapOf()
    }

    override fun punishTheFaithless(stack: ItemStack, player: EntityPlayer) {
        player.addPotionEffect(ModPotionEffect(ModPotions.featherweight, 600))
    }

    override fun onRenderTick(stack: ItemStack, player: EntityPlayer, render: IBaubleRender.RenderType, renderTick: Float) {
        //TODO
    }

    @SubscribeEvent
    fun floatInWater(e: TickEvent.PlayerTickEvent) {
        val player = e.player
        val world = player.worldObj
        val emblem = ItemFaithBauble.getEmblem(player, PriestlyEmblemNjord::class.java) ?: return

        if (player.capabilities.isFlying || player.isSneaking) return

        if (world.isAnyLiquid(player.entityBoundingBox)) {
            if (ManaItemHandler.requestManaExact(emblem, player, 2, true)) {
                player.motionY = 0.15
            }
        } else if (world.isAnyLiquid(player.entityBoundingBox.offset(0.0, -0.15, 0.0))) {
            if (ManaItemHandler.requestManaExact(emblem, player, 2, true)) {
                player.motionY = 0.0
                player.fallDistance = 0f
            }
        }
    }

    @SubscribeEvent
    fun onPlayerAttack(e: AttackEntityEvent) {
        val emblem = ItemFaithBauble.getEmblem(e.entityPlayer, PriestlyEmblemNjord::class.java) ?: return
        val entity = e.target
        if (entity is EntityLivingBase)
            if (ManaItemHandler.requestManaExact(emblem, e.entityPlayer, 20, true))
                entity.knockBack(e.entityPlayer, if (ItemFaithBauble.isAwakened(emblem)) 2.5f else 1f,
                        MathHelper.sin(e.entityPlayer.rotationYaw * Math.PI.toFloat() / 180).toDouble(),
                        -MathHelper.cos(e.entityPlayer.rotationYaw * Math.PI.toFloat() / 180).toDouble())
    }
}
