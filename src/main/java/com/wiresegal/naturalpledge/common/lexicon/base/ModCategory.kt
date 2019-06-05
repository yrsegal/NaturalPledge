package com.wiresegal.naturalpledge.common.lexicon.base

import net.minecraft.util.ResourceLocation
import com.wiresegal.naturalpledge.api.lib.LibMisc
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.lexicon.LexiconCategory

class ModCategory(unlocalizedName: String, priority: Int) : LexiconCategory("${LibMisc.MOD_ID}.category." + unlocalizedName) {

    init {
        icon = ResourceLocation(LibMisc.MOD_ID, "textures/gui/categories/$unlocalizedName.png")
        setPriority(priority)
        BotaniaAPI.addCategory(this)
    }

}
