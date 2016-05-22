package shadowfox.botanicaladdons.common.items

import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import shadowfox.botanicaladdons.api.item.IDiscordantItem
import shadowfox.botanicaladdons.client.core.ModelHandler
import shadowfox.botanicaladdons.common.BotanicalAddons
import shadowfox.botanicaladdons.common.items.base.ItemMod
import shadowfox.botanicaladdons.common.items.bauble.faith.ItemFaithBauble
import shadowfox.botanicaladdons.common.potions.ModPotions
import shadowfox.botanicaladdons.common.potions.base.ModPotionEffect
import vazkii.botania.api.mana.IManaItem
import vazkii.botania.api.mana.IManaTooltipDisplay
import vazkii.botania.api.mana.IManaUsingItem
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.core.helper.ItemNBTHelper
import vazkii.botania.common.core.helper.Vector3
import java.awt.Color

/**
 * @author WireSegal
 * Created at 12:15 PM on 4/25/16.
 */
class ItemMortalstone(name: String) : ItemMod(name), IManaUsingItem, IDiscordantItem, IManaItem, IManaTooltipDisplay, ModelHandler.IColorProvider {

    val RANGE = 5.0

    val MANA_PER_TICK = 1

    val TAG_MANA = "mana"
    val MAX_MANA = 600 * MANA_PER_TICK

    val PARTICLE_COLOR = 0xff0000

    init {
        setMaxStackSize(1)
    }

    @SideOnly(Side.CLIENT)
    override fun getColor(): IItemColor? {
        return IItemColor { itemStack, i ->
            if (i == 1)
                (if (getMana(itemStack) == 0)
                    0x600000
                else
                    BotanicalAddons.proxy.pulseColor(Color(0xB71010)).rgb)
            else 0xFFFFFF }
    }

    override fun onUpdate(stack: ItemStack, worldIn: World, entityIn: Entity, itemSlot: Int, isSelected: Boolean) {
        var flag = false

        if (entityIn is EntityPlayer)
            addMana(stack, ManaItemHandler.requestMana(stack, entityIn, MANA_PER_TICK * 3, true))

        if (isSelected && (entityIn !is EntityPlayer || ManaItemHandler.requestManaExact(stack, entityIn, MANA_PER_TICK, false))) {
            val entities = worldIn.getEntitiesWithinAABB(EntityPlayer::class.java, entityIn.entityBoundingBox.expandXyz(RANGE))
            for (entity in entities)
                if (entity is EntityPlayer && entity.positionVector.subtract(entityIn.positionVector).lengthVector() <= RANGE && ItemFaithBauble.getEmblem(entity) != null) {
                    entity.addPotionEffect(ModPotionEffect(ModPotions.faithlessness, 5, 0, true, true))
                    if (!entity.equals(entityIn) && !ModPotions.faithlessness.hasEffect(entity)) flag = true
                    BotanicalAddons.proxy.particleEmission(entity.worldObj, Vector3.fromEntityCenter(entity).add(-0.5, 0.0, -0.5), PARTICLE_COLOR, 0.7F)
                }
        }
        if (entityIn is EntityPlayer && isSelected) {
            if (flag)
                addMana(stack, -MANA_PER_TICK)
            entityIn.addPotionEffect(ModPotionEffect(ModPotions.faithlessness, 5, 0, true, true))
        }
    }

    override fun addMana(stack: ItemStack?, mana: Int) = ItemNBTHelper.setInt(stack, TAG_MANA, Math.max(0, Math.min(mana + getMana(stack), getMaxMana(stack))))
    override fun canExportManaToItem(stack: ItemStack?, p1: ItemStack?): Boolean = false
    override fun canExportManaToPool(stack: ItemStack?, p1: TileEntity?): Boolean = false
    override fun canReceiveManaFromItem(stack: ItemStack?, p1: ItemStack?) = true
    override fun canReceiveManaFromPool(stack: ItemStack?, p1: TileEntity?) = false
    override fun getMana(stack: ItemStack?) = ItemNBTHelper.getInt(stack, TAG_MANA, getMaxMana(stack))
    override fun getMaxMana(stack: ItemStack?) = MAX_MANA
    override fun isNoExport(stack: ItemStack?) = true

    override fun getEntityLifespan(itemStack: ItemStack?, world: World?) = Int.MAX_VALUE
    override fun onEntityItemUpdate(entityItem: EntityItem): Boolean {
        var flag = 0
        if (getMana(entityItem.entityItem) > 0) {

            val entities = entityItem.worldObj.getEntitiesWithinAABB(EntityPlayer::class.java, entityItem.entityBoundingBox.expandXyz(RANGE))
            for (entity in entities)
                if (entity is EntityPlayer && entity.positionVector.subtract(entityItem.positionVector).lengthVector() <= RANGE && ItemFaithBauble.getEmblem(entity) != null) {
                    if ((ModPotions.faithlessness.getEffect(entity) ?: PotionEffect(ModPotions.faithlessness)).duration <= 5) {
                        flag = flag or 1
                    }
                    entity.addPotionEffect(ModPotionEffect(ModPotions.faithlessness, 5, 0, true, true))
                    BotanicalAddons.proxy.particleEmission(entity.worldObj, Vector3.fromEntityCenter(entity).add(-0.5, 0.0, -0.5), 0x5e0a02, 0.7F)
                    flag = flag or 2
                }

            BotanicalAddons.proxy.particleEmission(entityItem.worldObj, Vector3.fromEntity(entityItem).add(-0.5, 0.0, -0.5), 0x5e0a02, if (flag and 2 == 0) 0.1F else 0.9F)
        }
        if (flag and 1 != 0)
            addMana(entityItem.entityItem, -MANA_PER_TICK)

        return false
    }

    override fun showDurabilityBar(stack: ItemStack?) = getMana(stack) < getMaxMana(stack) - MANA_PER_TICK
    override fun getDurabilityForDisplay(stack: ItemStack?) = 1 - getManaFractionForDisplay(stack).toDouble()
    override fun getManaFractionForDisplay(stack: ItemStack?) = getMana(stack) / getMaxMana(stack).toFloat()

    override fun usesMana(p0: ItemStack) = true
    override fun isDiscordant(stack: ItemStack) = true
}
