package shadowfox.botanicaladdons.common.block.colored

import com.teamwizardry.librarianlib.features.base.block.BlockModPlanks
import com.teamwizardry.librarianlib.features.base.block.IBlockColorProvider
import com.teamwizardry.librarianlib.features.base.block.ItemModBlock
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper.addToTooltip
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.common.lexicon.LexiconEntries
import shadowfox.botanicaladdons.common.lib.LibOreDict
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.lexicon.LexiconEntry

/**
 * @author WireSegal
 * Created at 10:20 PM on 5/27/16.
 */
class BlockIridescentPlanks(name: String) : BlockModPlanks(name, *Array(16, { name + LibOreDict.COLORS[it] })), IBlockColorProvider, ILexiconable {

    class BlockRainbowPlanks(name: String) : BlockModPlanks(name) {
        override fun addInformation(stack: ItemStack, player: EntityPlayer?, tooltip: MutableList<String>, advanced: Boolean) {
            addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.color.16")
        }
    }

    companion object {
        val COLOR = PropertyEnum.create("color", EnumDyeColor::class.java)
    }

    override fun getStateFromMeta(meta: Int): IBlockState? {
        return defaultState.withProperty(COLOR, EnumDyeColor.byMetadata(meta))
    }

    override fun getMetaFromState(state: IBlockState?): Int {
        return (state ?: return 0).getValue(COLOR).metadata
    }

    override fun damageDropped(state: IBlockState?): Int {
        return getMetaFromState(state)
    }

    override fun createBlockState(): BlockStateContainer? {
        return BlockStateContainer(this, COLOR)
    }

    override fun createItemForm(): ItemBlock? {
        return object : ItemModBlock(this) {
            override fun getUnlocalizedName(stack: ItemStack): String {
                return "tile.${LibMisc.MOD_ID}:" + bareName
            }
        }
    }

    override fun addInformation(stack: ItemStack, player: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
        addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.color.${stack.itemDamage}")
    }

    override val blockColorFunction: ((IBlockState, IBlockAccess?, BlockPos?, Int) -> Int)?
        get() = { iBlockState, _, _, _ -> iBlockState.getValue(BlockIridescentDirt.COLOR).mapColor.colorValue }

    override val itemColorFunction: ((ItemStack, Int) -> Int)?
        get() = { itemStack, _ -> EnumDyeColor.byMetadata(itemStack.itemDamage).mapColor.colorValue }

    override fun getEntry(p0: World?, p1: BlockPos?, p2: EntityPlayer?, p3: ItemStack): LexiconEntry? {
        return LexiconEntries.irisDirt
    }
}
