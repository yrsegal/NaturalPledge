package com.wiresegal.naturalpledge.common.events

import com.teamwizardry.librarianlib.features.config.ConfigProperty
import com.teamwizardry.librarianlib.features.helpers.setNBTBoolean
import com.teamwizardry.librarianlib.features.helpers.setNBTInt
import com.wiresegal.naturalpledge.api.lib.LibMisc
import com.wiresegal.naturalpledge.common.items.ModItems
import com.wiresegal.naturalpledge.common.items.travel.stones.ItemWaystone
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayer.PERSISTED_NBT_TAG
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import vazkii.botania.common.item.ModItems as BotaniaItems

/**
 * @author WireSegal
 * Created at 9:59 PM on 6/4/17.
 */
object PlayerStartHandler {
    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    @ConfigProperty("spawn",
            "What slot to spawn a Traveller's Waystone bound to spawn in when a player first joins. Use -1 to disable.")
    var spawnStone = -1
    @ConfigProperty("spawn",
            "Whether the spawned Traveller's Waystone can be rebound.")
    var rebindStone = false

    @ConfigProperty("spawn",
            "What slot to spawn a Stone of Return in when a player first joins. Use -1 to disable.")
    var spawnReturnStone = -1

    @ConfigProperty("spawn",
            "What slot to spawn a Stone of Nether Fracture in when a player first joins. Use -1 to disable.")
    var spawnNetherStone = -1

    @ConfigProperty("spawn",
            "What slot to spawn a Lexica Botania in when a player first joins. Use -1 to disable.")
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
                    stack.setNBTInt(ItemWaystone.TAG_X, pos.x)
                    stack.setNBTInt(ItemWaystone.TAG_Y, pos.y)
                    stack.setNBTInt(ItemWaystone.TAG_Z, pos.z)
                    if (!rebindStone)
                        stack.setNBTBoolean(ItemWaystone.TAG_NO_RESET, true)

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
