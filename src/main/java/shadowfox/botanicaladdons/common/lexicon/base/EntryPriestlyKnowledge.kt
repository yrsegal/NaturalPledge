package shadowfox.botanicaladdons.common.lexicon.base

import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.stats.Achievement
import shadowfox.botanicaladdons.api.priest.IFaithVariant
import shadowfox.botanicaladdons.common.items.bauble.faith.ItemFaithBauble
import vazkii.botania.api.lexicon.LexiconCategory

/**
 * @author WireSegal
 * Created at 1:26 PM on 4/16/16.
 */
class EntryPriestlyKnowledge(unlocName: String, category: LexiconCategory, icon: ItemStack, val pendant: Class<out IFaithVariant>? = null) : ModEntry(unlocName, category, icon) {

    companion object {
        val ACHIEVEMENT_MAP = mutableMapOf<Class<out IFaithVariant>, (EntityPlayer) -> Boolean>()
    }

    constructor(unlocalizedName: String, category: LexiconCategory, block: Block, pendant: Class<out IFaithVariant>? = null) : this(unlocalizedName, category, ItemStack(block), pendant)

    constructor(unlocalizedName: String, category: LexiconCategory, item: Item, pendant: Class<out IFaithVariant>? = null) : this(unlocalizedName, category, ItemStack(item), pendant)

    override fun isVisible(): Boolean {
        val entityPlayer = Minecraft.getMinecraft().player
        val ach = if (pendant == null)
            ACHIEVEMENT_MAP.any { it.value(entityPlayer) }
        else
            ACHIEVEMENT_MAP[pendant]?.let { it(entityPlayer) } ?: false
        return ach || entityPlayer.isCreative || ItemFaithBauble.getEmblem(entityPlayer, pendant) != null
    }
}
