package shadowfox.botanicaladdons.common.network

import baubles.api.BaubleType
import baubles.api.BaublesApi
import com.teamwizardry.librarianlib.features.autoregister.PacketRegister
import com.teamwizardry.librarianlib.features.network.PacketBase
import com.teamwizardry.librarianlib.features.saving.Save
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side
import shadowfox.botanicaladdons.common.items.travel.bauble.ItemToolbelt

/**
 * @author WireSegal
 * Created at 11:22 PM on 1/1/17.
 */
@PacketRegister(Side.SERVER)
class SetToolbeltItemServer(@Save var slot: Int = 0) : PacketBase() {
    override fun handle(ctx: MessageContext) {
        val toolbelt = BaublesApi.getBaublesHandler(ctx.serverHandler.player).getStackInSlot(BaubleType.BELT.validSlots[0])
        if (toolbelt.isEmpty) return
        val toolStack = ItemToolbelt.getItemForSlot(toolbelt, slot)
        val copy = toolStack.copy()

        val player = ctx.serverHandler.player

        if (!toolStack.isEmpty) {
            ItemToolbelt.setItem(toolbelt, ItemStack.EMPTY, slot)

            if (player.heldItemMainhand.isEmpty)
                player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, copy)
            else if (!player.inventory.addItemStackToInventory(copy))
                player.dropItem(toolStack.copy(), false)
        }
    }
}
