package com.wiresegal.naturalpledge.common.items.bauble.faith

import baubles.api.BaubleType
import com.teamwizardry.librarianlib.features.base.item.IItemColorProvider
import com.teamwizardry.librarianlib.features.helpers.getNBTBoolean
import com.teamwizardry.librarianlib.features.helpers.setNBTBoolean
import com.teamwizardry.librarianlib.features.kotlin.sendSpamlessMessage
import com.teamwizardry.librarianlib.features.utilities.client.ClientRunnable
import com.teamwizardry.librarianlib.features.utilities.client.pulseColor
import com.wiresegal.naturalpledge.api.item.IDiscordantItem
import com.wiresegal.naturalpledge.api.item.IPriestlyEmblem
import com.wiresegal.naturalpledge.api.lib.LibMisc
import com.wiresegal.naturalpledge.api.priest.IFaithVariant
import com.wiresegal.naturalpledge.common.NaturalPledge
import com.wiresegal.naturalpledge.common.items.base.ItemBaseBauble
import com.wiresegal.naturalpledge.common.items.bauble.faith.ItemFaithBauble.Companion.TAG_AWAKENED
import com.wiresegal.naturalpledge.common.items.bauble.faith.ItemFaithBauble.Companion.TAG_PENDANT
import com.wiresegal.naturalpledge.common.items.bauble.faith.ItemFaithBauble.Companion.isFaithless
import com.wiresegal.naturalpledge.common.lib.LibNames
import com.wiresegal.naturalpledge.common.potions.ModPotions
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.block.model.ItemCameraTransforms
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.init.MobEffects
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.EnumRarity
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.Style
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.util.text.TextFormatting
import net.minecraft.world.World
import net.minecraftforge.event.entity.player.AttackEntityEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.item.IBaubleRender
import vazkii.botania.api.mana.IManaUsingItem
import java.awt.Color

/**
 * @author WireSegal
 * Created at 1:50 PM on 4/13/16.
 */
