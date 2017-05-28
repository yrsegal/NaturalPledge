package shadowfox.botanicaladdons.common.items.bauble.faith

import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper
import net.minecraft.block.BlockSapling
import net.minecraft.block.IGrowable
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import shadowfox.botanicaladdons.api.priest.IFaithVariant
import shadowfox.botanicaladdons.api.sapling.ISaplingBlock
import shadowfox.botanicaladdons.common.lib.LibNames
import shadowfox.botanicaladdons.common.potions.ModPotions
import vazkii.botania.api.mana.ManaItemHandler
import java.util.*

/**
 * @author WireSegal
 * Created at 7:24 AM on 4/14/16.
 */
object PriestlyEmblemIdunn : IFaithVariant {

    override fun getName(): String = "idunn"

    override fun hasSubscriptions(): Boolean = true

    override fun getSpells(stack: ItemStack, player: EntityPlayer): MutableList<String> {
        return mutableListOf(LibNames.SPELL_PROTECTION, LibNames.SPELL_IDUNN_INFUSION)
    }

    val RANGE = 5
    override fun onUpdate(stack: ItemStack, player: EntityPlayer) {
        val saplings = ArrayList<Pair<BlockPos, IBlockState>>()
        val world = player.world

        if (!world.isRemote) {
            val cooldown = ItemNBTHelper.getInt(stack, TAG_COOLDOWN, 0)
            if (cooldown > 0) ItemNBTHelper.setInt(stack, TAG_COOLDOWN, cooldown - 1)
        }

        if (!ManaItemHandler.requestManaExact(stack, player, 10, false)) return

        if (world.totalWorldTime % 40 == 0L) for (pos in BlockPos.getAllInBoxMutable(
                BlockPos(player.posX - RANGE, player.posY - RANGE, player.posZ - RANGE),
                BlockPos(player.posX + RANGE, player.posY + RANGE, player.posZ + RANGE))) {
            val state = world.getBlockState(pos)
            val block = state.block
            if (block is BlockSapling || block is ISaplingBlock)
                saplings.add(BlockPos(pos) to state)
        }

        if (saplings.size == 0) return

        val pair = saplings[world.rand.nextInt(saplings.size)]

        val pos = pair.first
        val state = pair.second
        val block = state.block

        if (block is IGrowable && block.canGrow(world, pos, state, world.isRemote)) {
            if (world.isRemote)
                world.playEvent(2005, pos, 0)
            else if (block.canUseBonemeal(world, world.rand, pos, state) && ManaItemHandler.requestManaExact(stack, player, 10, true))
                grow(player, block, world, pos, state)
        }
    }

    fun grow(player: EntityPlayer, block: IGrowable, world: World, pos: BlockPos, state: IBlockState) {
        block.grow(world, world.rand, pos, state)
        world.playSound(player, pos, SoundEvents.BLOCK_LAVA_POP, SoundCategory.BLOCKS, 1f, 0.1f)
    }

    override fun punishTheFaithless(stack: ItemStack, player: EntityPlayer) {
        player.addPotionEffect(PotionEffect(ModPotions.rooted, 600))
    }

    val TAG_COOLDOWN = "cooldown"
    val COOLDOWN_LENGTH = 15

    @SubscribeEvent
    fun onClick(e: PlayerInteractEvent.RightClickBlock) {
        val emblem = ItemFaithBauble.getEmblem(e.entityPlayer, PriestlyEmblemIdunn::class.java) ?: return

        val cooldown = ItemNBTHelper.getInt(emblem, TAG_COOLDOWN, 0)

        if (cooldown == 0 && e.entityPlayer.isSneaking && ManaItemHandler.requestManaExact(emblem, e.entityPlayer, 50, false) && e.itemStack.isEmpty) {
            val world = e.world
            val pos = e.pos
            val state = e.world.getBlockState(pos)
            val block = state.block
            if (block is IGrowable && block.canGrow(world, pos, state, world.isRemote)) {
                if (world.isRemote)
                    world.playEvent(2005, pos, 0)
                else if (block.canUseBonemeal(world, world.rand, pos, state) && ManaItemHandler.requestManaExact(emblem, e.entityPlayer, 50, true)) {
                    grow(e.entityPlayer, block, world, pos, state)
                    ItemNBTHelper.setInt(emblem, TAG_COOLDOWN, COOLDOWN_LENGTH)
                }
            }
        }
    }
}
