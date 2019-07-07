package com.wiresegal.naturalpledge.common.block

import com.teamwizardry.librarianlib.features.autoregister.TileRegister
import com.teamwizardry.librarianlib.features.base.block.tile.BlockModContainer
import com.teamwizardry.librarianlib.features.base.block.tile.TileMod
import com.teamwizardry.librarianlib.features.saving.Save
import com.wiresegal.naturalpledge.api.lib.LibMisc
import com.wiresegal.naturalpledge.common.NaturalPledge
import com.wiresegal.naturalpledge.common.lexicon.LexiconEntries
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
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
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.common.Botania
import vazkii.botania.common.block.ModBlocks as BotaniaBlocks

/**
 * @author WireSegal
 * Created at 2:46 PM on 4/17/16.
 */
class BlockEnderBind(name: String) : BlockModContainer(name, Material.IRON), ILexiconable {

    companion object {
        val DEFAULT_COLOR = 0x1B7463

        val AABB = AxisAlignedBB(0.25, 0.25, 0.25, 0.75, 0.75, 0.75)
    }

    init {
        setHardness(5F)
        setResistance(10F)
        soundType = SoundType.METAL
        setLightLevel(0.5F)
    }

    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        val te = worldIn.getTileEntity(pos) as? TileEnderBind ?: return false
        if (te.playerName == null) {
            if (worldIn.isRemote) {
                val color = NaturalPledge.PROXY.pulseColor(DEFAULT_COLOR)
                val r = ((color and 0xff0000) shl 16) / 255f
                val g = ((color and 0xff00) shl 8) / 255f
                val b = (color and 0xff) / 255f
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
                worldIn.playSound(null, pos, SoundEvents.ENTITY_ENDERMEN_STARE, SoundCategory.PLAYERS, 1f, 100f)
                playerIn.sendMessage(TextComponentTranslation("misc.${LibMisc.MOD_ID}.actuator_bind")
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

    override fun getEntry(p0: World?, p1: BlockPos?, p2: EntityPlayer?, p3: ItemStack) = LexiconEntries.enderActuator

    @TileRegister("actuator")
    class TileEnderBind : TileMod() {
        val handler = object : IItemHandler {
            override fun getStackInSlot(slot: Int) = ItemStack.EMPTY
            override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean) = stack
            override fun getSlots() = 1
            override fun extractItem(slot: Int, amount: Int, simulate: Boolean) = ItemStack.EMPTY
            override fun getSlotLimit(slot: Int) = 64
        }

        @Save var playerName: String? = null
        @Save var tickSet = 0L

        fun createCapability(): IItemHandler {
            val player = playerName ?: return handler
            return world.playerEntities
                    .filter { it.cachedUniqueIdString == player }
                    .firstOrNull()?.inventoryEnderChest
                    ?.let(::InvWrapper) ?: handler
        }

        override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean {
            if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return true
            return super.hasCapability(capability, facing)
        }

        @Suppress("UNCHECKED_CAST")
        override fun <T : Any> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
            if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
                return createCapability() as T
            }
            return super.getCapability(capability, facing)
        }
    }
}

