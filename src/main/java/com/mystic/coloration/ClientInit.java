package com.mystic.coloration;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.stream.Stream;

@Mod.EventBusSubscriber(modid = "coloration", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientInit {

    @SubscribeEvent
    public static void onInitializeClient(FMLClientSetupEvent event) {
        registerBlockRenderLayers(RenderType.translucent(), Coloration.COLORED_CUBE.get());
    }

    private static void registerBlockRenderLayers(RenderType layer, Block... blocks) {
        Stream.of(blocks).forEach(block -> ItemBlockRenderTypes.setRenderLayer(block, layer));
    }
}
