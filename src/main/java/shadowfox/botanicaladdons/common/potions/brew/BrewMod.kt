package shadowfox.botanicaladdons.common.potions.brew

import net.minecraft.potion.PotionEffect
import net.minecraft.potion.PotionUtils
import shadowfox.botanicaladdons.api.lib.LibMisc
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.brew.Brew

/**
 * @author WireSegal
 * Created at 9:16 PM on 5/7/16.
 */
class BrewMod(name: String, color: Int, cost: Int, vararg effects: PotionEffect) : Brew(name, name, color, cost, *effects) {

    constructor(name: String, cost: Int, vararg effects: PotionEffect) :
    this(name, PotionUtils.getPotionColorFromEffectList(listOf(*effects)), cost, *effects)

    init {
        BotaniaAPI.registerBrew(this)
    }

    override fun getUnlocalizedName(): String {
        return "${LibMisc.MOD_ID}.brew.${super.getUnlocalizedName()}"
    }
}
