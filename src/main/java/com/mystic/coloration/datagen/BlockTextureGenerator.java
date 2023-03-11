package com.mystic.coloration.datagen;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.IOException;
import java.nio.file.Path;

public class BlockTextureGenerator implements DataProvider{
    private final Block block;
    private final DataGenerator generator;

    public static ResourceLocation blockTexture;

    public BlockTextureGenerator(DataGenerator generator, Block block) {
        this.generator = generator;
        this.block = block;
    }

    @Override
    public void run(CachedOutput cachedOutput) throws IOException {
        ResourceLocation originalTexture = ForgeRegistries.BLOCKS.getKey(block);
        ResourceLocation bwTexture = new ResourceLocation("coloration", "textures/block/" + originalTexture.getPath() + "_bw.png");

        // Get the original image
        NativeImage originalImage = NativeImage.read(Minecraft.getInstance().getResourceManager().getResource(originalTexture).get().open());

        // Create a new image for the black and white version
        NativeImage bwImage = new NativeImage(originalImage.getWidth(), originalImage.getHeight(), false);

        // Convert the original image to black and white
        for (int y = 0; y < originalImage.getHeight(); y++) {
            for (int x = 0; x < originalImage.getWidth(); x++) {
                int pixel = originalImage.getPixelRGBA(x, y);
                int grayscale = (int) (0.299 * ((pixel >> 16) & 0xff) + 0.587 * ((pixel >> 8) & 0xff) + 0.114 * (pixel & 0xff));
                bwImage.setPixelRGBA(x, y, (grayscale << 16) | (grayscale << 8) | grayscale);
            }
        }

        // Write the black and white image to a file
        Path output = Path.of(bwTexture.getPath());
        bwImage.writeToFile(output.toFile());

        blockTexture = bwTexture;
    }

    @Override
    public String getName() {
        return "BlockTextureDataGen";
    }
}