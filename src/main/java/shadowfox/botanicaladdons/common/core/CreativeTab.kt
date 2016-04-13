package shadowfox.botanicaladdons.common.core

import net.minecraft.block.Block
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import vazkii.botania.common.item.ModItems
import vazkii.botania.common.item.lens.ItemLens
import shadowfox.botanicaladdons.common.lib.LibMisc
import java.util.*

/**
 * @author WireSegal
 * Created at 1:17 PM on 3/20/16.
 */
class CreativeTab : CreativeTabs(LibMisc.MOD_ID) {
    internal lateinit var list: List<ItemStack>

    init {
        this.setNoTitle()
        this.backgroundImageName = "${LibMisc.MOD_ID}.png"
    }

    override fun getIconItemStack(): ItemStack {
        return ItemStack(ModItems.lens, 1, ItemLens.PAINT) //TODO
    }

    override fun getTabIconItem(): Item {
        return this.iconItemStack.item
    }

    override fun hasSearchBar(): Boolean {
        return true
    }

    override fun displayAllRelevantItems(list: List<ItemStack>) {
        this.list = list
        for (item in items)
            addItem(item)
    }

    private fun addItem(item: Item) {
        item.getSubItems(item, this, this.list)
    }

    companion object {
        var INSTANCE = CreativeTab()

        private val items = ArrayList<Item>()

        fun set(block: Block) {
            items.add(Item.getItemFromBlock(block))
        }

        fun set(item: Item) {
            items.add(item)
        }
    }
}

