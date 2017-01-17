package shadowfox.botanicaladdons.common.block

import com.teamwizardry.librarianlib.common.base.block.BlockModContainer
import com.teamwizardry.librarianlib.common.base.block.TileMod
import com.teamwizardry.librarianlib.common.util.autoregister.TileRegister
import com.teamwizardry.librarianlib.common.util.saving.Save
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.Style
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.util.text.TextFormatting
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.wrapper.InvWrapper
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.common.BotanicalAddons
import shadowfox.botanicaladdons.common.lexicon.LexiconEntries
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.common.Botania
import java.awt.Color
import vazkii.botania.common.block.ModBlocks as BotaniaBlocks

/**
 * @author WireSegal
 * Created at 2:46 PM on 4/17/16.
 */
class BlockEnderBind(name: String) : BlockModContainer(name, Material.IRON), ILexiconable {

    companion object {
        val DEFAULT_COLOR = Color(0x1B7463)

        val AABB = AxisAlignedBB(0.25, 0.25, 0.25, 0.75, 0.75, 0.75)
    }

    init {
        setHardness(5F)
        setResistance(10F)
        soundType = SoundType.METAL
        setLightLevel(0.5F)
    }

    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand, heldItem: ItemStack?, side: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        val te = worldIn.getTileEntity(pos) as? TileEnderBind ?: return false
        if (te.playerName == null) {
            if (worldIn.isRemote) {
                val color = BotanicalAddons.PROXY.pulseColor(DEFAULT_COLOR)
                val r = color.red / 255f
                val g = color.green / 255f
                val b = color.blue / 255f
                for (i in 0 until 30) {
                    val x1 = (pos.x + Math.random()).toFloat()
                    val y1 = (pos.y + Math.random()).toFloat()
                    val z1 = (pos.z + Math.random()).toFloat()
                    Botania.proxy.wispFX(x1.toDouble(), y1.toDouble(), z1.toDouble(), r, g, b, Math.random().toFloat() * 0.5f, -0.05f + Math.random().toFloat() * 0.05f)
                }
            }
            te.playerName = playerIn.cachedUniqueIdString
            te.tickSet = worldIn.totalWorldTime
            if (!worldIn.isRemote) {
                playerIn.addChatComponentMessage(TextComponentTranslation("misc.${LibMisc.MOD_ID}.actuatorBind")
                        .setStyle(Style().setColor(TextFormatting.DARK_GREEN)))
                te.markDirty()
            }
            return true
        }

        return false
    }

    override fun createTileEntity(world: World, state: IBlockState) = TileEnderBind()

    override fun getBoundingBox(state: IBlockState?, source: IBlockAccess?, pos: BlockPos?) = AABB

    override fun isOpaqueCube(state: IBlockState?) = false
    override fun isFullCube(state: IBlockState?) = false

    override fun getEntry(p0: World?, p1: BlockPos?, p2: EntityPlayer?, p3: ItemStack?) = LexiconEntries.enderActuator

    @TileRegister("actuator")
    class TileEnderBind : TileMod() {
        @Save var playerName: String? = null
        @Save var tickSet = 0L

        private var cachedCap: IItemHandler? = null

        fun createCapability(): IItemHandler? {
            val player = playerName ?: return null
            return world.playerEntities
                    .filter { it.cachedUniqueIdString == player }
                    .firstOrNull()?.inventoryEnderChest
                    ?.let(::InvWrapper)
        }

        override fun hasCapability(capability: Capability<*>?, facing: EnumFacing?): Boolean {
            cachedCap = createCapability()
            if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return cachedCap != null
            return super.hasCapability(capability, facing)
        }

        @Suppress("UNCHECKED_CAST")
        override fun <T : Any?> getCapability(capability: Capability<T>?, facing: EnumFacing?): T? {
            if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && cachedCap != null) {
                val cap = cachedCap as T
                cachedCap = null
                return cap
            }
            return super.getCapability(capability, facing)
        }
    }
}

