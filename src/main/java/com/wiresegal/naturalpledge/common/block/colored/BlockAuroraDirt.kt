package com.wiresegal.naturalpledge.common.block.colored

import com.teamwizardry.librarianlib.core.LibrarianLib
import com.teamwizardry.librarianlib.features.base.block.BlockMod
import com.teamwizardry.librarianlib.features.base.block.IBlockColorProvider
import com.teamwizardry.librarianlib.features.kotlin.plus
import com.teamwizardry.librarianlib.features.kotlin.times
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper.addToTooltip
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3i
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.common.IPlantable
import com.wiresegal.naturalpledge.api.lib.LibMisc
import com.wiresegal.naturalpledge.common.lexicon.LexiconEntries
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.lexicon.LexiconEntry


/**
 * @author WireSegal
 * Created at 10:38 PM on 5/6/16.
 */
class BlockAuroraDirt(name: String) : BlockMod(name, Material.GROUND), ILexiconable, IBlockColorProvider {

    companion object {
        fun fromPos(pos: BlockPos?): Int {
            if (pos == null) return 0x97C683
            return fromVec(fromPos(pos.x, pos.y, pos.z))
        }

        fun fromVec(vec: Vec3i): Int = (((vec.x + 256) % 256) shl 16) or (((vec.y + 256) % 256) shl 8) or ((vec.z + 256) % 256)
        fun fromPos(x: Double, y: Double, z: Double) = fromVec(trilinearInterp(x, y, z))

        fun fromPos(x: Int, y: Int, z: Int): Vec3i {
            var red = x * 32 + y * 16
            if (red and 256 != 0) {
                red = 255 - (red and 255)
            }
            red = red and 255

            var blue = y * 32 + z * 16
            if (blue and 256 != 0) {
                blue = 255 - (blue and 255)
            }
            blue = (blue xor 255) % 256

            var green = x * 16 + z * 32
            if (green and 256 != 0) {
                green = 255 - (green and 255)
            }
            green = green and 255

            return Vec3i(red, blue, green)
        }

        fun trilinearInterp(x: Double, y: Double, z: Double): Vec3i {
            val x0 = Math.round(x - 0.5).toInt()
            val y0 = Math.round(y - 0.5).toInt()
            val z0 = Math.round(z - 0.5).toInt()
            val x1 = Math.round(x + 0.5).toInt()
            val y1 = Math.round(y + 0.5).toInt()
            val z1 = Math.round(z + 0.5).toInt()

            val xd = x1 - x
            val xl = x1 - x0
            val yd = y1 - y
            val yl = y1 - y0
            val zd = z1 - z
            val zl = z1 - z0

            val c000 = Vec3d(fromPos(x0, y0, z0))
            val c001 = Vec3d(fromPos(x0, y0, z1))
            val c010 = Vec3d(fromPos(x0, y1, z0))
            val c011 = Vec3d(fromPos(x0, y1, z1))
            val c100 = Vec3d(fromPos(x1, y0, z0))
            val c101 = Vec3d(fromPos(x1, y0, z1))
            val c110 = Vec3d(fromPos(x1, y1, z0))
            val c111 = Vec3d(fromPos(x1, y1, z1))

            val c00 = (c000 * xd) + (c100 * (xl - xd))
            val c01 = (c001 * xd) + (c101 * (xl - xd))
            val c10 = (c010 * xd) + (c110 * (xl - xd))
            val c11 = (c011 * xd) + (c111 * (xl - xd))

            val c0 = (c00 * yd) + (c10 * (yl - yd))
            val c1 = (c01 * yd) + (c11 * (yl - yd))

            val c = (c0 * zd) + (c1 * (zl - zd))

            return Vec3i(c.x, c.y, c.z)
        }
    }

    init {
        soundType = SoundType.GROUND
        blockHardness = 0.5f
    }

    override fun addInformation(stack: ItemStack, player: World?, tooltip: MutableList<String>, advanced: ITooltipFlag) {
        addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.color.aurora")
    }

    override fun getEntry(p0: World?, p1: BlockPos?, p2: EntityPlayer?, p3: ItemStack): LexiconEntry? {
        return LexiconEntries.aurora
    }

    override fun isToolEffective(type: String?, state: IBlockState?): Boolean {
        return type == "shovel"
    }

    override fun getHarvestTool(state: IBlockState?): String? {
        return "shovel"
    }

    override val blockColorFunction: ((state: IBlockState, world: IBlockAccess?, pos: BlockPos?, tintIndex: Int) -> Int)?
        get() = { _, _, pos, _ -> fromPos(pos) }

    override val itemColorFunction: ((ItemStack, Int) -> Int)?
        get() = { _, _ ->
            val p = LibrarianLib.PROXY.getClientPlayer()
            fromPos(p.posX, p.posY, p.posZ)
        }

    override fun canSustainPlant(state: IBlockState?, world: IBlockAccess?, pos: BlockPos?, direction: EnumFacing?, plantable: IPlantable?): Boolean {
        return true
    }
}
