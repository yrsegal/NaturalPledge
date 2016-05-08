package shadowfox.botanicaladdons.common.block.tile

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ITickable
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.text.translation.I18n
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL12
import shadowfox.botanicaladdons.common.block.BlockSoulSuffuser
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.internal.VanillaPacketDispatcher
import vazkii.botania.api.mana.IManaReceiver
import vazkii.botania.api.recipe.RecipeRuneAltar
import vazkii.botania.api.sound.BotaniaSoundEvents
import vazkii.botania.client.core.handler.HUDHandler
import vazkii.botania.client.core.helper.RenderHelper
import vazkii.botania.common.Botania
import shadowfox.botanicaladdons.common.block.ModBlocks
import vazkii.botania.common.block.ModBlocks as BotaniaBlocks
import vazkii.botania.common.block.tile.TileAltar
import vazkii.botania.common.core.helper.Vector3
import vazkii.botania.common.item.ModItems
import java.util.*

/**
 * @author WireSegal
 * Created at 3:48 PM on 4/26/16.
 */
class TileSuffuser : TileSimpleInventory(), IManaReceiver, ITickable {
    private val TAG_MANA = "mana"
    private val TAG_MANA_TO_GET = "manaToGet"

    override val sizeInventory: Int = 16
    var manaToGet = 0
    private var mana = 0
    private var cooldown = 0
    var signal = 0

    var currentEmblem: ItemStack? = null
    internal var currentRecipe: RecipeRuneAltar? = null
    var lastRecipe: MutableList<ItemStack>? = null
    var recipeKeepTicks = 0

    override fun getRenderBoundingBox() = INFINITE_EXTENT_AABB

    fun addItem(player: EntityPlayer?, stack: ItemStack): Boolean {
        if (cooldown > 0 || stack.item === ModItems.twigWand || stack.item === ModItems.lexicon)
            return false

        if (stack.item === Item.getItemFromBlock(BotaniaBlocks.livingrock) && stack.itemDamage == 0) {
            if (player == null || !player.capabilities.isCreativeMode) {
                stack.stackSize--
                if (stack.stackSize == 0 && player != null)
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, null)
            }

            val item = EntityItem(worldObj, getPos().x + 0.5, (getPos().y + 1).toDouble(), getPos().z + 0.5, ItemStack(BotaniaBlocks.livingrock))
            item.setPickupDelay(40)
            item.motionX = 0.0
            item.motionY = 0.0
            item.motionZ = 0.0
            if (!worldObj.isRemote)
                worldObj.spawnEntityInWorld(item)

            return true
        }

        if (manaToGet != 0)
            return false

        var did = false

        for (i in 0..sizeInventory - 1)
            if (itemHandler.getStackInSlot(i) == null) {
                did = true
                val stackToAdd = stack.copy()
                stackToAdd.stackSize = 1
                itemHandler.setStackInSlot(i, stackToAdd)

                if (player == null || !player.capabilities.isCreativeMode) {
                    stack.stackSize--
                    if (stack.stackSize == 0 && player != null)
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, null)
                }

