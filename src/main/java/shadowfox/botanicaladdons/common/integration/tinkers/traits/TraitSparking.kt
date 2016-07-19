package shadowfox.botanicaladdons.common.integration.tinkers.traits

import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.effect.EntityLightningBolt
import net.minecraft.item.ItemStack
import net.minecraft.util.SoundCategory
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.EntityStruckByLightningEvent
import shadowfox.botanicaladdons.common.integration.tinkers.TinkersIntegration
import slimeknights.tconstruct.library.traits.AbstractTrait
import vazkii.botania.api.sound.BotaniaSoundEvents
import vazkii.botania.common.Botania
import vazkii.botania.common.core.helper.Vector3

/**
 * @author WireSegal
 * Created at 5:05 PM on 6/25/16.
 */
class TraitSparking : AbstractTrait("sparking", TinkersIntegration.THUNDERSTEEL_COLOR) {
    override fun onHit(tool: ItemStack?, player: EntityLivingBase, target: EntityLivingBase, damage: Float, isCritical: Boolean) {
        var stricken: EntityLivingBase? = null
        if (Math.random() < 0.25f)
            stricken = target
        else if (Math.random() < 0.15f)
            stricken = player

        val fakeBolt = EntityLightningBolt(player.worldObj, (stricken ?: return).posX, stricken.posY, stricken.posZ, true)

        val event = EntityStruckByLightningEvent(stricken, fakeBolt)
        MinecraftForge.EVENT_BUS.post(event)
        if (!event.isCanceled)
            stricken.onStruckByLightning(fakeBolt)
        if (player.worldObj.isRemote && stricken != player)
            Botania.proxy.lightningFX(player.worldObj, Vector3.fromEntityCenter(player), Vector3.fromEntityCenter(stricken), 1f, 0x00948B, 0x00E4D7)
        player.worldObj.playSound(null, player.position, BotaniaSoundEvents.missile, SoundCategory.PLAYERS, 1f, 1f)
    }
}
