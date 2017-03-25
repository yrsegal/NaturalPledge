package shadowfox.botanicaladdons.common.lexicon.base

import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import shadowfox.botanicaladdons.api.item.IPriestlyEmblem
import shadowfox.botanicaladdons.api.priest.IFaithVariant
import shadowfox.botanicaladdons.common.items.bauble.faith.ItemFaithBauble
import vazkii.botania.api.lexicon.LexiconCategory

/**
 * @author WireSegal
 * Created at 1:26 PM on 4/16/16.
 */
class EntryAwakenedKnowledge(unlocName: String, category: LexiconCategory, icon: ItemStack, val pendant: Class<out IFaithVariant>? = null) : ModEntry(unlocName, category, icon) {

    constructor(unlocalizedName: String, category: LexiconCategory, block: Block, pendant: Class<out IFaithVariant>? = null) : this(unlocalizedName, category, ItemStack(block), pendant)

    constructor(unlocalizedName: String, category: LexiconCategory, item: Item, pendant: Class<out IFaithVariant>? = null) : this(unlocalizedName, category, ItemStack(item), pendant)

    override fun isVisible(): Boolean {
        val entityPlayer = Minecraft.getMinecraft().player
        val emblem = ItemFaithBauble.getEmblem(entityPlayer, pendant)
        return entityPlayer.isCreative || emblem != null && (emblem.item as IPriestlyEmblem).isAwakened(emblem)
    }
}