class ItemRagnarokPendant(name: String) : ItemBaseBauble(name),
        IManaUsingItem, IBaubleRender, IItemColorProvider, IPriestlyEmblem, IDiscordantItem {

    companion object Ragnarok : IFaithVariant {

        private val FAITH_HATES_YOU = 857974

        fun hasAwakenedRagnarok(player: EntityPlayer): Boolean {
            val unlocked = ClientRunnable.produce {
                if (player is EntityPlayerSP) {
                    NaturalPledge.PROXY.hasAdvancement(player, "sacred_flame") &&
                            NaturalPledge.PROXY.hasAdvancement(player, "sacred_horn") &&
                            NaturalPledge.PROXY.hasAdvancement(player, "sacred_thunder") &&
                            NaturalPledge.PROXY.hasAdvancement(player, "sacred_life") &&
                            NaturalPledge.PROXY.hasAdvancement(player, "sacred_aqua")
                } else null
            }


            if (unlocked != null) return unlocked
            if (player is EntityPlayerMP)
                return NaturalPledge.PROXY.hasAdvancement(player, "sacred_flame") &&
                        NaturalPledge.PROXY.hasAdvancement(player, "sacred_horn") &&
                        NaturalPledge.PROXY.hasAdvancement(player, "sacred_thunder") &&
                        NaturalPledge.PROXY.hasAdvancement(player, "sacred_life") &&
                        NaturalPledge.PROXY.hasAdvancement(player, "sacred_aqua")
            return false
        }

        fun hasAwakenedRagnarok(): Boolean {
            val player = ClientRunnable.produce { Minecraft.getMinecraft().player }
            return hasAwakenedRagnarok(player ?: return true)
        }

        override fun onUpdate(stack: ItemStack, player: EntityPlayer) {
            PriestlyEmblemHeimdall.onUpdate(stack, player)
            PriestlyEmblemIdunn.onUpdate(stack, player)
            PriestlyEmblemLoki.onUpdate(stack, player)
            PriestlyEmblemNjord.onUpdate(stack, player)
            PriestlyEmblemThor.onUpdate(stack, player)
            if (!player.world.isRemote)
                player.addPotionEffect(PotionEffect(ModPotions.trapSeer, 5, 0, true, false))
        }

        override fun onAwakenedUpdate(stack: ItemStack, player: EntityPlayer) {
            PriestlyEmblemHeimdall.onAwakenedUpdate(stack, player)
            PriestlyEmblemIdunn.onAwakenedUpdate(stack, player)
            PriestlyEmblemLoki.onAwakenedUpdate(stack, player)
            PriestlyEmblemNjord.onAwakenedUpdate(stack, player)
            PriestlyEmblemThor.onAwakenedUpdate(stack, player)
        }

        override fun hasSubscriptions() = true

        @SubscribeEvent
        fun onLivingAttack(e: AttackEntityEvent) {
            val target = e.target
            val emblem = ItemFaithBauble.getEmblem(e.entityPlayer, Ragnarok::class.java)
            if (emblem != null && target is EntityLivingBase) target.addPotionEffect(PotionEffect(ModPotions.faithlessness, 100))
        }

        override fun getName(): String {
            return "ragnarok"
        }

        override fun getSpells(stack: ItemStack, player: EntityPlayer): MutableList<String> {
            return mutableListOf(LibNames.SPELL_SOUL_MANIFESTATION, LibNames.SPELL_LEAP, LibNames.SPELL_STRENGTH, LibNames.SPELL_SPHERE, LibNames.SPELL_PROTECTION, LibNames.SPELL_FLAME_JET)
        }

        override fun punishTheFaithless(stack: ItemStack, player: EntityPlayer) {
            player.apply {
                health = 0.01f
                addPotionEffect(PotionEffect(MobEffects.WITHER, 200, 3))
                removePotionEffect(MobEffects.NIGHT_VISION)
                setFire(10)
            }

        }

        @SideOnly(Side.CLIENT)
        override fun getColor(): IItemColor? {
            return IItemColor { _, tintIndex -> if (tintIndex == 1) Color.RED.darker().pulseColor().rgb else -1 }
        }
    }

    override val itemColorFunction: ((ItemStack, Int) -> Int)?
        get() = { stack, tintindex ->
            val variant = getVariant(stack)
            if (variant.color == null)
                0xFFFFFF
            else
                variant.color!!.colorMultiplier(stack, tintindex)
        }

    init {
        addPropertyOverride(ResourceLocation(LibMisc.MOD_ID, TAG_PENDANT)) {
            stack, _, _ ->
            if (stack.getNBTBoolean(TAG_PENDANT, false)) 1f else 0f
        }
        addPropertyOverride(ResourceLocation(LibMisc.MOD_ID, TAG_AWAKENED)) {
            stack, _, _ ->
            if (stack.getNBTBoolean(TAG_AWAKENED, false)) 1f else 0f
        }
    }

    override fun usesMana(p0: ItemStack) = true

    override fun isAwakened(stack: ItemStack) = stack.getNBTBoolean(TAG_AWAKENED, false)
    override fun setAwakened(stack: ItemStack, state: Boolean) = stack.setNBTBoolean(TAG_AWAKENED, state)

    override fun getRarity(stack: ItemStack): EnumRarity = if (isAwakened(stack)) BotaniaAPI.rarityRelic else EnumRarity.EPIC

    override fun getBaubleType(stack: ItemStack) = BaubleType.AMULET

    override fun onWornTick(stack: ItemStack, player: EntityLivingBase) {
        super.onWornTick(stack, player)

        val variant = getVariant(stack)

        if (player is EntityPlayer) {
            if (!isFaithless(player)) {
                if (isAwakened(stack)) variant.onAwakenedUpdate(stack, player)
                else variant.onUpdate(stack, player)
            } else if (isAwakened(stack) && player.health > 1f)
                player.attackEntityFrom(ItemFaithBauble.FaithSource, Math.min(3.5f, player.health - 1f))
        }
    }

    override fun isDiscordant(stack: ItemStack): Boolean {
        return true
    }

    override fun onPlayerBaubleRender(stack: ItemStack, player: EntityPlayer, render: IBaubleRender.RenderType, renderTick: Float) {
        val variant = getVariant(stack)

        if (render == IBaubleRender.RenderType.BODY) {
            val renderStack = stack.copy()
            renderStack.setNBTBoolean(TAG_PENDANT, true)
            val hasChestArmor = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty

            GlStateManager.pushMatrix()
            IBaubleRender.Helper.rotateIfSneaking(player)
            IBaubleRender.Helper.translateToChest()
            IBaubleRender.Helper.defaultTransforms()
            GlStateManager.translate(0.0, 0.15, if (hasChestArmor) 0.125 else 0.05)
            Minecraft.getMinecraft().renderItem.renderItem(renderStack, ItemCameraTransforms.TransformType.NONE)
            GlStateManager.popMatrix()
        }

        if (!isFaithless(player))
            variant.onRenderTick(stack, player, render, renderTick)

    }

    override fun onUpdate(stack: ItemStack, worldIn: World?, entityIn: Entity?, itemSlot: Int, isSelected: Boolean) {
        if (isAwakened(stack)) setAwakened(stack, false)

        if (entityIn is EntityPlayer && isSelected && !entityIn.world.isRemote)
            entityIn.addPotionEffect(PotionEffect(ModPotions.faithlessness, 5, 0, true, true))
    }

    override fun getVariant(stack: ItemStack) = Ragnarok

    private fun checkDiscordant(stack: ItemStack): Boolean {
        return !stack.isEmpty && stack.item is IDiscordantItem && (stack.item as IDiscordantItem).isDiscordant(stack)
    }

    override fun canUnequip(stack: ItemStack, player: EntityLivingBase): Boolean {
        return checkDiscordant(player.heldItemOffhand) || checkDiscordant(player.heldItemMainhand) || !isAwakened(stack)
    }

    override fun onEquipped(stack: ItemStack, player: EntityLivingBase) {
        super.onEquipped(stack, player)

        if (!player.world.isRemote)
            setAwakened(stack, false)
    }

    override fun onEntityItemUpdate(entityItem: EntityItem): Boolean {
        val stack = entityItem.item
        if (isAwakened(stack)) setAwakened(stack, false)
        return false
    }

    override fun onUnequipped(stack: ItemStack, player: EntityLivingBase) {
        super.onUnequipped(stack, player)
        val variant = getVariant(stack)
        if (player is EntityPlayer && !player.world.isRemote) {
            variant.punishTheFaithless(stack, player)
            player.sendSpamlessMessage(TextComponentTranslation((getUnlocalizedNameInefficiently(stack) + ".angry")).setStyle(Style().setColor(TextFormatting.RED)), FAITH_HATES_YOU)
            player.addPotionEffect(PotionEffect(ModPotions.faithlessness, 600))
            player.attackEntityFrom(ItemFaithBauble.FaithSource, Float.MAX_VALUE)
        }
        setAwakened(stack, false)
    }

    override fun addHiddenTooltip(stack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag) {
        super.addHiddenTooltip(stack, world, tooltip, flag)
        val variant = getVariant(stack)
        variant.addToTooltip(stack, world, tooltip, flag)
    }
}
