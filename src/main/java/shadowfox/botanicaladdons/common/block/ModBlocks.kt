package shadowfox.botanicaladdons.common.block

import com.teamwizardry.librarianlib.features.base.block.BlockMod
import com.teamwizardry.librarianlib.features.base.block.BlockModPane
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.oredict.OreDictionary
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.common.block.alt.BlockAltLeaves
import shadowfox.botanicaladdons.common.block.alt.BlockAltLog
import shadowfox.botanicaladdons.common.block.alt.BlockAltPlanks
import shadowfox.botanicaladdons.common.block.colored.*
import shadowfox.botanicaladdons.common.block.colored.BlockIridescentPlanks.BlockAuroraPlanks
import shadowfox.botanicaladdons.common.block.colored.BlockIridescentPlanks.BlockRainbowPlanks
import shadowfox.botanicaladdons.common.block.dendrics.calico.*
import shadowfox.botanicaladdons.common.block.dendrics.circuit.BlockCircuitLeaves
import shadowfox.botanicaladdons.common.block.dendrics.circuit.BlockCircuitLog
import shadowfox.botanicaladdons.common.block.dendrics.circuit.BlockCircuitPlanks
import shadowfox.botanicaladdons.common.block.dendrics.circuit.BlockCircuitSapling
import shadowfox.botanicaladdons.common.block.dendrics.sealing.*
import shadowfox.botanicaladdons.common.block.dendrics.thunder.*
import shadowfox.botanicaladdons.common.block.tile.TileCracklingStar
import shadowfox.botanicaladdons.common.block.tile.TileLivingwoodFunnel
import shadowfox.botanicaladdons.common.block.tile.TilePrismFlame
import shadowfox.botanicaladdons.common.block.tile.TileStar
import shadowfox.botanicaladdons.common.block.trap.*
import shadowfox.botanicaladdons.common.lib.LibNames
import shadowfox.botanicaladdons.common.lib.LibOreDict
import vazkii.botania.api.state.enums.StorageVariant
import vazkii.botania.common.block.ModBlocks as BotaniaBlocks

/**
 * @author WireSegal
 * Created at 2:46 PM on 4/17/16.
 */
object ModBlocks {
    val awakenerCore = BlockAwakenerCore(LibNames.AWAKENER)
    val star: BlockMod
    val flame: BlockMod
    val irisDirt: BlockMod
    val rainbowDirt: BlockMod
    val auroraDirt: BlockMod
    val irisPlanks: BlockMod
    val irisLogs: Array<BlockIridescentLog>
    val rainbowPlanks: BlockMod
    val auroraPlanks: BlockMod
    val rainbowLog: BlockMod
    val auroraLog: BlockMod
    val irisLeaves: Array<BlockIridescentLeaves>
    val rainbowLeaves: BlockMod
    val auroraLeaves: BlockMod
    val irisSapling: BlockMod
    val altLogs: Array<BlockAltLog>
    val altLeaves: Array<BlockAltLeaves>
    val altPlanks: BlockMod
    val storage: BlockMod
    val irisLamp: BlockMod

    val sealSapling: BlockMod
    val sealPlanks: BlockMod
    val sealLeaves: BlockMod
    val sealLog: BlockMod

    val amp: BlockMod

    val thunderSapling: BlockMod
    val thunderPlanks: BlockMod
    val thunderLeaves: BlockMod
    val thunderLog: BlockMod

    val circuitSapling: BlockMod
    val circuitPlanks: BlockMod
    val circuitLeaves: BlockMod
    val circuitLog: BlockMod

    val calicoSapling: BlockMod
    val calicoPlanks: BlockMod
    val calicoLeaves: BlockMod
    val calicoLog: BlockMod

    val aquaGlass: BlockMod
    val aquaPane: BlockModPane

    val funnel: BlockMod

    val thunderTrap: BlockMod
    val cracklingStar: BlockMod

    val corporeaResonator: BlockMod
    val enderActuator: BlockMod

    val disorientTrap: BlockMod
    val infernoTrap: BlockMod
    val launchTrap: BlockMod
    val rootTrap: BlockMod
    val sandTrap: BlockMod
    val signalTrap: BlockMod
    val wrathTrap: BlockMod

//    val gayBeacon: BlockMod

