package shadowfox.botanicaladdons.common.items.bauble

import baubles.api.BaubleType
import com.teamwizardry.librarianlib.client.core.ModelHandler
import com.teamwizardry.librarianlib.client.util.TooltipHelper.addToTooltip
import com.teamwizardry.librarianlib.common.base.IExtraVariantHolder
import com.teamwizardry.librarianlib.common.base.item.IItemColorProvider
import shadowfox.botanicaladdons.common.items.base.ItemModBauble
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType.NONE
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.client.ForgeHooksClient
import org.lwjgl.opengl.GL11
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.common.BotanicalAddons
import shadowfox.botanicaladdons.common.items.bauble.faith.ItemFaithBauble
import vazkii.botania.api.item.IBaubleRender
import vazkii.botania.api.item.ICosmeticBauble
import vazkii.botania.client.core.helper.ShaderHelper
import vazkii.botania.client.core.proxy.ClientProxy
import vazkii.botania.client.lib.LibResources
import vazkii.botania.client.model.ModelTinyPotato
import com.teamwizardry.librarianlib.common.util.ItemNBTHelper
import net.minecraft.util.NonNullList

/**
 * @author WireSegal
 * Created at 10:33 PM on 4/15/16.
 */
class ItemSymbol(name: String) : ItemModBauble(name), ICosmeticBauble, IExtraVariantHolder, IItemColorProvider {

    companion object {

        val POTATO_LOCATION by lazy {
            ResourceLocation(if (ClientProxy.dootDoot) LibResources.MODEL_TINY_POTATO_HALLOWEEN else LibResources.MODEL_TINY_POTATO)
        }

        val TAG_PLAYER = "player"

        val vaz = "8c826f34-113b-4238-a173-44639c53b6e6"
        val wire = "458391f5-6303-4649-b416-e4c0d18f837a"
        val tris = "d475af59-d73c-42be-90ed-f1a78f10d452"
        val l0ne = "d7a5f995-57d5-4077-bc9f-83cc36cadd66"
        val jansey = "eaf6956b-dd98-4e07-a890-becc9b6d1ba9"
        val wiiv = "0d054077-a977-4b19-9df9-8a4d5bf20ec3"
        val troll = "6e008af9-2d0c-4e4f-9312-fb0349416e75"
        val willie = "7a66d29d-6d01-4d73-a277-5b5c966dbd59"

        val specialPlayers = arrayOf(vaz, wire, tris, l0ne, jansey, wiiv, troll, willie)

        val headPlayers = arrayOf(vaz, wire, jansey, willie)

        fun getPlayer(stack: ItemStack) = ItemNBTHelper.getString(stack, TAG_PLAYER, "")

        fun setPlayer(stack: ItemStack, pUUID: String) = ItemNBTHelper.setString(stack, TAG_PLAYER, pUUID)
    }

    init {
        addPropertyOverride(ResourceLocation(LibMisc.MOD_ID, ItemFaithBauble.TAG_PENDANT)) {
            stack, _, _ ->
            if (ItemNBTHelper.getBoolean(stack, ItemFaithBauble.TAG_PENDANT, false)) 1f else 0f
        }
    }

    override val meshDefinition: ((ItemStack) -> ModelResourceLocation)?
        get() = {
            ModelHandler.resourceLocations[LibMisc.MOD_ID]!![when (getPlayer(it)) {
                vaz -> "headtato"
                wire -> "catalyst"
                tris -> "heart"
                l0ne -> "tail"
                jansey -> "headdress"
                wiiv -> "teru_head"
                troll -> "emblem_mystery"
                willie -> "fabulosity"
                else -> "holy_symbol"
            }] as ModelResourceLocation
        }

    override val itemColorFunction: ((ItemStack, Int) -> Int)?
        get() = { itemStack, _ -> if (getPlayer(itemStack) == willie) BotanicalAddons.PROXY.rainbow2(0.005f, 0.6f).rgb else 0xFFFFFF }

    override val extraVariants: Array<out String>
        get() = arrayOf("headtato", "catalyst", "heart", "tail", "headdress", "teru_head", "emblem_mystery", "fabulosity", "holy_symbol")

    override fun onEquipped(stack: ItemStack, player: EntityLivingBase) {
        super.onEquipped(stack, player)
        var nameChanged = false
        val displayName = stack.displayName.toLowerCase()
        fun nameCheck(player: String, checkName: String) {
            if (!nameChanged && getPlayer(stack) != player && displayName == checkName) {
                setPlayer(stack, player)
                nameChanged = true
            }
        }

        nameCheck("none", "none")
        nameCheck(vaz, "vazkii is bae")
        nameCheck(wire, "change the world")
        nameCheck(tris, "the power of the soul")
        nameCheck(l0ne, "do the foxtrot")
        nameCheck(jansey, "derp faced chieftain")
        nameCheck(wiiv, "rain rain go away")
        nameCheck(troll, "???")
        nameCheck(willie, "less fabulous than wire")

        if (nameChanged)
            stack.clearCustomName()
    }

    override fun getUnlocalizedName(stack: ItemStack): String {
        val above = super.getUnlocalizedName(stack)
        val player = getPlayer(stack)
        return when (player) {
            vaz -> "$above.potato"
            wire -> "$above.catalyst"
            tris -> "$above.heart"
            l0ne -> "$above.tail"
            jansey -> "$above.headdress"
            wiiv -> "$above.teru"
            troll -> "$above.mystery"
            willie -> "$above.rainbow"
            "none" -> "$above.none"
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
            "trollking" -> setPlayer(stack, troll)
            "willie" -> setPlayer(stack, willie)
        }
    }

