package com.mystic.coloration;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MaterialColor;

public interface IColoredBlock {
    BlockState withColor(BlockState blockState, MaterialColor color);
}