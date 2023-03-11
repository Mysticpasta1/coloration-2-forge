package com.mystic.coloration;

import com.mystic.coloration.blocks.ColoredBlocks;
import com.mystic.coloration.datagen.BlockTextureGenerator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelDataManager;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;

@Mod("coloration")
public class Coloration {

    DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "coloration");

    public Coloration() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::gatherData);
        BLOCKS.register(bus);

        for (Block block : ForgeRegistries.BLOCKS.getValues()) {
            RegistryObject<Block> dyeBlocks = block.
        }
    }

    public void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        if (event.includeServer()) {
            // Register data generators
            for (Block block1 : ForgeRegistries.BLOCKS.getValues()) {
                Block block = new ColoredBlocks.ColoredCubeBlock(block1);
                // ... create your block object ...
                BlockTextureGenerator textureGenerator = new BlockTextureGenerator(generator, block);
                generator.addProvider(true, new BlockModelProvider(event.getGenerator(), "coloration", event.getExistingFileHelper()) {
                    @Override
                    protected void registerModels() {
                        // Get the model data manager
                        ModelManager modelDataManager = Minecraft.getInstance().getModelManager();

                        // Get the model data for the original block
                        BakedModel originalModel = modelDataManager.getModel(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)));

                        // ... create your black and white texture ...
                        ResourceLocation blockTexture = BlockTextureGenerator.blockTexture;//TODO something with this

                        // Register the new model and texture for this block
                        ModelResourceLocation newModelLocation = getModels();
                        BakedModel newModel = new SimpleBakedModel.Builder().build();
                    }

                    private ModelResourceLocation getModels() {
                        ResourceLocation originalRegistryName = ForgeRegistries.BLOCKS.getKey(block);
                        if (originalRegistryName == null) {
                            throw new IllegalArgumentException("Original block has null registry name");
                        }
                        return new ModelResourceLocation(originalRegistryName, "normal");
                    }


                });
                generator.addProvider(true, textureGenerator);
            }
        }
    }
}
