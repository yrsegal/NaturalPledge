package shadowfox.botanicaladdons.common.items.bauble.faith

import net.minecraft.block.material.Material
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.MobEffects
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect
import net.minecraftforge.event.entity.living.LivingEvent
import net.minecraftforge.event.entity.player.AttackEntityEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import shadowfox.botanicaladdons.api.SpellRegistry
import shadowfox.botanicaladdons.api.item.IPriestlyEmblem
import shadowfox.botanicaladdons.api.priest.IFaithVariant
import shadowfox.botanicaladdons.common.items.ItemResource.Companion.of
import shadowfox.botanicaladdons.common.items.ItemResource.Variants.THUNDER_STEEL
import shadowfox.botanicaladdons.common.items.ItemSpellIcon.Variants.LIGHTNING_INFUSION
import shadowfox.botanicaladdons.common.lib.LibNames
import shadowfox.botanicaladdons.common.potions.ModPotions
import shadowfox.botanicaladdons.common.potions.base.ModPotionEffect
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.Botania
import vazkii.botania.common.core.helper.Vector3

/**
 * @author WireSegal
 * Created at 8:09 PM on 4/14/16.
 */
class PriestlyEmblemThor : IFaithVariant {

    init {
        SpellRegistry.registerSpell(LibNames.SPELL_LIGHTNING, Spells.Thor.Lightning())
        SpellRegistry.registerSpell(LibNames.SPELL_STRENGTH, Spells.Thor.Strength())
        SpellRegistry.registerSpell(LibNames.SPELL_PULL, Spells.Thor.Pull())
        SpellRegistry.registerSpell(LibNames.SPELL_THOR_INFUSION,
                Spells.ObjectInfusion(LIGHTNING_INFUSION, "ingotIron",
                        THUNDER_STEEL, 150, 0xE5DD00))
    }

    override fun getName(): String = "thor"

    override fun hasSubscriptions(): Boolean = true

    override fun getSpells(stack: ItemStack, player: EntityPlayer): MutableList<String> {
        return mutableListOf(LibNames.SPELL_LIGHTNING, LibNames.SPELL_STRENGTH, LibNames.SPELL_PULL, LibNames.SPELL_THOR_INFUSION)
    }

    override fun punishTheFaithless(stack: ItemStack, player: EntityPlayer) {
        player.addPotionEffect(ModPotionEffect(ModPotions.overcharged, 600))
    }

    @SubscribeEvent
    fun updatePlayerStep(e: LivingEvent.LivingUpdateEvent) {
        val player = e.entityLiving
        if (player is EntityPlayer) {
            val emblem = ItemFaithBauble.getEmblem(player, PriestlyEmblemThor::class.java) ?: return
            if ((player.onGround || player.capabilities.isFlying) && player.moveForward > 0F && !player.isInsideOfMaterial(Material.WATER))
                if (ManaItemHandler.requestManaExact(emblem, player, 1, true))
                    player.moveRelative(0.0f, 1.0f, 0.035f * if ((emblem.item as IPriestlyEmblem).isAwakened(emblem)) 1.5f else 1f)
        }
    }

    @SubscribeEvent
    fun onHitSomething(e: AttackEntityEvent) {
        val emblem = ItemFaithBauble.getEmblem(e.entityPlayer, PriestlyEmblemThor::class.java) ?: return
        val stackInHand = e.entityPlayer.heldItemMainhand

        if (stackInHand != null && stackInHand.item.getToolClasses(stackInHand).contains("axe") && e.target is EntityLivingBase) {
            if (ManaItemHandler.requestManaExact(emblem, e.entityPlayer, 10, true)) {
                Botania.proxy.lightningFX(e.entityPlayer.worldObj, Vector3.fromEntityCenter(e.entityPlayer), Vector3.fromEntityCenter(e.target), 1f, 0x00948B, 0x00E4D7)
                (e.target as EntityLivingBase).addPotionEffect(PotionEffect(MobEffects.SLOWNESS, 100, 3, true, false))
            }
        }
    }
}
