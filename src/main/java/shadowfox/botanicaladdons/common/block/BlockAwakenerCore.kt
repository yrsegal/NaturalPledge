package shadowfox.botanicaladdons.common.block

import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntityBeacon
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import shadowfox.botanicaladdons.common.block.base.BlockMod
import shadowfox.botanicaladdons.common.events.AwakeningEventHandler
import shadowfox.botanicaladdons.common.lexicon.LexiconEntries
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.lexicon.multiblock.Multiblock
import vazkii.botania.api.lexicon.multiblock.MultiblockSet
import vazkii.botania.api.lexicon.multiblock.component.MultiblockComponent
import vazkii.botania.api.state.BotaniaStateProps
import vazkii.botania.api.state.enums.PylonVariant
import vazkii.botania.common.block.ModBlocks as BotaniaBlocks

/**
 * @author WireSegal
 * Created at 2:46 PM on 4/17/16.
 */
class BlockAwakenerCore(name: String) : BlockMod(name, Material.iron), ILexiconable {

    companion object {
        val AABB = AxisAlignedBB(3.0 / 16, 3.0 / 16, 3.0 / 16, 13.0 / 16, 13.0 / 16, 13.0 / 16)

        val PYLON_LOCATIONS = arrayOf(BlockPos(4, 1, 4), BlockPos(4, 1, -4), BlockPos(-4, 1, 4), BlockPos(-4, 1, -4))

        fun makeMultiblockSet(): MultiblockSet {
            val mb = Multiblock()

            for (p in AwakeningEventHandler.CORE_LOCATIONS) mb.addComponent(p.up(), ModBlocks.awakenerCore.defaultState)
            for (p in PYLON_LOCATIONS) mb.addComponent(p.up(), BotaniaBlocks.pylon.defaultState.withProperty(BotaniaStateProps.PYLON_VARIANT, PylonVariant.GAIA))
            for (i in 0..2) for (j in 0..2) mb.addComponent(BeaconComponent(BlockPos(i - 1, 0, j - 1)));
            mb.addComponent(BeaconBeamComponent(BlockPos(0, 1, 0)))
            mb.setRenderOffset(BlockPos(0, -1, 0))

            return mb.makeSet()
        }

        lateinit var multiblock: MultiblockSet

        private class BeaconBeamComponent(relPos: BlockPos) : MultiblockComponent(relPos, Blocks.beacon.defaultState) {

            override fun matches(world: World, pos: BlockPos): Boolean {
                return world.getTileEntity(pos) is TileEntityBeacon
            }
        }

        private class BeaconComponent(relPos: BlockPos) : MultiblockComponent(relPos, Blocks.iron_block.defaultState) {

            override fun matches(world: World, pos: BlockPos): Boolean {
                return world.getBlockState(pos).block.isBeaconBase(world, pos, pos.add(BlockPos(-relPos.x, -relPos.y, -relPos.z)))
            }
        }
    }

    init {

        AwakeningEventHandler.register()

        setHardness(5F)
        setResistance(10F)
        setStepSound(SoundType.METAL)
        setLightLevel(0.5F)
    }

    override fun getEnchantPowerBonus(world: World?, pos: BlockPos?) = 15f

    override fun getBlockRarity(stack: ItemStack) = BotaniaAPI.rarityRelic

    override fun getBoundingBox(state: IBlockState?, source: IBlockAccess?, pos: BlockPos?) = AABB
    override fun getCollisionBoundingBox(worldIn: IBlockState?, pos: World?, state: BlockPos?) = AABB
    override fun getSelectedBoundingBox(blockState: IBlockState?, worldIn: World?, pos: BlockPos?) = AABB

    override fun isOpaqueCube(state: IBlockState?) = false
    override fun isFullCube(state: IBlockState?) = false

    override fun getEntry(p0: World?, p1: BlockPos?, p2: EntityPlayer?, p3: ItemStack?) = LexiconEntries.awakening
}

