package com.wiresegal.naturalpledge.common.items.travel.stones

import com.teamwizardry.librarianlib.core.LibrarianLib
import com.teamwizardry.librarianlib.features.base.item.IItemColorProvider
import com.teamwizardry.librarianlib.features.base.item.ItemMod
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper
import net.minecraft.block.material.Material
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import com.wiresegal.naturalpledge.api.lib.LibMisc
import com.wiresegal.naturalpledge.common.NaturalPledge
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import vazkii.botania.api.wand.ICoordBoundItem
import vazkii.botania.common.Botania
import vazkii.botania.common.core.handler.ModSounds
import vazkii.botania.common.core.helper.Vector3

/**
 * @author WireSegal
 * Created at 9:33 PM on 1/20/17.
 */
class ItemPortalStone(name: String) : ItemMod(name), ICoordBoundItem, IItemColorProvider {

    companion object {
        val TAG_X = "x"
        val TAG_Y = "y"
        val TAG_Z = "z"
        val TAG_DIM = "dim"
    }

    init {
        setMaxStackSize(1)
        MinecraftForge.EVENT_BUS.register(this)
    }

    override val itemColorFunction: ((ItemStack, Int) -> Int)?
        get() = { _, i ->
            if (i == 1)
                NaturalPledge.PROXY.rainbow(0.25f).rgb
            else 0xFFFFFF
        }

    @SideOnly(Side.CLIENT)
    override fun addInformation(stack: ItemStack, worldIn: World?, tooltip: MutableList<String>, flagIn: ITooltipFlag?) {
        if (Minecraft.getMinecraft().player!=null) {
            val dirVec = getDirVec(stack, LibrarianLib.PROXY.getClientPlayer())
            val distance = Math.round((dirVec ?: Vector3.ZERO).mag()).toInt()
            if (getBinding(stack) != null) {
                if (distance < 5)
                    TooltipHelper.addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.tracking_block_close")
                else
                    TooltipHelper.addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.tracking_block", distance)
            }
        }
    }

    override fun onUpdate(stack: ItemStack, worldIn: World, entityIn: Entity, itemSlot: Int, isSelected: Boolean) {
        if (!worldIn.isRemote && entityIn.isInsideOfMaterial(Material.PORTAL)) {
            val pos = entityIn.position
            ItemNBTHelper.setInt(stack, TAG_X, pos.x)
            ItemNBTHelper.setInt(stack, TAG_Y, pos.y)
            ItemNBTHelper.setInt(stack, TAG_Z, pos.z)
            ItemNBTHelper.setInt(stack, TAG_DIM, worldIn.provider.dimension)
        }

        if (!worldIn.isRemote
                || entityIn !is EntityLivingBase
                || entityIn.heldItemMainhand != stack && entityIn.heldItemOffhand != stack
                || worldIn.provider.dimension != ItemNBTHelper.getInt(stack, TAG_DIM, 0)) return

        val startVec = Vector3.fromEntityCenter(entityIn)
        val dirVec = getDirVec(stack, entityIn) ?: return
        val endVec = startVec.add(dirVec.normalize().multiply(Math.min(dirVec.mag(), 10.0)))

        Botania.proxy.setWispFXDepthTest(false)
        NaturalPledge.PROXY.particleStream(startVec.add(dirVec.normalize()).add(0.0, 0.5, 0.0), endVec, NaturalPledge.PROXY.wireFrameRainbow().rgb)
        Botania.proxy.setWispFXDepthTest(true)
    }

    override fun onItemRightClick(worldIn: World, player: EntityPlayer, hand: EnumHand?): ActionResult<ItemStack>? {
        val stack = player.getHeldItem(hand)
        if (player.isSneaking && getBinding(stack) != null && hand == EnumHand.MAIN_HAND) {
            ItemNBTHelper.removeEntry(stack, TAG_X)
            ItemNBTHelper.removeEntry(stack, TAG_Y)
            ItemNBTHelper.removeEntry(stack, TAG_Z)
            worldIn.playSound(player, player.posX, player.posY, player.posZ, ModSounds.ding, SoundCategory.PLAYERS, 1f, 5f)
        }

        return super.onItemRightClick(worldIn, player, hand)
    }

    fun getDirVec(stack: ItemStack, player: Entity): Vector3? {
        val pos = getEndVec(stack) ?: return null

        val entityPos = Vector3.fromEntityCenter(player).subtract(Vector3(0.5, 0.5, 0.5))
        return pos.subtract(entityPos)
    }

    fun getEndVec(stack: ItemStack): Vector3? {
        return Vector3.fromBlockPos(getBinding(stack) ?: return null)
    }

    override fun getBinding(stack: ItemStack): BlockPos? {
        val x = ItemNBTHelper.getInt(stack, TAG_X, 0)
        val y = ItemNBTHelper.getInt(stack, TAG_Y, Int.MIN_VALUE)
        val z = ItemNBTHelper.getInt(stack, TAG_Z, 0)
        return if (y == Int.MIN_VALUE) null else BlockPos(x, y, z)
    }
}
