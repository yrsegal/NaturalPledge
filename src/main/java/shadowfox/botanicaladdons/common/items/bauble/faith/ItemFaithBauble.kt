package shadowfox.botanicaladdons.common.items.bauble.faith

import baubles.api.BaubleType
import baubles.common.lib.PlayerHandler
import com.google.common.collect.Multimap
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraftforge.common.MinecraftForge
import shadowfox.botanicaladdons.common.items.base.ItemAttributeBauble
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.common.core.helper.ItemNBTHelper

/**
 * @author WireSegal
 * Created at 1:50 PM on 4/13/16.
 */
class ItemFaithBauble(name: String) : ItemAttributeBauble(name, *Array(variants.size, {"emblem${variants[it].name.capitalizeFirst()}"})) {

    interface IFaithVariant {
        val name: String

        fun onUpdate(stack: ItemStack, player: EntityPlayer)

        fun onAwakenedUpdate(stack: ItemStack, player: EntityPlayer)

        fun addToTooltip(stack: ItemStack, player: EntityPlayer?, tooltip: MutableList<String>, advanced: Boolean)

        fun punishTheFaithless(stack: ItemStack, player: EntityPlayer)

        fun getAwakenerBlock(): IBlockState?

        fun fillAttributes(map: Multimap<String, AttributeModifier>, stack: ItemStack)

        val hasSubscriptions: Boolean
    }

    companion object {

        private fun String.capitalizeFirst(): String {
            if (this.length == 0) return this
            return this.slice(0..0).capitalize() + this.slice(1..this.length-1)
        }

        val TAG_AWAKENED = "awakened"

        val variants = arrayOf<IFaithVariant>(
            PriestlyEmblemNjord()
        )

        init {
            for (variant in variants)
                if (variant.hasSubscriptions)
                    MinecraftForge.EVENT_BUS.register(variant)
        }

        fun getEmblem(player: EntityPlayer, variant: Class<out IFaithVariant>): ItemStack? {
            var baubles = PlayerHandler.getPlayerBaubles(player)
            var stack = baubles.getStackInSlot(0)
            if (stack != null && stack.item is ItemFaithBauble) {
                val variantInstance = getVariant(stack)
                if (variantInstance != null && variant.isInstance(variantInstance))
                    return stack
            }
            return null
        }

        fun isAwakened(stack: ItemStack) = ItemNBTHelper.getBoolean(stack, TAG_AWAKENED, false)
        fun setAwakened(stack: ItemStack, state: Boolean) = ItemNBTHelper.setBoolean(stack, TAG_AWAKENED, state)

        fun getVariant(stack: ItemStack) = if (variants.size == 0) null else variants[stack.itemDamage % variants.size]
    }

    override fun getRarity(stack: ItemStack) = if (isAwakened(stack)) BotaniaAPI.rarityRelic else super.getRarity(stack)

    override fun getBaubleType(p0: ItemStack) = BaubleType.AMULET

    override fun onWornTick(stack: ItemStack, player: EntityLivingBase) {
        super.onWornTick(stack, player)

        val variant = getVariant(stack)
        if (variant != null && player is EntityPlayer) {
            if (isAwakened(stack)) {
                variant.onAwakenedUpdate(stack, player)
            } else {
                variant.onUpdate(stack, player)
            }
        }
    }

    override fun canUnequip(stack: ItemStack, player: EntityLivingBase) = !isAwakened(stack)
    override fun onEquipped(stack: ItemStack, player: EntityLivingBase?) {
        super.onEquipped(stack, player)
        setAwakened(stack, false)
    }
    override fun onUnequipped(stack: ItemStack, player: EntityLivingBase) {
        super.onUnequipped(stack, player)
        val variant = getVariant(stack)
        if (variant != null && player is EntityPlayer)
            variant.punishTheFaithless(stack, player)
    }

    override fun addHiddenTooltip(stack: ItemStack, player: EntityPlayer?, tooltip: MutableList<String>, advanced: Boolean) {
        super.addHiddenTooltip(stack, player, tooltip, advanced)
        val variant = getVariant(stack) ?: return
        variant.addToTooltip(stack, player, tooltip, advanced)
    }

    override fun fillModifiers(map: Multimap<String, AttributeModifier>, stack: ItemStack) {
        val variant = getVariant(stack) ?: return

        variant.fillAttributes(map, stack)
    }
}
