package shadowfox.botanicaladdons.common.items.bauble

import baubles.api.BaubleType
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.ItemRenderer
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.block.model.ItemCameraTransforms
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.client.ForgeHooksClient
import org.lwjgl.opengl.GL11
import shadowfox.botanicaladdons.common.items.base.ItemModBauble
import shadowfox.botanicaladdons.common.items.bauble.faith.ItemFaithBauble
import shadowfox.botanicaladdons.common.lib.LibMisc
import vazkii.botania.api.item.IBaubleRender
import vazkii.botania.api.item.ICosmeticBauble
import vazkii.botania.client.core.helper.ShaderHelper
import vazkii.botania.client.core.proxy.ClientProxy
import vazkii.botania.client.lib.LibResources
import vazkii.botania.client.model.ModelTinyPotato
import vazkii.botania.common.core.helper.ItemNBTHelper

/**
 * @author WireSegal
 * Created at 10:33 PM on 4/15/16.
 */
class ItemSymbol(name: String) : ItemModBauble(name), ICosmeticBauble {

    companion object {

        val POTATO_LOCATION = ResourceLocation(if (ClientProxy.dootDoot) LibResources.MODEL_TINY_POTATO_HALLOWEEN else LibResources.MODEL_TINY_POTATO)

        val TAG_PLAYER = "player"

        val vaz = "8c826f34-113b-4238-a173-44639c53b6e6"
        val wire = "458391f5-6303-4649-b416-e4c0d18f837a"
        val tris = "d475af59-d73c-42be-90ed-f1a78f10d452"
        val l0ne = "d7a5f995-57d5-4077-bc9f-83cc36cadd66"

        val specialPlayers = arrayOf(vaz, wire, tris, l0ne)

        val headPlayers = arrayOf(vaz, wire, tris)

        fun getPlayer(stack: ItemStack) = ItemNBTHelper.getString(stack, TAG_PLAYER, "")

        fun setPlayer(stack: ItemStack, pUUID: String) = ItemNBTHelper.setString(stack, TAG_PLAYER, pUUID)
    }

    init {
        addPropertyOverride(ResourceLocation(LibMisc.MOD_ID, TAG_PLAYER)) {
            stack, entity, world ->
            val playerAs = getPlayer(stack.copy())
            if (playerAs in specialPlayers)
                specialPlayers.indexOf(playerAs).toFloat() + 1
            else 0f
        }
        addPropertyOverride(ResourceLocation(LibMisc.MOD_ID, ItemFaithBauble.TAG_PENDANT)) {
            stack, world, entity ->
            if (ItemNBTHelper.getBoolean(stack, ItemFaithBauble.TAG_PENDANT, false)) 1f else 0f
        }
    }

    override fun onEquipped(stack: ItemStack, player: EntityLivingBase?) {
        super.onEquipped(stack, player)
        if (getPlayer(stack) != vaz && stack.displayName.toLowerCase().equals("vazkii is bae")) {
            setPlayer(stack, vaz)
            if (stack.tagCompound.hasKey("display") && stack.tagCompound.getCompoundTag("display").hasKey("Name"))
                    stack.tagCompound.getCompoundTag("display").removeTag("Name")
        }
    }

    override fun getUnlocalizedName(par1ItemStack: ItemStack?): String {
        val above = super.getUnlocalizedName(par1ItemStack)
        val player = getPlayer(par1ItemStack ?: return above)
        return when (player) {
            vaz -> "$above.potato"
            wire -> "$above.catalyst"
            tris -> "$above.mask"
            l0ne -> "$above.tail"
            else -> above
        }
    }

    override fun onUpdate(stack: ItemStack, worldIn: World?, entityIn: Entity?, itemSlot: Int, isSelected: Boolean) {
        val player = getPlayer(stack)
        when (player) {
            "vaz" -> setPlayer(stack, vaz)
            "wire" -> setPlayer(stack, wire)
            "tris" -> setPlayer(stack, tris)
            "l0ne" -> setPlayer(stack, l0ne)
        }
    }

    override fun getBaubleType(stack: ItemStack?) = BaubleType.AMULET

