package shadowfox.botanicaladdons.common.items.bauble.faith

import baubles.api.BaubleType
import com.teamwizardry.librarianlib.features.base.item.IItemColorProvider
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper
import com.teamwizardry.librarianlib.features.kotlin.sendSpamlessMessage
import com.teamwizardry.librarianlib.features.utilities.client.ClientRunnable
import com.teamwizardry.librarianlib.features.utilities.client.pulseColor
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.block.model.ItemCameraTransforms
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.MobEffects
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.EnumRarity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect
import net.minecraft.util.NonNullList
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.Style
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.util.text.TextFormatting
import net.minecraft.world.World
import net.minecraftforge.event.entity.player.AttackEntityEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import shadowfox.botanicaladdons.api.item.IDiscordantItem
import shadowfox.botanicaladdons.api.item.IPriestlyEmblem
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.api.priest.IFaithVariant
import shadowfox.botanicaladdons.common.achievements.ModAchievements
import shadowfox.botanicaladdons.common.items.base.ItemBaseBauble
import shadowfox.botanicaladdons.common.items.bauble.faith.ItemFaithBauble.Companion.TAG_AWAKENED
import shadowfox.botanicaladdons.common.items.bauble.faith.ItemFaithBauble.Companion.TAG_PENDANT
import shadowfox.botanicaladdons.common.items.bauble.faith.ItemFaithBauble.Companion.isFaithless
import shadowfox.botanicaladdons.common.lib.LibNames
import shadowfox.botanicaladdons.common.potions.ModPotions
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

        fun hasAwakenedRagnarok(player: EntityPlayer): Boolean {
            val unlocked = ClientRunnable.produce {
                if (player is EntityPlayerSP) {
                    val writer = player.statFileWriter
                    writer.hasAchievementUnlocked(ModAchievements.sacredFlame) &&
                            writer.hasAchievementUnlocked(ModAchievements.sacredHorn) &&
                            writer.hasAchievementUnlocked(ModAchievements.sacredThunder) &&
                            writer.hasAchievementUnlocked(ModAchievements.sacredLife) &&
                            writer.hasAchievementUnlocked(ModAchievements.sacredAqua)
                } else null
            }


            if (unlocked != null) return unlocked

            return player.hasAchievement(ModAchievements.sacredFlame) &&
                    player.hasAchievement(ModAchievements.sacredHorn) &&
                    player.hasAchievement(ModAchievements.sacredThunder) &&
                    player.hasAchievement(ModAchievements.sacredLife) &&
                    player.hasAchievement(ModAchievements.sacredAqua)
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
            player.health = 0.01f
            player.addPotionEffect(PotionEffect(MobEffects.WITHER, 200, 3))
            player.removePotionEffect(MobEffects.NIGHT_VISION)
            player.setFire(10)
        }

        @SideOnly(Side.CLIENT)
        override fun getColor(): IItemColor? {
            return IItemColor { _, tintIndex -> if (tintIndex == 1) Color.RED.darker().pulseColor().rgb else -1 }
        }
    }

    override fun getSubItems(itemIn: Item, tab: CreativeTabs?, subItems: NonNullList<ItemStack>) {
        if (ItemRagnarokPendant.hasAwakenedRagnarok())
            super.getSubItems(itemIn, tab, subItems)
    }

    override val itemColorFunction: ((ItemStack, Int) -> Int)?
        get() = { stack, tintindex ->
            val variant = getVariant(stack)
            if (variant.color == null)
                0xFFFFFF
            else
                variant.color!!.getColorFromItemstack(stack, tintindex)
        }

    init {
        addPropertyOverride(ResourceLocation(LibMisc.MOD_ID, TAG_PENDANT)) {
            stack, _, _ ->
            if (ItemNBTHelper.getBoolean(stack, TAG_PENDANT, false)) 1f else 0f
        }
        addPropertyOverride(ResourceLocation(LibMisc.MOD_ID, TAG_AWAKENED)) {
            stack, _, _ ->
            if (ItemNBTHelper.getBoolean(stack, TAG_AWAKENED, false)) 1f else 0f
        }
    }

    override fun usesMana(p0: ItemStack) = true

    override fun isAwakened(stack: ItemStack) = ItemNBTHelper.getBoolean(stack, TAG_AWAKENED, false)
    override fun setAwakened(stack: ItemStack, state: Boolean) = ItemNBTHelper.setBoolean(stack, TAG_AWAKENED, state)

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
            ItemNBTHelper.setBoolean(renderStack, TAG_PENDANT, true)
            val armor = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST) != null

            GlStateManager.pushMatrix()
            IBaubleRender.Helper.rotateIfSneaking(player)
            IBaubleRender.Helper.translateToChest()
            IBaubleRender.Helper.defaultTransforms()
            GlStateManager.translate(0.0, 0.15, if (armor) 0.125 else 0.05)
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

        if (player is EntityPlayer)
            player.addStat(ModAchievements.donEmblem)

        if (!player.world.isRemote)
            setAwakened(stack, false)
    }

    override fun onEntityItemUpdate(entityItem: EntityItem): Boolean {
        val stack = entityItem.item
        if (isAwakened(stack)) setAwakened(stack, false)
        return false
    }

    val FAITH_HATES_YOU = 857974

    override fun onUnequipped(stack: ItemStack, player: EntityLivingBase) {
        super.onUnequipped(stack, player)
        val variant = getVariant(stack)
        if (player is EntityPlayer && !player.world.isRemote) {
            variant.punishTheFaithless(stack, player)
            player.sendSpamlessMessage(TextComponentTranslation((stack.unlocalizedName + ".angry")).setStyle(Style().setColor(TextFormatting.RED)), FAITH_HATES_YOU)
            player.addPotionEffect(PotionEffect(ModPotions.faithlessness, 600))
            if (isAwakened(stack))
                player.attackEntityFrom(ItemFaithBauble.FaithSource, Float.MAX_VALUE)
        }
        setAwakened(stack, false)
    }

    override fun addHiddenTooltip(stack: ItemStack, player: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
        super.addHiddenTooltip(stack, player, tooltip, advanced)
        val variant = getVariant(stack)
        variant.addToTooltip(stack, player, tooltip, advanced)
    }
}
