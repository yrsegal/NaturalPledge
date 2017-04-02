package shadowfox.botanicaladdons.common.items.armor

import com.teamwizardry.librarianlib.common.network.PacketHandler
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraft.util.DamageSource
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.NetworkRegistry
import shadowfox.botanicaladdons.common.items.base.ItemBaseArmor
import shadowfox.botanicaladdons.common.network.ManastormLightningMessage
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.item.equipment.tool.ToolCommons
import java.util.*

/**
 * @author WireSegal
 * Created at 5:09 PM on 4/2/17.
 */
class ItemEclipseArmor(name: String, type: EntityEquipmentSlot, mat: ArmorMaterial) : ItemBaseArmor(name, type, mat) {
    override val armorTexture: String
        get() = "missingno"
    override val armorSetStacks: ArmorSet by lazy {
        ArmorSet(null, null, null, null)
    }

    override val manaDiscount: Float
        get() = 0.3f

    fun manaMasquerade(stack: ItemStack, player: EntityPlayer, amount: Int): Boolean {
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
            val amountTakable = if (pl == player)
                ManaItemHandler.requestManaForTool(stack, pl, amountLeft, false)
            else
                ManaItemHandler.requestMana(stack, pl, amountLeft, false)
            if (amountTakable != 0) {
                playersToAmounts.add(pl to amountTakable)
                amountLeft -= amountTakable
            }
        }
        if (amountLeft != 0) return false

        val positions = mutableListOf<Vec3d>()

        for ((pl, amountToTake) in playersToAmounts) {
            ManaItemHandler.requestManaExact(stack, pl, amountToTake, true)
            if (pl != player)
                for (i in 0..(amountToTake - 10) / 10)
                    positions.add(pl.positionVector)
        }

        if (!player.world.isRemote)
            PacketHandler.NETWORK.sendToAllAround(ManastormLightningMessage(playerPosition, positions.toTypedArray()),
                    NetworkRegistry.TargetPoint(player.world.provider.dimension,
                            playerPosition.xCoord,
                            playerPosition.yCoord,
                            playerPosition.zCoord,
                            64.0))

        return true
    }

    override fun onArmorTick(world: World, player: EntityPlayer, stack: ItemStack) {
        if (!world.isRemote && stack.itemDamage > 0 && manaMasquerade(stack, player, MANA_PER_DAMAGE * 2))
            stack.itemDamage = stack.itemDamage - 1
    }

    override fun damageArmor(entity: EntityLivingBase, stack: ItemStack, source: DamageSource, damage: Int, slot: Int) {
        val manaToRequest = damage * MANA_PER_DAMAGE
        val manaRequested = if (entity is EntityPlayer) manaMasquerade(stack, entity, manaToRequest) else false
        if (!manaRequested) stack.damageItem(damage, entity)
    }
}
