package com.wiresegal.naturalpledge.common.potions.brew

import net.minecraft.potion.PotionEffect
import com.wiresegal.naturalpledge.common.lib.LibNames
import com.wiresegal.naturalpledge.common.potions.ModPotions
import vazkii.botania.api.brew.Brew

/**
 * @author WireSegal
 * Created at 9:15 PM on 5/7/16.
 */
object ModBrews {
    val immortality: Brew
    val drained: Brew

    init {
        immortality = BrewMod(LibNames.IMMORTALITY, 16000, PotionEffect(ModPotions.immortal, 1800)).setNotBloodPendantInfusable()
        drained = BrewMod(LibNames.DRAB_VISION, 1000, PotionEffect(ModPotions.drab, 4800, 1))
    }
}