    override fun addHiddenTooltip(stack: ItemStack, player: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
        addToTooltip(tooltip, "botaniamisc.cosmeticBauble")
        super.addHiddenTooltip(stack, player, tooltip, advanced)
    }

    override fun getBaubleType(stack: ItemStack) = BaubleType.TRINKET

    override fun onPlayerBaubleRender(stack: ItemStack, player: EntityPlayer, type: IBaubleRender.RenderType, p3: Float) {
        val renderStack = stack.copy()

        if (getPlayer(renderStack) !in specialPlayers && player.uniqueID.toString() in specialPlayers && getPlayer(renderStack) != "none")
            setPlayer(renderStack, player.uniqueID.toString())
        ItemNBTHelper.setBoolean(renderStack, ItemFaithBauble.TAG_PENDANT, true)

        var playerAs = getPlayer(renderStack)
        if (playerAs !in specialPlayers) playerAs = ""
        GlStateManager.pushMatrix()
        if (type == IBaubleRender.RenderType.HEAD && playerAs in headPlayers) {
            IBaubleRender.Helper.translateToHeadLevel(player)
            IBaubleRender.Helper.translateToFace()
            IBaubleRender.Helper.defaultTransforms()
            if (playerAs == vaz) {
                Minecraft.getMinecraft().renderEngine.bindTexture(POTATO_LOCATION)
                val model = ModelTinyPotato()
                GlStateManager.scale(1.8, 1.8, 1.8)
                GlStateManager.translate(0F, 1.75F, 0.25F)
                GlStateManager.rotate(180F, 0F, 0F, 1F)
                model.render()
            } else if (playerAs == jansey) {
                GlStateManager.scale(1.8, 1.8, 1.8)
                GlStateManager.translate(0f, 0f, 0.25f)
                GlStateManager.rotate(180F, 0f, 1f, 0f)
                Minecraft.getMinecraft().renderItem.renderItem(renderStack, NONE)
            } else if (playerAs == willie) {
                GlStateManager.translate(0f, 0.875f, 0.625f)
                Minecraft.getMinecraft().renderItem.renderItem(renderStack, NONE)
            } else {
                if (playerAs == wire) {
                    GlStateManager.scale(0.5, 0.5, 0.5)
                    GlStateManager.translate(0.425, -0.3, -0.4)
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
            IBaubleRender.Helper.rotateIfSneaking(player)
            IBaubleRender.Helper.translateToChest()
            IBaubleRender.Helper.defaultTransforms()
            if (playerAs == l0ne) {
                GlStateManager.rotate(90F, 0F, 1F, 0F)
                GlStateManager.translate(0.5F, -0.75F, 0F)
                Minecraft.getMinecraft().renderItem.renderItem(renderStack, NONE)
            } else if (playerAs == wiiv) {
                GlStateManager.scale(1.36, 1.36, 1.36)
                GlStateManager.translate(0F, -0.25F, 0F)
                GlStateManager.rotate(180F, 0F, 1F, 0F)
                Minecraft.getMinecraft().renderItem.renderItem(renderStack, NONE)
            } else if (playerAs == tris) {
                val armor = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST) != null
                GlStateManager.translate(0F, -0.25F, if (armor) 0.325F else 0.2F)
                GlStateManager.rotate(180F, 0F, 1F, 0F)
                GlStateManager.enableBlend()
                GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f)
                GlStateManager.alphaFunc(GL11.GL_EQUAL, 1f)
                ShaderHelper.useShader(ShaderHelper.halo)
                renderItem(renderStack)
                GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f)
                ShaderHelper.releaseShader()
            } else {
                val armor = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST) != null
                GlStateManager.translate(0.0, 0.15, if (armor) 0.125 else 0.05)
                Minecraft.getMinecraft().renderItem.renderItem(renderStack, NONE)
            }
        }
        GlStateManager.popMatrix()
    }

    override fun getSubItems(itemIn: Item, tab: CreativeTabs?, subItems: NonNullList<ItemStack>) {
        super.getSubItems(itemIn, tab, subItems)
        if (tab == null) {
            for (player in specialPlayers) {
                val stack = ItemStack(itemIn)
                setPlayer(stack, player)
                subItems.add(stack)
            }
        }
    }

    fun renderItem(stack: ItemStack) {

        var ibakedmodel = Minecraft.getMinecraft().renderItem.getItemModelWithOverrides(stack, null, null)

        Minecraft.getMinecraft().textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)
        Minecraft.getMinecraft().textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false)
        GlStateManager.enableRescaleNormal()
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO)
        GlStateManager.pushMatrix()
        ibakedmodel = ForgeHooksClient.handleCameraTransforms(ibakedmodel, NONE, false)
        Minecraft.getMinecraft().renderItem.renderItem(stack, ibakedmodel)
        GlStateManager.cullFace(GlStateManager.CullFace.BACK)
        GlStateManager.popMatrix()
        GlStateManager.disableRescaleNormal()
        Minecraft.getMinecraft().textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)
        Minecraft.getMinecraft().textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap()
    }
}
