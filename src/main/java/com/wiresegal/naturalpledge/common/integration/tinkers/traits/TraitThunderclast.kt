//package com.wiresegal.naturalpledge.common.integration.tinkers.traits
//
//import net.minecraft.nbt.NBTTagCompound
//import com.wiresegal.naturalpledge.common.integration.tinkers.TinkersIntegration
//import slimeknights.tconstruct.library.traits.AbstractTrait
//import slimeknights.tconstruct.library.utils.TagUtil
//import slimeknights.tconstruct.library.utils.TinkerUtil
//
///**
// * @author WireSegal
// * Created at 5:00 PM on 6/25/16.
// */
//class TraitThunderclast : AbstractTrait("thunderclast", TinkersIntegration.THUNDERSTEEL_COLOR) {
//    override fun applyEffect(rootCompound: NBTTagCompound, modifierTag: NBTTagCompound) {
//        if (!TinkerUtil.hasTrait(rootCompound, identifier)) {
//            val data = TagUtil.getToolStats(rootCompound)
//            data.attack *= 1.75f
//            data.attackSpeedMultiplier /= 2f
//            TagUtil.setToolTag(rootCompound, data.get())
//        }
//        super.applyEffect(rootCompound, modifierTag)
//    }
//}
