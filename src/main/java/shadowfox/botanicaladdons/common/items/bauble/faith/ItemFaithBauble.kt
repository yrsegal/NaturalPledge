package shadowfox.botanicaladdons.common.items.bauble.faith

import baubles.api.BaubleType
import baubles.common.lib.PlayerHandler
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.block.model.ItemCameraTransforms
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraft.util.DamageSource
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.Style
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.util.text.TextFormatting
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import shadowfox.botanicaladdons.api.item.IDiscordantItem
import shadowfox.botanicaladdons.api.item.IPriestlyEmblem
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.api.priest.IFaithVariant
import shadowfox.botanicaladdons.client.core.ModelHandler
import shadowfox.botanicaladdons.common.achievements.ModAchievements
import shadowfox.botanicaladdons.common.items.ModItems
import shadowfox.botanicaladdons.common.items.base.ItemModBauble
import shadowfox.botanicaladdons.common.potions.ModPotions
import shadowfox.botanicaladdons.common.potions.base.ModPotionEffect
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.item.IBaubleRender
import vazkii.botania.api.mana.IManaUsingItem
import vazkii.botania.common.core.helper.ItemNBTHelper

/**
 * @author WireSegal
 * Created at 1:50 PM on 4/13/16.
 */
class ItemFaithBauble(name: String) : ItemModBauble(name, *Array(priestVariants.size, { "emblem${priestVariants[it].name.capitalizeFirst()}" })),
        IManaUsingItem, IBaubleRender, ModelHandler.IColorProvider, IPriestlyEmblem {

    companion object {

        fun String.capitalizeFirst(): String {
            if (this.length == 0) return this
            return this.slice(0..0).capitalize() + this.slice(1..this.length - 1)
        }

        val TAG_PENDANT = "pendant"
        val TAG_AWAKENED = "awakened"

        val priestVariants = arrayOf(
                PriestlyEmblemNjord(),
                PriestlyEmblemIdunn(),
                PriestlyEmblemThor(),
                PriestlyEmblemHeimdall()
        )

        init {
            for (variant in priestVariants)
                if (variant.hasSubscriptions())
                    MinecraftForge.EVENT_BUS.register(variant)
        }

        fun getEmblem(player: EntityPlayer, variant: Class<out IFaithVariant>? = null): ItemStack? {
            if (isFaithless(player)) return null

            var baubles = PlayerHandler.getPlayerBaubles(player)
            var stack = baubles.getStackInSlot(0)
            if (stack != null && stack.item is IPriestlyEmblem) {
                val variantInstance = (stack.item as IPriestlyEmblem).getVariant(stack)
                if (variant == null || (variantInstance != null && variant.isInstance(variantInstance)))
                    return stack
            }
            return null
        }

        fun emblemOf(variant: Class<out IFaithVariant>): ItemStack? {
            for (i in priestVariants.indices) {
                if (variant.isInstance(priestVariants[i]))
                    return ItemStack(ModItems.emblem, 1, i)
            }
            return null
        }

        fun isFaithless(player: EntityPlayer): Boolean {
            return ModPotions.faithlessness.hasEffect(player)
        }
    }

    @SideOnly(Side.CLIENT)
    override fun getColor() = IItemColor { stack, tintindex ->
        val variant = getVariant(stack)
        if (variant == null || variant.color == null)
            0xFFFFFF
        else
            variant.color!!.getColorFromItemstack(stack, tintindex)
    }

    init {
        addPropertyOverride(ResourceLocation(LibMisc.MOD_ID, TAG_PENDANT)) {
            stack, world, entity ->
            if (ItemNBTHelper.getBoolean(stack, TAG_PENDANT, false)) 1f else 0f
        }
        addPropertyOverride(ResourceLocation(LibMisc.MOD_ID, TAG_AWAKENED)) {
            stack, world, entity ->
            if (ItemNBTHelper.getBoolean(stack, TAG_AWAKENED, false)) 1f else 0f
        }
    }

    override fun usesMana(p0: ItemStack) = true

    override fun isAwakened(stack: ItemStack) = ItemNBTHelper.getBoolean(stack, TAG_AWAKENED, false)
    override fun setAwakened(stack: ItemStack, state: Boolean) = ItemNBTHelper.setBoolean(stack, TAG_AWAKENED, state)

    override fun getRarity(stack: ItemStack) = if (isAwakened(stack)) BotaniaAPI.rarityRelic else super.getRarity(stack)

    override fun getBaubleType(stack: ItemStack) = BaubleType.AMULET

    override fun onWornTick(stack: ItemStack, player: EntityLivingBase) {
        super.onWornTick(stack, player)

        val variant = getVariant(stack)

        if (variant != null && player is EntityPlayer) {
            if (!isFaithless(player)) {
                if (isAwakened(stack)) variant.onAwakenedUpdate(stack, player)
                else variant.onUpdate(stack, player)
            } else if (isAwakened(stack) && player.health > 1f)
                player.attackEntityFrom(DamageSource.magic, Math.min(3.5f, player.health - 1f))
        }
    }

    override fun onPlayerBaubleRender(stack: ItemStack, player: EntityPlayer, render: IBaubleRender.RenderType, renderTick: Float) {
        val variant = getVariant(stack) ?: return

        GlStateManager.pushMatrix()
        if (render == IBaubleRender.RenderType.BODY) {
            if (player.isSneaking) {
                GlStateManager.translate(0.0, 0.3, 0.0)
                GlStateManager.rotate(90 / Math.PI.toFloat(), 1.0f, 0.0f, 0.0f)
            }
            val armor = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST) != null
            GlStateManager.rotate(180F, 1F, 0F, 0F)
            GlStateManager.translate(0.0, -0.3, if (armor) 0.175 else 0.05)

            val renderStack = stack.copy()
            ItemNBTHelper.setBoolean(renderStack, TAG_PENDANT, true)

            Minecraft.getMinecraft().renderItem.renderItem(renderStack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND)
        }
        GlStateManager.popMatrix()

        if (!isFaithless(player))
            variant.onRenderTick(stack, player, render, renderTick)

    }

    override fun onUpdate(stack: ItemStack, worldIn: World?, entityIn: Entity?, itemSlot: Int, isSelected: Boolean) {
        if (isAwakened(stack)) setAwakened(stack, false)
    }

    override fun getVariant(stack: ItemStack): IFaithVariant? {
        return if (priestVariants.size == 0) null else priestVariants[stack.itemDamage % priestVariants.size]
    }

    private fun checkDiscordant(stack: ItemStack?): Boolean {
        return stack != null && stack.item is IDiscordantItem && (stack.item as IDiscordantItem).isDiscordant(stack)
    }

    override fun canUnequip(stack: ItemStack, player: EntityLivingBase): Boolean {
        return checkDiscordant(player.heldItemOffhand) || checkDiscordant(player.heldItemMainhand) || !isAwakened(stack)
    }

    override fun onEquipped(stack: ItemStack, player: EntityLivingBase?) {
        super.onEquipped(stack, player)

        if (player is EntityPlayer)
            player.addStat(ModAchievements.donEmblem)

        setAwakened(stack, false)
    }

    override fun onEntityItemUpdate(entityItem: EntityItem): Boolean {
        val stack = entityItem.entityItem ?: return false
        if (isAwakened(stack)) setAwakened(stack, false)
        return false
    }

    override fun onUnequipped(stack: ItemStack, player: EntityLivingBase) {
        super.onUnequipped(stack, player)
        setAwakened(stack, false)
        val variant = getVariant(stack)
        if (variant != null && player is EntityPlayer) {
            player.addPotionEffect(ModPotionEffect(ModPotions.faithlessness, 600))
            if (player.worldObj.isRemote)
                player.addChatComponentMessage(TextComponentTranslation((stack.unlocalizedName + ".angry")).setStyle(Style().setColor(TextFormatting.RED)))
            variant.punishTheFaithless(stack, player)
        }
    }

    override fun addHiddenTooltip(stack: ItemStack, player: EntityPlayer?, tooltip: MutableList<String>, advanced: Boolean) {
        super.addHiddenTooltip(stack, player, tooltip, advanced)
        val variant = getVariant(stack) ?: return
        variant.addToTooltip(stack, player, tooltip, advanced)
    }
}
