package shadowfox.botanicaladdons.common.items.base

import com.teamwizardry.librarianlib.client.util.TooltipHelper
import com.teamwizardry.librarianlib.common.base.item.ItemModArmor
import com.teamwizardry.librarianlib.common.util.ItemNBTHelper
import com.teamwizardry.librarianlib.common.util.VariantHelper
import com.teamwizardry.librarianlib.common.util.currentModId
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.model.ModelBiped
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.DamageSource
import net.minecraft.util.text.TextFormatting
import net.minecraft.world.World
import net.minecraftforge.common.ISpecialArmor
import net.minecraftforge.fml.common.Optional
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import thaumcraft.api.items.IRunicArmor
import vazkii.botania.api.item.IPhantomInkable
import vazkii.botania.api.mana.IManaDiscountArmor
import vazkii.botania.api.mana.IManaUsingItem
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.client.lib.LibResources
import vazkii.botania.common.core.handler.ConfigHandler
import vazkii.botania.common.item.equipment.tool.ToolCommons
import java.util.*

/**
 * @author WireSegal
 * Created at 3:52 PM on 4/2/17.
 */
@Optional.Interface(modid = "thaumcraft", iface = "thaumcraft.api.items.IRunicArmor")
abstract class ItemBaseArmor(name: String, val type: EntityEquipmentSlot, mat: ArmorMaterial) : ItemModArmor(name, mat, type), ISpecialArmor, IManaUsingItem, IPhantomInkable, IRunicArmor, IManaDiscountArmor {

    val matName = VariantHelper.toSnakeCase(mat.getName())

    protected val models: MutableMap<EntityEquipmentSlot, Any> by lazy {
        EnumMap<EntityEquipmentSlot, Any>(EntityEquipmentSlot::class.java)
    }

    override fun getProperties(player: EntityLivingBase, armor: ItemStack, source: DamageSource, damage: Double, slot: Int): ISpecialArmor.ArmorProperties {
        if (source.isUnblockable)
            return ISpecialArmor.ArmorProperties(0, 0.0, 0)
        return ISpecialArmor.ArmorProperties(0, damageReduceAmount / 25.0, armor.maxDamage + 1 - armor.itemDamage)
    }

    override fun getArmorDisplay(player: EntityPlayer, armor: ItemStack, slot: Int) = damageReduceAmount

    override fun onUpdate(stack: ItemStack, world: World, player: Entity, par4: Int, par5: Boolean) {
        if (player is EntityPlayer && !world.isRemote && stack.itemDamage > 0 && ManaItemHandler.requestManaExact(stack, player, MANA_PER_DAMAGE * 2, true))
            stack.itemDamage = stack.itemDamage - 1
    }

    override fun onArmorTick(world: World, player: EntityPlayer, stack: ItemStack) {
        if (!world.isRemote && stack.itemDamage > 0 && ManaItemHandler.requestManaExact(stack, player, MANA_PER_DAMAGE * 2, true))
            stack.itemDamage = stack.itemDamage - 1
    }

    override fun damageArmor(entity: EntityLivingBase, stack: ItemStack, source: DamageSource, damage: Int, slot: Int)
            = ToolCommons.damageItem(stack, damage, entity, MANA_PER_DAMAGE)

    override fun getArmorTexture(stack: ItemStack, entity: Entity?, slot: EntityEquipmentSlot, type: String?)
            = if (hasPhantomInk(stack)) LibResources.MODEL_INVISIBLE_ARMOR else armorTexture

    abstract val armorTexture: String

    @SideOnly(Side.CLIENT)
    override fun getArmorModel(entityLiving: EntityLivingBase?, itemStack: ItemStack?, armorSlot: EntityEquipmentSlot, original: ModelBiped): ModelBiped? {
        if (ConfigHandler.enableArmorModels) {
            val model = provideArmorModelForSlot(armorSlot)
            model?.setModelAttributes(original)
            return model
        }

        return super.getArmorModel(entityLiving, itemStack, armorSlot, original)
    }

    @SideOnly(Side.CLIENT)
    open fun makeArmorModel(slot: EntityEquipmentSlot): ModelBiped? = null

