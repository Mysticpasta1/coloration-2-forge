package com.mystic.coloration;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface IColoredBlock {
    BlockState withColor(BlockState blockState, int color, Level level, BlockPos blockPos);
}