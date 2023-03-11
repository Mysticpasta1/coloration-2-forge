package com.mystic.coloration.blocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.ForgeRegistries;

public class ColoredBlocks {
    public static class ColoredCubeBlock extends Block {
        public ColoredCubeBlock(Block block) {
            super(Properties.copy(block));
        }
    }

    public static class ColoredStairsBlock extends StairBlock {
        public ColoredStairsBlock(StairBlock block) {
            super(block::defaultBlockState, Properties.copy(block));
        }
    }

    public static class ColoredSlabBlock extends SlabBlock {
        public ColoredSlabBlock(SlabBlock block) {
            super(Properties.copy(block));
        }
    }

    public static class ColoredFenceBlock extends FenceBlock {
        public ColoredFenceBlock(SlabBlock block) {
            super(BlockBehaviour.Properties.copy(block));
        }
    }

    public static class ColoredWallBlock extends WallBlock {
        public ColoredWallBlock(SlabBlock block) {
            super(BlockBehaviour.Properties.copy(block));
        }
    }
}