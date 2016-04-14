package shadowfox.botanicaladdons.common.items.bauble.faith

import com.google.common.collect.Multimap
import net.minecraft.block.BlockSapling
import net.minecraft.block.IGrowable
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.core.helper.ItemNBTHelper
import java.util.*

/**
 * @author WireSegal
 * Created at 7:24 AM on 4/14/16.
 */
class PriestlyEmblemIdunn : ItemFaithBauble.IFaithVariant {
    override val hasSubscriptions = true

    override val name: String = "idunn"

    override fun addToTooltip(stack: ItemStack, player: EntityPlayer?, tooltip: MutableList<String>, advanced: Boolean) {
        //TODO
    }

    override fun getAwakenerBlock() = null

    override fun fillAttributes(map: Multimap<String, AttributeModifier>, stack: ItemStack) {}

    val RANGE = 5
    override fun onUpdate(stack: ItemStack, player: EntityPlayer) {
        val saplings = ArrayList<Pair<BlockPos, IBlockState>>()
        val world = player.worldObj

        if (!ManaItemHandler.requestManaExact(stack, player, 10, false)) return

        if (world.totalWorldTime % 20 == 0L)
            for (x in -RANGE..RANGE)
                for (y in -RANGE..RANGE)
                    for (z in -RANGE..RANGE) {
                        val pos = BlockPos(player.posX+x, player.posY+y, player.posZ+z)
                        val state = world.getBlockState(pos)
                        val block = state.block
                        if (block is BlockSapling)
                            saplings.add(Pair(pos, state))

                    }

        if (saplings.size == 0) return

        val pair = saplings[world.rand.nextInt(saplings.size)]

        val pos = pair.first
        val state = pair.second
        val block = state.block

        if (block is BlockSapling && block.canGrow(world, pos, state, world.isRemote)) {
            if (world.isRemote)
                world.playAuxSFX(2005, pos, 0)
            else if (block.canUseBonemeal(world, world.rand, pos, state) && ManaItemHandler.requestManaExact(stack, player, 10, true))
                block.grow(world, world.rand, pos, state)
        }

        val cooldown = ItemNBTHelper.getInt(stack, TAG_COOLDOWN, 0)
        if (cooldown > 0) ItemNBTHelper.setInt(stack, TAG_COOLDOWN, cooldown - 1)
    }
    override fun onAwakenedUpdate(stack: ItemStack, player: EntityPlayer) {
        onUpdate(stack, player)
    }
    override fun punishTheFaithless(stack: ItemStack, player: EntityPlayer) {
        //TODO
    }

    val TAG_COOLDOWN = "cooldown"

    @SubscribeEvent
    fun onClick(e: PlayerInteractEvent.RightClickBlock) {
        val emblem = ItemFaithBauble.getEmblem(e.entityPlayer, PriestlyEmblemIdunn::class.java) ?: return

        val cooldown = ItemNBTHelper.getInt(emblem, TAG_COOLDOWN, 0)

        if (cooldown == 0 && e.entityPlayer.isSneaking && ManaItemHandler.requestManaExact(emblem, e.entityPlayer, 50, false) && e.itemStack == null) {
            val world = e.world
            val pos = e.pos
            val state = e.world.getBlockState(pos)
            val block = state.block
            if (block is IGrowable && block.canGrow(world, pos, state, world.isRemote)) {
                if (world.isRemote)
                    world.playAuxSFX(2005, pos, 0)
                else if (block.canUseBonemeal(world, world.rand, pos, state) && ManaItemHandler.requestManaExact(emblem, e.entityPlayer, 50, true))
                    block.grow(world, world.rand, pos, state)
            }
        }
    }
}
