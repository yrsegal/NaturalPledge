package shadowfox.botanicaladdons.common.items

import com.teamwizardry.librarianlib.common.base.item.IItemColorProvider
import com.teamwizardry.librarianlib.common.base.item.ItemMod
import com.teamwizardry.librarianlib.common.util.ItemNBTHelper
import com.teamwizardry.librarianlib.common.util.MethodHandleHelper
import net.minecraft.client.Minecraft
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
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.apache.commons.lang3.text.WordUtils
import shadowfox.botanicaladdons.common.BotanicalAddons
import shadowfox.botanicaladdons.common.block.BlockCorporeaResonator
import shadowfox.botanicaladdons.common.block.BlockCorporeaResonator.TileCorporeaResonator
import vazkii.botania.api.corporea.CorporeaHelper
import vazkii.botania.api.corporea.ICorporeaAutoCompleteController
import vazkii.botania.api.sound.BotaniaSoundEvents
import vazkii.botania.api.wand.ICoordBoundItem
import vazkii.botania.common.Botania
import vazkii.botania.common.achievement.ModAchievements
import vazkii.botania.common.block.tile.corporea.TileCorporeaIndex
import java.util.regex.Pattern

/**
 * @author WireSegal
 * Created at 7:45 PM on 1/16/17.
 */
class ItemCorporeaFocus(name: String) : ItemMod(name), ICoordBoundItem, IItemColorProvider {
    companion object : ICorporeaAutoCompleteController {
        val TAG_X = "x"
        val TAG_Y = "y"
        val TAG_Z = "z"
        val TAG_DIM = "dim"

        init {
            MinecraftForge.EVENT_BUS.register(this)
            CorporeaHelper.registerAutoCompleteController(this)
        }

        val patternsGetter = MethodHandleHelper.wrapperForStaticGetter(TileCorporeaIndex::class.java, "patterns")
        @Suppress("UNCHECKED_CAST")
        val patterns: Map<Pattern, TileCorporeaIndex.IRegexStacker> by lazy {
            patternsGetter() as Map<Pattern, TileCorporeaIndex.IRegexStacker>
        }

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        fun onChatMessage(event: ServerChatEvent) {
            val item = event.player.heldItemMainhand ?: return
            if (item.item is ItemCorporeaFocus) {
                val pos = getBinding(item, event.player.worldObj) ?: return
                val resonator = event.player.worldObj.getTileEntity(pos) as? TileCorporeaResonator ?: return
                val spark = CorporeaHelper.getSparkForBlock(event.player.worldObj, pos) ?: return

                val msg = event.message.toLowerCase().trim { it <= ' ' }

                var name = ""
                var count = 0

                for ((pattern, stacker) in patterns) {
                    val matcher = pattern.matcher(msg)
                    if (matcher.matches()) {
                        count = stacker.getCount(matcher)
                        name = stacker.getName(matcher).toLowerCase().trim { it <= ' ' }
                    }
                }

                if (name == "this") {
                    val stack1 = event.player.heldItemMainhand
                    if (stack1 != null) name = stack1.displayName.toLowerCase().trim { it <= ' ' }
                }

                resonator.doCorporeaRequest(name, count, spark)
                event.player.addChatMessage(TextComponentTranslation("botaniamisc.requestMsg",
                        Integer.valueOf(count),
                        WordUtils.capitalizeFully(name),
                        Integer.valueOf(CorporeaHelper.lastRequestMatches),
                        Integer.valueOf(CorporeaHelper.lastRequestExtractions))
                        .setStyle(Style().setColor(TextFormatting.LIGHT_PURPLE)))
                event.isCanceled = true
                if (CorporeaHelper.lastRequestExtractions >= 50000) event.player.addStat(ModAchievements.superCorporeaRequest, 1)
            }
        }

        @SideOnly(Side.CLIENT)
        override fun shouldAutoComplete(): Boolean {
            return Minecraft.getMinecraft().thePlayer.heldItemMainhand?.item is ItemCorporeaFocus
        }

        fun getBinding(stack: ItemStack, world: World): BlockPos? {
            val dim = ItemNBTHelper.getInt(stack, TAG_DIM, 0)
            if (dim != world.provider.dimension) return null
            val x = ItemNBTHelper.getInt(stack, TAG_X, 0)
            val y = ItemNBTHelper.getInt(stack, TAG_Y, Int.MIN_VALUE)
            val z = ItemNBTHelper.getInt(stack, TAG_Z, 0)
            return if (y == Int.MIN_VALUE) null else BlockPos(x, y, z)
        }
    }

    override fun getBinding(stack: ItemStack): BlockPos? {
        val x = ItemNBTHelper.getInt(stack, TAG_X, 0)
        val y = ItemNBTHelper.getInt(stack, TAG_Y, Int.MIN_VALUE)
        val z = ItemNBTHelper.getInt(stack, TAG_Z, 0)
        return if (y == Int.MIN_VALUE) null else BlockPos(x, y, z)
    }

    override fun onItemUse(stack: ItemStack, player: EntityPlayer, world: World, pos: BlockPos, hand: EnumHand?, side: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
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
                ItemNBTHelper.setInt(stack, TAG_X, pos.x)
                ItemNBTHelper.setInt(stack, TAG_Y, pos.y)
                ItemNBTHelper.setInt(stack, TAG_Z, pos.z)
                ItemNBTHelper.setInt(stack, TAG_DIM, world.provider.dimension)
                world.playSound(player, player.posX, player.posY, player.posZ, BotaniaSoundEvents.ding, SoundCategory.PLAYERS, 1f, 5f)
                return EnumActionResult.SUCCESS
            }
        }

        return EnumActionResult.PASS
    }

    override val itemColorFunction: ((ItemStack, Int) -> Int)?
        get() = { itemStack, i ->
            if (i == 1)
                BotanicalAddons.PROXY.rainbow(0.25f).rgb
            else 0xFFFFFF
        }
}