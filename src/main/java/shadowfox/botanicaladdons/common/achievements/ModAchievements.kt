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

    val donEmblem: ModAchievement = ModAchievement("donEmblem", -3, 0, ModItems.symbol, null)
    val focus = ModAchievement("focus", 0, 0, ModItems.spellFocus, donEmblem)
    val awakening = ModAchievement("awakening", 3, 0, ModBlocks.awakenerCore, focus)

    val createLife = ModAchievement("createLife", -2, 1, ItemResource.of(ItemResource.Variants.LIFE_ROOT), focus)
    val createThunder = ModAchievement("createThunder", 1, -2, ItemResource.of(ItemResource.Variants.THUNDER_STEEL), focus)
    val createAqua = ModAchievement("createAqua", -1, -2, ItemResource.of(ItemResource.Variants.AQUAMARINE), focus)
    val iridescence = ModAchievement("iridescence", -2, -1, ItemStack(ModItems.iridescentDye, 1, 16), focus)
    val createFire = ModAchievement("createFire", -1, 2, ItemResource.of(ItemResource.Variants.HEARTHSTONE), focus)

    val sacredLife = ModAchievement("sacredLife", 5, -1, ModItems.apple, awakening)
    val sacredThunder = ModAchievement("sacredThunder", 2, 2, ModItems.mjolnir, awakening)
    val sacredAqua = ModAchievement("sacredAqua", 4, 2, ModItems.sealArrow, awakening)
    val sacredHorn = ModAchievement("sacredHorn", 5, 1, ModItems.fateHorn, awakening)
    val sacredFlame = ModAchievement("sacredFlame", 4, -2, ModItems.perditionFist, awakening)

    val createSpirit: Achievement = ModAchievement("ragnarok", 3, 5, ItemResource.of(ItemResource.Variants.GOD_SOUL), null).setSpecial()
    val initiateRagnarok: Achievement = ModAchievement("beginRagnarok", 0, -6, ItemSpellIcon.of(ItemSpellIcon.Variants.SOUL_MANIFESTATION), null).setSpecial()

    init {
        ModAchievement.producePage()
    }
}
