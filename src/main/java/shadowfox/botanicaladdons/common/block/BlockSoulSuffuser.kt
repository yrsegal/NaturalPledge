package shadowfox.botanicaladdons.common.block

import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.common.IPlantable
import shadowfox.botanicaladdons.api.SpellRegistry
import shadowfox.botanicaladdons.api.lib.LibNames
import shadowfox.botanicaladdons.common.block.base.BlockModContainer
import shadowfox.botanicaladdons.common.block.tile.TileSuffuser
import shadowfox.botanicaladdons.common.items.bauble.faith.Spells
import vazkii.botania.api.wand.IWandHUD
import vazkii.botania.api.wand.IWandable

/**
 * @author WireSegal
 * Created at 3:34 PM on 4/26/16.
 */
class BlockSoulSuffuser(name: String) : BlockModContainer(name, Material.rock, "soulSuffuser", "dendricSuffuser"), IWandHUD, IWandable {
    companion object {
        val PROP_DENDRIC = PropertyBool.create("dendric")

        fun isDendric(state: IBlockState?): Boolean {
            return state != null && state.getValue(PROP_DENDRIC)
        }
    }

    val AABB = AxisAlignedBB(1 / 16.0, 0.0, 1 / 16.0, 15 / 16.0, 1.0, 15 / 16.0)

    init {
        defaultState = makeDefaultState()
        SpellRegistry.registerSpell(LibNames.SPELL_INFUSION, Spells.Infuse())
    }

    private fun makeDefaultState(): IBlockState {
        return blockState.baseState.withProperty(PROP_DENDRIC, false);
    }

    override fun onUsedByWand(p0: EntityPlayer?, p1: ItemStack, p2: World, p3: BlockPos, p4: EnumFacing?): Boolean {
        (p2.getTileEntity(p3) as TileSuffuser).startSuffuser()
        return true
    }

    override fun getMetaFromState(state: IBlockState): Int {
        return if (state.getValue(PROP_DENDRIC)) 1 else 0
    }

    override fun getStateFromMeta(meta: Int): IBlockState? {
        return defaultState.withProperty(PROP_DENDRIC, meta == 1)
    }

    override fun createBlockState(): BlockStateContainer? {
        return BlockStateContainer(this, *arrayOf(PROP_DENDRIC))
    }

    override fun getBoundingBox(state: IBlockState?, source: IBlockAccess?, pos: BlockPos?) = AABB
    override fun isOpaqueCube(state: IBlockState?) = false
    override fun isFullCube(state: IBlockState?) = false

    override fun renderHUD(p0: Minecraft, p1: ScaledResolution, p2: World, p3: BlockPos) {
        (p2.getTileEntity(p3) as TileSuffuser).renderHUD(p0, p1)
    }

    override fun onBlockActivated(worldIn: World?, pos: BlockPos?, state: IBlockState?, par5EntityPlayer: EntityPlayer?, hand: EnumHand?, stack: ItemStack?, side: EnumFacing?, par7: Float, par8: Float, par9: Float): Boolean {
        val altar = worldIn!!.getTileEntity(pos) as TileSuffuser? ?: return false

        if (par5EntityPlayer!!.isSneaking) {
            if (altar.manaToGet == 0)
                for (i in altar.sizeInventory - 1 downTo 0) {
                    val stackAt = altar.getItemHandler().getStackInSlot(i)
                    if (stackAt != null) {
                        val copy = stackAt.copy()
                        if (!par5EntityPlayer.inventory.addItemStackToInventory(copy))
                            par5EntityPlayer.dropPlayerItemWithRandomChoice(copy, false)
                        altar.getItemHandler().setStackInSlot(i, null)
                        worldIn.updateComparatorOutputLevel(pos, this)
                        break
                    }
                }
        } else if (altar.isEmpty() && stack == null)
            altar.trySetLastRecipe(par5EntityPlayer)
        else if (stack != null)
            return altar.addItem(par5EntityPlayer, stack)
        return false
    }

    override fun breakBlock(worldIn: World, pos: BlockPos, state: IBlockState) {
        val inv = worldIn.getTileEntity(pos) as TileSuffuser?

        val random = worldIn.rand

        if (inv != null) {
            for (j1 in 0..inv.sizeInventory - 1) {
                val itemstack = inv.getItemHandler().getStackInSlot(j1)

                if (itemstack != null) {
                    val f = random.nextFloat() * 0.8f + 0.1f
                    val f1 = random.nextFloat() * 0.8f + 0.1f
                    var entityitem: EntityItem

                    val f2 = random.nextFloat() * 0.8f + 0.1f
                    while (itemstack.stackSize > 0) {
                        var k1 = random.nextInt(21) + 10

                        if (k1 > itemstack.stackSize)
                            k1 = itemstack.stackSize

                        itemstack.stackSize -= k1
                        entityitem = EntityItem(worldIn, (pos.x + f).toDouble(), (pos.y + f1).toDouble(), (pos.z + f2).toDouble(), ItemStack(itemstack.item, k1, itemstack.itemDamage))
                        val f3 = 0.05f
                        entityitem.motionX = (random.nextGaussian().toFloat() * f3).toDouble()
                        entityitem.motionY = (random.nextGaussian().toFloat() * f3 + 0.2f).toDouble()
                        entityitem.motionZ = (random.nextGaussian().toFloat() * f3).toDouble()

                        if (itemstack.hasTagCompound())
                            entityitem.entityItem.tagCompound = itemstack.tagCompound.copy() as NBTTagCompound
                        worldIn.spawnEntityInWorld(entityitem)
                    }
                }
            }

            worldIn.updateComparatorOutputLevel(pos, state.block)
        }

        super.breakBlock(worldIn, pos, state)
    }

    override fun hasComparatorInputOverride(state: IBlockState?) = true
    override fun getComparatorInputOverride(blockState: IBlockState?, worldIn: World?, pos: BlockPos?): Int {
        val altar = worldIn!!.getTileEntity(pos) as TileSuffuser? ?: return 0
        return altar.signal
    }

    override fun createNewTileEntity(worldIn: World, meta: Int): TileEntity {
        return TileSuffuser()
    }

    override fun canSustainPlant(state: IBlockState?, world: IBlockAccess?, pos: BlockPos?, direction: EnumFacing?, plantable: IPlantable?): Boolean {
        return if (isDendric(state)) true else super.canSustainPlant(state, world, pos, direction, plantable)
    }
}
