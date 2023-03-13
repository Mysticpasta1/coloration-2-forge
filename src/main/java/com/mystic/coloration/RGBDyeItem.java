package com.mystic.coloration;

import com.mystic.coloration.blocks.ColoredBlocks;
import com.mystic.coloration.gui.ColorPickerGui;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class RGBDyeItem extends Item {

    public RGBDyeItem() {
        super(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS));
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Player playerIn = context.getPlayer();
        Level worldIn = context.getLevel();
        InteractionHand handIn = context.getHand();
        if (playerIn != null) {
            ItemStack heldItemStack = playerIn.getItemInHand(handIn);

            BlockState blockState = worldIn.getBlockState(context.getClickedPos());
            Block block = blockState.getBlock();
            if (block instanceof IColoredBlock) {
                // Get the RGBA color value from the held item stack's NBT tag
                CompoundTag nbt = heldItemStack.getTag();
                if (nbt != null && nbt.contains("color", CompoundTag.TAG_INT)) {
                    int color = getColor(heldItemStack);
                    if (color < 0) {
                        color = color * -1;
                    }

                    if (worldIn.getBlockEntity(context.getClickedPos()) instanceof ColoredBlocks.ColoredCubeBlockEntity cubeBlockEntity) {
                        cubeBlockEntity.sync();
                        cubeBlockEntity.color = color;
                        heldItemStack.shrink(1);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack heldItemStack = playerIn.getItemInHand(handIn);
        if (worldIn.isClientSide && playerIn.isShiftKeyDown()) {
            openColorPicker();
            return InteractionResultHolder.success(heldItemStack);
        }
        return InteractionResultHolder.pass(heldItemStack);
    }

    // Set the RGBA color value in the held item stack's NBT tag
    public static void setColor(ItemStack itemStack, int color) {
        CompoundTag nbt = itemStack.getOrCreateTag();
        nbt.putInt("color", color);
    }

    // Get the RGBA color value from the held item stack's NBT tag
    public static int getColor(ItemStack itemStack) {
        CompoundTag nbt = itemStack.getTag();
        if (nbt != null && nbt.contains("color", CompoundTag.TAG_INT)) {
            return nbt.getInt("color");
        }
        return 0xFFFFFFFF; // default to white if no color is set
    }

    private void openColorPicker() {
        Minecraft.getInstance().setScreen(new ColorPickerGui());
    }
}