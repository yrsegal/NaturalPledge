package shadowfox.botanicaladdons.common.block

import com.teamwizardry.librarianlib.features.autoregister.TileRegister
import com.teamwizardry.librarianlib.features.base.block.BlockModContainer
import com.teamwizardry.librarianlib.features.base.block.TileMod
import com.teamwizardry.librarianlib.features.structure.InWorldRender.pos
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.stats.Achievement
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.ItemHandlerHelper
import shadowfox.botanicaladdons.common.lexicon.LexiconEntries
import vazkii.botania.api.corporea.CorporeaHelper
import vazkii.botania.api.corporea.ICorporeaRequestor
import vazkii.botania.api.corporea.ICorporeaSpark
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.common.achievement.ICraftAchievement
import vazkii.botania.common.achievement.ModAchievements
import vazkii.botania.common.block.tile.corporea.TileCorporeaFunnel
import vazkii.botania.common.core.helper.InventoryHelper

/**
 * @author WireSegal
 * Created at 7:32 PM on 1/16/17.
 */
class BlockCorporeaResonator(name: String) : BlockModContainer(name, Material.IRON), ICraftAchievement, ILexiconable {
    override fun getAchievementOnCraft(p0: ItemStack, p1: EntityPlayer, p2: IInventory): Achievement = ModAchievements.corporeaCraft

    override fun createTileEntity(world: World, state: IBlockState): TileEntity? {
        return TileCorporeaResonator()
    }

    init {
        soundType = SoundType.METAL
        setHardness(5.5f)
    }

    @TileRegister("resonator")
    class TileCorporeaResonator : TileMod(), ICorporeaRequestor {
        val handler = object : IItemHandler {
            override fun getStackInSlot(slot: Int) = ItemStack.EMPTY
            override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean) = stack
            override fun getSlots() = 1
            override fun extractItem(slot: Int, amount: Int, simulate: Boolean) = ItemStack.EMPTY
            override fun getSlotLimit(slot: Int) = 64
        }

        override fun hasCapability(capability: Capability<*>, facing: EnumFacing?)
                = capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing)

        @Suppress("UNCHECKED_CAST")
        override fun <T : Any> getCapability(capability: Capability<T>, facing: EnumFacing?): T?
                = if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) handler as T else super.getCapability(capability, facing)

        override fun doCorporeaRequest(request: Any, count: Int, spark: ICorporeaSpark) {
            if (request is String) {
                val inv = getInv()
                val stacks = CorporeaHelper.requestItem(request, count, spark, true, true)
                spark.onItemsRequested(stacks)
                for (stack in stacks) {
                    if (inv != null && ItemHandlerHelper.insertItemStacked(inv, stack, true).isEmpty) {
                        ItemHandlerHelper.insertItemStacked(inv, stack, false)
                    } else {
                        val item = EntityItem(world, pos.x.toDouble() + 0.5, pos.y.toDouble() + 1.5, pos.z.toDouble() + 0.5, stack)
                        world.spawnEntity(item)
                    }
                }
            }
        }

        private fun getInv(): IItemHandler? {
            var te = world.getTileEntity(pos.down())
            var ret = InventoryHelper.getInventory(world, pos.down(), EnumFacing.UP)
                    ?: InventoryHelper.getInventory(world, pos.down(), null)

            if (ret != null && te !is TileCorporeaFunnel) {
                return ret
            } else {
                te = world.getTileEntity(pos.down(2))
                ret = InventoryHelper.getInventory(world, pos.down(2), EnumFacing.UP)

                if (ret == null) ret = InventoryHelper.getInventory(world, pos.down(2), null)

                return if (ret != null && te !is TileCorporeaFunnel) ret else null
            }
        }
    }

    override fun getEntry(p0: World, p1: BlockPos, p2: EntityPlayer, p3: ItemStack) = LexiconEntries.corporeaRecall
}
