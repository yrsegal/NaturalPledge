package shadowfox.botanicaladdons.common.potions

import net.minecraft.client.Minecraft
import net.minecraft.entity.EntityLivingBase
import net.minecraft.potion.Potion
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import shadowfox.botanicaladdons.common.lib.LibMisc

/**
 * @author WireSegal
 * Created at 9:27 AM on 4/15/16.
 */
open class PotionMod(name: String, badEffect: Boolean, color: Int, iconIndex: Int) : Potion(badEffect, color) {
    init {
        GameRegistry.register(this, ResourceLocation(LibMisc.MOD_ID, name))
        setPotionName("${LibMisc.MOD_ID}.potion." + name);
        setIconIndex(iconIndex % 8, iconIndex / 8);
    }

    @SideOnly(Side.CLIENT)
    override fun getStatusIconIndex(): Int {
        Minecraft.getMinecraft().renderEngine.bindTexture(resource);

        return super.getStatusIconIndex();
    }

    fun hasEffect(entity: EntityLivingBase): Boolean {
        return hasEffect(entity, this);
    }

    companion object {
        val resource = ResourceLocation(LibMisc.MOD_ID, "textures/gui/potions.png");

        fun hasEffect(entity: EntityLivingBase, potion: Potion): Boolean {
            return entity.getActivePotionEffect(potion) != null;
        }
    }
}
