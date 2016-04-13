package shadowfox.botanicaladdons.common.items.bauble.faith

import com.google.common.collect.Multimap
import net.minecraft.block.BlockLiquid
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraftforge.event.entity.player.AttackEntityEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

/**
 * @author WireSegal
 * Created at 4:38 PM on 4/13/16.
 */
class PriestlyEmblemNjord : ItemFaithBauble.IFaithVariant {
    override val hasSubscriptions = true

    override val name: String = "njord"

    override fun addToTooltip(stack: ItemStack, player: EntityPlayer?, tooltip: MutableList<String>, advanced: Boolean) {
        //TODO
    }

    override fun getAwakenerBlock() = null

    override fun fillAttributes(map: Multimap<String, AttributeModifier>, stack: ItemStack) {}
    override fun onUpdate(stack: ItemStack, player: EntityPlayer) {}
    override fun onAwakenedUpdate(stack: ItemStack, player: EntityPlayer) {
        //TODO
    }
    override fun punishTheFaithless(stack: ItemStack, player: EntityPlayer) {
        //TODO
    }

    @SubscribeEvent
    fun floatInWater(e: TickEvent.PlayerTickEvent) {
        val player = e.player
        val world = player.worldObj
        ItemFaithBauble.getEmblem(player, PriestlyEmblemNjord::class.java) ?: return

        if (player.capabilities.isFlying || player.isSneaking) return

        val shiftedPos = BlockPos(player.posX, player.posY-0.1, player.posZ)
        val secondPos = BlockPos(player.posX, player.posY+0.4, player.posZ)

        if (world.getBlockState(shiftedPos).block is BlockLiquid || world.getBlockState(player.position).block is BlockLiquid)
            player.motionY = 0.15
        else if (world.getBlockState(shiftedPos.down()).block is BlockLiquid && world.getBlockState(secondPos.down()).block is BlockLiquid) {
            player.motionY = 0.0
            player.fallDistance = 0f
        }
    }

    @SubscribeEvent
    fun onPlayerAttack(e: AttackEntityEvent) {
        val emblem = ItemFaithBauble.getEmblem(e.entityPlayer, PriestlyEmblemNjord::class.java) ?: return
        val entity = e.target
        if (entity is EntityLivingBase)
            entity.knockBack(e.entityPlayer, if (ItemFaithBauble.isAwakened(emblem)) 1f else 0.4f, MathHelper.sin(e.entityPlayer.rotationYaw * 0.017453292f).toDouble(), (-MathHelper.cos(e.entityPlayer.rotationYaw * 0.017453292f)).toDouble())
    }
}
