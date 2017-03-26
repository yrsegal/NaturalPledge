package shadowfox.botanicaladdons.common.achievements

import com.teamwizardry.librarianlib.common.base.ModAchievement
import net.minecraft.init.Items
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

    val donEmblem: ModAchievement
    val focus: ModAchievement
    val awakening: ModAchievement

    val createLife: ModAchievement
    val createThunder: ModAchievement
    val createAqua: ModAchievement
    val iridescence: ModAchievement
    val createFire: ModAchievement

    val sacredLife: ModAchievement
    val sacredThunder: ModAchievement
    val sacredAqua: ModAchievement
    val sacredHorn: ModAchievement
    val sacredFlame: ModAchievement

    val achievementPage: AchievementPage

    init {
        donEmblem = ModAchievement("donEmblem", 1, 1, ModItems.symbol, null)
        focus = ModAchievement("focus", 3, 1, ModItems.spellFocus, donEmblem)
        awakening = ModAchievement("awakening", 0, 2, ModBlocks.awakenerCore, focus)

        createLife = ModAchievement("createLife", 5, 2, ItemResource.of(ItemResource.Variants.LIFE_ROOT), focus)
        createThunder = ModAchievement("createThunder", 2, -1, ItemResource.of(ItemResource.Variants.THUNDER_STEEL), focus)
        createAqua = ModAchievement("createAqua", 4, -1, ItemResource.of(ItemResource.Variants.AQUAMARINE), focus)
        iridescence = ModAchievement("iridescence", 5, 0, ItemStack(ModItems.iridescentDye, 1, 16), focus)
        createFire = ModAchievement("createFire", 4, 3, ItemResource.of(ItemResource.Variants.HEARTHSTONE), focus)

        sacredLife = ModAchievement("sacredLife", -2, 1, ModItems.apple, awakening)
        sacredThunder = ModAchievement("sacredThunder", -2, 3, ModItems.mjolnir, awakening)
        sacredAqua = ModAchievement("sacredAqua", -1, 4, ModItems.sealArrow, awakening)
        sacredHorn = ModAchievement("sacredHorn", 1, 4, ModItems.fateHorn, awakening)
        sacredFlame = ModAchievement("sacredFlame", -1, 0, ModItems.perditionFist, awakening)

        achievementPage = ModAchievement.producePage()
    }
}
