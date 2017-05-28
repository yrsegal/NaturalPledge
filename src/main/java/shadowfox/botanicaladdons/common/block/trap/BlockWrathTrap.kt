package shadowfox.botanicaladdons.common.block.trap

import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.init.MobEffects
import net.minecraft.init.SoundEvents
import net.minecraft.potion.PotionEffect
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import shadowfox.botanicaladdons.common.lib.LibNames
import shadowfox.botanicaladdons.common.potions.ModPotions
import vazkii.botania.common.Botania
import java.util.*

/**
 * @author WireSegal
 * Created at 4:23 PM on 3/25/17.
 */
class BlockWrathTrap : BlockBaseTrap(LibNames.WRATH_TRAP) {
    override fun trapActivation(stateIn: IBlockState, world: World, pos: BlockPos, entityIn: EntityLivingBase) {
        entityIn.addPotionEffect(PotionEffect(ModPotions.faithlessness, 200))
        entityIn.addPotionEffect(PotionEffect(MobEffects.GLOWING, 200))
        world.playSound(null, pos, SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.BLOCKS, 1f, 1f)

        world.setBlockToAir(pos)
    }

    override fun particlesForSeer(stateIn: IBlockState, world: World, pos: BlockPos, rand: Random) {
        for (xS in 0..1) for (zS in 0..1)
            Botania.proxy.wispFX(xS.toDouble() + pos.x, pos.y + 0.5, zS.toDouble() + pos.z, R, G, B, 0.5f)
    }
}
