package shadowfox.botanicaladdons.common.items.travel.stones

import com.teamwizardry.librarianlib.core.LibrarianLib
import com.teamwizardry.librarianlib.features.base.item.IItemColorProvider
import com.teamwizardry.librarianlib.features.base.item.ItemMod
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper
import com.teamwizardry.librarianlib.features.methodhandles.MethodHandleHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.AxisAlignedBB
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
import shadowfox.botanicaladdons.common.BotanicalAddons
import shadowfox.botanicaladdons.common.block.BlockCorporeaResonator
import shadowfox.botanicaladdons.common.block.BlockCorporeaResonator.TileCorporeaResonator
import vazkii.botania.api.corporea.CorporeaHelper
import vazkii.botania.api.corporea.ICorporeaAutoCompleteController
import vazkii.botania.api.corporea.ICorporeaSpark
import vazkii.botania.api.sound.BotaniaSoundEvents
import vazkii.botania.api.wand.ICoordBoundItem
import vazkii.botania.common.Botania
import vazkii.botania.common.achievement.ModAchievements
import vazkii.botania.common.block.tile.corporea.TileCorporeaIndex
import vazkii.botania.common.core.handler.ModSounds
import vazkii.botania.common.entity.EntityCorporeaSpark
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

        private val patternsGetter = MethodHandleHelper.wrapperForStaticGetter(TileCorporeaIndex::class.java, "patterns")
        @Suppress("UNCHECKED_CAST")
        private val patterns: Map<Pattern, TileCorporeaIndex.IRegexStacker> by lazy {
            patternsGetter() as Map<Pattern, TileCorporeaIndex.IRegexStacker>
        }

        private val restartNetwork = MethodHandleHelper.wrapperForMethod(EntityCorporeaSpark::class.java, "restartNetwork", null)

        private val setMaster = MethodHandleHelper.wrapperForSetter(EntityCorporeaSpark::class.java, "master")

        private fun getNearbySparks(spark: Entity): List<ICorporeaSpark> {
            return spark.world.getEntitiesWithinAABB(Entity::class.java, AxisAlignedBB(spark.posX - 8.0, spark.posY - 8.0, spark.posZ - 8.0, spark.posX + 8.0, spark.posY + 8.0, spark.posZ + 8.0)) {
                it is ICorporeaSpark
            }.filterIsInstance<ICorporeaSpark>()
        }

        private fun findNetwork(spark: ICorporeaSpark) {
            if (spark !is EntityCorporeaSpark) return

            val sparks = getNearbySparks(spark)
            if (sparks.isNotEmpty()) for (other in sparks) if (other.network == spark.network && !(other as Entity).isDead) {
                val master = (other.master ?: if (other.isMaster) other else null) ?: continue

                setMaster(spark, master)
                restartNetwork(spark, arrayOf())
                break
            }
        }

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        fun onChatMessage(event: ServerChatEvent) {
            val item = event.player.heldItemMainhand ?: return
            if (item.item is ItemCorporeaFocus) {
                val pos = getBinding(item, event.player.world) ?: return
                val resonator = event.player.world.getTileEntity(pos) as? TileCorporeaResonator ?: return
                val spark = CorporeaHelper.getSparkForBlock(event.player.world, pos) ?: return

                if (spark.master == null) (spark as Entity).onUpdate()
                if (spark.master == null) findNetwork(spark)

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

                resonator.doCorporeaRequest(name, count, spark)
                event.player.sendMessage(TextComponentTranslation("botaniamisc.requestMsg",
                        if (count == Int.MAX_VALUE) "\u221E" else count,
                        WordUtils.capitalizeFully(name),
                        CorporeaHelper.lastRequestMatches + CorporeaHelper.lastRequestExtractions,
                        CorporeaHelper.lastRequestExtractions)
                        .setStyle(Style().setColor(TextFormatting.LIGHT_PURPLE)))
                event.isCanceled = true
                if (CorporeaHelper.lastRequestExtractions >= 50000) event.player.addStat(ModAchievements.superCorporeaRequest, 1)
            }
        }

        override fun shouldAutoComplete(): Boolean {
            return LibrarianLib.PROXY.getClientPlayer().heldItemMainhand?.item is ItemCorporeaFocus
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
                ItemNBTHelper.setInt(stack, TAG_X, pos.x)
                ItemNBTHelper.setInt(stack, TAG_Y, pos.y)
                ItemNBTHelper.setInt(stack, TAG_Z, pos.z)
                ItemNBTHelper.setInt(stack, TAG_DIM, world.provider.dimension)
                world.playSound(player, player.posX, player.posY, player.posZ, ModSounds.ding, SoundCategory.PLAYERS, 1f, 5f)
                return EnumActionResult.SUCCESS
            }
        }

        return EnumActionResult.PASS
    }

    override val itemColorFunction: ((ItemStack, Int) -> Int)?
        get() = { _, i ->
            if (i == 1)
                BotanicalAddons.PROXY.rainbow(0.25f).rgb
            else 0xFFFFFF
        }
}
