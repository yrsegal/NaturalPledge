package shadowfox.botanicaladdons.common.items.sacred

import net.minecraft.item.ItemStack
import shadowfox.botanicaladdons.common.items.base.ItemModFood
import shadowfox.botanicaladdons.common.potions.ModPotions
import shadowfox.botanicaladdons.common.potions.base.ModPotionEffect
import vazkii.botania.api.BotaniaAPI

/**
 * @author WireSegal
 * Created at 12:59 PM on 4/27/16.
 */
class ItemImmortalApple(name: String) : ItemModFood(name, 4, 1.2f, false) {
    init {
        setPotionEffect(ModPotionEffect(ModPotions.immortal, 6000, 0, true, true), 1f)
        setAlwaysEdible()
    }

    override fun getRarity(stack: ItemStack?) = BotaniaAPI.rarityRelic
}
