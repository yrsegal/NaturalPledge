package shadowfox.botanicaladdons.common.items.sacred

import com.teamwizardry.librarianlib.common.base.item.ItemModFood
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.stats.Achievement
import shadowfox.botanicaladdons.common.achievements.ModAchievements
import shadowfox.botanicaladdons.common.potions.ModPotions
import shadowfox.botanicaladdons.common.potions.base.ModPotionEffect
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.common.achievement.ICraftAchievement

/**
 * @author WireSegal
 * Created at 12:59 PM on 4/27/16.
 */
class ItemImmortalApple(name: String) : ItemModFood(name, 4, 1.2f, false), ICraftAchievement {
    init {
        setPotionEffect(ModPotionEffect(ModPotions.immortal, 6000, 0, true, true), 1f)
        setAlwaysEdible()
    }

    override fun getRarity(stack: ItemStack?) = BotaniaAPI.rarityRelic

    override fun getAchievementOnCraft(p0: ItemStack?, p1: EntityPlayer?, p2: IInventory?): Achievement? {
        return ModAchievements.sacredLife
    }
}
