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

    val donEmblem: ModAchievement = ModAchievement("donEmblem", 1, 0, ModItems.symbol, null)
    val focus = ModAchievement("focus", 2, -1, ModItems.spellFocus, donEmblem)
    val awakening = ModAchievement("awakening", 0, 1, ModBlocks.awakenerCore, focus)

    val createLife = ModAchievement("createLife", 1, -3, ItemResource.of(ItemResource.Variants.LIFE_ROOT), focus)
    val createThunder = ModAchievement("createThunder", 3, -3, ItemResource.of(ItemResource.Variants.THUNDER_STEEL), focus)
    val createAqua = ModAchievement("createAqua", 4, -2, ItemResource.of(ItemResource.Variants.AQUAMARINE), focus)
    val iridescence = ModAchievement("iridescence", 4, 0, ItemStack(ModItems.iridescentDye, 1, 16), focus)
    val createFire = ModAchievement("createFire", 3, 1, ItemResource.of(ItemResource.Variants.HEARTHSTONE), focus)

    val sacredLife = ModAchievement("sacredLife", 1, 3, ModItems.apple, awakening)
    val sacredThunder = ModAchievement("sacredThunder", -1, 3, ModItems.mjolnir, awakening)
    val sacredAqua = ModAchievement("sacredAqua", -2, 2, ModItems.sealArrow, awakening)
    val sacredHorn = ModAchievement("sacredHorn", -2, 0, ModItems.fateHorn, awakening)
    val sacredFlame = ModAchievement("sacredFlame", -1, -1, ModItems.perditionFist, awakening)

    val createSpirit: Achievement = ModAchievement("ragnarok", -1, -3, ItemResource.of(ItemResource.Variants.GOD_SOUL), null).setSpecial()
    val initiateRagnarok: Achievement = ModAchievement("beginRagnarok", 3, 3, ItemSpellIcon.of(ItemSpellIcon.Variants.WIN), null).setSpecial()

    init {
        ModAchievement.producePage()
    }
}
