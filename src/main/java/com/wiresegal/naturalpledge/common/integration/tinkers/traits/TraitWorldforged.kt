//package com.wiresegal.naturalpledge.common.integration.tinkers.traits
//
//import net.minecraft.block.Block
//import net.minecraft.block.state.IBlockState
//import net.minecraft.entity.EntityLivingBase
//import net.minecraft.entity.player.EntityPlayer
//import net.minecraft.init.Blocks
//import net.minecraft.init.MobEffects
//import net.minecraft.item.ItemStack
//import net.minecraft.potion.PotionEffect
//import net.minecraft.util.Rotation
//import net.minecraft.util.SoundCategory
//import net.minecraft.util.math.BlockPos
//import net.minecraft.world.World
//import net.minecraftforge.common.ForgeHooks
//import net.minecraftforge.event.world.BlockEvent
//import net.minecraftforge.items.ItemHandlerHelper
//import net.minecraftforge.oredict.OreDictionary
//import com.wiresegal.naturalpledge.common.block.BlockAwakenerCore
//import com.wiresegal.naturalpledge.common.integration.tinkers.TinkersIntegration
//import slimeknights.tconstruct.library.tools.IAoeTool
//import slimeknights.tconstruct.library.traits.AbstractTrait
//import slimeknights.tconstruct.library.utils.ToolHelper
//import vazkii.botania.api.BotaniaAPI
//import vazkii.botania.api.mana.ManaItemHandler
//import vazkii.botania.api.sound.BotaniaSoundEvents
//import vazkii.botania.common.core.handler.ConfigHandler
//import vazkii.botania.common.item.equipment.tool.elementium.ItemElementiumPick
//
//class TraitWorldforged : AbstractTrait("worldforged", TinkersIntegration.TERRASTEEL_COLOR) {
//
//    override fun beforeBlockBreak(tool: ItemStack, event: BlockEvent.BreakEvent) {
//        val player = event.player
//        val world = player.world
//        if (!player.isSneaking) {
//            event.isCanceled = true
//            if (!world.isRemote) {
//                val aoes = (tool.item as IAoeTool).getAOEBlocks(tool, world, player, event.pos)
//                val poses = aoes.toMutableList()
//                poses.add(event.pos)
//                poses
//                        .map { it to world.getBlockState(it) }
//                        .filter { breakFurthestBlock(tool, world, it.first, player, it.second) }
//                        .forEach { world.playSound(null, it.first, BotaniaSoundEvents.endoflame, SoundCategory.PLAYERS, 0.15f, 1.0f) }
//            }
//        }
//    }
//
//    override fun onHit(tool: ItemStack, player: EntityLivingBase, target: EntityLivingBase, damage: Float, isCritical: Boolean) {
//        if (Math.random() < 0.15f && (player !is EntityPlayer || ManaItemHandler.requestManaExact(tool, player as EntityPlayer?, 10, true))) {
//            target.addPotionEffect(PotionEffect(MobEffects.LEVITATION, 30, -5, true, false))
//            target.addPotionEffect(PotionEffect(MobEffects.SLOWNESS, 30))
//        }
//    }
//
//    companion object {
//        private var lastPos: BlockPos = BlockPos.ORIGIN
//        private var lastDistance: Double = 0.toDouble()
//
//        fun isSemiDisposable(block: Block): Boolean {
//            val stack = ItemStack(block)
//            val var1 = OreDictionary.getOreIDs(stack)
//            val var2 = var1.size
//
//            return (0..var2 - 1)
//                    .map { OreDictionary.getOreName(var1[it]) }
//                    .any { BotaniaAPI.semiDisposableBlocks.contains(it) }
//        }
//
//        fun breakFurthestBlock(tool: ItemStack, world: World, pos: BlockPos, player: EntityPlayer, state: IBlockState): Boolean {
//            val block = state.block
//
//            if (ItemElementiumPick.isDisposable(block) || isSemiDisposable(block) || block == Blocks.STONE)
//                return harvestBlock(tool, world, player, pos)
//
//            if (!ForgeHooks.canHarvestBlock(block, player, world, pos)) return false
//
//            lastPos = pos
//            lastDistance = 0.0
//            findBlocks(world, pos, state)
//            return harvestBlock(tool, world, player, lastPos)
//        }
//
//        fun findBlocks(world: World, pos: BlockPos, originalState: IBlockState) {
//            for (xx in -2..2) {
//                for (yy in 2 downTo -2) {
//                    for (zz in -2..2) {
//                        if (Math.abs(lastPos.x + xx - pos.x) > 24)
//                            return
//                        if (Math.abs(lastPos.y + yy - pos.y) > 48)
//                            return
//                        if (Math.abs(lastPos.z + zz - pos.z) > 24)
//                            return
//                        val state = world.getBlockState(lastPos.add(xx, yy, zz))
//                        if (originalState.block !== state.block || state.withRotation(Rotation.NONE) !== state.withRotation(Rotation.NONE))
//                            continue
//                        val xd = (lastPos.x + xx - pos.x).toDouble()
//                        val yd = (lastPos.y + yy - pos.y).toDouble()
//                        val zd = (lastPos.z + zz - pos.z).toDouble()
//                        val d = xd * xd + yd * yd + zd * zd
//                        if (d > lastDistance) {
//                            lastDistance = d
//                            lastPos = lastPos.add(xx, yy, zz)
//                            findBlocks(world, pos, originalState)
//                            return
//                        }
//                    }
//                }
//            }
//        }
//
//        fun harvestBlock(tool: ItemStack, world: World, player: EntityPlayer, pos: BlockPos): Boolean {
//            val state = world.getBlockState(pos)
//            if (!ForgeHooks.canHarvestBlock(state.block, player, world, pos)) return false
//            removeBlockWithDrops(player, tool, world, pos)
//            return true
//        }
//
//        fun removeBlockWithDrops(player: EntityPlayer, stack: ItemStack, world: World, pos: BlockPos) {
//            if (!world.isBlockLoaded(pos))
//                return
//
//            val state = world.getBlockState(pos)
//            val blk = state.block
//
//            if (!world.isRemote && !blk.isAir(state, world, pos) && state.getPlayerRelativeBlockHardness(player, world, pos) > 0) {
//                if (!player.capabilities.isCreativeMode) {
//                    val localState = world.getBlockState(pos)
//                    blk.onBlockHarvested(world, pos, localState, player)
//
//                    if (blk.removedByPlayer(state, world, pos, player, true)) {
//                        blk.onBlockDestroyedByPlayer(world, pos, state)
//                        BlockAwakenerCore.captureBlockDrops(true)
//                        blk.harvestBlock(world, player, pos, state, world.getTileEntity(pos), stack)
//                        for (i in BlockAwakenerCore.captureBlockDrops(false))
//                            ItemHandlerHelper.giveItemToPlayer(player, i)
//                    }
//
//                    ToolHelper.damageTool(stack, 1, player)
//
//                } else
//                    world.setBlockToAir(pos)
//
//                if (ConfigHandler.blockBreakParticles && ConfigHandler.blockBreakParticlesTool)
//                    world.playEvent(2001, pos, Block.getStateId(state))
//            }
//        }
//    }
//}