    @SideOnly(Side.CLIENT)
    fun provideArmorModelForSlot(slot: EntityEquipmentSlot): ModelBiped? {
        val model = models[slot]
        if (model == null) {
            val newModel = makeArmorModel(slot) ?: return null
            models.put(slot, newModel)
            return newModel
        }
        return models[slot] as ModelBiped
    }

    override fun usesMana(stack: ItemStack) = true

    @SideOnly(Side.CLIENT)
    override fun addInformation(stack: ItemStack, player: EntityPlayer, list: MutableList<String>, adv: Boolean) {
        if (GuiScreen.isShiftKeyDown())
            addInformationAfterShift(stack, player, list)
        else
            TooltipHelper.addToTooltip(list, "botaniamisc.shiftinfo")
    }

    @SideOnly(Side.CLIENT)
    fun addInformationAfterShift(stack: ItemStack, player: EntityPlayer, list: MutableList<String>) {
        TooltipHelper.addToTooltip(list, getArmorSetTitle(player))
        addArmorSetDescription(list)
        val stacks = armorSetStacks
        EntityEquipmentSlot.values()
                .filter { it.slotType == EntityEquipmentSlot.Type.ARMOR }
                .forEach { TooltipHelper.addToTooltip(list, (if (hasArmorSetItem(player, it)) TextFormatting.GREEN.toString() else "") + " - " + ItemStack(stacks[it]).displayName) }
        if (hasPhantomInk(stack))
            TooltipHelper.addToTooltip(list, "botaniamisc.hasPhantomInk")
    }

    protected data class ArmorSet(val helm: Item?, val chest: Item?, val legs: Item?, val boots: Item?)
            : List<Item> by makeList(helm, chest, legs, boots) {

        companion object {
            private fun makeList(head: Item?, chest: Item?, legs: Item?, boots: Item?): List<Item> {
                val list = mutableListOf<Item>()
                if (head != null) list.add(head)
                if (chest != null) list.add(chest)
                if (legs != null) list.add(legs)
                if (boots != null) list.add(boots)
                return list
            }
        }

        operator fun get(slot: EntityEquipmentSlot): Item? = when (slot) {
            EntityEquipmentSlot.HEAD -> helm
            EntityEquipmentSlot.CHEST -> chest
            EntityEquipmentSlot.LEGS -> legs
            EntityEquipmentSlot.FEET -> boots
            else -> null
        }
    }

    abstract protected val armorSetStacks: ArmorSet

    fun hasFullSet(player: EntityLivingBase) = getSetPiecesEquipped(player) == 4

    fun hasArmorSetItem(player: EntityLivingBase, slot: EntityEquipmentSlot): Boolean {
        if (armorSetStacks[slot] == null) return true
        val stack = player.getItemStackFromSlot(slot)
        if (stack.isEmpty) return false
        return stack.item == armorSetStacks[slot]
    }

    private fun getSetPiecesEquipped(player: EntityLivingBase)
            = EntityEquipmentSlot.values().count { it.slotType == EntityEquipmentSlot.Type.ARMOR && hasArmorSetItem(player, it) }

    val modId = currentModId

    val armorSetName: String
        get() = TooltipHelper.local("$modId.armorset.$matName.name")

    private fun getArmorSetTitle(player: EntityPlayer)
            = TooltipHelper.local("botaniamisc.armorset") + " " + armorSetName + " (" + getSetPiecesEquipped(player) + "/" + armorSetStacks.size + ")"

    open fun addArmorSetDescription(list: MutableList<String>)
            = TooltipHelper.addToTooltip(list, "$modId.armorset.$matName.desc")

    override fun hasPhantomInk(stack: ItemStack)
            = ItemNBTHelper.getBoolean(stack, TAG_PHANTOM_INK, false)

    override fun setPhantomInk(stack: ItemStack, ink: Boolean)
            = ItemNBTHelper.setBoolean(stack, TAG_PHANTOM_INK, ink)

    abstract val manaDiscount: Float

    override fun getDiscount(stack: ItemStack, slot: Int, player: EntityPlayer, tool: ItemStack?): Float {
        return if (slot == 0 && hasFullSet(player)) manaDiscount else 0f
    }

    @Optional.Method(modid = "thaumcraft")
    override fun getRunicCharge(itemstack: ItemStack) = 0

    companion object {
        val MANA_PER_DAMAGE = 70
        private val TAG_PHANTOM_INK = "phantomInk"
    }
}
