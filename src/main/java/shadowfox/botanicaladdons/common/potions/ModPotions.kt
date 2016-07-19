package shadowfox.botanicaladdons.common.potions

import shadowfox.botanicaladdons.common.potions.base.PotionMod

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

    private var iconIndex = 0

    init {
        faithlessness = PotionFaithlessness(iconIndex++)
        drab = PotionDrabVision(iconIndex++)
        rooted = PotionRooted(iconIndex++)
        overcharged = PotionOvercharge(iconIndex++)
        featherweight = PotionFeatherweight(iconIndex++)
        immortal = PotionImmortality(iconIndex++)
        everburn = PotionEverburn(iconIndex++)
    }
}
