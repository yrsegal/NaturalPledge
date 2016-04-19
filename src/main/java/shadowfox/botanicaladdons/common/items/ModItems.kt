package shadowfox.botanicaladdons.common.items

import net.minecraftforge.oredict.OreDictionary
import shadowfox.botanicaladdons.common.BotanicalAddons
import shadowfox.botanicaladdons.common.items.base.ItemMod
import shadowfox.botanicaladdons.common.items.bauble.ItemSymbol
import shadowfox.botanicaladdons.common.items.bauble.faith.ItemFaithBauble
import shadowfox.botanicaladdons.common.lib.LibNames

/**
 * @author WireSegal
 * Created at 5:39 PM on 4/13/16.
 */
object ModItems {
    val emblem: ItemMod
    val symbol: ItemMod
    val spellIcon: ItemMod
    val spellFocus: ItemMod

    lateinit var gaiaKiller: ItemMod

    init {
        emblem = ItemFaithBauble(LibNames.PRIESTLY_EMBLEM)
        symbol = ItemSymbol(LibNames.HOLY_SYMBOL)
        spellIcon = ItemSpellIcon(LibNames.SPELL_ICON)
        spellFocus = ItemTerrestrialFocus(LibNames.SPELL_FOCUS)

        if (BotanicalAddons.isDevEnv)
            gaiaKiller = ItemGaiaSlayer("gaiaKiller")

        OreDictionary.registerOre("holySymbol", symbol)
    }
}
