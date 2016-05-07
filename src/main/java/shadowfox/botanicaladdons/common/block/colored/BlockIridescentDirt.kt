package shadowfox.botanicaladdons.common.block.colored

import net.minecraft.block.SoundType
import net.minecraft.block.material.MapColor
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.color.IBlockColor
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.api.lib.LibOreDict
import shadowfox.botanicaladdons.client.core.ModelHandler
import shadowfox.botanicaladdons.common.block.base.BlockMod
import shadowfox.botanicaladdons.common.block.base.ItemModBlock
import shadowfox.botanicaladdons.common.items.base.ItemMod

/**
 * @author WireSegal
 * Created at 9:50 PM on 5/6/16.
 */
class BlockIridescentDirt(val name: String) : BlockMod(name, Material.ground, *Array(16, { name + LibOreDict.COLORS[it] })), ModelHandler.IBlockColorProvider {
    companion object {
        val COLOR = PropertyEnum.create("color", EnumDyeColor::class.java)
    }

    init {
        this.defaultState = this.blockState.baseState.withProperty(COLOR, EnumDyeColor.WHITE)
        soundType = SoundType.GROUND
    }

    override val item: ItemBlock
        get() = object : ItemModBlock(this) {
            override fun getUnlocalizedName(par1ItemStack: ItemStack?): String {
                return "tile.${LibMisc.MOD_ID}:" + bareName
            }
        }

    override fun addInformation(stack: ItemStack, player: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
        ItemMod.addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.color.${stack.itemDamage}")
    }

    override fun getBlockColor(): IBlockColor? {
        return IBlockColor { iBlockState, iBlockAccess, blockPos, i -> iBlockState.getValue(COLOR).mapColor.colorValue }
    }

    override fun getColor(): IItemColor? {
        return IItemColor { itemStack, i -> EnumDyeColor.byMetadata(itemStack.itemDamage).mapColor.colorValue }
    }

    override fun createBlockState(): BlockStateContainer {
        return BlockStateContainer(this, COLOR)
    }

    override fun damageDropped(state: IBlockState): Int {
        return state.getValue<EnumDyeColor>(COLOR).metadata
    }

    override fun getMapColor(state: IBlockState): MapColor {
        return state.getValue(COLOR).mapColor
    }

    override fun getStateFromMeta(meta: Int): IBlockState {
        return this.defaultState.withProperty(COLOR, EnumDyeColor.byMetadata(meta))
    }

    override fun getMetaFromState(state: IBlockState): Int {
        return state.getValue(COLOR).metadata
    }
}