    override fun onPlayerBaubleRender(stack: ItemStack, player: EntityPlayer, type: IBaubleRender.RenderType, p3: Float) {
        val renderStack = stack.copy()

        if (getPlayer(renderStack) !in specialPlayers && player.uniqueID.toString() in specialPlayers)
            setPlayer(renderStack, player.uniqueID.toString())

        var playerAs = getPlayer(renderStack)
        if (playerAs !in specialPlayers) playerAs = ""
        GlStateManager.pushMatrix()
        if (type == IBaubleRender.RenderType.HEAD && playerAs in headPlayers) {
            if (playerAs == vaz) {
                Minecraft.getMinecraft().renderEngine.bindTexture(POTATO_LOCATION)
                val model = ModelTinyPotato()
                GlStateManager.scale(0.0F, -2.0F, 0.0F)
                GlStateManager.rotate(-90F, 0F, 1F, 0F)
                model.render()
            } else {
                IBaubleRender.Helper.translateToHeadLevel(player)
                GlStateManager.enableBlend()
                GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f)
                faceTranslate()
                if (playerAs == wire) {
                    GlStateManager.scale(0.5, 0.5, 0.5)
                    GlStateManager.translate(0.25, 0.05, -0.1)
                    GlStateManager.alphaFunc(GL11.GL_EQUAL, 1f)
                    ShaderHelper.useShader(ShaderHelper.halo)
                }
                renderItem(renderStack)
                if (playerAs == wire) {
                    GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f)
                    ShaderHelper.releaseShader()
                }
            }
        } else if (type == IBaubleRender.RenderType.BODY && playerAs !in headPlayers) {
            if (playerAs == l0ne) {
                if (player.isSneaking) {
                    GlStateManager.translate(0.0, 0.2, 0.0)
                    GlStateManager.rotate(90 / Math.PI.toFloat(), 1.0f, 0.0f, 0.0f)
                }
                chestTranslate()
                GlStateManager.rotate(90F, 0F, 1F, 0F)
                GlStateManager.translate(0.25F, -0.75F, -0.055F)
                Minecraft.getMinecraft().renderItem.renderItem(renderStack, THIRD_PERSON_RIGHT_HAND)
            } else {
                if (player.isSneaking) {
                    GlStateManager.translate(0.0, 0.3, 0.0)
                    GlStateManager.rotate(90 / Math.PI.toFloat(), 1.0f, 0.0f, 0.0f)
                }
                val armor = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST) != null
                GlStateManager.rotate(180F, 1F, 0F, 0F)
                GlStateManager.translate(0.0, -0.3, if (armor) 0.175 else 0.05)

                ItemNBTHelper.setBoolean(renderStack, ItemFaithBauble.TAG_PENDANT, true)

                Minecraft.getMinecraft().renderItem.renderItem(renderStack, THIRD_PERSON_RIGHT_HAND)
            }
        }
        GlStateManager.popMatrix()
    }

    fun renderItem(stack: ItemStack) {

        var ibakedmodel = Minecraft.getMinecraft().renderItem.getItemModelWithOverrides(stack, null, null)

        Minecraft.getMinecraft().textureManager.bindTexture(TextureMap.locationBlocksTexture)
        Minecraft.getMinecraft().textureManager.getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false)
        GlStateManager.enableRescaleNormal()
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO)
        GlStateManager.pushMatrix()
        ibakedmodel = ForgeHooksClient.handleCameraTransforms(ibakedmodel, THIRD_PERSON_RIGHT_HAND, false)
        Minecraft.getMinecraft().renderItem.renderItem(stack, ibakedmodel)
        GlStateManager.cullFace(GlStateManager.CullFace.BACK)
        GlStateManager.popMatrix()
        GlStateManager.disableRescaleNormal()
        Minecraft.getMinecraft().textureManager.bindTexture(TextureMap.locationBlocksTexture)
        Minecraft.getMinecraft().textureManager.getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap()
    }

    fun faceTranslate() {
        GlStateManager.rotate(90F, 0F, 1F, 0F)
        GlStateManager.rotate(180F, 1F, 0F, 0F)
        GlStateManager.translate(0f, -1.55f, -0.3f)
    }

    fun chestTranslate() {
        GlStateManager.rotate(180F, 1F, 0F, 0F)
    }
}
