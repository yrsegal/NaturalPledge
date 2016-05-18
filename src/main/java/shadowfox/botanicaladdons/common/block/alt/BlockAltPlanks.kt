package shadowfox.botanicaladdons.common.block.alt

import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import shadowfox.botanicaladdons.common.block.base.BlockMod
import shadowfox.botanicaladdons.common.core.tab.ModCreativeTab
import shadowfox.botanicaladdons.common.core.tab.ModTabs
import vazkii.botania.api.state.enums.AltGrassVariant

/**
 * @author WireSegal
 * Created at 3:22 PM on 5/17/16.
 */
class BlockAltPlanks(name: String) : BlockMod(name, Material.wood, *Array(6, { name + AltGrassVariant.values()[it].getName().capitalizeFirst() })) {
    companion object {
        val TYPE = PropertyEnum.create("type", AltGrassVariant::class.java)

        fun String.capitalizeFirst(): String {
            if (this.length == 0) return this
            return this.slice(0..0).capitalize() + this.slice(1..this.length - 1)
        }
    }

    init {
        soundType = SoundType.WOOD
        setHardness(2f)
        setResistance(5f)
    }

    override fun getHarvestTool(state: IBlockState?): String? {
        return "axe"
    }

    override fun isToolEffective(type: String?, state: IBlockState?): Boolean {
        return type == "axe"
    }

    override val creativeTab: ModCreativeTab?
        get() = ModTabs.TabWood

    override fun getStateFromMeta(meta: Int): IBlockState? {
        return defaultState.withProperty(TYPE, AltGrassVariant.values()[meta])
    }

    override fun getMetaFromState(state: IBlockState?): Int {
        return (state ?: return 0).getValue(TYPE).ordinal
    }

    override fun damageDropped(state: IBlockState?): Int {
        return getMetaFromState(state)
    }

    override fun createBlockState(): BlockStateContainer? {
        return BlockStateContainer(this, TYPE)
    }
}
