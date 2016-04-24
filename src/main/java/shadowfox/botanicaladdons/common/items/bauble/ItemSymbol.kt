package shadowfox.botanicaladdons.common.items.bauble

import baubles.api.BaubleType
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType.NONE
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
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
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

        @SideOnly(Side.CLIENT)
        var POTATO_LOCATION: ResourceLocation? = null

        val TAG_PLAYER = "player"

        val vaz = "8c826f34-113b-4238-a173-44639c53b6e6"
        val wire = "458391f5-6303-4649-b416-e4c0d18f837a"
        val tris = "d475af59-d73c-42be-90ed-f1a78f10d452"
        val l0ne = "d7a5f995-57d5-4077-bc9f-83cc36cadd66"
        val jansey = "eaf6956b-dd98-4e07-a890-becc9b6d1ba9" //todo
        val wiiv = "0d054077-a977-4b19-9df9-8a4d5bf20ec3"

        val specialPlayers = arrayOf(vaz, wire, tris, l0ne, jansey, wiiv)

        val headPlayers = arrayOf(vaz, wire)

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
        var nameChanged = false
        if (getPlayer(stack) != vaz && stack.displayName.toLowerCase().equals("vazkii is bae")) {
            setPlayer(stack, vaz)
            nameChanged = true
        } else if (getPlayer(stack) != wiiv && stack.displayName.toLowerCase().equals("rain rain go away")) {
            setPlayer(stack, wiiv)
            nameChanged == true
        }

        if (nameChanged)
            if (stack.tagCompound.hasKey("display") && stack.tagCompound.getCompoundTag("display").hasKey("Name"))
                stack.tagCompound.getCompoundTag("display").removeTag("Name")
    }

    override fun getUnlocalizedName(par1ItemStack: ItemStack?): String {
        val above = super.getUnlocalizedName(par1ItemStack)
        val player = getPlayer(par1ItemStack ?: return above)
        return when (player) {
            vaz -> "$above.potato"
            wire -> "$above.catalyst"
            tris -> "$above.heart"
            l0ne -> "$above.tail"
            jansey -> "$above"
            wiiv -> "$above.teru"
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
            "jansey" -> setPlayer(stack, jansey)
            "wiiv" -> setPlayer(stack, wiiv)
        }
    }

    override fun addHiddenTooltip(stack: ItemStack, player: EntityPlayer?, tooltip: MutableList<String>, advanced: Boolean) {
        addToTooltip(tooltip, "botaniamisc.cosmeticBauble")
        super.addHiddenTooltip(stack, player, tooltip, advanced)
    }

    override fun getBaubleType(stack: ItemStack?) = BaubleType.AMULET

    override fun onPlayerBaubleRender(stack: ItemStack, player: EntityPlayer, type: IBaubleRender.RenderType, p3: Float) {
        val renderStack = stack.copy()

        if (getPlayer(renderStack) !in specialPlayers && player.uniqueID.toString() in specialPlayers && getPlayer(renderStack) != "none")
            setPlayer(renderStack, player.uniqueID.toString())

        var playerAs = getPlayer(renderStack)
        if (playerAs !in specialPlayers) playerAs = ""
        GlStateManager.pushMatrix()
        if (type == IBaubleRender.RenderType.HEAD && playerAs in headPlayers) {
            if (playerAs == vaz) {
                if (POTATO_LOCATION == null)
                    POTATO_LOCATION = ResourceLocation(if (ClientProxy.dootDoot) LibResources.MODEL_TINY_POTATO_HALLOWEEN else LibResources.MODEL_TINY_POTATO)
                Minecraft.getMinecraft().renderEngine.bindTexture(POTATO_LOCATION)
                val model = ModelTinyPotato()
                GlStateManager.scale(0.0F, -2.0F, 0.0F)
                GlStateManager.rotate(-90F, 0F, 1F, 0F)
                model.render()
            } else {
                IBaubleRender.Helper.translateToHeadLevel(player)
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
            } else if (playerAs == wiiv) {
                if (player.isSneaking) {
                    GlStateManager.translate(0.0, 0.2, 0.0)
                    GlStateManager.rotate(90 / Math.PI.toFloat(), 1.0f, 0.0f, 0.0f)
                }
                chestTranslate()
                GlStateManager.scale(0.75, 0.75, 0.75)
                GlStateManager.translate(0F, -0.5F, 0.1F)
                GlStateManager.rotate(180F, 0F, 1F, 0F)
                Minecraft.getMinecraft().renderItem.renderItem(renderStack, NONE)
            } else if (playerAs == tris) {
                if (player.isSneaking) {
                    GlStateManager.translate(0.0, 0.2, 0.0)
                    GlStateManager.rotate(90 / Math.PI.toFloat(), 1.0f, 0.0f, 0.0f)
                }
                chestTranslate()
                val armor = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST) != null
                GlStateManager.translate(0F, -0.5F, if (armor) 0.325F else 0.2F)
                GlStateManager.rotate(180F, 0F, 1F, 0F)
                GlStateManager.enableBlend()
                GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f)
                GlStateManager.alphaFunc(GL11.GL_EQUAL, 1f)
                ShaderHelper.useShader(ShaderHelper.halo)
                renderItem(renderStack)
                GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f)
                ShaderHelper.releaseShader()
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
