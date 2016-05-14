package shadowfox.botanicaladdons.common.lexicon.base

import net.minecraft.client.Minecraft
import net.minecraft.item.ItemStack
import shadowfox.botanicaladdons.api.priest.IFaithVariant
import shadowfox.botanicaladdons.api.item.IPriestlyEmblem
import shadowfox.botanicaladdons.common.items.bauble.faith.ItemFaithBauble
import vazkii.botania.api.lexicon.LexiconCategory

/**
 * @author WireSegal
 * Created at 1:26 PM on 4/16/16.
 */
class EntryAwakenedKnowledge(unlocName: String, category: LexiconCategory, icon: ItemStack, val pendant: Class<out IFaithVariant>? = null) : ModEntry(unlocName, category, icon) {

    override fun isVisible(): Boolean {
        val entityPlayer = Minecraft.getMinecraft().thePlayer
        val emblem = ItemFaithBauble.getEmblem(entityPlayer, pendant)
        return entityPlayer.isCreative || emblem != null && (emblem.item as IPriestlyEmblem).isAwakened(emblem)
    }
}
