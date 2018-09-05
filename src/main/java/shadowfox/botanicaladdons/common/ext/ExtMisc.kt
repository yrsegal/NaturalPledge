package shadowfox.botanicaladdons.common.ext

import com.teamwizardry.librarianlib.features.kotlin.get
import vazkii.botania.common.entity.EntityDoppleganger

val EntityDoppleganger.isHardMode: Boolean
    get() = serializeNBT().getBoolean("hardMode")