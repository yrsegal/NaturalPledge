package com.wiresegal.naturalpledge.common.crafting.recipe

import net.minecraft.block.state.IBlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import vazkii.botania.api.recipe.RecipePureDaisy
import vazkii.botania.api.subtile.SubTileEntity

class RecipePureDaisyExclusion(input: Any, output: IBlockState) : RecipePureDaisy(input, output) {

    override fun matches(world: World, pos: BlockPos, pureDaisy: SubTileEntity, state: IBlockState): Boolean {
        return super.matches(world, pos, pureDaisy, state) && state != outputState
    }

}
