package com.mystic.coloration;

import net.minecraft.world.level.block.state.BlockState;

public interface IColoredBlock {
    BlockState withColor(BlockState blockState, int color);
}