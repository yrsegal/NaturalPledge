//package shadowfox.botanicaladdons.common.integration.tinkers
//
//import net.minecraft.item.ItemStack
//import net.minecraftforge.fml.relauncher.FMLLaunchHandler
//import shadowfox.botanicaladdons.api.lib.LibMisc
//import shadowfox.botanicaladdons.common.integration.tinkers.TinkersIntegration.AQUAMARINE_COLORS
//import shadowfox.botanicaladdons.common.integration.tinkers.TinkersIntegration.DREAMWOOD_COLOR
//import shadowfox.botanicaladdons.common.integration.tinkers.TinkersIntegration.ELEMENTIUM_COLOR
//import shadowfox.botanicaladdons.common.integration.tinkers.TinkersIntegration.LIVINGROCK_COLOR
//import shadowfox.botanicaladdons.common.integration.tinkers.TinkersIntegration.LIVINGWOOD_COLOR
//import shadowfox.botanicaladdons.common.integration.tinkers.TinkersIntegration.MANASTEEL_COLOR
//import shadowfox.botanicaladdons.common.integration.tinkers.TinkersIntegration.SOULROOT_COLORS
//import shadowfox.botanicaladdons.common.integration.tinkers.TinkersIntegration.TERRASTEEL_COLOR
//import shadowfox.botanicaladdons.common.integration.tinkers.TinkersIntegration.THUNDERSTEEL_COLOR
//import shadowfox.botanicaladdons.common.integration.tinkers.traits.*
//import shadowfox.botanicaladdons.common.items.ItemResource
//import shadowfox.botanicaladdons.common.items.ItemResource.Variants.*
//import shadowfox.botanicaladdons.common.lib.LibOreDict
//import slimeknights.tconstruct.TinkerIntegration
//import slimeknights.tconstruct.library.TinkerRegistry
//import slimeknights.tconstruct.library.client.MaterialRenderInfo
//import slimeknights.tconstruct.library.client.material.MaterialRenderInfoLoader.addRenderInfo
//import slimeknights.tconstruct.library.materials.*
//import slimeknights.tconstruct.library.traits.AbstractTrait
//import slimeknights.tconstruct.library.utils.HarvestLevels.*
//import slimeknights.tconstruct.tools.TinkerMaterials
//import vazkii.botania.common.block.ModBlocks
//import vazkii.botania.common.item.ModItems
//import vazkii.botania.common.lib.LibOreDict as BotaniaOreDict
//
///**
// * @author WireSegal
// * Created at 8:51 AM on 6/25/16.
// */
//object TinkersMaterials {
//
//    val thundersteel = mat("thundersteel", THUNDERSTEEL_COLOR, true)
//    val aquamarine = mat("aquamarine", AQUAMARINE_COLORS[1], true)
//    val manasteel = mat("manasteel", MANASTEEL_COLOR, false)
//    val terrasteel = mat("terrasteel", TERRASTEEL_COLOR, false)
//    val elementium = mat("elementium", ELEMENTIUM_COLOR, false)
//
//    val soulroot = mat("soulroot", SOULROOT_COLORS[0], true)
//    val livingwood = mat("livingwood", LIVINGWOOD_COLOR, false)
//    val dreamwood = mat("dreamwood", DREAMWOOD_COLOR, false)
//
//    val livingrock = mat("livingrock", LIVINGROCK_COLOR, false)
//
//    val thunderclast = TraitThunderclast()
//    val sparking = TraitSparking()
//
//    val storming = TraitStorming()
//    val aerodynamic = TraitAerodynamic()
//
//    val soulbonded = TraitSoulbonded()
//
//    val arcane: AbstractTrait = TraitArcane()
//
//    val worldforged: AbstractTrait = TraitWorldforged()
//    val unyielding: AbstractTrait = TraitUnyielding()
//
//    val elsetouched: AbstractTrait = TraitElsetouched()
//    val alfwrought: AbstractTrait = TraitAlfwrought()
//
//    val regrowth: AbstractTrait = TraitRegrowth()
//
//    val idealistic: AbstractTrait = TraitIdealistic()
//    val stargazer: AbstractTrait = TraitStargazer()
//
//    val elemental: AbstractTrait = TraitElemental()
//    val enduring: AbstractTrait = TraitEnduring()
//
//    init {
//        if (TinkersProxy.loadNpTinkers) {
//            thundersteel.addItem(LibOreDict.THUNDERSTEEL, 1, Material.VALUE_Ingot)
//            thundersteel.representativeItem = ItemResource.of(THUNDER_STEEL)
//            thundersteel.addTrait(thunderclast, MaterialTypes.HEAD)
//            thundersteel.addTrait(sparking)
//            TinkerRegistry.addMaterialStats(thundersteel,
//                    HeadMaterialStats(204, 6.00f, 4.00f, OBSIDIAN),
//                    HandleMaterialStats(0.85f, 60),
//                    ExtraMaterialStats(50))
//
//            aquamarine.isCraftable = true
//            aquamarine.addItem(LibOreDict.AQUAMARINE, 1, Material.VALUE_Ingot)
//            aquamarine.addItem(LibOreDict.BLOCK_AQUAMARINE, 1, Material.VALUE_Block)
//            aquamarine.representativeItem = ItemResource.of(AQUAMARINE)
//            aquamarine.addTrait(storming, MaterialTypes.HEAD)
//            aquamarine.addTrait(aerodynamic)
//            TinkerRegistry.addMaterialStats(aquamarine,
//                    HeadMaterialStats(60, 4.00f, 2.90f, IRON),
//                    HandleMaterialStats(0.50f, 0),
//                    ExtraMaterialStats(0))
//
//            soulroot.isCraftable = true
//            soulroot.addItem(LibOreDict.LIFE_ROOT, 1, Material.VALUE_Ingot)
//            soulroot.representativeItem = ItemResource.of(LIFE_ROOT)
//            soulroot.addTrait(soulbonded)
//            TinkerRegistry.addMaterialStats(soulroot,
//                    HeadMaterialStats(35, 2.00f, 2.00f, STONE),
//                    HandleMaterialStats(1.00f, 10),
//                    ExtraMaterialStats(10))
//        }
//
//        if (TinkersProxy.loadBotaniaTinkers) {
//            manasteel.addItem(BotaniaOreDict.MANA_STEEL, 1, Material.VALUE_Ingot)
//            manasteel.representativeItem = ItemStack(ModItems.manaResource, 1, 0)
//            manasteel.addTrait(arcane)
//            TinkerRegistry.addMaterialStats(manasteel,
//                    HeadMaterialStats(204, 6.00f, 4.00f, OBSIDIAN),
//                    HandleMaterialStats(0.85f, 60),
//                    ExtraMaterialStats(50))
//
//            terrasteel.addItem(BotaniaOreDict.TERRA_STEEL, 1, Material.VALUE_Ingot)
//            terrasteel.representativeItem = ItemStack(ModItems.manaResource, 1, 4)
//            terrasteel.addTrait(worldforged, MaterialTypes.HEAD)
//            terrasteel.addTrait(unyielding)
//            TinkerRegistry.addMaterialStats(terrasteel,
//                    HeadMaterialStats(540, 9.0f, 6.00f, COBALT),
//                    HandleMaterialStats(0.9f, 150),
//                    ExtraMaterialStats(25))
//
//            elementium.addItem(BotaniaOreDict.ELEMENTIUM, 1, Material.VALUE_Ingot)
//            elementium.representativeItem = ItemStack(ModItems.manaResource, 1, 7)
//            elementium.addTrait(elsetouched, MaterialTypes.HEAD)
//            elementium.addTrait(alfwrought)
//            TinkerRegistry.addMaterialStats(elementium,
//                    HeadMaterialStats(204, 6.00f, 4.00f, OBSIDIAN),
//                    HandleMaterialStats(0.85f, 60),
//                    ExtraMaterialStats(50))
//
//            livingwood.isCraftable = true
//            livingwood.addItem(ItemStack(ModBlocks.livingwood, 1, 1), 1, Material.VALUE_Fragment)
//            livingwood.addItem(BotaniaOreDict.LIVING_WOOD, 1, Material.VALUE_Ingot)
//            livingwood.addItem(BotaniaOreDict.LIVINGWOOD_TWIG, 1, Material.VALUE_Ingot * 2)
//            livingwood.representativeItem = ItemStack(ModItems.manaResource, 1, 3)
//            livingwood.addTrait(regrowth)
//            TinkerRegistry.addMaterialStats(livingwood,
//                    HeadMaterialStats(15, 2.00f, 2.00f, STONE),
//                    HandleMaterialStats(1.00f, 0),
//                    ExtraMaterialStats(0))
//
//            dreamwood.isCraftable = true
//            dreamwood.addItem(ItemStack(ModBlocks.dreamwood, 1, 1), 1, Material.VALUE_Fragment)
//            dreamwood.addItem(BotaniaOreDict.DREAM_WOOD, 1, Material.VALUE_Ingot)
//            dreamwood.addItem(BotaniaOreDict.DREAMWOOD_TWIG, 1, Material.VALUE_Ingot * 2)
//            dreamwood.representativeItem = ItemStack(ModItems.manaResource, 1, 13)
//            dreamwood.addTrait(idealistic, MaterialTypes.HEAD)
//            dreamwood.addTrait(stargazer)
//            TinkerRegistry.addMaterialStats(dreamwood,
//                    HeadMaterialStats(15, 2.00f, 2.00f, STONE),
//                    HandleMaterialStats(1.00f, 0),
//                    ExtraMaterialStats(0))
//
//            livingrock.isCraftable = true
//            livingrock.addItem(BotaniaOreDict.LIVING_ROCK, 1, Material.VALUE_Ingot)
//            livingrock.representativeItem = ItemStack(ModBlocks.livingrock)
//            livingrock.addTrait(elemental, MaterialTypes.HEAD)
//            livingrock.addTrait(enduring)
//            TinkerRegistry.addMaterialStats(livingrock,
//                    HeadMaterialStats(60, 4.00f, 2.90f, IRON),
//                    HandleMaterialStats(0.50f, 0),
//                    ExtraMaterialStats(0))
//        }
//
//        if (FMLLaunchHandler.side().isClient) addRenderInfo()
//
//        if (TinkersProxy.loadNpTinkers) {
//            TinkerIntegration.integrate(thundersteel, TinkersFluids.thundersteel, LibOreDict.THUNDERSTEEL.replace("ingot", "")).toolforge().integrate()
//            TinkerIntegration.integrate(aquamarine).integrate()
//            TinkerIntegration.integrate(soulroot).integrate()
//        }
//
//        if (TinkersProxy.loadBotaniaTinkers) {
//            TinkerIntegration.integrate(manasteel, TinkersFluids.manasteel, BotaniaOreDict.MANA_STEEL.replace("ingot", "")).toolforge().integrate()
//            TinkerIntegration.integrate(terrasteel, TinkersFluids.terrasteel, BotaniaOreDict.TERRA_STEEL.replace("ingot", "")).toolforge().integrate()
//            TinkerIntegration.integrate(elementium, TinkersFluids.elementium, BotaniaOreDict.ELEMENTIUM.replace("ingot", "")).toolforge().integrate()
//            TinkerIntegration.integrate(livingwood).integrate()
//            TinkerIntegration.integrate(dreamwood).integrate()
//            TinkerIntegration.integrate(livingrock).integrate()
//        }
//    }
//
//    fun addRenderInfo() {
//        if (TinkersProxy.loadNpTinkers) {
//            thundersteel.setRenderInfo(MaterialRenderInfo.Metal(thundersteel.materialTextColor, 0.25f, 0.5f, -0.1f))
//            aquamarine.setRenderInfo(MaterialRenderInfo.BlockTexture("${LibMisc.MOD_ID}:block/aquamarineBlock"))
//            soulroot.setRenderInfo(MaterialRenderInfo.MultiColor(SOULROOT_COLORS[0], SOULROOT_COLORS[1], SOULROOT_COLORS[2]))
//        }
//
//        if (TinkersProxy.loadBotaniaTinkers) {
//            manasteel.setRenderInfo(MaterialRenderInfo.Metal(manasteel.materialTextColor, 0.25f, 0.5f, -0.1f))
//            terrasteel.setRenderInfo(MaterialRenderInfo.Metal(terrasteel.materialTextColor, 0.25f, 0.5f, -0.1f))
//            elementium.setRenderInfo(MaterialRenderInfo.Metal(elementium.materialTextColor, 0.25f, 0.5f, -0.1f))
//            livingwood.setRenderInfo(livingwood.materialTextColor)
//            dreamwood.setRenderInfo(dreamwood.materialTextColor)
//            livingrock.setRenderInfo(livingrock.materialTextColor)
//        }
//    }
//
//    private fun mat(name: String, color: Int, natPledge: Boolean): Material {
//        val mat = Material(name, color)
//        if ((natPledge && TinkersProxy.loadNpTinkers) || (!natPledge && TinkersProxy.loadBotaniaTinkers))
//            TinkerMaterials.materials.add(mat)
//        return mat
//    }
//
//}
