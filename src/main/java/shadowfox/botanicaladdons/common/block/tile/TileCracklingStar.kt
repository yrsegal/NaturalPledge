package shadowfox.botanicaladdons.common.block.tile

import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import shadowfox.botanicaladdons.common.BotanicalAddons
import shadowfox.botanicaladdons.common.core.helper.RainbowItemHelper
import vazkii.botania.common.Botania
import vazkii.botania.common.core.helper.Vector3
import java.awt.Color

/**
 * @author WireSegal
 * Created at 1:46 PM on 5/4/16.
 */
class TileCracklingStar : TileModTickable() {
    private val TAG_COLOR = "color"
    var starColor = -1

    override fun updateEntity() {
        if (world.isRemote) {
            var flag = false
            val currentBlock = world.getBlockState(pos)
            fun makeLine(vec: Vector3) {
                val blockAt = world.getBlockState(pos.add(vec.x, vec.y, vec.z))
                if (blockAt.block == currentBlock.block) {
                    flag = true
                    BotanicalAddons.PROXY.wispLine(world, Vector3.fromBlockPos(pos).add(0.5 + (Math.random() - 0.5) * 0.05, 0.5 + (Math.random() - 0.5) * 0.05, 0.5 + (Math.random() - 0.5) * 0.05), vec, RainbowItemHelper.colorFromInt(starColor), Math.random() * 6.0, 10)
                }
            }

            for (i in EnumFacing.VALUES) makeLine(Vector3(i.directionVec.x.toDouble(), i.directionVec.y.toDouble(), i.directionVec.z.toDouble()))
            for (i in BiFacing.values()) makeLine(i.vec)
            for (i in TriFacing.values()) makeLine(i.vec)
            if (!flag) {
                val color = Color(RainbowItemHelper.colorFromIntAndPos(starColor, pos))
                Botania.proxy.wispFX(world, pos.x + 0.5, pos.y + 0.5, pos.z + 0.5, color.red / 255f, color.green / 255f, color.blue / 255f, 0.25f)
            }
        }
    }

    enum class BiFacing(x: Int, y: Int, z: Int) {
        UP_NORTH(0, 1, -1),
        UP_SOUTH(0, 1, 1),
        UP_WEST(-1, 1, 0),
        UP_EAST(1, 1, 0),
        DOWN_NORTH(0, -1, -1),
        DOWN_SOUTH(0, -1, 1),
        DOWN_WEST(-1, -1, 0),
        DOWN_EAST(1, -1, 0),
        EAST_NORTH(1, 0, -1),
        EAST_SOUTH(1, 0, 1),
        WEST_NORTH(-1, 0, 1),
        WEST_SOUTH(-1, 0, -1);

        val vec: Vector3 = Vector3(x.toDouble(), y.toDouble(), z.toDouble())
    }

    enum class TriFacing(x: Int, y: Int, z: Int) {
        UP_SOUTH_EAST(1, 1, 1),
        UP_SOUTH_WEST(-1, 1, 1),
        UP_NORTH_EAST(1, 1, -1),
        UP_NORTH_WEST(-1, 1, -1),
        DOWN_SOUTH_WEST(-1, -1, 1),
        DOWN_SOUTH_EAST(1, -1, 1),
        DOWN_NORTH_WEST(-1, -1, -1),
        DOWN_NORTH_EAST(1, -1, -1);

        val vec: Vector3 = Vector3(x.toDouble(), y.toDouble(), z.toDouble())
    }

    override fun writeCustomNBT(cmp: NBTTagCompound) {
        cmp.setInteger(TAG_COLOR, starColor)
    }

    override fun readCustomNBT(cmp: NBTTagCompound) {
        if (cmp.hasKey(TAG_COLOR))
            this.starColor = cmp.getInteger(TAG_COLOR)
    }

    fun getColor(): Int {
        return this.starColor
    }
}
