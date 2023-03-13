package com.mystic.coloration.blocks;

import com.mystic.coloration.Coloration;
import com.mystic.coloration.IColoredBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ColoredBlocks {
    public static class ColoredCubeBlock extends BaseEntityBlock implements IColoredBlock {

        public ColoredCubeBlock() {
            super(Properties.copy(Blocks.STONE).lootFrom(() -> Blocks.STONE));
        }

        @Override
        public RenderShape getRenderShape(BlockState blockState) {
            return RenderShape.MODEL;
        }

        @Override
        public BlockState withColor(BlockState blockState, int color, Level level, BlockPos blockPos) {
            return blockState;
        }

        @org.jetbrains.annotations.Nullable
        @Override
        public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
            return new ColoredCubeBlockEntity(blockPos, blockState);
        }
    }

    public static class ColoredGasBlock extends BaseEntityBlock implements IColoredBlock {

        public ColoredGasBlock() {
            super(Properties.copy(Blocks.AIR));
        }

        @Override
        public RenderShape getRenderShape(BlockState blockState) {
            return RenderShape.MODEL;
        }

        @Override
        public BlockState withColor(BlockState blockState, int color, Level level, BlockPos blockPos) {
            return blockState;
        }

        @org.jetbrains.annotations.Nullable
        @Override
        public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
            return new ColoredCubeBlockEntity(blockPos, blockState);
        }
    }

    public static class ColoredCubeBlockEntity extends BlockEntity {

        public static int color = 0;

        public ColoredCubeBlockEntity(BlockPos pos, BlockState state) {
            super(Coloration.COLORED_CUBE_ENTITY.get(), pos, state);
        }

        public void readNbt(CompoundTag nbt) {
            if(nbt != null) {
                if (nbt.contains("color", CompoundTag.TAG_INT)) {
                    color = nbt.getInt("color");
                }
            }
        }
        public void writeNbt(CompoundTag nbt) {
            nbt.putInt("color", color);
        }

        public void sync() {
            setChanged();
            if(getLevel() != null)
                getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 2);
        }

        @Override
        public void load(@NotNull CompoundTag nbt) {
            super.load(nbt);
            nbt.putInt("color", color);
            readNbt(nbt);
        }

        @Override
        protected void saveAdditional(@NotNull CompoundTag nbt) {
            super.saveAdditional(nbt);
            if (nbt.contains("color", CompoundTag.TAG_INT)) {
                nbt.getInt("color");
            }
            writeNbt(nbt);
        }

        @Override
        public @NotNull CompoundTag getUpdateTag() {
            CompoundTag tag = new CompoundTag();
            saveAdditional(tag);
            return tag;
        }

        @Nullable
        @Override
        public Packet<ClientGamePacketListener> getUpdatePacket() {
            return ClientboundBlockEntityDataPacket.create(this);
        }
    }
}