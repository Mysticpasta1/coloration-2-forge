package com.mystic.coloration.blocks;

import com.mystic.coloration.IColoredBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import java.util.List;

public class ColoredBlocks {
    public static IntegerProperty COLOR = IntegerProperty.create("color", 0xFFFFFFFF, 0x00000000);
    public static IntegerProperty TINT_INDEX = IntegerProperty.create("tint_index", 0, 3);

    public static class ColoredCubeBlock extends Block implements IColoredBlock {
        public ColoredCubeBlock(Properties properties) {
            super(properties.strength(4.0f).noOcclusion());
        }

        @Override
        public BlockState withColor(BlockState blockState, int color) {
            blockState =  blockState.setValue(COLOR, color);

            ModelResourceLocation modelLocation = new ModelResourceLocation(ModelLocationUtils.getModelLocation(blockState.getBlock()).getPath());
            BakedModel model = Minecraft.getInstance().getModelManager().getModel(modelLocation);
            for (Direction face : Direction.values()) {
                List<BakedQuad> quad = model.getQuads(blockState, face, RandomSource.create());
                for (BakedQuad bakedQuad : quad) {
                    if (bakedQuad != null && bakedQuad.getTintIndex() == 1) {
                        blockState = blockState.setValue(TINT_INDEX, 1);
                        break;
                    }
                }
            }

            return blockState;
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
}