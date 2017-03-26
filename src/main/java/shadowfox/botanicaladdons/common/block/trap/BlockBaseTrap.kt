package shadowfox.botanicaladdons.common.block.trap

import baubles.api.BaublesApi
import baubles.api.BaubleType
import com.teamwizardry.librarianlib.LibrarianLib
import com.teamwizardry.librarianlib.client.core.JsonGenerationUtils
import com.teamwizardry.librarianlib.client.core.ModelHandler
import com.teamwizardry.librarianlib.common.base.IModelGenerator
import com.teamwizardry.librarianlib.common.base.block.BlockMod
import com.teamwizardry.librarianlib.common.base.item.IModItemProvider
import com.teamwizardry.librarianlib.common.util.builders.json
import net.minecraft.block.Block
import net.minecraft.block.SoundType
import net.minecraft.block.properties.IProperty
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumBlockRenderType
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import shadowfox.botanicaladdons.common.block.ModMaterials
import shadowfox.botanicaladdons.common.items.ModItems
import shadowfox.botanicaladdons.common.lexicon.LexiconEntries
import shadowfox.botanicaladdons.common.potions.ModPotions
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.lexicon.LexiconEntry
import vazkii.botania.common.Botania
import java.util.*

/**
 * @author WireSegal
 * Created at 4:46 PM on 3/25/17.
 */
abstract class BlockBaseTrap(name: String) : BlockMod(name, ModMaterials.TRANSPARENT), IModelGenerator, ILexiconable {
    companion object {
        val TRIPPED: PropertyBool = PropertyBool.create("tripped")

        val COLOR = 0xFFB43F
        val R = 0xFF / 255f
        val G = 0xB4 / 255f
        val B = 0x3F / 255f
    }

    private val AABB = AxisAlignedBB(0.25, 0.25, 0.25, 0.75, 0.75, 0.75)


    init {
        soundType = SoundType.CLOTH
        blockHardness = 0.5f
        defaultState = defaultState.withProperty(TRIPPED, false)
    }

    override val ignoredProperties: Array<IProperty<*>>?
        get() = arrayOf(TRIPPED)

    override fun getBoundingBox(state: IBlockState?, world: IBlockAccess?, pos: BlockPos?) = AABB
    override fun getRenderType(state: IBlockState?) = EnumBlockRenderType.INVISIBLE
    override fun isOpaqueCube(state: IBlockState?) = false
    override fun isFullCube(state: IBlockState?) = false
    override fun isPassable(world: IBlockAccess?, pos: BlockPos?) = true
    override fun getCollisionBoundingBox(blockState: IBlockState?, worldIn: IBlockAccess?, pos: BlockPos?) = NULL_AABB
    override fun canSpawnInBlock(): Boolean = true

    override fun onEntityCollidedWithBlock(worldIn: World, pos: BlockPos, state: IBlockState, entityIn: Entity) {
        if (state.getValue(TRIPPED)) return
        if (entityIn is EntityLivingBase && !worldIn.isRemote) {
            if (entityIn is EntityPlayer) {
                val baubles = BaublesApi.getBaublesHandler(entityIn)
                val stack = baubles.getStackInSlot(BaubleType.BODY.validSlots[0])
                if (stack.item == ModItems.cloak && stack.itemDamage == 4)
                    return
            }

            worldIn.setBlockState(pos, state.withProperty(TRIPPED, true))
            worldIn.scheduleUpdate(pos, this, 20)
            trapActivation(state, worldIn, pos, entityIn)
        }
    }

    override fun getEntry(p0: World?, p1: BlockPos?, p2: EntityPlayer?, p3: ItemStack?): LexiconEntry {
        return LexiconEntries.traps
    }

    override fun createBlockState() = BlockStateContainer(this, TRIPPED)
    override fun getStateFromMeta(meta: Int): IBlockState = defaultState.withProperty(TRIPPED, (meta and 1) != 0)
    override fun getMetaFromState(state: IBlockState) = if (state.getValue(TRIPPED)) 1 else 0

    override fun updateTick(worldIn: World, pos: BlockPos, state: IBlockState, rand: Random) {
        val aabb = getBoundingBox(state, worldIn, pos).offset(pos)
        val entities = worldIn.getEntitiesWithinAABB(EntityLivingBase::class.java, aabb)
        if (entities.isEmpty())
            worldIn.setBlockState(pos, state.withProperty(TRIPPED, false))
        else
            worldIn.scheduleUpdate(pos, this, 20)
    }

    abstract fun trapActivation(stateIn: IBlockState, world: World, pos: BlockPos, entityIn: EntityLivingBase)
    abstract fun particlesForSeer(stateIn: IBlockState, world: World, pos: BlockPos, rand: Random)

    override fun randomDisplayTick(stateIn: IBlockState, world: World, pos: BlockPos, rand: Random) {
        if (ModPotions.trapSeer.hasEffect(LibrarianLib.PROXY.getClientPlayer()))
            particlesForSeer(stateIn, world, pos, rand)
        else if (rand.nextFloat() < 0.5f) {
            Botania.proxy.sparkleFX(
                    pos.x + 0.5 + rand.nextDouble() * 0.25,
                    pos.y + 0.5 + rand.nextDouble() * 0.25,
                    pos.z + 0.5 + rand.nextDouble() * 0.25,
                    R, G, B, 0.5f, 1)
        }
    }

    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand?, facing: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        if (playerIn.isSneaking && ModPotions.trapSeer.hasEffect(playerIn)) {
            dropBlockAsItem(worldIn, pos, state, 10)
            worldIn.setBlockToAir(pos)
            return true
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ)
    }

    override fun quantityDropped(state: IBlockState, fortune: Int, random: Random) = Math.min(fortune / 10, 1)

    override fun generateMissingBlockstate(mapper: ((Block) -> Map<IBlockState, ModelResourceLocation>)?): Boolean {
        ModelHandler.generateBlockJson(this, {
            JsonGenerationUtils.generateBlockStates(this, mapper) {
                json {
                    obj(
                            "model" to "botanicaladdons:empty"
                    )
                }
            }
        }, { mapOf() })
        return true
    }

    override fun generateMissingItem(variant: String): Boolean {
        val item = itemForm
        if (item is IModItemProvider)
            ModelHandler.generateItemJson(item) {
                mapOf(JsonGenerationUtils.getPathForItemModel(item, variant)
                        to JsonGenerationUtils.generateRegularItemModel(item, variant))
            }
        return true
    }
}
