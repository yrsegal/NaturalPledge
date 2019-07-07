package com.wiresegal.naturalpledge.common.items.travel.stones

import com.teamwizardry.librarianlib.core.LibrarianLib
import com.teamwizardry.librarianlib.features.base.item.IItemColorProvider
import com.teamwizardry.librarianlib.features.base.item.ItemMod
import com.teamwizardry.librarianlib.features.helpers.getNBTInt
import com.teamwizardry.librarianlib.features.helpers.setNBTInt
import com.teamwizardry.librarianlib.features.methodhandles.MethodHandleHelper
import com.wiresegal.naturalpledge.common.NaturalPledge
import com.wiresegal.naturalpledge.common.block.BlockCorporeaResonator
import com.wiresegal.naturalpledge.common.block.BlockCorporeaResonator.TileCorporeaResonator
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.Style
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.util.text.TextFormatting
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.ServerChatEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.apache.commons.lang3.text.WordUtils
import vazkii.botania.api.corporea.CorporeaHelper
import vazkii.botania.api.corporea.CorporeaIndexRequestEvent
import vazkii.botania.api.corporea.ICorporeaAutoCompleteController
import vazkii.botania.api.wand.ICoordBoundItem
import vazkii.botania.common.Botania
import vazkii.botania.common.advancements.CorporeaRequestTrigger
import vazkii.botania.common.block.tile.corporea.TileCorporeaIndex
import vazkii.botania.common.core.handler.ModSounds
import vazkii.botania.common.entity.EntityCorporeaSpark
import java.util.regex.Pattern

/**
 * @author WireSegal
 * Created at 7:45 PM on 1/16/17.
 */
class ItemCorporeaFocus(name: String) : ItemMod(name), ICoordBoundItem, IItemColorProvider {

    override fun getBinding(stack: ItemStack): BlockPos? {
        val x = stack.getNBTInt(TAG_X, 0)
        val y = stack.getNBTInt(TAG_Y, Int.MIN_VALUE)
        val z = stack.getNBTInt(TAG_Z, 0)
        return if (y == Int.MIN_VALUE) null else BlockPos(x, y, z)
    }

    override fun onItemUse(player: EntityPlayer, world: World, pos: BlockPos, hand: EnumHand?, side: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        val stack = player.getHeldItem(hand)
        if (player.isSneaking && world.getBlockState(pos).block is BlockCorporeaResonator) {
            if (world.isRemote) {
                player.swingArm(hand)
                for (i in 0..9) {
                    val x1 = (pos.x + Math.random()).toFloat()
                    val y1 = (pos.y + 1).toFloat()
                    val z1 = (pos.z + Math.random()).toFloat()
                    Botania.proxy.wispFX(x1.toDouble(), y1.toDouble(), z1.toDouble(), Math.random().toFloat(), Math.random().toFloat(), Math.random().toFloat(), Math.random().toFloat() * 0.5f, -0.05f + Math.random().toFloat() * 0.05f)
                }
                return EnumActionResult.SUCCESS
            } else {
                stack.setNBTInt(TAG_X, pos.x)
                stack.setNBTInt(TAG_Y, pos.y)
                stack.setNBTInt(TAG_Z, pos.z)
                stack.setNBTInt(TAG_DIM, world.provider.dimension)
                world.playSound(player, player.posX, player.posY, player.posZ, ModSounds.ding, SoundCategory.PLAYERS, 1f, 5f)
                return EnumActionResult.SUCCESS
            }
        }

        return EnumActionResult.PASS
    }

    override val itemColorFunction: ((ItemStack, Int) -> Int)?
        get() = { _, i ->
            if (i == 1)
                NaturalPledge.PROXY.rainbow(0.25f)
            else 0xFFFFFF
        }

    companion object : ICorporeaAutoCompleteController {
        private const val TAG_X = "x"
        private const val TAG_Y = "y"
        private const val TAG_Z = "z"
        private const val TAG_DIM = "dim"

        init {
            MinecraftForge.EVENT_BUS.register(this)
            CorporeaHelper.registerAutoCompleteController(this)
        }
        private val patternsGetter = MethodHandleHelper.wrapperForStaticGetter(TileCorporeaIndex::class.java, "patterns")
        @Suppress("UNCHECKED_CAST")
        private val patterns: Map<Pattern, TileCorporeaIndex.IRegexStacker> by lazy {
            patternsGetter() as Map<Pattern, TileCorporeaIndex.IRegexStacker>
        }

        private val firstTick = MethodHandleHelper.wrapperForSetter(EntityCorporeaSpark::class.java, "firstTick")

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        fun onChatMessage(event: ServerChatEvent) {
            val item = event.player.heldItemMainhand ?: return
            if (item.item is ItemCorporeaFocus) {
                val pos = getBinding(item, event.player.world) ?: return
                val resonator = event.player.world.getTileEntity(pos) as? TileCorporeaResonator ?: return
                val spark = CorporeaHelper.getSparkForBlock(event.player.world, pos) ?: return

                if (spark.master == null) {
                    firstTick(spark as EntityCorporeaSpark, true)
                    spark.onUpdate()
                }

                val msg = event.message.toLowerCase().trim { it <= ' ' }

                var name = ""
                var count = 0

                for ((pattern, stacker) in patterns.entries) {
                    val matcher = pattern.matcher(msg)
                    if (matcher.matches()) {
                        count = stacker.getCount(matcher)
                        name = stacker.getName(matcher).toLowerCase().trim { it <= ' ' }
                    }
                }

                if (name == "this") name = item.displayName.toLowerCase().trim { it <= ' ' }

                val indexReqEvent = CorporeaIndexRequestEvent(event.player, name, count, spark)
                if (!MinecraftForge.EVENT_BUS.post(indexReqEvent)) {
                    resonator.doCorporeaRequest(name, count, spark)
                    event.player.sendMessage(TextComponentTranslation("botaniamisc.requestMsg",
                            if (count == Int.MAX_VALUE) "\u221E" else count,
                            WordUtils.capitalizeFully(name),
                            CorporeaHelper.lastRequestMatches,
                            CorporeaHelper.lastRequestExtractions)
                            .setStyle(Style().setColor(TextFormatting.LIGHT_PURPLE)))
                    CorporeaRequestTrigger.INSTANCE.trigger(event.player, event.player.serverWorld, pos, CorporeaHelper.lastRequestExtractions)
                }
                event.isCanceled = true

            }
        }

        override fun shouldAutoComplete(): Boolean {
            return LibrarianLib.PROXY.getClientPlayer().heldItemMainhand.item is ItemCorporeaFocus
        }

        private fun getBinding(stack: ItemStack, world: World): BlockPos? {
            val dim = stack.getNBTInt(TAG_DIM, 0)
            if (dim != world.provider.dimension) return null
            val x = stack.getNBTInt(TAG_X, 0)
            val y = stack.getNBTInt(TAG_Y, Int.MIN_VALUE)
            val z = stack.getNBTInt(TAG_Z, 0)
            return if (y == Int.MIN_VALUE) null else BlockPos(x, y, z)
        }
    }


}
