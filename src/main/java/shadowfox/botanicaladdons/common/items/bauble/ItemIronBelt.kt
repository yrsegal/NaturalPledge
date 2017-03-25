package shadowfox.botanicaladdons.common.items.bauble

import baubles.api.BaubleType
import shadowfox.botanicaladdons.common.items.base.ItemModBauble
import net.minecraft.client.Minecraft
import net.minecraft.client.model.ModelBiped
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.common.items.travel.bauble.ItemToolbelt
import vazkii.botania.api.item.IBaubleRender

/**
 * @author WireSegal
 * Created at 11:38 PM on 1/1/17.
 */
class ItemIronBelt(name: String) : ItemModBauble(name), IBaubleRender {
    val beltTexture = ResourceLocation(LibMisc.MOD_ID, "textures/model/iron_belt.png")

    override fun onWornTick(stack: ItemStack, player: EntityLivingBase) {
        if ((player !is EntityPlayer || !player.capabilities.isFlying) && !player.onGround && !player.isInWater && player.motionY < 0)
            player.motionY -= 0.025
    }

    override fun getBaubleType(p0: ItemStack) = BaubleType.BELT

    override fun onPlayerBaubleRender(stack: ItemStack, player: EntityPlayer, type: IBaubleRender.RenderType, partticks: Float) {
        if (type == IBaubleRender.RenderType.BODY) {

            if (ItemToolbelt.model == null)
                ItemToolbelt.model = ModelBiped()

            Minecraft.getMinecraft().renderEngine.bindTexture(beltTexture)
            IBaubleRender.Helper.rotateIfSneaking(player)

            if (!player.isSneaking)
                GlStateManager.translate(0F, 0.2F, 0F)

            val s = 1.05F / 16F
            GlStateManager.scale(s, s, s)

            (ItemToolbelt.model as ModelBiped).bipedBody.render(1F)
        }
    }
}
