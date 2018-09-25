package com.wiresegal.naturalpledge.common.network

import baubles.api.BaubleType
import baubles.api.BaublesApi
import com.teamwizardry.librarianlib.features.autoregister.PacketRegister
import com.teamwizardry.librarianlib.features.helpers.vec
import com.teamwizardry.librarianlib.features.network.PacketBase
import net.minecraft.init.SoundEvents
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side
import com.wiresegal.naturalpledge.common.core.helper.NPMethodHandles
import com.wiresegal.naturalpledge.common.items.bauble.ItemDivineCloak

@PacketRegister(Side.SERVER)
class BlinkMessage : PacketBase() {

    override fun handle(ctx: MessageContext) {

        val player = ctx.serverHandler.player
        val baubles = BaublesApi.getBaublesHandler(player)
        val body = baubles.getStackInSlot(BaubleType.BODY.validSlots[0])
        if (body.item is ItemDivineCloak && body.itemDamage == 3) {
            val look = player.lookVec
            val dist = 6.0

            val position = vec(player.posX + look.x * dist, player.posY + look.y * dist, player.posZ + look.z * dist)
            val blockAt = BlockPos(position)
            if (player.world.getBlockState(blockAt).isFullCube || !player.world.getBlockState(blockAt.up()).isFullCube) {
                ctx.serverHandler.setPlayerLocation(position.x, position.y, position.z, player.rotationYaw, player.rotationPitch)
                NPMethodHandles.captureCurrentPosition(ctx.serverHandler)
                player.world.playSound(position.x, position.y, position.z, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1f, 1f, false)
            }
        }
    }
}


