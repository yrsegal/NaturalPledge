package shadowfox.botanicaladdons.common.items.armor

import com.teamwizardry.librarianlib.LibrarianLib
import com.teamwizardry.librarianlib.client.util.TooltipHelper
import com.teamwizardry.librarianlib.common.network.PacketHandler
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.DamageSource
import net.minecraft.util.NonNullList
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.NetworkRegistry
import shadowfox.botanicaladdons.common.items.ModItems
import shadowfox.botanicaladdons.common.items.ModItems.ECLIPSE
import shadowfox.botanicaladdons.common.items.base.ItemBaseArmor
import shadowfox.botanicaladdons.common.items.bauble.faith.ItemRagnarokPendant
import shadowfox.botanicaladdons.common.network.ManastormLightningMessage
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.core.helper.Vector3
import vazkii.botania.common.item.equipment.tool.ToolCommons
import java.util.*

/**
 * @author WireSegal
 * Created at 5:09 PM on 4/2/17.
 */
class ItemEclipseArmor(name: String, type: EntityEquipmentSlot) : ItemBaseArmor(name, type, ECLIPSE) {
    override val armorTexture: String
        get() = armorMaterial.getName()

    override val armorSetStacks: ArmorSet by lazy {
        ArmorSet(ModItems.eclipseHelm, ModItems.eclipseChest, ModItems.eclipseLegs, ModItems.eclipseBoots)
    }

    override val manaDiscount: Float
        get() = 0.2f

    override fun getSubItems(itemIn: Item, tab: CreativeTabs?, subItems: NonNullList<ItemStack>) {
        val ragnarokRises = try {
            ItemRagnarokPendant.hasAwakenedRagnarok(LibrarianLib.PROXY.getClientPlayer())
        } catch (e: IllegalStateException) {
            false
        }
        if (ragnarokRises)
            super.getSubItems(itemIn, tab, subItems)
    }

    fun manaMasquerade(stack: ItemStack, player: EntityPlayer, amount: Int, exact: Boolean = true, take: Boolean = true): Int {
        val playerPosition = player.positionVector
        val players = player.world.getEntitiesWithinAABB(EntityPlayer::class.java, player.entityBoundingBox.expandXyz(10.0)) {
            it != null && it != player && it.positionVector.squareDistanceTo(playerPosition) <= 100.0
        }
        Collections.shuffle(players)
        players.add(player)
        var amountLeft = amount
        val playersToAmounts = mutableListOf<Pair<EntityPlayer, Int>>()
        for (pl in players) {
            if (amountLeft == 0) continue
            val amountTakeaway = if (pl == player)
                ManaItemHandler.requestManaForTool(stack, pl, amountLeft, false)
            else
                ManaItemHandler.requestMana(stack, pl, amountLeft, false)
            if (amountTakeaway != 0) {
                playersToAmounts.add(pl to amountTakeaway)
                amountLeft -= amountTakeaway
            }
        }
        if (amountLeft != 0 && exact) return 0

        if (take) {
            val positions = mutableListOf<Vec3d>()

            for ((pl, amountToTake) in playersToAmounts) {
                ManaItemHandler.requestManaExact(stack, pl, amountToTake, true)
                if (pl != player)
                    for (i in 0..(amountToTake - 10) / 10)
                        positions.add(Vector3.fromEntityCenter(pl).toVec3D())
            }

            if (!player.world.isRemote)
                PacketHandler.NETWORK.sendToAllAround(ManastormLightningMessage(Vector3.fromEntityCenter(player).toVec3D(), positions.toTypedArray()),
                        NetworkRegistry.TargetPoint(player.world.provider.dimension,
                                playerPosition.xCoord,
                                playerPosition.yCoord,
                                playerPosition.zCoord,
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
            amount = manaMasquerade(stack, player, amount, false, false)
            amount = ManaItemHandler.dispatchMana(stack, player, amount, false)

            ManaItemHandler.dispatchMana(stack, player, manaMasquerade(stack, player, amount, false), true)
        }
    }

    override fun damageArmor(entity: EntityLivingBase, stack: ItemStack, source: DamageSource, damage: Int, slot: Int) {
        val manaToRequest = damage * MANA_PER_DAMAGE
        val manaRequested = if (entity is EntityPlayer) manaMasquerade(stack, entity, manaToRequest) != 0 else false
        if (!manaRequested) stack.damageItem(damage, entity)
    }
}
