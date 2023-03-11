package com.mystic.coloration.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraftforge.client.gui.widget.ForgeSlider;

public class ColorPickerGui extends Screen {

    private final int DEFAULT_COLOR = 0xFFFFFFFF; // Default color is white
    private int color;
    private Runnable callback;
    private ForgeSlider redSlider;
    private ForgeSlider greenSlider;
    private ForgeSlider blueSlider;
    private ForgeSlider alphaSlider;

    public ColorPickerGui() {
        super(Component.translatable("gui.color_picker.title"));
    }

    public ColorPickerGui(int color, Runnable callback) {
        super(Component.translatable("gui.color_picker.title"));
        this.color = color;
        this.callback = callback;
    }

    @Override
    protected void init() {
        int x = (this.width - 200) / 2;
        int y = (this.height - 160) / 2;

        redSlider = new ForgeSlider(x + 10, y + 30, 180, 20, Component.translatable("gui.color_picker.red"), Component.empty(), 0, 255, (color & 0xFF000000) >>> 24, 1, 1, false);
        this.addWidget(redSlider);

        greenSlider = new ForgeSlider(x + 10, y + 55, 180, 20, Component.translatable("gui.color_picker.green"), Component.empty(), 0, 255, (color & 0x00FF0000) >>> 16, 1, 1, false);
        this.addWidget(greenSlider);

        blueSlider = new ForgeSlider(x + 10, y + 80, 180, 20, Component.translatable("gui.color_picker.blue"), Component.empty(), 0, 255, (color & 0x0000FF00) >>> 8, 1, 1, false);
        this.addWidget(blueSlider);

        alphaSlider = new ForgeSlider(x + 10, y + 105, 180, 20, Component.translatable("gui.color_picker.alpha"), Component.empty(), 0, 255, (color & 0x000000FF), 1, 1, false);
        this.addWidget(alphaSlider);

        Button doneButton = this.addWidget(new Button(x + 60, y + 130, 80, 20, Component.translatable("gui.done"), (p_214318_1_) -> {
            callback.run();
            this.minecraft.setScreen(null);
        }));
        this.setFocused(doneButton);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        drawCenteredString(matrixStack, this.font, I18n.get("gui.color_picker.title"), this.width / 2, this.height / 4 - 40, 16777215);

        int r = (int) redSlider.getValue();
        int g = (int) greenSlider.getValue();
        int b = (int) blueSlider.getValue();
        int a = (int) alphaSlider.getValue();
        int color = (r << 24) | (g << 16) | (b << 8) | a;
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();

        // Draw color preview box
        fillGradient(matrixStack, this.width / 2 + 40, this.height / 2 - 20, this.width / 2 + 70, this.height / 2 + 10, color, color);

        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    @Override
    public void onClose() {
        super.onClose();
        this.callback.run();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void fillGradient(PoseStack matrixStack, int x1, int y1, int x2, int y2, int color1, int color2) {
        float f = (color1 >> 24 & 255) / 255.0F;
        float f1 = (color1 >> 16 & 255) / 255.0F;
        float f2 = (color1 >> 8 & 255) / 255.0F;
        float f3 = (color1 & 255) / 255.0F;
        float f4 = (color2 >> 24 & 255) / 255.0F;
        float f5 = (color2 >> 16 & 255) / 255.0F;
        float f6 = (color2 >> 8 & 255) / 255.0F;
        float f7 = (color2 & 255) / 255.0F;
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.blendFuncSeparate(770, 771, 1, 0);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        bufferbuilder.vertex(matrixStack.last().pose(), x2, y1, this.getBlitOffset()).color(f1, f2, f3, f).endVertex();
        bufferbuilder.vertex(matrixStack.last().pose(), x1, y1, this.getBlitOffset()).color(f1, f2, f3, f).endVertex();
        bufferbuilder.vertex(matrixStack.last().pose(), x1, y2, this.getBlitOffset()).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.vertex(matrixStack.last().pose(), x2, y2, this.getBlitOffset()).color(f5, f6, f7, f4).endVertex();
        tessellator.end();
        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
    }

    public int getColor() {
        int r = Mth.clamp((int) redSlider.getValue(), 0, 255);
        int g = Mth.clamp((int) greenSlider.getValue(), 0, 255);
        int b = Mth.clamp((int) blueSlider.getValue(), 0, 255);
        int a = Mth.clamp((int) alphaSlider.getValue(), 0, 255);
        return (r << 24) | (g << 16) | (b << 8) | a;
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

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        for (GuiEventListener widget : this.children()) {
            if (widget.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
                return true;
            }
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (GuiEventListener widget : this.children()) {
            if (widget.mouseReleased(mouseX, mouseY, button)) {
                return true;
            }
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }
}