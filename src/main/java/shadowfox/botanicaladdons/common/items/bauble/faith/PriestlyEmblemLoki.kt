package shadowfox.botanicaladdons.common.items.bauble.faith

//import shadowfox.botanicaladdons.common.network.LeftClickMessage
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.util.DamageSource
import net.minecraftforge.event.entity.living.LivingAttackEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import shadowfox.botanicaladdons.api.item.IPriestlyEmblem
import shadowfox.botanicaladdons.api.priest.IFaithVariant
import shadowfox.botanicaladdons.common.potions.ModPotions
import shadowfox.botanicaladdons.common.potions.base.ModPotionEffect

/**
 * @author WireSegal
 * Created at 4:38 PM on 4/13/16.
 */
class PriestlyEmblemLoki : IFaithVariant {

    override fun getName(): String = "loki"

    override fun hasSubscriptions(): Boolean = true

    override fun getSpells(stack: ItemStack, player: EntityPlayer): MutableList<String> {
        return mutableListOf()
    }

    override fun punishTheFaithless(stack: ItemStack, player: EntityPlayer) {
        player.addPotionEffect(ModPotionEffect(ModPotions.everburn, 600))
    }

    override fun onUpdate(stack: ItemStack, player: EntityPlayer) {
        if (player.isBurning) player.extinguish()
    }

    @SubscribeEvent
    fun leftClick(evt: PlayerInteractEvent.LeftClickEmpty) {
        if (evt.itemStack != null && evt.itemStack?.item == Items.FIRE_CHARGE && !evt.entityPlayer.cooldownTracker.hasCooldown(Items.FIRE_CHARGE)) {
//            BotanicalAddons.NETWORK.sendToServer(LeftClickMessage())
        }
    }

    @SubscribeEvent
    fun onPlayerHurt(e: LivingAttackEvent) {
        if (e.entityLiving !is EntityPlayer) return
        val bauble = ItemFaithBauble.getEmblem(e.entityLiving as EntityPlayer, PriestlyEmblemLoki::class.java) ?: return
        val awakened = (bauble.item as IPriestlyEmblem).isAwakened(bauble)
        if (e.source.isFireDamage && (e.source != DamageSource.lava || awakened)) {
            e.isCanceled = true
            e.entityLiving.heal(0.025f)
        }
    }
}
