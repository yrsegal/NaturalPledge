package shadowfox.botanicaladdons.common.items.travel.bauble

import baubles.api.BaubleType
import com.mojang.authlib.GameProfile
import com.teamwizardry.librarianlib.features.base.item.ItemModBauble
import com.teamwizardry.librarianlib.features.kotlin.isNotEmpty
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
import shadowfox.botanicaladdons.common.items.base.ItemBaseBauble
import vazkii.botania.api.item.IBaubleRender
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.item.ModItems

/**
 * @author WireSegal
 * Created at 6:47 PM on 5/21/16.
 */
class ItemFoodBelt(name: String) : ItemBaseBauble(name), IBaubleRender {

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

    override fun getBaubleType(stack: ItemStack) = BaubleType.BELT

    override fun onWornTick(stack: ItemStack, player: EntityLivingBase) {
        if (player is EntityPlayer && !player.world.isRemote && player.ticksExisted % 20 == 0) {
            val foods = mutableMapOf<Int, ItemStack>()
            for (i in 0..8) {
                val food = player.inventory.getStackInSlot(i) ?: continue
                if (isEdible(food, player)
                        || (food.item == ModItems.infiniteFruit && ManaItemHandler.requestManaExact(food, player, 500, false)))
                    foods[i] = food
            }

            val food = foods.entries.sortedByDescending {
                when {
                    it.value.item is ItemFood -> (it.value.item as ItemFood).getSaturationModifier(it.value) * (it.value.item as ItemFood).getHealAmount(it.value)
                    it.value.item == ModItems.infiniteFruit -> Float.MAX_VALUE
                    else -> 0f
                }
            }.firstOrNull() ?: return

            if (food.value.item is ItemFood) {
                var newFood = food.value.onItemUseFinish(player.world, player)
                // Not sure if this is needed anymore
                if (newFood.isNotEmpty && newFood.count <= 0)
                    newFood = ItemStack.EMPTY
                player.inventory.setInventorySlotContents(food.key, newFood)
            } else if (food.value.item == ModItems.infiniteFruit) {
                ManaItemHandler.requestManaExact(food.value, player, 500, true)
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

    private fun isEdible(food: ItemStack, player: EntityPlayer): Boolean {
        if (food.isEmpty) return false
        if (!player.canEat(false)) return false

        var flag = false

        if (food.item is ItemFood) {

            if (BAMethodHandles.isAlwaysEdible(food.item as ItemFood)) return false

            flag = true
            for (i in 0..15) {
                val fakePlayer = FakePlayerPotion(player.world, GameProfile(null, "foodBeltPlayer"))
                fakePlayer.testFinishItemUse(food)

                var returnFlag = true

                if (fakePlayer.activePotionEffects.isNotEmpty())
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
            stack.copy().onItemUseFinish(world, this)
            captureSounds = false
        }

        override fun isSpectator() = false
        override fun isCreative() = false
    }
}
