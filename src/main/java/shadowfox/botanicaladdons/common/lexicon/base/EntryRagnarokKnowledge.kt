package shadowfox.botanicaladdons.common.lexicon.base

import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.stats.Achievement
import shadowfox.botanicaladdons.api.item.IPriestlyEmblem
import shadowfox.botanicaladdons.api.priest.IFaithVariant
import shadowfox.botanicaladdons.common.items.bauble.faith.ItemFaithBauble
import shadowfox.botanicaladdons.common.items.bauble.faith.ItemRagnarokPendant
import vazkii.botania.api.lexicon.LexiconCategory

/**
 * @author WireSegal
 * Created at 1:26 PM on 4/16/16.
 */
class EntryRagnarokKnowledge(unlocName: String, category: LexiconCategory, icon: ItemStack) : ModEntry(unlocName, category, icon) {

    constructor(unlocalizedName: String, category: LexiconCategory, block: Block) : this(unlocalizedName, category, ItemStack(block))

    constructor(unlocalizedName: String, category: LexiconCategory, item: Item) : this(unlocalizedName, category, ItemStack(item))

    override fun isVisible(): Boolean {
        val entityPlayer = Minecraft.getMinecraft().player
        return ItemRagnarokPendant.hasAwakenedRagnarok(entityPlayer)
    }
}
