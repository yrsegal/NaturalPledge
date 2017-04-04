package shadowfox.botanicaladdons.common.achievements

import com.teamwizardry.librarianlib.common.base.ModAchievement
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.stats.Achievement
import net.minecraftforge.common.AchievementPage
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.common.block.ModBlocks
import shadowfox.botanicaladdons.common.items.ItemResource
import shadowfox.botanicaladdons.common.items.ItemSpellIcon
import shadowfox.botanicaladdons.common.items.ModItems

/**
 * @author WireSegal
 * Created at 5:14 PM on 4/19/16.
 */
object ModAchievements {

    val donEmblem: ModAchievement = ModAchievement("donEmblem", 1, 1, ModItems.symbol, null)
    val focus = ModAchievement("focus", 3, 1, ModItems.spellFocus, donEmblem)
    val awakening = ModAchievement("awakening", 0, 2, ModBlocks.awakenerCore, focus)

    val createLife = ModAchievement("createLife", 5, 2, ItemResource.of(ItemResource.Variants.LIFE_ROOT), focus)
    val createThunder = ModAchievement("createThunder", 2, -1, ItemResource.of(ItemResource.Variants.THUNDER_STEEL), focus)
    val createAqua = ModAchievement("createAqua", 4, -1, ItemResource.of(ItemResource.Variants.AQUAMARINE), focus)
    val iridescence = ModAchievement("iridescence", 5, 0, ItemStack(ModItems.iridescentDye, 1, 16), focus)
    val createFire = ModAchievement("createFire", 4, 3, ItemResource.of(ItemResource.Variants.HEARTHSTONE), focus)

    val sacredLife = ModAchievement("sacredLife", -2, 1, ModItems.apple, awakening)
    val sacredThunder = ModAchievement("sacredThunder", -2, 3, ModItems.mjolnir, awakening)
    val sacredAqua = ModAchievement("sacredAqua", -1, 4, ModItems.sealArrow, awakening)
    val sacredHorn = ModAchievement("sacredHorn", 1, 4, ModItems.fateHorn, awakening)
    val sacredFlame = ModAchievement("sacredFlame", -1, 0, ModItems.perditionFist, awakening)

    val initiateRagnarok: Achievement = ModAchievement("ragnarok", 0, -4, ItemSpellIcon.of(ItemSpellIcon.Variants.SOUL_MANIFESTATION), null).setSpecial()

    init {
        ModAchievement.producePage()
    }
}
