package com.mystic.coloration.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mystic.coloration.RGBDyeItem;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.widget.ForgeSlider;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
public class ColorPickerGui extends Screen {

    public static final int DEFAULT_COLOR = 0xFFFFFFFF; // Default color is white
    private int color;
    private static ForgeSlider redSlider;
    private static ForgeSlider greenSlider;
    private static ForgeSlider blueSlider;
    private static ForgeSlider alphaSlider;
    private static Button doneButton;

    public ColorPickerGui() {
        super(Component.translatable("gui.color_picker.title"));
    }

    public ColorPickerGui(int color) {
        super(Component.translatable("gui.color_picker.title"));
        this.color = color;
    }

    @Override
    protected void init() {
        int x = (this.width - 200) / 2;
        int y = (this.height - 190) / 2;
        Color color2 = new Color(color);

        redSlider = new ForgeSlider(x + 10, y + 30, 180, 20, Component.translatable("gui.color_picker.red"), Component.empty(), 0, 255, color2.getRed(), 1, 1, true);
        this.addWidget(redSlider);

        greenSlider = new ForgeSlider(x + 10, y + 55, 180, 20, Component.translatable("gui.color_picker.green"), Component.empty(), 0, 255, color2.getGreen(), 1, 1, true);
        this.addWidget(greenSlider);

        blueSlider = new ForgeSlider(x + 10, y + 80, 180, 20, Component.translatable("gui.color_picker.blue"), Component.empty(), 0, 255, color2.getBlue(), 1, 1, true);
        this.addWidget(blueSlider);

        doneButton = this.addWidget(new Button(x + 60, y + 205, 80, 20, Component.translatable("gui.done"), button -> {
            if(this.minecraft != null) {
                int color = ColorPickerGui.getColor();
                if (ColorPickerGui.getColor() < 0) {
                    color = ColorPickerGui.getColor() * -1;
                }
                if (this.minecraft.player != null) {
                    RGBDyeItem.setColor(this.minecraft.player.getItemInHand(minecraft.player.getUsedItemHand()), color);
                }
                this.minecraft.setScreen(null);
            }
        }));
        this.setFocused(doneButton);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        drawCenteredString(matrixStack, this.font, I18n.get("gui.color_picker.title"), this.width / 2, this.height / 4 - 40, 16777215);

        doneButton.render(matrixStack, mouseX, mouseY, partialTicks);

        redSlider.render(matrixStack, mouseX, mouseY, partialTicks);
        greenSlider.render(matrixStack, mouseX, mouseY, partialTicks);
        blueSlider.render(matrixStack, mouseX, mouseY, partialTicks);

        int r = Mth.clamp((int) redSlider.getValue(), 0, 255);
        int g = Mth.clamp((int) greenSlider.getValue(), 0, 255);
        int b = Mth.clamp((int) blueSlider.getValue(), 0, 255);

        RenderSystem.enableBlend();
        RenderSystem.disableTexture();

        // Draw color preview box
        fillGradient(matrixStack, this.width / 2 + 40, this.height / 2 - 20, this.width / 2 + 70, this.height / 2 + 10, r, g, b);

        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public static void fillGradient(PoseStack matrixStack, int x1, int y1, int x2, int y2, int r, int g, int b) {
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        bufferbuilder.vertex(matrixStack.last().pose(), x2, y1, 0.0f).color(r, g, b, 255).endVertex();
        bufferbuilder.vertex(matrixStack.last().pose(), x1, y1, 0.0f).color(r, g, b, 255).endVertex();
        bufferbuilder.vertex(matrixStack.last().pose(), x1, y2, 0.0f).color(r, g, b, 255).endVertex();
        bufferbuilder.vertex(matrixStack.last().pose(), x2, y2, 0.0f).color(r, g, b, 255).endVertex();
        tessellator.end();
        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
    }

    public static int getColor() {
        int r = Mth.clamp((int) redSlider.getValue(), 0, 255);
        int g = Mth.clamp((int) greenSlider.getValue(), 0, 255);
        int b = Mth.clamp((int) blueSlider.getValue(), 0, 255);
        int color = (r << 16) | (g << 8) | b;

        if(color < 0) {
            color = color * -1;
        }

        return color;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (GuiEventListener widget : this.children()) {
            if (widget.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}