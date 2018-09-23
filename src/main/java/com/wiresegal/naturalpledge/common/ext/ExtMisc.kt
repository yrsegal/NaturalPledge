package com.wiresegal.naturalpledge.common.ext

import vazkii.botania.common.entity.EntityDoppleganger

val EntityDoppleganger.isHardMode: Boolean
    get() = serializeNBT().getBoolean("hardMode")