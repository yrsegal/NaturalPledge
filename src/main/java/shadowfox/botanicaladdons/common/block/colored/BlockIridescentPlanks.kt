package shadowfox.botanicaladdons.common.block.colored

import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.color.IBlockColor
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.client.core.ModelHandler
import shadowfox.botanicaladdons.common.block.base.BlockModPlanks
import shadowfox.botanicaladdons.common.block.base.ItemModBlock
import shadowfox.botanicaladdons.common.items.base.ItemMod
import shadowfox.botanicaladdons.common.lexicon.LexiconEntries
import shadowfox.botanicaladdons.common.lib.LibOreDict
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.lexicon.LexiconEntry

/**
 * @author WireSegal
 * Created at 10:20 PM on 5/27/16.
 */
class BlockIridescentPlanks(name: String) : BlockModPlanks(name, *Array(16, { name + LibOreDict.COLORS[it] })), ModelHandler.IBlockColorProvider, ILexiconable {

    class BlockRainbowPlanks(name: String) : BlockModPlanks(name) {
        override fun addInformation(stack: ItemStack, player: EntityPlayer?, tooltip: MutableList<String>, advanced: Boolean) {
            addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.color.16")
        }
    }

    companion object {
        val COLOR = PropertyEnum.create("color", EnumDyeColor::class.java)

        fun String.capitalizeFirst(): String {
            if (this.length == 0) return this
            return this.slice(0..0).capitalize() + this.slice(1..this.length - 1)
        }
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

    override val item: ItemBlock
        get() = object : ItemModBlock(this), ModelHandler.ICustomLogHolder {
            override fun getUnlocalizedName(stack: ItemStack?): String {
                return "tile.${LibMisc.MOD_ID}:" + bareName
            }

            override val sortingVariantCount: Int
                get() = 1

            override fun customLog(): String = "${ModelHandler.namePad} |  Variants by dye color"
            override fun customLogVariant(variantId: Int, variant: String): String = ""
            override fun shouldLogForVariant(variantId: Int, variant: String): Boolean = false
        }

    override fun addInformation(stack: ItemStack, player: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
        ItemMod.addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.color.${stack.itemDamage}")
    }

    @SideOnly(Side.CLIENT)
    override fun getBlockColor(): IBlockColor? {
        return IBlockColor { iBlockState, iBlockAccess, blockPos, i -> iBlockState.getValue(BlockIridescentDirt.COLOR).mapColor.colorValue }
    }

    @SideOnly(Side.CLIENT)
    override fun getItemColor(): IItemColor? {
        return IItemColor { itemStack, i -> EnumDyeColor.byMetadata(itemStack.itemDamage).mapColor.colorValue }
    }

    override fun getEntry(p0: World?, p1: BlockPos?, p2: EntityPlayer?, p3: ItemStack?): LexiconEntry? {
        return LexiconEntries.irisDirt
    }
}
