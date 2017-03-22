package shadowfox.botanicaladdons.common.network

import baubles.api.BaubleType
import baubles.api.BaublesApi
import com.teamwizardry.librarianlib.LibrarianLib
import com.teamwizardry.librarianlib.common.network.PacketBase
import com.teamwizardry.librarianlib.common.util.saving.Save
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import shadowfox.botanicaladdons.common.BotanicalAddons
import shadowfox.botanicaladdons.common.items.travel.bauble.ItemToolbelt

/**
 * @author WireSegal
 * Created at 11:22 PM on 1/1/17.
 */
class SetToolbeltItemClient(@Save var stack: ItemStack? = null, @Save var slot: Int = 0) : PacketBase() {
    override fun handle(ctx: MessageContext) {
        val toolbelt = BaublesApi.getBaublesHandler(LibrarianLib.PROXY.getClientPlayer()).getStackInSlot(BaubleType.BELT.validSlots[0]) ?: return
        ItemToolbelt.setItem(toolbelt, stack, slot)
    }
}
