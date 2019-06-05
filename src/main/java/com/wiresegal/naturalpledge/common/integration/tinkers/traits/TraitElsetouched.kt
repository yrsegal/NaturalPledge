//package com.wiresegal.naturalpledge.common.integration.tinkers.traits
//
//import net.minecraft.block.Block
//import net.minecraft.entity.EntityLivingBase
//import net.minecraft.entity.player.EntityPlayer
//import net.minecraft.init.MobEffects
//import net.minecraft.item.ItemStack
//import net.minecraft.potion.PotionEffect
//import net.minecraftforge.common.MinecraftForge
//import net.minecraftforge.event.world.BlockEvent
//import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
//import com.wiresegal.naturalpledge.common.integration.tinkers.TinkersIntegration
//import com.wiresegal.naturalpledge.common.integration.tinkers.traits.TraitWorldforged.Companion.isSemiDisposable
//import slimeknights.tconstruct.library.TinkerRegistry
//import slimeknights.tconstruct.library.traits.AbstractTrait
//import slimeknights.tconstruct.library.utils.TagUtil
//import vazkii.botania.api.mana.ManaItemHandler
//import vazkii.botania.common.entity.EntityPixie
//import vazkii.botania.common.item.ModItems
//import vazkii.botania.common.item.equipment.armor.elementium.ItemElementiumHelm
//import vazkii.botania.common.item.equipment.tool.elementium.ItemElementiumPick.isDisposable
//
//
//
//
//class TraitElsetouched : AbstractTrait("elsetouched", TinkersIntegration.ELEMENTIUM_COLOR) {
//
//    override fun onHit(tool: ItemStack, player: EntityLivingBase, target: EntityLivingBase, damage: Float, isCritical: Boolean) {
//        if (Math.random() < 0.25 && (player !is EntityPlayer || ManaItemHandler.requestManaExact(tool, player as EntityPlayer?, 10, true))) {
//            val useEffects = player is EntityPlayer && (ModItems.elementiumHelm as ItemElementiumHelm).hasArmorSet(player as EntityPlayer?)
//            summonPixie(player, target, damage / 2f, useEffects)
//        }
//    }
//
//
//
//    override fun beforeBlockBreak(tool: ItemStack, event: BlockEvent.BreakEvent?) {
//        ModItems.elementiumShovel.onBlockStartBreak(tool, event!!.pos, event.player)
//    }
//
//    companion object {
//
//        init {
//            MinecraftForge.EVENT_BUS.register(this)
//        }
//
//        @SubscribeEvent
//        fun onHarvestDrops(event: HarvestDropsEvent) {
//            if (event.harvester != null) {
//                val stack = event.harvester.heldItemMainhand
//                if (stack != null && hasTrait(stack)) {
//                    for (i in 0..event.drops.size - 1) {
//                        val drop = event.drops[i]
//                        if (drop != null) {
//                            val block = Block.getBlockFromItem(drop.item)
//                            if (block != null) {
//                                if (isDisposable(block) || isSemiDisposable(block) && !event.harvester.isSneaking)
//                                    event.drops.removeAt(i)
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        val potions = arrayOf(MobEffects.BLINDNESS, MobEffects.WITHER, MobEffects.SLOWNESS, MobEffects.WEAKNESS)
//
//        fun summonPixie(summoner: EntityLivingBase, target: EntityLivingBase, damage: Float, useEffects: Boolean) {
//            if (summoner.world.isRemote) return
//
//            val pixie = EntityPixie(summoner.world)
//            pixie.setPosition(summoner.posX, summoner.posY + 2, summoner.posZ)
//
//            if (useEffects)
//                pixie.setApplyPotionEffect(PotionEffect(potions[summoner.world.rand.nextInt(potions.size)], 40, 0))
//
//            pixie.setProps(target, summoner, 0, damage)
//            summoner.world.spawnEntityInWorld(pixie)
//        }
//
//        fun hasTrait(stack: ItemStack): Boolean {
//            val list = TagUtil.getTraitsTagList(stack)
//            return (0..list.tagCount() - 1)
//                    .map { TinkerRegistry.getTrait(list.getStringTagAt(it)) }
//                    .any { it is TraitElsetouched }
//        }
//    }
//}
//
