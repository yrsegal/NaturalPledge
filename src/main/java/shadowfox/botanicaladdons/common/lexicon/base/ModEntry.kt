package shadowfox.botanicaladdons.common.lexicon.base

import com.teamwizardry.librarianlib.common.util.VariantHelper
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import shadowfox.botanicaladdons.api.lib.LibMisc
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.lexicon.*

/**
 * @author WireSegal
 * Created at 1:44 PM on 4/16/16.
 */
@Suppress("LeakingThis")
open class ModEntry : LexiconEntry, IAddonEntry {
    constructor(unlocalizedName: String, category: LexiconCategory, stack: ItemStack) : super(VariantHelper.toSnakeCase(unlocalizedName), category) {
        if (!stack.isEmpty) icon = stack
        BotaniaAPI.addEntry(this, category)
    }

    constructor(unlocalizedName: String, category: LexiconCategory, block: Block) : this(unlocalizedName, category, ItemStack(block))

    constructor(unlocalizedName: String, category: LexiconCategory, item: Item) : this(unlocalizedName, category, ItemStack(item))

    constructor(unlocalizedName: String, category: LexiconCategory) : this(unlocalizedName, category, ItemStack.EMPTY)

    override fun setLexiconPages(vararg pages: LexiconPage): LexiconEntry {
        for (page in pages) {
            page.unlocalizedName = "${LibMisc.MOD_ID}.page." + getLazyUnlocalizedName() + VariantHelper.toSnakeCase(page.unlocalizedName)
            if (page is ITwoNamedPage) {
                page.secondUnlocalizedName = "${LibMisc.MOD_ID}.page." + getLazyUnlocalizedName() + VariantHelper.toSnakeCase(page.secondUnlocalizedName)
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
