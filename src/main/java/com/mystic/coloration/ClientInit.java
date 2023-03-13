package com.mystic.coloration;

import com.mystic.coloration.blocks.ColoredBlocks;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.stream.Stream;

@Mod.EventBusSubscriber(modid = "coloration", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientInit {

    @SubscribeEvent
    public static void onInitializeClient(FMLClientSetupEvent event) {
        registerBlockRenderLayers(RenderType.solid(), Coloration.COLORED_CUBE.get());
        registerBlockRenderLayers(RenderType.translucent(), Coloration.COLORED_AIR.get());
    }

    private static void registerBlockRenderLayers(RenderType layer, Block... blocks) {
        Stream.of(blocks).forEach(block -> ItemBlockRenderTypes.setRenderLayer(block, layer));
    }

    @SubscribeEvent
    public static void getBlockEntityColor(RegisterColorHandlersEvent.Block event) {
        event.register((blockState, blockAndTintGetter, blockPos, color) -> {
            if (blockAndTintGetter.getBlockEntity(blockPos) instanceof ColoredBlocks.ColoredCubeBlockEntity cubeBlockEntity) {
                cubeBlockEntity.sync();
                return cubeBlockEntity.color;
            } else {
                return 0x00000000;
            }
        }, Coloration.COLORED_CUBE.get(), Coloration.COLORED_AIR.get());
    }
}
