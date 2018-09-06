package shadowfox.botanicaladdons.common.items.travel.stones

import baubles.api.BaubleType
import baubles.api.BaublesApi
import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import com.teamwizardry.librarianlib.features.base.item.IItemColorProvider
import com.teamwizardry.librarianlib.features.base.item.ItemModTool
import com.teamwizardry.librarianlib.features.kotlin.isNotEmpty
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumHand
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.util.EnumHelper
import net.minecraftforge.event.entity.living.LivingEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import shadowfox.botanicaladdons.common.BotanicalAddons
import shadowfox.botanicaladdons.common.items.ModItems
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.item.ISortableTool
import vazkii.botania.common.item.equipment.tool.ToolCommons
import vazkii.botania.common.item.ModItems as BotaniaItems

/**
 * @author WireSegal
 * Created at 10:19 AM on 1/21/17.
 */
class ItemPolyStone(name: String) : ItemModTool(name, BotaniaAPI.manasteelToolMaterial, setOf()), IItemColorProvider, ISortableTool {
    override val itemColorFunction: ((ItemStack, Int) -> Int)?
        get() = { _, i ->
            if (i == 1)
                BotanicalAddons.PROXY.rainbow(0.25f).rgb
            else 0xFFFFFF
        }

    init {
        maxDamage = 0
    }

    override fun isEnchantable(stack: ItemStack) = true

    companion object {
        init {
            MinecraftForge.EVENT_BUS.register(this)
        }

        val NONE: ISortableTool.ToolType = EnumHelper.addEnum(ISortableTool.ToolType::class.java, "NONE", arrayOf())!!

        // Direct copy from ItemSwapRing, except for adding the NONE check and changing the type check
        @SubscribeEvent
        fun onPlayerTick(e: LivingEvent.LivingUpdateEvent) {
            val entity = e.entityLiving
            if (entity is EntityPlayer) {
                val baubles = BaublesApi.getBaublesHandler(entity) ?: return
                if (BaubleType.RING.validSlots
                        .map { baubles.getStackInSlot(it) }
                        .none { it.item == BotaniaItems.swapRing }) return

                val currentStack = entity.heldItemMainhand
                if (currentStack.isNotEmpty && currentStack.item is ISortableTool) {
                    val tool = currentStack.item as ISortableTool
                    val pos = ToolCommons.raytraceFromEntity(entity.world, entity, true, 4.5)
                    var typeToFind: ISortableTool.ToolType? = null
                    if (entity.isSwingInProgress && pos != null && pos.blockPos != null) {
                        val bestTool = entity.world.getBlockState(pos.blockPos)
                        // Changed type check
                        /*
                        if (!bestTool.material.isLiquid && ModItems.polyStone.getStrVsBlock(ItemStack.EMPTY, bestTool) != 1.0F)
                            typeToFind = NONE
                        */
                        if (!bestTool.material.isLiquid)
                            typeToFind = NONE


                    }

                    // The changed null check
                    if (typeToFind == NONE) {
                        var var15: ItemStack = currentStack
                        var var16 = if (tool.getSortingType(currentStack) == typeToFind) tool.getSortingPriority(currentStack) else -1
                        var bestSlot = -1

                        for (i in 0 until entity.inventory.sizeInventory) {
                            val stackInSlot = entity.inventory.getStackInSlot(i)
                            if (stackInSlot.isNotEmpty && stackInSlot.item is ISortableTool && stackInSlot != currentStack) {
                                val toolInSlot = stackInSlot.item as ISortableTool
                                if (toolInSlot.getSortingType(stackInSlot) == typeToFind) {
                                    val priority = toolInSlot.getSortingPriority(stackInSlot)
                                    if (priority > var16) {
                                        var15 = stackInSlot
                                        var16 = priority
                                        bestSlot = i
                                    }
                                }
                            }
                        }

                        if (bestSlot != -1) {
                            entity.setHeldItem(EnumHand.MAIN_HAND, var15)
                            entity.inventory.setInventorySlotContents(bestSlot, currentStack)
                        }

                    }
                }
            }
        }
    }

    private val defaultToolClasses = arrayOf("pickaxe", "shovel", "axe")
    private val toolsToCheck = arrayOf(Items.DIAMOND_PICKAXE, Items.DIAMOND_AXE, Items.DIAMOND_AXE).map(::ItemStack)

    /*
    override fun getStrVsBlock(stack: ItemStack, state: IBlockState): Float {
        defaultToolClasses.forEach {
            if (state.block.isToolEffective(it, state)) return super.getStrVsBlock(stack, state)
        }
        toolsToCheck.forEach {
            if (it.getStrVsBlock(state) != 1.0F) return super.getStrVsBlock(stack, state)
        }
        return 6.0F
    }
    */

    override fun getAttributeModifiers(slot: EntityEquipmentSlot, stack: ItemStack): Multimap<String, AttributeModifier>
            = HashMultimap.create()

    override fun getSortingPriority(stack: ItemStack) = 10
    override fun getSortingType(stack: ItemStack): ISortableTool.ToolType = NONE
}
