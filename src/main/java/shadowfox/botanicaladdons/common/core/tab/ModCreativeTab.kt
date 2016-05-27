package shadowfox.botanicaladdons.common.core.tab

import net.minecraft.block.Block
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import shadowfox.botanicaladdons.api.lib.LibMisc
import java.util.*

/**
 * @author WireSegal
 * Created at 1:17 PM on 3/20/16.
 */
abstract class ModCreativeTab(val name: String) : CreativeTabs("${LibMisc.MOD_ID}.$name") {
    internal lateinit var list: MutableList<ItemStack>

    init {
        this.setNoTitle()
        this.backgroundImageName = "${LibMisc.MOD_ID}/$name.png"
    }

    abstract override fun getIconItemStack(): ItemStack

    override fun getTabIconItem(): Item? {
        return this.iconItemStack.item
    }

    override fun hasSearchBar(): Boolean {
        return true
    }

    override fun displayAllRelevantItems(list: MutableList<ItemStack>) {
        this.list = list
        for (item in items)
            addItem(item)
        addEnchantmentBooksToList(list, *relevantEnchantmentTypes)
    }

    private fun addItem(item: Item) {
        val tempList = mutableListOf<ItemStack>()
        item.getSubItems(item, this, tempList)
        if (item == tabIconItem)
            this.list.addAll(0, tempList)
        else
            this.list.addAll(tempList)
    }

    private val items = ArrayList<Item>()

    fun set(block: Block) {
        val item = Item.getItemFromBlock(block) ?: return
        items.add(item)
        block.setCreativeTab(this)
    }

    fun set(item: Item) {
        items.add(item)
        item.creativeTab = this
    }
}

