package com.mystic.coloration;

import com.mystic.coloration.blocks.ColoredBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;
import java.util.function.Supplier;

@Mod("coloration")
public class Coloration {

    private static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "coloration");
    private static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "coloration");

    public static RegistryObject<Block> COLORED_CUBE;

    public Coloration() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        BLOCKS.register(bus);
        ITEMS.register(bus);

        COLORED_CUBE = registerBlock("colored_solid_block", () -> new ColoredBlocks.ColoredCubeBlock(BlockBehaviour.Properties.of(Material.STONE).sound(SoundType.STONE)));
        ITEMS.register("rgb_picker", RGBDyeItem::new);
    }

    private static RegistryObject<Block> registerBlock(String name, Supplier<Block> block) {
        return registerBlock(name, block, b -> () -> new BlockItem(b.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
    }

    private static RegistryObject<Block> registerBlock(String name, Supplier<Block> block, Function<RegistryObject<Block>, Supplier<? extends BlockItem>> item) {
        var reg = BLOCKS.register(name, block);
        ITEMS.register(name, () -> item.apply(reg).get());
        return reg;
    }
}
