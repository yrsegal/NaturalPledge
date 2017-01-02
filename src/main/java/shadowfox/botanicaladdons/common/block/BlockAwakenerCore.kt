package shadowfox.botanicaladdons.common.block

import com.teamwizardry.librarianlib.common.base.block.BlockMod
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
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import shadowfox.botanicaladdons.common.BotanicalAddons
import shadowfox.botanicaladdons.common.events.AwakeningEventHandler
import shadowfox.botanicaladdons.common.lexicon.LexiconEntries
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.lexicon.multiblock.Multiblock
import vazkii.botania.api.lexicon.multiblock.MultiblockSet
import vazkii.botania.api.lexicon.multiblock.component.MultiblockComponent
import vazkii.botania.api.state.BotaniaStateProps
import vazkii.botania.api.state.enums.PylonVariant
import vazkii.botania.common.Botania
import vazkii.botania.common.core.helper.Vector3
import java.util.*
import vazkii.botania.common.block.ModBlocks as BotaniaBlocks

/**
 * @author WireSegal
 * Created at 2:46 PM on 4/17/16.
 */
class BlockAwakenerCore(name: String) : BlockMod(name, Material.IRON), ILexiconable {

    companion object {
        val AABB = AxisAlignedBB(0.25, 0.25, 0.25, 0.75, 0.75, 0.75)

        val PYLON_LOCATIONS = arrayOf(BlockPos(4, 1, 4), BlockPos(4, 1, -4), BlockPos(-4, 1, 4), BlockPos(-4, 1, -4))

        private fun makeMultiblockSet(): MultiblockSet {
            val mb = Multiblock()

            for (p in AwakeningEventHandler.CORE_LOCATIONS) mb.addComponent(p.up(), ModBlocks.awakenerCore.defaultState)
            for (p in PYLON_LOCATIONS) mb.addComponent(p.up(), BotaniaBlocks.pylon.defaultState.withProperty(BotaniaStateProps.PYLON_VARIANT, PylonVariant.GAIA))
            for (i in 0..2) for (j in 0..2) mb.addComponent(BeaconComponent(BlockPos(i - 1, 0, j - 1)));
            mb.addComponent(BeaconBeamComponent(BlockPos(0, 1, 0)))
            mb.setRenderOffset(BlockPos(0, -1, 0))

            return mb.makeSet()
        }

        val multiblock: MultiblockSet by lazy {
            makeMultiblockSet()
        }

        private class BeaconBeamComponent(relPos: BlockPos) : MultiblockComponent(relPos, Blocks.BEACON.defaultState) {

            override fun matches(world: World, pos: BlockPos): Boolean {
                return world.getTileEntity(pos) is TileEntityBeacon
            }
        }

        private class BeaconComponent(relPos: BlockPos) : MultiblockComponent(relPos, Blocks.IRON_BLOCK.defaultState) {

            override fun matches(world: World, pos: BlockPos): Boolean {
                return world.getBlockState(pos).block.isBeaconBase(world, pos, pos.add(BlockPos(-relPos.x, -relPos.y, -relPos.z)))
            }
        }
    }

    init {
        AwakeningEventHandler.register()

        setHardness(5F)
        setResistance(10F)
        soundType = SoundType.METAL
        setLightLevel(0.5F)
    }

    @SideOnly(Side.CLIENT)
    override fun randomDisplayTick(state: IBlockState, world: World, pos: BlockPos, rand: Random) {
        val color = BotanicalAddons.PROXY.rainbow(pos, 0.4f)
        val colorBright = color.brighter().rgb
        val colorDark = color.darker().rgb
        val origVector = Vector3(pos.x + 0.5, pos.y + 0.5, pos.z + 0.5)
        val endVector = origVector.add(rand.nextDouble() * 2.0 - 1.0, rand.nextDouble() * 2.0 - 1.0, rand.nextDouble() * 2.0 - 1.0)
        Botania.proxy.lightningFX(origVector, endVector, 5.0f, colorDark, colorBright)
    }

    override fun getEnchantPowerBonus(world: World?, pos: BlockPos?) = 15f

    override fun getBlockRarity(stack: ItemStack) = BotaniaAPI.rarityRelic

    override fun getBoundingBox(state: IBlockState?, source: IBlockAccess?, pos: BlockPos?) = AABB

    override fun isOpaqueCube(state: IBlockState?) = false
    override fun isFullCube(state: IBlockState?) = false

    override fun getEntry(p0: World?, p1: BlockPos?, p2: EntityPlayer?, p3: ItemStack?) = LexiconEntries.awakening
}

