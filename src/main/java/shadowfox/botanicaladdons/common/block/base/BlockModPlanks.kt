package shadowfox.botanicaladdons.common.block.base

import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState

/**
 * @author WireSegal
 * Created at 10:29 PM on 5/27/16.
 */
open class BlockModPlanks(name: String, vararg variants: String) : BlockMod(name, Material.WOOD, *variants) {
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
}
