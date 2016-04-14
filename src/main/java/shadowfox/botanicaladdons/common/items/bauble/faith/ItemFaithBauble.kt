package shadowfox.botanicaladdons.common.items.bauble.faith

import baubles.api.BaubleType
import baubles.common.lib.PlayerHandler
import com.google.common.collect.Multimap
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import shadowfox.botanicaladdons.client.core.ModelHandler
import shadowfox.botanicaladdons.common.items.base.ItemAttributeBauble
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.item.IBaubleRender
import vazkii.botania.common.core.helper.ItemNBTHelper

/**
 * @author WireSegal
 * Created at 1:50 PM on 4/13/16.
 */
open class ItemFaithBauble(name: String) : ItemAttributeBauble(name, *Array(variants.size, { "emblem${variants[it].name.capitalizeFirst()}" })), IBaubleRender, ModelHandler.IExtraVariantHolder {

    interface IFaithVariant {
        val name: String

        val hasSubscriptions: Boolean
            get() = false

        fun onUpdate(stack: ItemStack, player: EntityPlayer) {
        }

        fun onAwakenedUpdate(stack: ItemStack, player: EntityPlayer) {
            onUpdate(stack, player)
        }

        fun onRenderTick(stack: ItemStack, player: EntityPlayer, render: IBaubleRender.RenderType, renderTick: Float) {
        }

        fun addToTooltip(stack: ItemStack, player: EntityPlayer?, tooltip: MutableList<String>, advanced: Boolean) {
        }

        fun punishTheFaithless(stack: ItemStack, player: EntityPlayer)

        fun fillAttributes(map: Multimap<String, AttributeModifier>, stack: ItemStack) {
        }

    }

    companion object {

        fun String.capitalizeFirst(): String {
            if (this.length == 0) return this
            return this.slice(0..0).capitalize() + this.slice(1..this.length - 1)
        }

        val TAG_AWAKENED = "awakened"

        val variants = arrayOf(
                PriestlyEmblemNjord(),
                PriestlyEmblemIdunn()
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
                val variantInstance = (stack.item as ItemFaithBauble).getVariant(stack)
                if (variantInstance != null && variant.isInstance(variantInstance))
                    return stack
            }
            return null
        }

        fun isAwakened(stack: ItemStack) = ItemNBTHelper.getBoolean(stack, TAG_AWAKENED, false)
        fun setAwakened(stack: ItemStack, state: Boolean) = ItemNBTHelper.setBoolean(stack, TAG_AWAKENED, state)

        fun getVariantBase(stack: ItemStack) = if (variants.size == 0) null else variants[stack.itemDamage % variants.size]
    }

    override fun getRarity(stack: ItemStack) = if (isAwakened(stack)) BotaniaAPI.rarityRelic else super.getRarity(stack)

    override fun getBaubleType(stack: ItemStack) = BaubleType.AMULET

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

    override fun onPlayerBaubleRender(stack: ItemStack, player: EntityPlayer, render: IBaubleRender.RenderType, renderTick: Float) {
        val variant = getVariant(stack) ?: return
        variant.onRenderTick(stack, player, render, renderTick)
        //TODO render pendant
    }

    override fun onUpdate(stack: ItemStack, worldIn: World?, entityIn: Entity?, itemSlot: Int, isSelected: Boolean) {
        if (isAwakened(stack)) setAwakened(stack, false)
    }

    open fun getVariant(stack: ItemStack): IFaithVariant? {
        return getVariantBase(stack)
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

    override val extraVariants: Array<out String>
        get() = Array(Companion.variants.size, { "pendant${Companion.variants[it].name.capitalizeFirst()}" })
}
