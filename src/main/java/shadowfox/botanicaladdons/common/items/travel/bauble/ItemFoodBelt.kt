package shadowfox.botanicaladdons.common.items.travel.bauble

import baubles.api.BaubleType
import com.mojang.authlib.GameProfile
import net.minecraft.client.Minecraft
import net.minecraft.client.model.ModelBiped
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.MobEffects
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemFood
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect
import net.minecraft.stats.StatBase
import net.minecraft.stats.StatList
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.chunk.IChunkProvider
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.ReflectionHelper
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.common.items.base.ItemModBauble
import vazkii.botania.api.item.IBaubleRender
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.item.ModItems

/**
 * @author WireSegal
 * Created at 6:47 PM on 5/21/16.
 */
class ItemFoodBelt(name: String) : ItemModBauble(name), IBaubleRender {

    companion object {
        val beltTexture = ResourceLocation(LibMisc.MOD_ID, "textures/model/foodbelt.png")

        init {
            MinecraftForge.EVENT_BUS.register(this)
        }

        var captureSounds = false

        @SubscribeEvent
        fun onSoundPlayed(e: PlaySoundAtEntityEvent) {
            if (captureSounds) e.isCanceled = true
        }
    }

    override fun getBaubleType(p0: ItemStack?) = BaubleType.BELT

    override fun onWornTick(stack: ItemStack, player: EntityLivingBase) {
        if (player is EntityPlayer && !player.worldObj.isRemote && player.ticksExisted % 20 === 0) {
            val foods = mutableListOf<Pair<Int, ItemStack>>()
            for (i in 0..8) {
                val food = player.inventory.getStackInSlot(i) ?: continue
                if (isEdible(food, player)) {
                    foods.add(Pair(i, food))
                } else if (food.item == ModItems.infiniteFruit) {
                    if (ManaItemHandler.requestManaExact(food, player, 500, false)) {
                        foods.add(Pair(i, food))
                    }
                }
            }
            if (foods.size == 0)
                return
            val food = foods.sortedByDescending {
                if (it.second.item is ItemFood) (it.second.item as ItemFood).getSaturationModifier(it.second) * (it.second.item as ItemFood).getHealAmount(it.second)
                else if (it.second.item == ModItems.infiniteFruit) Float.MAX_VALUE
                else 0f
            }[0]

            if (food.second.item is ItemFood) {
                var newFood = food.second.onItemUseFinish(player.worldObj, player)
                if (newFood != null && newFood.stackSize <= 0)
                    newFood = null
                player.inventory.setInventorySlotContents(food.first, newFood)
            } else if (food.second.item == ModItems.infiniteFruit) {
                ManaItemHandler.requestManaExact(food.second, player, 500, false)
                val amountNeeded = 20 - player.foodStats.foodLevel
                for (i in 1..amountNeeded) player.foodStats.addStats(1, 1f)
            }
        }
    }

    override fun onPlayerBaubleRender(stack: ItemStack, player: EntityPlayer, type: IBaubleRender.RenderType, partTicks: Float) {
        if (type == IBaubleRender.RenderType.BODY) {

            if (ItemToolbelt.model == null)
                ItemToolbelt.model = ModelBiped()

            Minecraft.getMinecraft().renderEngine.bindTexture(beltTexture)
            if (player.isSneaking)
                GlStateManager.translate(0f, 0.3f, 0f)
            IBaubleRender.Helper.rotateIfSneaking(player)

            if (!player.isSneaking)
                GlStateManager.translate(0F, 0.2F, 0F)

            val s = 1.05F / 16F
            GlStateManager.scale(s, s, s)

            (ItemToolbelt.model as ModelBiped).bipedBody.render(1F)
        }
    }

    private fun isEdible(food: ItemStack?, player: EntityPlayer): Boolean {
        if (food == null) return false
        if (!player.canEat(false)) return false

        var flag = false

        if (food.item is ItemFood) {
            flag = true
            for (i in 0..15) {
                val fakePlayer = FakePlayerPotion(player.worldObj, GameProfile(null, "foodBeltPlayer"))
                fakePlayer.setPosition(0.0, 999.0, 0.0)
                captureSounds = true
                food.copy().onItemUseFinish(player.worldObj, fakePlayer)
                captureSounds = false

                var returnFlag = true

                if (fakePlayer.activePotionEffects.size > 0)
                    returnFlag = returnFlag && fakePlayer.isPotionActive(MobEffects.SATURATION) && fakePlayer.activePotionEffects.size == 1
                if (fakePlayer.position.x != 0 && fakePlayer.position.y != 999 && fakePlayer.position.z != 0)
                    returnFlag = false
                flag = flag && returnFlag

                if (!flag) return false
            }
        }
        return flag
    }

    class FakePlayerPotion(world: World, profile: GameProfile) : EntityPlayer(world, profile) {

        override fun onNewPotionEffect(par1PotionEffect: PotionEffect) {
            if (!this.worldObj.isRemote)
                par1PotionEffect.potion.applyAttributesModifiersToEntity(this, this.attributeMap, par1PotionEffect.amplifier)
        }
        override fun canCommandSenderUseCommand(permLevel: Int, commandName: String?) = false

        override fun addStat(p_addStat_1_: StatBase?) {}

        override fun getPosition() = BlockPos(this.posX, this.posY + 0.5, this.posZ)
        override fun isSpectator() = false
        override fun isCreative() = false

    }
}
