package shadowfox.botanicaladdons.common.items.armor

import com.teamwizardry.librarianlib.features.base.item.IGlowingItem
import com.teamwizardry.librarianlib.features.network.PacketHandler
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper
import net.minecraft.client.renderer.block.model.IBakedModel
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityArmorStand
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraft.util.DamageSource
import net.minecraft.util.NonNullList
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.client.render.entity.ModelArmorEclipse
import shadowfox.botanicaladdons.common.BotanicalAddons
import shadowfox.botanicaladdons.common.items.ModItems
import shadowfox.botanicaladdons.common.items.ModItems.ECLIPSE
import shadowfox.botanicaladdons.common.items.base.ItemBaseArmor
import shadowfox.botanicaladdons.common.items.bauble.faith.ItemRagnarokPendant
import shadowfox.botanicaladdons.common.items.weapons.ItemFlarebringer
import shadowfox.botanicaladdons.common.network.ManastormLightningMessage
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.core.helper.Vector3
import java.util.*

/**
 * @author WireSegal
 * Created at 5:09 PM on 4/2/17.
 */
class ItemEclipseArmor(name: String, type: EntityEquipmentSlot) : ItemBaseArmor(name, type, ECLIPSE), IGlowingItem {

    override fun getArmorTexture(type: String?) = "${LibMisc.MOD_ID}:textures/armor/eclipse_layer_${if (type == "glow") 1 else 0}.png"

    override fun makeArmorModel(slot: EntityEquipmentSlot) = ModelArmorEclipse(slot)


    @SideOnly(Side.CLIENT)
    override fun transformToGlow(itemStack: ItemStack, model: IBakedModel) = IGlowingItem.Helper.wrapperBake(model, false, 1)
    @SideOnly(Side.CLIENT)
    override fun shouldDisableLightingForGlow(itemStack: ItemStack, model: IBakedModel) = true

    override val armorSetStacks: ArmorSet by lazy {
        ArmorSet(ModItems.eclipseHelm, ModItems.eclipseChest, ModItems.eclipseLegs, ModItems.eclipseBoots)
    }

    override val manaDiscount: Float
        get() = 0.2f

    override fun getSubItems(tab: CreativeTabs, subItems: NonNullList<ItemStack>) {
        if (ItemRagnarokPendant.hasAwakenedRagnarok())
            super.getSubItems(tab, subItems)
    }

    fun manaMasquerade(stack: ItemStack, player: EntityPlayer, amount: Int, exact: Boolean = true, take: Boolean = true): Int {
        val playerPosition = player.positionVector
        val players = if (hasFullSet(player)) player.world.getEntitiesWithinAABB(EntityLivingBase::class.java, player.entityBoundingBox.grow(10.0)) {
            it != null && it !is EntityArmorStand && it != player && it.positionVector.squareDistanceTo(playerPosition) <= 100.0
        } else mutableListOf()
        Collections.shuffle(players)
        players.add(player)
        var amountLeft = amount
        val playersToAmounts = mutableListOf<Pair<EntityPlayer, Int>>()
        val entitiesToDrain = mutableListOf<EntityLivingBase>()
        for (pl in players) {
            if (amountLeft == 0) break
            if (pl is EntityPlayer) {
                if (pl.hurtResistantTime == 0) {
                    val amountTakeaway = if (pl == player)
                        ManaItemHandler.requestManaForTool(stack, pl, amountLeft, false)
                    else
                        ManaItemHandler.requestMana(stack, pl, amountLeft, false)


                    if (amountTakeaway != 0) {
                        playersToAmounts.add(pl to amountTakeaway)
                        amountLeft -= amountTakeaway
                    }
                }
            } else if (!pl.isDead && !pl.isEntityInvulnerable(DamageSource.MAGIC) && pl.hurtResistantTime == 0 && pl.health > 1f) {
                entitiesToDrain.add(pl)
                amountLeft -= Math.min(500, amountLeft)
            }
        }
        if (amountLeft != 0 && exact) return 0

        if (take) {
            val positions = mutableListOf<Vec3d>()

            for ((pl, amountToTake) in playersToAmounts) {
                ManaItemHandler.requestManaExact(stack, pl, amountToTake, true)
                if (pl != player) {
                    pl.hurtResistantTime = pl.maxHurtResistantTime
                    for (i in 0 until amountToTake / 10)
                        positions.add(Vector3.fromEntityCenter(pl).toVec3D())
                }
            }
            for (entity in entitiesToDrain) {
                entity.attackEntityFrom(DamageSource.MAGIC, 1f)
                if (entity != player)
                    positions.add(Vector3.fromEntityCenter(entity).toVec3D())
            }

            if (!player.world.isRemote && positions.isNotEmpty())
                PacketHandler.NETWORK.sendToAllAround(ManastormLightningMessage(Vector3.fromEntityCenter(player).toVec3D(), positions.toTypedArray()),
                        NetworkRegistry.TargetPoint(player.world.provider.dimension,
                                playerPosition.x,
                                playerPosition.y,
                                playerPosition.z,
                                64.0))
        }

        return amount - amountLeft
    }

    override fun addArmorSetDescription(list: MutableList<String>) {
        TooltipHelper.addToTooltip(list, "$modId.armorset.$matName.desc")
        TooltipHelper.addToTooltip(list, "$modId.armorset.$matName.desc1")
    }

    override fun onArmorTick(world: World, player: EntityPlayer, stack: ItemStack) {
        if (!world.isRemote) {
            if (stack.itemDamage > 0 && manaMasquerade(stack, player, MANA_PER_DAMAGE * 2) != 0)
                stack.itemDamage = stack.itemDamage - 1
            var amount = 1000
            amount = ManaItemHandler.dispatchMana(stack, player, amount, false)
            amount = manaMasquerade(stack, player, amount, false, false)

            ManaItemHandler.dispatchMana(stack, player, manaMasquerade(stack, player, amount, false), true)
        } else if (hasFullSet(player) && world.totalWorldTime % 3 == 0L) {
            val playerPos = player.positionVector
            player.world.getEntitiesWithinAABB(EntityLivingBase::class.java, player.entityBoundingBox.grow(ItemFlarebringer.RANGE)) {
                it != null && it !is EntityArmorStand && it != player && !it.isDead && it.health <= 5f && it.positionVector.squareDistanceTo(playerPos) <= ItemFlarebringer.RANGE * ItemFlarebringer.RANGE
            }.forEach {
                val pos = Vector3.fromEntityCenter(it).add(-0.5, 0.2, -0.5)
                BotanicalAddons.PROXY.particleRing(pos.x, pos.y, pos.z, 0.75, 1F, 0F, 0F, 0.025F, 0.05F, 0.1F)
            }
        }
    }

    override fun damageArmor(entity: EntityLivingBase, stack: ItemStack, source: DamageSource, damage: Int, slot: Int) {
        val manaToRequest = damage * MANA_PER_DAMAGE
        val manaRequested = if (entity is EntityPlayer) manaMasquerade(stack, entity, manaToRequest) != 0 else false
        if (!manaRequested) stack.damageItem(damage, entity)
    }
}
