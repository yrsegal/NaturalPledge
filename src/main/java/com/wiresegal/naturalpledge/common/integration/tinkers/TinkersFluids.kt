//package com.wiresegal.naturalpledge.common.integration.tinkers
//
//import net.minecraft.block.Block
//import net.minecraft.util.ResourceLocation
//import net.minecraft.util.text.TextFormatting
//import net.minecraftforge.fluids.Fluid
//import net.minecraftforge.fluids.FluidRegistry
//import net.minecraftforge.fml.common.registry.GameRegistry
//import net.minecraftforge.fml.common.registry.IForgeRegistryEntry
//import com.wiresegal.naturalpledge.api.lib.LibMisc
//import slimeknights.tconstruct.library.Util
//import slimeknights.tconstruct.library.fluid.FluidMolten
//import slimeknights.tconstruct.smeltery.block.BlockMolten
//import vazkii.botania.client.lib.LibResources
//import java.util.*
//
///**
// * @author WireSegal
// * Created at 8:51 AM on 6/25/16.
// */
//object TinkersFluids {
//
//    val thundersteel = FluidMolten("${LibMisc.MOD_ID}_thundersteel", TinkersIntegration.THUNDERSTEEL_FLUID_COLOR)
//    val manasteel = FluidMolten("${LibMisc.MOD_ID}_manasteel", Util.enumChatFormattingToColor(TextFormatting.AQUA))
//    val terrasteel = FluidMolten("${LibMisc.MOD_ID}_terrasteel", Util.enumChatFormattingToColor(TextFormatting.GREEN))
//    val elementium = FluidMolten("${LibMisc.MOD_ID}_elementium", Util.enumChatFormattingToColor(TextFormatting.LIGHT_PURPLE))
//
//    init {
//        thundersteel.temperature = 769
//        manasteel.temperature = 769
//        terrasteel.temperature = 1000
//        elementium.temperature = 769
//
//        registerFluid(thundersteel)
//        registerFluid(manasteel)
//        registerFluid(terrasteel)
//        registerFluid(elementium)
//    }
//
//    private fun registerFluid(fluid: Fluid) {
//        FluidRegistry.registerFluid(fluid)
//        FluidRegistry.addBucketForFluid(fluid)
//        registerMoltenBlock(fluid)
//    }
//
//    private fun registerMoltenBlock(fluid: Fluid): BlockMolten {
//        val block = BlockMolten(fluid)
//        return registerBlock(block, "molten_" + fluid.name) // molten_foobar prefix
//    }
//
//    private fun <T : Block> registerBlock(block: T, name: String): T {
//        if (name != name.toLowerCase(Locale.US)) {
//            throw IllegalArgumentException(String.format("Unlocalized names need to be all lowercase! Block: %s", name))
//        }
//
//        val prefixedName = LibResources.PREFIX_MOD + name
//        block.unlocalizedName = prefixedName
//
//        register(block, name)
//        return block
//    }
//
//    private fun <T : IForgeRegistryEntry<*>> register(thing: T, name: String): T {
//        thing.registryName = ResourceLocation(LibMisc.MOD_ID, name)
//        GameRegistry.register(thing)
//        return thing
//    }
//}
