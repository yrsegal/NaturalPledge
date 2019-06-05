package com.wiresegal.naturalpledge.common.core.tab

import com.teamwizardry.librarianlib.features.base.ModCreativeTab
import net.minecraft.enchantment.EnumEnchantmentType
import net.minecraft.item.ItemStack
import com.wiresegal.naturalpledge.api.lib.LibMisc
import com.wiresegal.naturalpledge.common.enchantment.ModEnchantments
import com.wiresegal.naturalpledge.common.items.ModItems

/**
 * @author WireSegal
 * Created at 1:05 PM on 5/17/16.
 */
object ModTab : ModCreativeTab("divine") {

    init {
        registerDefaultTab()
        setNoTitle()
        backgroundImageName = "${LibMisc.MOD_ID}/divine.png"
    }

    override fun hasSearchBar(): Boolean {
        return true
    }

    override val iconStack by lazy { ItemStack(ModItems.symbol) }

    override fun getRelevantEnchantmentTypes(): Array<out EnumEnchantmentType>? {
        return arrayOf(ModEnchantments.WEIGHTY)
    }
}
