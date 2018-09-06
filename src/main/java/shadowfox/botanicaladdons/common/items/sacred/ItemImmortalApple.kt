package shadowfox.botanicaladdons.common.items.sacred

import com.teamwizardry.librarianlib.features.base.item.ItemModFood
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect
import shadowfox.botanicaladdons.common.potions.ModPotions
import vazkii.botania.api.BotaniaAPI

/**
 * @author WireSegal
 * Created at 12:59 PM on 4/27/16.
 */
class ItemImmortalApple(name: String) : ItemModFood(name, 4, 1.2f, false){
    init {
        setPotionEffect(PotionEffect(ModPotions.immortal, 6000, 0, true, true), 1f)
        setAlwaysEdible()
    }

    override fun getRarity(stack: ItemStack) = BotaniaAPI.rarityRelic!!

}
