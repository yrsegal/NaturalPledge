package shadowfox.botanicaladdons.common.achievements

import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.stats.Achievement
import shadowfox.botanicaladdons.common.lib.LibMisc
import java.util.*

/**
 * @author WireSegal
 * Created at 5:06 PM on 4/19/16.
 */
class AchievementMod(name: String, x: Int, y: Int, icon: ItemStack, parent: Achievement?) : Achievement("achievement.${LibMisc.MOD_ID}:" + name, "${LibMisc.MOD_ID}:" + name, x, y, icon, parent) {

    init {
        achievements.add(this)
        registerStat()
    }

    constructor(name: String, x: Int, y: Int, icon: Item, parent: Achievement?) : this(name, x, y, ItemStack(icon), parent) {
    }

    constructor(name: String, x: Int, y: Int, icon: Block, parent: Achievement?) : this(name, x, y, ItemStack(icon), parent) {
    }

    companion object {
        val achievements = ArrayList<AchievementMod>()
    }

}