                break
            }

        if (did)
            VanillaPacketDispatcher.dispatchTEToNearbyPlayers(worldObj, pos)

        return true
    }

    override fun update() = updateEntity()

    fun updateEntity() {

        // Update every tick.
        recieveMana(0)

        if (!worldObj.isRemote && manaToGet == 0) {
            val items = worldObj.getEntitiesWithinAABB(EntityItem::class.java, AxisAlignedBB(pos, pos.add(1.0, 2.0, 1.0)))
            for (item in items)
                if (!item.isDead && item.entityItem != null && item.entityItem.item !== Item.getItemFromBlock(BotaniaBlocks.livingrock)) {
                    val stack = item.entityItem
                    if (addItem(null, stack) && stack.stackSize == 0)
                        item.setDead()
                }
        }


        if (worldObj.isRemote)
            markDirty()
            if ( manaToGet > 0 && mana >= manaToGet) {
                if (worldObj.rand.nextInt(20) == 0) {
                    val vec = Vector3.fromTileEntityCenter(this)
                    val endVec = vec.copy().add(0.0, 2.5, 0.0)
                    Botania.proxy.lightningFX(worldObj, vec, endVec, 2f, 0x00948B, 0x00E4D7)
                }
            }

        if (cooldown > 0) {
            cooldown--
            Botania.proxy.wispFX(world, pos.x + Math.random(), pos.y + 0.8, pos.z + Math.random(), 0.2f, 0.2f, 0.2f, 0.2f, -0.025f)
        }

        var newSignal = 0
        if (manaToGet > 0) {
            newSignal++
            if (mana >= manaToGet)
                newSignal++
        }

        if (newSignal != signal) {
            signal = newSignal
            worldObj.updateComparatorOutputLevel(pos, worldObj.getBlockState(pos).block)
        }

        if (recipeKeepTicks > 0)
            --recipeKeepTicks
        else
            lastRecipe = null

        manaToGet = if (currentRecipe != null) currentRecipe!!.manaUsage else 0

        if (manaToGet > 0 && mana >= manaToGet) {
            if (isDendric()) {

            } else {
                finishRunic()
            }
        }
    }

    fun startSuffuser() {
        val _manaToGet = this.manaToGet

        this.manaToGet = 0
        if (currentRecipe != null)
            this.manaToGet = currentRecipe!!.manaUsage
        else {

            if (isDendric()) {
                //TODO: something here
            } else {
                for (recipe in BotaniaAPI.runeAltarRecipes)
                    if (recipe.matches(itemHandler)) {
                        currentRecipe = recipe
                        this.manaToGet = recipe.manaUsage
                        break
                    }
            }
        }

        if (_manaToGet != this.manaToGet) {
            worldObj.playSound(null, pos, BotaniaSoundEvents.runeAltarStart, SoundCategory.BLOCKS, 1f, 1f)
            VanillaPacketDispatcher.dispatchTEToNearbyPlayers(worldObj, pos)
        }
    }

    fun saveLastRecipe() {
        lastRecipe = ArrayList<ItemStack>()
        for (i in 0..sizeInventory - 1) {
            val stack = itemHandler.getStackInSlot(i) ?: break
            lastRecipe!!.add(stack.copy())
        }
        recipeKeepTicks = 400
    }

    fun trySetLastRecipe(player: EntityPlayer) {
        TileAltar.tryToSetLastRecipe(player, itemHandler, lastRecipe)
        if (!isEmpty())
            VanillaPacketDispatcher.dispatchTEToNearbyPlayers(worldObj, pos)
    }

    fun isDendric() = BlockSoulSuffuser.isDendric(world.getBlockState(getPos()))

    fun finishDendric() {
        //TODO: something here
    }

    fun finishRunic() {
        var recipe: RecipeRuneAltar? = null

        if (currentRecipe != null)
            recipe = currentRecipe
        else
            for (recipe_ in BotaniaAPI.runeAltarRecipes) {
                if (recipe_.matches(itemHandler)) {
                    recipe = recipe_
                    break
                }
            }

        if (manaToGet > 0 && mana >= manaToGet && recipe != null) {
            recieveMana(-recipe.manaUsage)

            if (!worldObj.isRemote) {
                val output = recipe.output.copy()
                val outputItem = EntityItem(worldObj, pos.x + 0.5, pos.y + 1.5, pos.z + 0.5, output)
                worldObj.spawnEntityInWorld(outputItem)

                currentRecipe = null
                cooldown = 60
            }

            saveLastRecipe()
            if (!worldObj.isRemote) {
                for (i in 0..sizeInventory - 1) {
                    val stack = itemHandler.extractItem(i, 1, false)
                    if (stack != null) {
                        if (stack.item === ModItems.rune) {
                            val outputItem = EntityItem(worldObj, getPos().x + 0.5, getPos().y + 1.5, getPos().z + 0.5, stack.copy())
                            worldObj.spawnEntityInWorld(outputItem)
                        }
                    }
                }

                VanillaPacketDispatcher.dispatchTEToNearbyPlayers(worldObj, pos);
            }
            craftingFanciness()
        }

    }

    fun craftingFanciness() {
        worldObj.playSound(null, pos, BotaniaSoundEvents.runeAltarCraft, SoundCategory.BLOCKS, 1f, 1f)
        for (i in 0..24) {
            val red = Math.random().toFloat()
            val green = Math.random().toFloat()
            val blue = Math.random().toFloat()
            Botania.proxy.sparkleFX(worldObj, pos.x.toDouble() + 0.5 + Math.random() * 0.4 - 0.2, (pos.y + 1).toDouble(), pos.z.toDouble() + 0.5 + Math.random() * 0.4 - 0.2, red, green, blue, Math.random().toFloat(), 10)
        }
    }

    fun isEmpty(): Boolean {
        for (i in 0..itemHandler.slots - 1)
            if (itemHandler.getStackInSlot(i) != null)
                return false

        return true
    }

    override fun writeCustomNBT(cmp: NBTTagCompound) {
        cmp.setInteger(TAG_MANA, mana)
        cmp.setInteger(TAG_MANA_TO_GET, manaToGet)
        super.writeCustomNBT(cmp)

    }

    override fun readCustomNBT(cmp: NBTTagCompound) {
        super.readCustomNBT(cmp)
        mana = cmp.getInteger(TAG_MANA)
        manaToGet = cmp.getInteger(TAG_MANA_TO_GET)
    }

    override fun createItemHandler() = object : TileSimpleInventory.SimpleItemStackHandler(this, true) {

        override fun getStackLimit(slot: Int, stack: ItemStack?): Int {
            return super.getStackLimit(slot, stack)
        }

    }

    override fun getCurrentMana() = mana


    override fun isFull(): Boolean {
        return mana >= manaToGet
    }

    override fun recieveMana(mana: Int) {
        this.mana = Math.min(this.mana + mana, manaToGet)
    }

    override fun canRecieveManaFromBursts() = !isFull

    fun renderHUD(mc: Minecraft, res: ScaledResolution) {
        val xc = res.scaledWidth / 2
        val yc = res.scaledHeight / 2

        var angle = -90f
        val radius = 24
        var amt = 0
        for (i in 0..sizeInventory - 1) {
            if (itemHandler.getStackInSlot(i) == null)
                break
            amt++
        }

        if (amt > 0) {
            val anglePer = 360f / amt
            for (recipe in BotaniaAPI.runeAltarRecipes)
                if (recipe.matches(itemHandler)) {
                    GL11.glEnable(GL11.GL_BLEND)
                    GL11.glEnable(GL12.GL_RESCALE_NORMAL)
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

                    recipe.output
                    val progress = mana.toFloat() / manaToGet.toFloat()

                    mc.renderEngine.bindTexture(HUDHandler.manaBar)
                    GL11.glColor4f(1f, 1f, 1f, 1f)
                    RenderHelper.drawTexturedModalRect(xc + radius + 9, yc - 8, 0f, if (progress == 1f) 0 else 22, 8, 22, 15)

                    net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting()
                    if (progress == 1f) {
                        mc.renderItem.renderItemIntoGUI(ItemStack(BotaniaBlocks.livingrock), xc + radius + 16, yc + 8)
                        GL11.glTranslatef(0f, 0f, 100f)
                        mc.renderItem.renderItemIntoGUI(ItemStack(ModItems.twigWand), xc + radius + 24, yc + 8)
                        GL11.glTranslatef(0f, 0f, -100f)
                    }

                    RenderHelper.renderProgressPie(xc + radius + 32, yc - 8, progress, recipe.output)
                    net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting()

                    if (progress == 1f)
                        mc.fontRendererObj.drawStringWithShadow("+", xc + radius + 14.toFloat(), (yc + 12).toFloat(), 0xFFFFFF)
                }

            net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting()
            for (i in 0..amt - 1) {
                val xPos = xc + Math.cos(angle * Math.PI / 180.0) * radius - 8
                val yPos = yc + Math.sin(angle * Math.PI / 180.0) * radius - 8
                GL11.glTranslated(xPos, yPos, 0.0)
                mc.renderItem.renderItemIntoGUI(itemHandler.getStackInSlot(i), 0, 0)
                GL11.glTranslated(-xPos, -yPos, 0.0)

                angle += anglePer
            }
            net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting()
        } else if (recipeKeepTicks > 0) {
            var s = I18n.translateToLocal("botaniamisc.altarRefill0")
            mc.fontRendererObj.drawStringWithShadow(s, (xc - mc.fontRendererObj.getStringWidth(s) / 2).toFloat(), (yc + 10).toFloat(), 0xFFFFFF)
            s = I18n.translateToLocal("botaniamisc.altarRefill1")
            mc.fontRendererObj.drawStringWithShadow(s, (xc - mc.fontRendererObj.getStringWidth(s) / 2).toFloat(), (yc + 20).toFloat(), 0xFFFFFF)
        }
    }

    fun getTargetMana(): Int = manaToGet

}
