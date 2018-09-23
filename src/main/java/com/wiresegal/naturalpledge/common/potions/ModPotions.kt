package com.wiresegal.naturalpledge.common.potions

import com.teamwizardry.librarianlib.features.base.PotionMod


/**
 * @author WireSegal
 * Created at 9:22 AM on 4/15/16.
 */
object ModPotions {
    val faithlessness: PotionMod
    val drab: PotionMod
    val rooted: PotionMod
    val overcharged: PotionMod
    val featherweight: PotionMod
    val immortal: PotionMod
    val everburn: PotionMod
    val trapSeer: PotionMod

    init {
        faithlessness = PotionFaithlessness()
        drab = PotionDrabVision()
        rooted = PotionRooted()
        overcharged = PotionOvercharge()
        featherweight = PotionFeatherweight()
        immortal = PotionImmortality()
        everburn = PotionEverburn()
        trapSeer = PotionTrapSeer()
    }
}
