package shadowfox.botanicaladdons.common.events

import com.teamwizardry.librarianlib.features.config.ConfigPropertyBoolean
import com.teamwizardry.librarianlib.features.config.ConfigPropertyInt
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper
import com.teamwizardry.librarianlib.features.kotlin.get
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayer.PERSISTED_NBT_TAG
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.common.items.ModItems
import shadowfox.botanicaladdons.common.items.travel.stones.ItemWaystone
import vazkii.botania.common.item.ModItems as BotaniaItems

/**
 * @author WireSegal
 * Created at 9:59 PM on 6/4/17.
 */
object PlayerStartHandler {
    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    @ConfigPropertyInt(LibMisc.MOD_ID, "spawn", "spawn_waystone", "What slot to spawn a Traveller's Waystone bound to spawn in when a player first joins. Use -1 to disable.", -1)
    var spawnStone = -1
    @ConfigPropertyBoolean(LibMisc.MOD_ID, "spawn", "spawned_waystone_bindable", "Whether the spawned Traveller's Waystone can be rebound.", false)
    var rebindStone = false

    @ConfigPropertyInt(LibMisc.MOD_ID, "spawn", "spawn_return_stone", "What slot to spawn a Stone of Return in when a player first joins. Use -1 to disable.", -1)
    var spawnReturnStone = -1

    @ConfigPropertyInt(LibMisc.MOD_ID, "spawn", "spawn_nether_stone", "What slot to spawn a Stone of Nether Fracture in when a player first joins. Use -1 to disable.", -1)
    var spawnNetherStone = -1

    @ConfigPropertyInt(LibMisc.MOD_ID, "spawn", "spawn_lexica_botania", "What slot to spawn a Lexica Botania in when a player first joins. Use -1 to disable.", -1)
    var spawnLexicaBotania = -1


    val tag = "${LibMisc.MOD_ID}.playerJoinedWorld"

    @SubscribeEvent
    fun onPlayerJoinWorld(e: EntityJoinWorldEvent) {
        val player = e.entity
        if (player is EntityPlayer && !player.world.isRemote) {
            val customData = player.entityData
            if (!customData.hasKey(PERSISTED_NBT_TAG)) customData.setTag(PERSISTED_NBT_TAG, NBTTagCompound())
            val persistentData = customData.getCompoundTag(PERSISTED_NBT_TAG)

            val hasJoined = if (!persistentData.hasKey(tag)) false else persistentData.getBoolean(tag)

            if (!hasJoined) {
                if (spawnStone != -1) {
                    val stack = ItemStack(ModItems.finder)
                    val pos = player.world.getTopSolidOrLiquidBlock(player.world.spawnPoint)
                    ItemNBTHelper.setInt(stack, ItemWaystone.TAG_X, pos.x)
                    ItemNBTHelper.setInt(stack, ItemWaystone.TAG_Y, pos.y)
                    ItemNBTHelper.setInt(stack, ItemWaystone.TAG_Z, pos.z)
                    if (!rebindStone)
                        ItemNBTHelper.setBoolean(stack, ItemWaystone.TAG_NO_RESET, true)

                    if (player.inventory.getStackInSlot(spawnStone).isEmpty) {
                        if (!player.inventory.addItemStackToInventory(stack))
                            player.dropItem(stack.copy(), false)
                    } else player.inventory.setInventorySlotContents(spawnStone, stack)
                }

                if (spawnReturnStone != -1) {
                    val stack = ItemStack(ModItems.deathFinder)
                    if (player.inventory.getStackInSlot(spawnReturnStone).isEmpty) {
                        if (!player.inventory.addItemStackToInventory(stack))
                            player.dropItem(stack.copy(), false)
                    } else player.inventory.setInventorySlotContents(spawnReturnStone, stack)
                }

                if (spawnNetherStone != -1) {
                    val stack = ItemStack(ModItems.portalStone)
                    if (player.inventory.getStackInSlot(spawnNetherStone).isEmpty) {
                        if (!player.inventory.addItemStackToInventory(stack))
                            player.dropItem(stack.copy(), false)
                    } else player.inventory.setInventorySlotContents(spawnNetherStone, stack)
                }

                if (spawnLexicaBotania != -1) {
                    val stack = ItemStack(BotaniaItems.lexicon)
                    if (player.inventory.getStackInSlot(spawnLexicaBotania).isEmpty) {
                        if (!player.inventory.addItemStackToInventory(stack))
                            player.dropItem(stack.copy(), false)
                    } else player.inventory.setInventorySlotContents(spawnLexicaBotania, stack)
                }

                persistentData.setBoolean(tag, true)
            }
        }
    }
}
