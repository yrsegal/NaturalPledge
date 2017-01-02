package shadowfox.botanicaladdons.common.items.bauble.faith

import baubles.api.BaubleType
import baubles.api.BaublesApi
import com.teamwizardry.librarianlib.common.base.item.IItemColorProvider
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.block.model.ItemCameraTransforms
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
import shadowfox.botanicaladdons.api.item.IDiscordantItem
import shadowfox.botanicaladdons.api.item.IPriestlyEmblem
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.api.priest.IFaithVariant
import shadowfox.botanicaladdons.common.achievements.ModAchievements
import shadowfox.botanicaladdons.common.items.ModItems
import shadowfox.botanicaladdons.common.items.base.ItemModBauble
import shadowfox.botanicaladdons.common.lib.capitalizeFirst
import shadowfox.botanicaladdons.common.potions.ModPotions
import shadowfox.botanicaladdons.common.potions.base.ModPotionEffect
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.item.IBaubleRender
import vazkii.botania.api.mana.IManaUsingItem
import com.teamwizardry.librarianlib.common.util.ItemNBTHelper

/**
 * @author WireSegal
 * Created at 1:50 PM on 4/13/16.
 */
class ItemFaithBauble(name: String) : ItemModBauble(name, *Array(priestVariants.size, { "emblem${priestVariants[it].name.capitalizeFirst()}" })),
        IManaUsingItem, IBaubleRender, IItemColorProvider, IPriestlyEmblem {

    companion object {

        val TAG_PENDANT = "pendant"
        val TAG_AWAKENED = "awakened"

        val priestVariants = arrayOf(
                PriestlyEmblemNjord(),
                PriestlyEmblemIdunn(),
                PriestlyEmblemThor(),
                PriestlyEmblemHeimdall()/*,
                PriestlyEmblemLoki()*/
        )

        init {
            priestVariants
                    .filter { it.hasSubscriptions() }
                    .forEach { MinecraftForge.EVENT_BUS.register(it) }
        }

        fun getEmblem(player: EntityPlayer, variant: Class<out IFaithVariant>? = null): ItemStack? {
            if (isFaithless(player)) return null

            val baubles = BaublesApi.getBaublesHandler(player)
            val stack = baubles.getStackInSlot(0)
            if (stack != null && stack.item is IPriestlyEmblem) {
                val variantInstance = (stack.item as IPriestlyEmblem).getVariant(stack)
                if (variant == null || (variantInstance != null && variant.isInstance(variantInstance)))
                    return stack
            }
            return null
        }

        fun emblemOf(variant: Class<out IFaithVariant>): ItemStack? {
            return priestVariants.withIndex()
                    .firstOrNull { variant.isInstance(it.value) }
                    ?.let { ItemStack(ModItems.emblem, 1, it.index) }
        }

        fun isFaithless(player: EntityPlayer): Boolean {
            return ModPotions.faithlessness.hasEffect(player)
        }

        object faithSource : DamageSource("${LibMisc.MOD_ID}.faith") {
            init {
                setDamageBypassesArmor()
                setDamageIsAbsolute()
                setMagicDamage()
            }
        }
    }

    override val itemColorFunction: ((ItemStack, Int) -> Int)?
        get() = { stack, tintindex ->
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
                player.attackEntityFrom(faithSource, Math.min(3.5f, player.health - 1f))
        }
    }

    override fun onPlayerBaubleRender(stack: ItemStack, player: EntityPlayer, render: IBaubleRender.RenderType, renderTick: Float) {
        val variant = getVariant(stack) ?: return

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
    }

    override fun getVariant(stack: ItemStack): IFaithVariant? {
        return if (priestVariants.isEmpty()) null else priestVariants[stack.itemDamage % priestVariants.size]
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
        val variant = getVariant(stack)
        if (variant != null && player is EntityPlayer && !player.worldObj.isRemote) {
            player.addPotionEffect(ModPotionEffect(ModPotions.faithlessness, 600))
            if (isAwakened(stack))
                player.attackEntityFrom(faithSource, Float.MAX_VALUE)
            else {
                variant.punishTheFaithless(stack, player)
                player.addChatComponentMessage(TextComponentTranslation((stack.unlocalizedName + ".angry")).setStyle(Style().setColor(TextFormatting.RED)))
            }
        }
        setAwakened(stack, false)
    }

    override fun addHiddenTooltip(stack: ItemStack, player: EntityPlayer?, tooltip: MutableList<String>, advanced: Boolean) {
        super.addHiddenTooltip(stack, player, tooltip, advanced)
        val variant = getVariant(stack) ?: return
        variant.addToTooltip(stack, player, tooltip, advanced)
    }
}
