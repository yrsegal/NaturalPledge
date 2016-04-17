package shadowfox.botanicaladdons.common.lexicon.base

import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import shadowfox.botanicaladdons.common.lib.LibMisc
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.lexicon.*

/**
 * @author WireSegal
 * Created at 1:44 PM on 4/16/16.
 */
open class ModEntry : LexiconEntry, IAddonEntry {
    constructor(unlocalizedName: String, category: LexiconCategory, stack: ItemStack?) : super(unlocalizedName, category) {
        if (stack != null) icon = stack
        BotaniaAPI.addEntry(this, category)
    }

    constructor(unlocalizedName: String, category: LexiconCategory, block: Block) : this(unlocalizedName, category, ItemStack(block)) {
    }

    constructor(unlocalizedName: String, category: LexiconCategory, item: Item) : this(unlocalizedName, category, ItemStack(item)) {
    }

    constructor(unlocalizedName: String, category: LexiconCategory) : this(unlocalizedName, category, null as ItemStack?) {
    }

    override fun setLexiconPages(vararg pages: LexiconPage): LexiconEntry {
        for (page in pages) {
            page.unlocalizedName = "${LibMisc.MOD_ID}.page." + getLazyUnlocalizedName() + page.unlocalizedName
            if (page is ITwoNamedPage) {
                page.secondUnlocalizedName = "${LibMisc.MOD_ID}.page." + getLazyUnlocalizedName() + page.secondUnlocalizedName
            }
        }

        return super.setLexiconPages(*pages)
    }

    override fun getUnlocalizedName(): String {
        return "${LibMisc.MOD_ID}.entry." + super.getUnlocalizedName()
    }

    override fun getTagline(): String {
        return "${LibMisc.MOD_ID}.tagline." + super.getUnlocalizedName()
    }

    override fun getSubtitle(): String? {
        return "[${LibMisc.MOD_NAME}]"
    }

    fun getLazyUnlocalizedName(): String {
        return super.getUnlocalizedName()
    }
}
