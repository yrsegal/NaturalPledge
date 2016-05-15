package shadowfox.botanicaladdons.common.achievements

import net.minecraft.item.ItemStack
import net.minecraftforge.common.AchievementPage
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.common.block.ModBlocks
import shadowfox.botanicaladdons.common.items.ModItems

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
        sacredItem = AchievementMod("sacredItem", -3, 2, ModItems.mjolnir, awakening)
        dendricSuffusion = AchievementMod("dendricSuffusion", 1, 2, ModBlocks.irisSapling, focus)
        iridescence = AchievementMod("iridescence", 5, 2, ItemStack(ModItems.iridescentDye, 1, 16), focus)

        achievementPage = AchievementPage(LibMisc.MOD_NAME, *AchievementMod.achievements.toTypedArray())
        AchievementPage.registerAchievementPage(achievementPage)
    }
}
