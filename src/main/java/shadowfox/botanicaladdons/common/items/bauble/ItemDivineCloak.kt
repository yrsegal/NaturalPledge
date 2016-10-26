package shadowfox.botanicaladdons.common.items.bauble

import baubles.api.BaubleType
import baubles.api.BaublesApi
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.MobEffects
import net.minecraft.init.SoundEvents
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraft.util.DamageSource
import net.minecraft.util.EntityDamageSource
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingFallEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.common.BotanicalAddons
import shadowfox.botanicaladdons.common.core.helper.BAMethodHandles
import shadowfox.botanicaladdons.common.items.base.ItemModBauble
import shadowfox.botanicaladdons.common.network.SetPositionMessage
import vazkii.botania.api.item.IBaubleRender
import vazkii.botania.client.model.ModelCloak

/**
 * @author WireSegal
 * Created at 9:50 PM on 10/21/16.
 */
class ItemDivineCloak(name: String) : ItemModBauble(name, *variants), IBaubleRender {
    companion object {
        val variants = arrayOf("cloakNjord",
                "cloakIdunn",
                "cloakThor",
                "cloakHeimdall")

        init {
            MinecraftForge.EVENT_BUS.register(this)
        }

        fun damageSourceEarthquake(e: EntityPlayer): DamageSource {
            return EntityDamageSource("${LibMisc.MOD_ID}.earthquake", e)
        }

        @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
        fun onFall(e: LivingFallEvent) {
            val player = e.entityLiving
            if (player is EntityPlayer) {
                val baubles = BaublesApi.getBaublesHandler(player)
                val body = baubles.getStackInSlot(BaubleType.BODY.validSlots[0])
                if (body != null && body.item is ItemDivineCloak && body.itemDamage == 2) {

                    val jump = player.getActivePotionEffect(MobEffects.JUMP_BOOST)
                    val f = if (jump == null) 0.0f else (jump.amplifier + 1).toFloat()
                    val damage = Math.min((e.distance - 3.0f - f) * e.damageMultiplier, 5.0f)
                    if (damage > 0.0f) {
                        e.isCanceled = true
                        val entities = player.worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, player.entityBoundingBox.expand(4.0, 3.0, 4.0))
                        for (entity in entities) if (entity != player && entity.onGround)
                            entity.attackEntityFrom(damageSourceEarthquake(player), damage * 2)

                        for (pos in BlockPos.getAllInBoxMutable(BlockPos(player.positionVector).add(-1, -1, -1), BlockPos(player.positionVector).add(1, -1, 1))) {
                            val state = player.worldObj.getBlockState(pos)
                            if (state.isFullCube)
                                player.worldObj.playEvent(2001, pos, state.block.getMetaFromState(state))
                        }
                    }
                }
            }
        }
    }

    override fun getBaubleType(p0: ItemStack?) = BaubleType.BODY

    override fun onWornTick(stack: ItemStack, player: EntityLivingBase) {
        super.onWornTick(stack, player)
        if (stack.itemDamage == 0) {
            if ((player !is EntityPlayer || !player.capabilities.isFlying) && !player.onGround && !player.isElytraFlying)
                player.motionY += 0.05
            player.fallDistance = 0f
        } else if (stack.itemDamage == 3) {
            if (player.worldObj.isRemote && player.isSprinting && BAMethodHandles.getJumpTicks(player) == 10) {
                val look = player.lookVec
                val dist = 6.0
                val vec = Vec3d(player.posX + look.xCoord * dist, player.posY + look.yCoord * dist, player.posZ + look.zCoord * dist)
                val blockAt = BlockPos(vec)
                if (!player.worldObj.getBlockState(blockAt).isFullCube && !player.worldObj.getBlockState(blockAt.up()).isFullCube) {
                    BAMethodHandles.setIsJumping(player, false)
                    BotanicalAddons.NETWORK.sendToServer(SetPositionMessage(vec))
                    player.worldObj.playSound(vec.xCoord, vec.yCoord, vec.zCoord, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1f, 1f, false)
                }
            }
        }
    }

    val model: Any by lazy {
        ModelCloak()
    }

    val cloakNjord = ResourceLocation(LibMisc.MOD_ID, "textures/model/njordCloak.png")
    val cloakIdunn = ResourceLocation(LibMisc.MOD_ID, "textures/model/idunnCloak.png")
    val cloakThor = ResourceLocation(LibMisc.MOD_ID, "textures/model/thorCloak.png")
    val cloakHeimdall = ResourceLocation(LibMisc.MOD_ID, "textures/model/heimdallCloak.png")

    val cloakNjordGlow = ResourceLocation(LibMisc.MOD_ID, "textures/model/njordCloakGlow.png")
    val cloakIdunnGlow = ResourceLocation(LibMisc.MOD_ID, "textures/model/idunnCloakGlow.png")
    val cloakThorGlow = ResourceLocation(LibMisc.MOD_ID, "textures/model/thorCloakGlow.png")
    val cloakHeimdallGlow = ResourceLocation(LibMisc.MOD_ID, "textures/model/heimdallCloakGlow.png")

    fun getCloakTexture(stack: ItemStack): ResourceLocation {
        return when (stack.itemDamage) {
            0 -> cloakNjord
            1 -> cloakIdunn
            2 -> cloakThor
            else -> cloakHeimdall
        }
    }

    fun getCloakGlowTexture(stack: ItemStack): ResourceLocation {
        return when (stack.itemDamage) {
            0 -> cloakNjordGlow
            1 -> cloakIdunnGlow
            2 -> cloakThorGlow
            else -> cloakHeimdallGlow
        }
    }

    @SideOnly(Side.CLIENT)
    override fun onPlayerBaubleRender(stack: ItemStack, player: EntityPlayer, type: IBaubleRender.RenderType, partialTicks: Float) {
        if (type == vazkii.botania.api.item.IBaubleRender.RenderType.BODY) {
            vazkii.botania.api.item.IBaubleRender.Helper.rotateIfSneaking(player)
            val armor = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST) != null
            GlStateManager.translate(0.0f, if (armor) -0.07f else -0.01f, 0.0f)
            val s = 0.0625f
            GlStateManager.scale(s, s, s)

            GlStateManager.enableLighting()
            GlStateManager.enableRescaleNormal()
            Minecraft.getMinecraft().renderEngine.bindTexture(this.getCloakTexture(stack))
            (model as ModelCloak).render(1.0f)
            val light = 15728880
            val lightmapX = light % 65536
            val lightmapY = light / 65536
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightmapX.toFloat(), lightmapY.toFloat())
            Minecraft.getMinecraft().renderEngine.bindTexture(this.getCloakGlowTexture(stack))
            (model as ModelCloak).render(1.0f)
        }

    }
}
