package shadowfox.botanicaladdons.common.items.bauble.faith

import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.MobEffects
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect
import net.minecraft.util.math.BlockPos
import net.minecraftforge.event.entity.living.LivingEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import shadowfox.botanicaladdons.common.items.ItemTerrestrialFocus
import shadowfox.botanicaladdons.common.potions.ModPotions
import shadowfox.botanicaladdons.common.potions.base.ModPotionEffect
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.Botania
import vazkii.botania.common.block.ModBlocks
import vazkii.botania.common.block.tile.TileBifrost
import vazkii.botania.common.item.ModItems
import java.awt.Color
import java.util.*

/**
 * @author WireSegal
 * Created at 9:49 AM on 4/18/16.
 */
class PriestlyEmblemHeimdall : ItemFaithBauble.IFaithVariant {
    override val name: String
        get() = "heimdall"

    override val hasSubscriptions: Boolean
        get() = true

    override val color: IItemColor?
        get() = IItemColor { itemStack, i ->
            if (i == 1)
                Color.HSBtoRGB((Botania.proxy.worldElapsedTicks * 2L % 360L).toFloat() / 360.0f, 1.0f, 1.0f)
            else
                0xFFFFFF
        }

    private interface IHeimdallEffect

    override fun onUpdate(stack: ItemStack, player: EntityPlayer) {
        if (ManaItemHandler.requestManaExact(stack, player, 1, true))
            player.addPotionEffect(object : PotionEffect(MobEffects.nightVision, 610, 0, true, false), IHeimdallEffect {})

    }

    override fun getSpells(stack: ItemStack, player: EntityPlayer): HashMap<String, out ItemTerrestrialFocus.IFocusSpell> {
        return hashMapOf()
    }

    override fun punishTheFaithless(stack: ItemStack, player: EntityPlayer) {
        player.addPotionEffect(ModPotionEffect(ModPotions.drab, 600))
        if (player.getActivePotionEffect(MobEffects.nightVision) is IHeimdallEffect)
            player.removeActivePotionEffect(MobEffects.nightVision)
    }

    @SubscribeEvent
    fun bifrostPlatform(e: LivingEvent.LivingUpdateEvent) {
        val player = e.entityLiving
        val world = player.worldObj
        if (player is EntityPlayer) {
            if ((player.heldItemMainhand?.item == ModItems.rainbowRod ?: false) || (player.heldItemOffhand?.item == ModItems.rainbowRod ?: false)) {
                val emblem = ItemFaithBauble.getEmblem(player, PriestlyEmblemHeimdall::class.java) ?: return
                if (ManaItemHandler.requestManaExact(emblem, player, 10, false)) {
                    val pos = BlockPos(player.posX, player.posY - 1, player.posZ)
                    val state = world.getBlockState(pos)
                    val block = state.block
                    if (block.isAir(state, world, pos) || block.isReplaceable(world, pos) || block.getMaterial(state).isLiquid) {
                        world.setBlockState(pos, ModBlocks.bifrost.defaultState)
                        val tileBifrost = world.getTileEntity(pos) as TileBifrost
                        tileBifrost.ticks = 10
                        player.fallDistance = 0f
                        ManaItemHandler.requestManaExact(emblem, player, 10, true)
                    } else if (block == ModBlocks.bifrost) {
                        val tileBifrost = world.getTileEntity(pos) as TileBifrost
                        if (tileBifrost.ticks < 2) {
                            tileBifrost.ticks = 10
                            ManaItemHandler.requestManaExact(emblem, player, 10, true)
                        }
                    }
                }
            }
        }
    }
}
