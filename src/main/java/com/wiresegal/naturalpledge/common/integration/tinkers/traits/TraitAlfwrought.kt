//package com.wiresegal.naturalpledge.common.integration.tinkers.traits
//
//import net.minecraft.item.ItemStack
//import com.wiresegal.naturalpledge.common.integration.tinkers.TinkersIntegration
//import slimeknights.tconstruct.library.traits.AbstractTrait
//import slimeknights.tconstruct.library.utils.TagUtil
//import slimeknights.tconstruct.library.utils.Tags
//import slimeknights.tconstruct.library.utils.ToolHelper
//
//class TraitAlfwrought : AbstractTrait("alfwrought", TinkersIntegration.ELEMENTIUM_COLOR) {
//
//    private fun getOrigIntTag(stack: ItemStack, key: String): Int {
//        val tag = TagUtil.getToolTag(stack)
//        val origTag = tag.getCompoundTag(Tags.TOOL_DATA_ORIG)
//        return origTag.getInteger(key)
//    }
//
//    private fun setOrigIntTag(stack: ItemStack, key: String, value: Int) {
//        val tag = TagUtil.getToolTag(stack)
//        val origTag = tag.getCompoundTag(Tags.TOOL_DATA_ORIG)
//        origTag.setInteger(key, value)
//    }
//
//    private fun setIntTag(stack: ItemStack, key: String, value: Int) {
//        val tag = TagUtil.getToolTag(stack)
//        tag.setInteger(key, value)
//    }
//
//    override fun onRepair(tool: ItemStack, amount: Int) {
//        val newDurability = ToolHelper.getMaxDurability(tool) + Math.max(amount, 25)
//
//        var orig = getOrigIntTag(tool, Tags.DURABILITY)
//        if (getOrigIntTag(tool, Tags.DURABILITY) == 0) {
//            orig = ToolHelper.getMaxDurability(tool)
//            setOrigIntTag(tool, Tags.DURABILITY, orig)
//        }
//
//        if (newDurability / orig > 3) return
//        if (ToolHelper.getCurrentDurability(tool) == 0) setIntTag(tool, Tags.DURABILITY, newDurability)
//    }
//}
//
