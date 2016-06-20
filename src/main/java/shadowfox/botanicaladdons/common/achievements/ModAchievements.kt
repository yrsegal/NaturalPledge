package shadowfox.botanicaladdons.common.achievements

import net.minecraft.item.ItemStack
import net.minecraftforge.common.AchievementPage
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.common.block.ModBlocks
import shadowfox.botanicaladdons.common.items.ItemResource
import shadowfox.botanicaladdons.common.items.ModItems

/**
 * @author WireSegal
 * Created at 5:14 PM on 4/19/16.
 */
object ModAchievements {

    val donEmblem: AchievementMod
    val focus: AchievementMod
    val awakening: AchievementMod

    val createLife: AchievementMod
    val createThunder: AchievementMod
    val createAqua: AchievementMod
    val iridescence: AchievementMod

    val sacredLife: AchievementMod
    val sacredThunder: AchievementMod
    val sacredAqua: AchievementMod
    val sacredHorn: AchievementMod

    val achievementPage: AchievementPage

    init {
        donEmblem = AchievementMod("donEmblem", 1, 1, ModItems.symbol, null)
        focus = AchievementMod("focus", 3, 1, ModItems.spellFocus, donEmblem)
        awakening = AchievementMod("awakening", 0, 2, ModBlocks.awakenerCore, focus)

        createLife = AchievementMod("createLife", 5, 2, ItemResource.of(ItemResource.Variants.LIFE_ROOT), focus)
        createThunder = AchievementMod("createThunder", 2, -1, ItemResource.of(ItemResource.Variants.THUNDER_STEEL), focus)
        createAqua = AchievementMod("createAqua", 4, -1, ItemResource.of(ItemResource.Variants.AQUAMARINE), focus)
        iridescence = AchievementMod("iridescence", 5, 0, ItemStack(ModItems.iridescentDye, 1, 16), focus)

        sacredLife = AchievementMod("sacredLife", -2, 1, ModItems.apple, awakening)
        sacredThunder = AchievementMod("sacredThunder", -2, 3, ModItems.mjolnir, awakening)
        sacredAqua = AchievementMod("sacredAqua", -1, 4, ModItems.sealArrow, awakening)
        sacredHorn = AchievementMod("sacredHorn", 1, 4, ModItems.fateHorn, awakening)

        achievementPage = AchievementPage(LibMisc.MOD_NAME, *AchievementMod.achievements.toTypedArray())
        AchievementPage.registerAchievementPage(achievementPage)
    }
}
