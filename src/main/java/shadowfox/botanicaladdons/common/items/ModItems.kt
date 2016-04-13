package shadowfox.botanicaladdons.common.items

import shadowfox.botanicaladdons.common.items.base.ItemMod
import shadowfox.botanicaladdons.common.items.bauble.faith.ItemFaithBauble
import shadowfox.botanicaladdons.common.lib.LibNames

/**
 * @author WireSegal
 * Created at 5:39 PM on 4/13/16.
 */
object ModItems {
    val emblem: ItemMod

    init {
        emblem = ItemFaithBauble(LibNames.PRIESTLY_EMBLEM)
    }
}
