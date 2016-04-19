package shadowfox.botanicaladdons.common.achievements

import net.minecraft.init.Items
import net.minecraftforge.common.AchievementPage
import shadowfox.botanicaladdons.common.block.ModBlocks
import shadowfox.botanicaladdons.common.items.ModItems
import shadowfox.botanicaladdons.common.lib.LibMisc

/**
 * @author WireSegal
 * Created at 5:14 PM on 4/19/16.
 */
object ModAchievements {

    val donEmblem: AchievementMod
    val focus: AchievementMod
    val awakening: AchievementMod
    val sacredItem: AchievementMod
    val dendricSuffusion: AchievementMod
    val iridescence: AchievementMod

    val achievementPage: AchievementPage

    init {
        donEmblem = AchievementMod("donEmblem", 1, 0, ModItems.symbol, null)
        focus = AchievementMod("focus", 3, 1, ModItems.spellFocus, donEmblem)
        awakening = AchievementMod("awakening", -1, 1, ModBlocks.awakenerCore, donEmblem)
        sacredItem = AchievementMod("sacredItem", -3, 2, Items.potato, awakening) //todo change temp item
        dendricSuffusion = AchievementMod("dendricSuffusion", 1, 2, Items.potato, focus) //todo change temp item
        iridescence = AchievementMod("iridescence", 5, 2, Items.potato, focus) //todo change temp item

        achievementPage = AchievementPage(LibMisc.MOD_NAME, *AchievementMod.achievements.toTypedArray())
        AchievementPage.registerAchievementPage(achievementPage)
    }
}
