package shadowfox.botanicaladdons.common.items.travel.bauble

import baubles.api.BaubleType
import com.mojang.authlib.GameProfile
import net.minecraft.client.Minecraft
import net.minecraft.client.model.ModelBiped
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.MobEffects
import net.minecraft.item.ItemFood
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.common.core.helper.BAMethodHandles
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
            val foods = mutableMapOf<Int, ItemStack>()
            for (i in 0..8) {
                val food = player.inventory.getStackInSlot(i) ?: continue
                if (isEdible(food, player)
                        || (food.item == ModItems.infiniteFruit && ManaItemHandler.requestManaExact(food, player, 500, false)))
                    foods.put(i, food)
            }

            val food = foods.entries.sortedByDescending {
                if (it.value.item is ItemFood) (it.value.item as ItemFood).getSaturationModifier(it.value) * (it.value.item as ItemFood).getHealAmount(it.value)
                else if (it.value.item == ModItems.infiniteFruit) Float.MAX_VALUE
                else 0f
            }.firstOrNull() ?: return

            if (food.value.item is ItemFood) {
                var newFood = food.value.onItemUseFinish(player.worldObj, player)
                if (newFood != null && newFood.stackSize <= 0)
                    newFood = null
                player.inventory.setInventorySlotContents(food.key, newFood)
            } else if (food.value.item == ModItems.infiniteFruit) {
                ManaItemHandler.requestManaExact(food.value, player, 500, false)
                for (i in 0 until 20) player.foodStats.addStats(1, 1f)
            }
        }
    }

    override fun onPlayerBaubleRender(stack: ItemStack, player: EntityPlayer, type: IBaubleRender.RenderType, partTicks: Float) {
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

    private fun isEdible(food: ItemStack?, player: EntityPlayer): Boolean {
        food ?: return false
        if (!player.canEat(false)) return false

        var flag = false

        if (food.item is ItemFood) {

            if (BAMethodHandles.isAlwaysEdible(food.item as ItemFood)) return false

            flag = true
            for (i in 0..15) {
                val fakePlayer = FakePlayerPotion(player.worldObj, GameProfile(null, "foodBeltPlayer"))
                fakePlayer.testFinishItemUse(food)

                var returnFlag = true

                if (fakePlayer.activePotionEffects.size > 0)
                    returnFlag = returnFlag && fakePlayer.isPotionActive(MobEffects.SATURATION) && fakePlayer.activePotionEffects.size == 1
                if (fakePlayer.position.x != 0 && fakePlayer.position.y != 1000 && fakePlayer.position.z != 0)
                    returnFlag = false
                flag = flag && returnFlag

                if (!flag) return false
            }
        }
        return flag
    }

    class FakePlayerPotion(world: World, profile: GameProfile) : EntityPlayer(world, profile) {

        init {
            setPosition(0.0, 1000.0, 0.0)
        }

        fun testFinishItemUse(stack: ItemStack) {
            captureSounds = true
            stack.copy().onItemUseFinish(worldObj, this)
            captureSounds = false
        }

        override fun canCommandSenderUseCommand(permLevel: Int, commandName: String?) = false
        override fun isSpectator() = false
        override fun isCreative() = false

    }
}
