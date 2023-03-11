package com.mystic.coloration;

import com.mystic.coloration.gui.ColorPickerGui;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MaterialColor;
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
            if (worldIn.isClientSide) {
                openColorPicker();
                return InteractionResult.SUCCESS;
            } else {

                BlockState blockState = worldIn.getBlockState(context.getClickedPos());
                Block block = blockState.getBlock();
                if (block instanceof IColoredBlock coloredBlock) {
                    // Get the RGBA color value from the held item stack's NBT tag
                    CompoundTag nbt = heldItemStack.getTag();
                    if (nbt != null && nbt.contains("color", CompoundTag.TAG_INT)) {
                        int color = getColor(heldItemStack);
                        worldIn.setBlock(context.getClickedPos(), coloredBlock.withColor(blockState, MaterialColor.byId(color)), 3);
                        heldItemStack.shrink(1);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }
        return InteractionResult.PASS;
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