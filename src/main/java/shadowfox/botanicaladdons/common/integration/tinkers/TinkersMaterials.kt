package shadowfox.botanicaladdons.common.integration.tinkers

import net.minecraftforge.fml.relauncher.FMLLaunchHandler
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.common.integration.tinkers.TinkersIntegration.AQUAMARINE_COLORS
import shadowfox.botanicaladdons.common.integration.tinkers.TinkersIntegration.SOULROOT_COLORS
import shadowfox.botanicaladdons.common.integration.tinkers.TinkersIntegration.THUNDERSTEEL_COLOR
import shadowfox.botanicaladdons.common.integration.tinkers.traits.*
import shadowfox.botanicaladdons.common.items.ItemResource
import shadowfox.botanicaladdons.common.items.ItemResource.Variants.*
import shadowfox.botanicaladdons.common.lib.LibOreDict
import slimeknights.tconstruct.TinkerIntegration
import slimeknights.tconstruct.library.TinkerRegistry
import slimeknights.tconstruct.library.client.MaterialRenderInfo
import slimeknights.tconstruct.library.materials.ExtraMaterialStats
import slimeknights.tconstruct.library.materials.HandleMaterialStats
import slimeknights.tconstruct.library.materials.HeadMaterialStats
import slimeknights.tconstruct.library.materials.Material
import slimeknights.tconstruct.library.utils.HarvestLevels.*
import slimeknights.tconstruct.tools.TinkerMaterials

/**
 * @author WireSegal
 * Created at 8:51 AM on 6/25/16.
 */
object TinkersMaterials {

    val thundersteel = mat("thundersteel", THUNDERSTEEL_COLOR)
    val aquamarine = mat("aquamarine", AQUAMARINE_COLORS[1])
    val soulroot = mat("soulroot", SOULROOT_COLORS[0])

    val thunderclast = TraitThunderclast()
    val sparking = TraitSparking()

    val storming = TraitStorming()
    val aerodynamic = TraitAerodynamic()

    val soulbonded = TraitSoulbonded()

    init {
        thundersteel.addItem(LibOreDict.THUNDERSTEEL, 1, Material.VALUE_Ingot)
        thundersteel.representativeItem = ItemResource.of(THUNDER_STEEL)
        thundersteel.addTrait(thunderclast, HeadMaterialStats.TYPE)
        thundersteel.addTrait(sparking)
        TinkerRegistry.addMaterialStats(thundersteel,
                HeadMaterialStats(204, 6.00f, 4.00f, OBSIDIAN),
                HandleMaterialStats(0.85f, 60),
                ExtraMaterialStats(50))

        aquamarine.isCraftable = true
        aquamarine.addItem(LibOreDict.AQUAMARINE, 1, Material.VALUE_Ingot)
        aquamarine.addItem(LibOreDict.BLOCK_AQUAMARINE, 1, Material.VALUE_Block)
        aquamarine.representativeItem = ItemResource.of(AQUAMARINE)
        aquamarine.addTrait(storming, HeadMaterialStats.TYPE)
        aquamarine.addTrait(aerodynamic)
        TinkerRegistry.addMaterialStats(aquamarine,
                HeadMaterialStats(60, 4.00f, 2.90f, IRON),
                HandleMaterialStats(0.50f, 0),
                ExtraMaterialStats(0))

        soulroot.isCraftable = true
        soulroot.addItem(LibOreDict.LIFE_ROOT, 1, Material.VALUE_Ingot)
        soulroot.representativeItem = ItemResource.of(LIFE_ROOT)
        soulroot.addTrait(soulbonded)
        TinkerRegistry.addMaterialStats(soulroot,
                HeadMaterialStats(35, 2.00f, 2.00f, STONE),
                HandleMaterialStats(1.00f, 10),
                ExtraMaterialStats(10))

        if (FMLLaunchHandler.side().isClient) addRenderInfo()

        TinkerIntegration.integrate(thundersteel, TinkersFluids.thundersteel, LibOreDict.THUNDERSTEEL.replace("ingot", "")).toolforge().integrate()
        TinkerIntegration.integrate(aquamarine).integrate()
        TinkerIntegration.integrate(soulroot).integrate()
    }

    fun addRenderInfo() {
        thundersteel.setRenderInfo(MaterialRenderInfo.Metal(thundersteel.materialTextColor, 0.25f, 0.5f, -0.1f))
        aquamarine.setRenderInfo(MaterialRenderInfo.BlockTexture("${LibMisc.MOD_ID}:block/aquamarineBlock"))
        soulroot.setRenderInfo(MaterialRenderInfo.MultiColor(SOULROOT_COLORS[0], SOULROOT_COLORS[1], SOULROOT_COLORS[2]))
    }

    private fun mat(name: String, color: Int): Material {
        val mat = Material(name, color)
        TinkerMaterials.materials.add(mat)
        return mat
    }

}
