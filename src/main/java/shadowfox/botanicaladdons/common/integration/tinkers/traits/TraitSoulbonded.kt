//package shadowfox.botanicaladdons.common.integration.tinkers.traits
//
//import net.minecraft.entity.Entity
//import net.minecraft.entity.EntityLivingBase
//import net.minecraft.init.Blocks
//import net.minecraft.item.ItemStack
//import net.minecraft.world.EnumSkyBlock
//import net.minecraft.world.World
//import shadowfox.botanicaladdons.common.integration.tinkers.TinkersIntegration
//import slimeknights.tconstruct.library.traits.AbstractTrait
//import slimeknights.tconstruct.library.utils.ToolHelper
//
///**
// * @author WireSegal
// * * Created at 7:05 PM on 6/25/16.
// */
//class TraitSoulbonded : AbstractTrait("soulbonded", TinkersIntegration.SOULROOT_COLORS[0]) {
//
//    override fun onUpdate(tool: ItemStack, world: World?, entity: Entity, itemSlot: Int, isSelected: Boolean) {
//
//        val skylight = (entity.world.getLightFor(EnumSkyBlock.SKY, entity.position) * entity.world.provider.getSunBrightnessFactor(1F)).toInt()
//        val blocklight = entity.world.getLightFor(EnumSkyBlock.BLOCK, entity.position)
//        val light = Math.max(skylight, blocklight)
//
//        if (Blocks.SAPLING.canPlaceBlockAt(world, entity.position) && light > 9) {
//            val chance = 15
//            if (!world!!.isRemote && entity is EntityLivingBase && random.nextInt(20 * chance) == 0) {
//                ToolHelper.healTool(tool, 1, entity as EntityLivingBase?)
//            }
//        }
//    }
//}
//
