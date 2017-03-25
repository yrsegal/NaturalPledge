package shadowfox.botanicaladdons.common.block

import net.minecraft.block.material.MapColor
import net.minecraft.block.material.Material
import net.minecraft.block.material.MaterialTransparent

/**
 * @author WireSegal
 * Created at 9:33 PM on 6/13/16.
 */
object ModMaterials {
    val TRANSPARENT = object : Material(MapColor.AIR) {
        override fun isSolid() = false
        override fun blocksLight() = false
        override fun blocksMovement() = false
    }
}