    init {
        star = BlockFrozenStar(LibNames.STAR)
        flame = BlockPrismFlame(LibNames.PRISM_FLAME)
        irisDirt = BlockIridescentDirt(LibNames.IRIS_DIRT)
        rainbowDirt = BlockRainbowDirt(LibNames.RAINBOW_DIRT)
        irisPlanks = BlockIridescentPlanks(LibNames.IRIS_PLANKS)
        irisLogs = Array(4) {
            object : BlockIridescentLog(LibNames.IRIS_LOG, it) {
                override val colorSet: Int
                    get() = it
            }
        }
        rainbowPlanks = BlockRainbowPlanks(LibNames.RAINBOW_PLANKS)
        rainbowLog = BlockRainbowLog(LibNames.RAINBOW_LOG)
        irisLeaves = Array(4) {
            object : BlockIridescentLeaves(LibNames.IRIS_LEAVES, it) {
                override val colorSet: Int
                    get() = it
            }
        }
        rainbowLeaves = BlockRainbowLeaves(LibNames.RAINBOW_LEAVES)

        auroraDirt = BlockAuroraDirt(LibNames.AURORA_DIRT)
        auroraPlanks = BlockAuroraPlanks(LibNames.AURORA_PLANKS)
        auroraLog = BlockAuroraLog(LibNames.AURORA_LOG)
        auroraLeaves = BlockAuroraLeaves(LibNames.AURORA_LEAVES)

        irisSapling = BlockIrisSapling(LibNames.IRIS_SAPLING)
        altLogs = Array(2) {
            object : BlockAltLog(LibNames.ALT_LOG, it) {
                override val colorSet: Int
                    get() = it
            }
        }
        altLeaves = Array(2) {
            object : BlockAltLeaves(LibNames.ALT_LEAVES, it) {
                override val colorSet: Int
                    get() = it
            }
        }
        altPlanks = BlockAltPlanks(LibNames.ALT_PLANKS)
        storage = BlockStorage(LibNames.STORAGE)
        irisLamp = BlockColoredLamp(LibNames.IRIS_LAMP)

        SoundSealEventHandler
        sealSapling = BlockSealSapling(LibNames.SEAL_SAPLING)
        sealPlanks = BlockSealPlanks(LibNames.SEAL_PLANKS)
        sealLeaves = BlockSealLeaves(LibNames.SEAL_LEAVES)
        sealLog = BlockSealingLog(LibNames.SEAL_LOG)

        amp = BlockAmplifier(LibNames.AMPLIFIER)

        ThunderEventHandler
        thunderSapling = BlockThunderSapling(LibNames.THUNDER_SAPLING)
        thunderPlanks = BlockThunderPlanks(LibNames.THUNDER_PLANKS)
        thunderLeaves = BlockThunderLeaves(LibNames.THUNDER_LEAVES)
        thunderLog = BlockThunderLog(LibNames.THUNDER_LOG)

        circuitSapling = BlockCircuitSapling(LibNames.CIRCUIT_SAPLING)
        circuitPlanks = BlockCircuitPlanks(LibNames.CIRCUIT_PLANKS)
        circuitLeaves = BlockCircuitLeaves(LibNames.CIRCUIT_LEAVES)
        circuitLog = BlockCircuitLog(LibNames.CIRCUIT_LOG)

        CalicoEventHandler
        calicoSapling = BlockCalicoSapling(LibNames.CALICO_SAPLING)
        calicoPlanks = BlockCalicoPlanks(LibNames.CALICO_PLANKS)
        calicoLeaves = BlockCalicoLeaves(LibNames.CALICO_LEAVES)
        calicoLog = BlockCalicoLog(LibNames.CALICO_LOG)

        aquaGlass = BlockAquamarineGlass(LibNames.AQUA_GLASS)
        aquaPane = BlockAquamarinePane(LibNames.AQUA_PANE)

        funnel = BlockFunnel(LibNames.FUNNEL)

        thunderTrap = BlockThunderTrap(LibNames.TRAP)
        cracklingStar = BlockCracklingStar(LibNames.CRACKLING)

        corporeaResonator = BlockCorporeaResonator(LibNames.CORPOREA_RESONATOR)
        enderActuator = BlockEnderBind(LibNames.ENDER_ACTUATOR)

        disorientTrap = BlockDisorientationTrap()
        infernoTrap = BlockInfernoTrap()
        launchTrap = BlockLaunchTrap()
        rootTrap = BlockRootTrap()
        sandTrap = BlockSandTrap()
        signalTrap = BlockSignalTrap()
        wrathTrap = BlockWrathTrap()

//        gayBeacon = BlockGayBeacon()

        GameRegistry.registerTileEntity(TileStar::class.java, ResourceLocation(LibMisc.MOD_ID, LibNames.STAR).toString())
        GameRegistry.registerTileEntity(TileCracklingStar::class.java, ResourceLocation(LibMisc.MOD_ID, LibNames.CRACKLING).toString())
        GameRegistry.registerTileEntity(TilePrismFlame::class.java, ResourceLocation(LibMisc.MOD_ID, LibNames.PRISM_FLAME).toString())
        GameRegistry.registerTileEntity(TileLivingwoodFunnel::class.java, ResourceLocation(LibMisc.MOD_ID, LibNames.FUNNEL).toString())

        OreDictionary.registerOre(LibOreDict.BLOCK_AQUAMARINE, ItemStack(storage, 1, BlockStorage.Variants.AQUAMARINE.ordinal))
        OreDictionary.registerOre(LibOreDict.BLOCK_THUNDERSTEEL, ItemStack(storage, 1, BlockStorage.Variants.THUNDERSTEEL.ordinal))

        OreDictionary.registerOre(LibOreDict.THUNDER_PLANKS, ItemStack(thunderPlanks, 1, OreDictionary.WILDCARD_VALUE))
        OreDictionary.registerOre(LibOreDict.SEAL_PLANKS, ItemStack(sealPlanks, 1, OreDictionary.WILDCARD_VALUE))
        OreDictionary.registerOre(LibOreDict.CIRCUIT_PLANKS, ItemStack(circuitPlanks, 1, OreDictionary.WILDCARD_VALUE))
        OreDictionary.registerOre(LibOreDict.CALICO_PLANKS, ItemStack(calicoPlanks, 1, OreDictionary.WILDCARD_VALUE))

        OreDictionary.registerOre(LibOreDict.IRIS_DIRT, ItemStack(irisDirt, 1, OreDictionary.WILDCARD_VALUE))
        OreDictionary.registerOre(LibOreDict.IRIS_DIRT, ItemStack(rainbowDirt, 1, OreDictionary.WILDCARD_VALUE))
        OreDictionary.registerOre(LibOreDict.IRIS_DIRT, ItemStack(auroraDirt, 1, OreDictionary.WILDCARD_VALUE))
        OreDictionary.registerOre("dirt", ItemStack(irisDirt, 1, OreDictionary.WILDCARD_VALUE))
        OreDictionary.registerOre("dirt", ItemStack(irisDirt, 1, OreDictionary.WILDCARD_VALUE))
        OreDictionary.registerOre("dirt", ItemStack(auroraDirt, 1, OreDictionary.WILDCARD_VALUE))
        OreDictionary.registerOre("blockGlass", ItemStack(aquaGlass, 1, OreDictionary.WILDCARD_VALUE))

        OreDictionary.registerOre(LibOreDict.BLOCK_MANASTEEL, ItemStack(BotaniaBlocks.storage, 1, StorageVariant.MANASTEEL.ordinal))
        OreDictionary.registerOre(LibOreDict.BLOCK_TERRASTEEL, ItemStack(BotaniaBlocks.storage, 1, StorageVariant.TERRASTEEL.ordinal))
        OreDictionary.registerOre(LibOreDict.BLOCK_ELEMENTIUM, ItemStack(BotaniaBlocks.storage, 1, StorageVariant.ELEMENTIUM.ordinal))

        OreDictionary.registerOre(LibOreDict.DYES[16], BotaniaBlocks.bifrostPerm)
    }
}
