package shadowfox.botanicaladdons.common.core

import net.minecraft.block.Block
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.common.items.ModItems
import java.util.*

/**
 * @author WireSegal
 * Created at 1:17 PM on 3/20/16.
 */
class CreativeTab : CreativeTabs(LibMisc.MOD_ID) {
    internal lateinit var list: MutableList<ItemStack>

    init {
        this.setNoTitle()
        this.backgroundImageName = "${LibMisc.MOD_ID}.png"
    }

    override fun getIconItemStack(): ItemStack {
        return ItemStack(ModItems.symbol)
    }

    override fun getTabIconItem(): Item {
        return this.iconItemStack.item
    }

    override fun hasSearchBar(): Boolean {
        return true
    }

    override fun displayAllRelevantItems(list: MutableList<ItemStack>) {
        this.list = list
        for (item in items)
            addItem(item)
    }

    private fun addItem(item: Item) {
        val tempList = mutableListOf<ItemStack>()
        item.getSubItems(item, this, tempList)
        this.list.addAll(tempList)
    }

    companion object {
        var INSTANCE = CreativeTab()

        private val items = ArrayList<Item>()

        fun set(block: Block) {
            if (Item.getItemFromBlock(block) != null)
                items.add(Item.getItemFromBlock(block))
            block.setCreativeTab(INSTANCE)
        }

        fun set(item: Item) {
            items.add(item)
            item.creativeTab = INSTANCE
        }
    }
}

