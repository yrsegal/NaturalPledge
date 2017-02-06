package shadowfox.botanicaladdons.common.integration.tinkers.traits

import net.minecraft.item.ItemStack
import shadowfox.botanicaladdons.common.integration.tinkers.TinkersIntegration
import slimeknights.tconstruct.library.traits.AbstractTrait
import slimeknights.tconstruct.library.utils.TagUtil
import slimeknights.tconstruct.library.utils.Tags
import slimeknights.tconstruct.library.utils.ToolHelper

class TraitAlfwrought : AbstractTrait("alfwrought", TinkersIntegration.ELEMENTIUM_COLOR) {

    private fun getIntTag(stack: ItemStack, key: String): Int {
        val tag = TagUtil.getToolTag(stack)

        return tag.getInteger(key)
    }

    private fun getOrigIntTag(stack: ItemStack, key: String): Int {
        val tag = TagUtil.getToolTag(stack)
        val origTag = tag.getCompoundTag(Tags.TOOL_DATA_ORIG)
        return origTag.getInteger(key)
    }

    private fun setOrigIntTag(stack: ItemStack, key: String, value: Int) {
        val tag = TagUtil.getToolTag(stack)
        val origTag = tag.getCompoundTag(Tags.TOOL_DATA_ORIG)
        origTag.setInteger(key, value)
    }

    private fun setIntTag(stack: ItemStack, key: String, value: Int) {
        val tag = TagUtil.getToolTag(stack)

        tag.setInteger(key, value)
    }

    override fun onRepair(tool: ItemStack, amount: Int) {
        val newDurability = getIntTag(tool, Tags.DURABILITY) + Math.max(amount, 25)
        if (getOrigIntTag(tool, Tags.DURABILITY) == 0) setOrigIntTag(tool, Tags.DURABILITY, getIntTag(tool, Tags.DURABILITY))
        if (newDurability / getOrigIntTag(tool, Tags.DURABILITY) > 3) return
        if (ToolHelper.getCurrentDurability(tool) == 0) {
            setIntTag(tool, Tags.DURABILITY, getIntTag(tool, Tags.DURABILITY) + Math.max(amount, 25))
        }
    }
}

