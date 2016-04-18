package shadowfox.botanicaladdons.common.lexicon.base

import net.minecraft.client.Minecraft
import net.minecraft.item.ItemStack
import shadowfox.botanicaladdons.common.items.bauble.faith.ItemFaithBauble
import vazkii.botania.api.lexicon.LexiconCategory

/**
 * @author WireSegal
 * Created at 1:26 PM on 4/16/16.
 */
class EntryAwakenedKnowledge(unlocName: String, category: LexiconCategory, icon: ItemStack, val pendant: Class<out ItemFaithBauble.IFaithVariant>? = null) : ModEntry(unlocName, category, icon) {

    override fun isVisible(): Boolean {
        val entityPlayer = Minecraft.getMinecraft().thePlayer
        val emblem = ItemFaithBauble.getEmblem(entityPlayer, pendant)
        return emblem != null && ItemFaithBauble.isAwakened(emblem)
    }
}
