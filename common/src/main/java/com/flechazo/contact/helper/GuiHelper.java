package com.flechazo.contact.helper;

import com.flechazo.contact.client.gui.hud.TexturePos;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Optional;

public final class GuiHelper {
    public static void drawTexturedModalRect(PoseStack poseStack, int x, int y, int u, int v, int width, int height, float zLevel) {
        final float uScale = 1f / 0x100;
        final float vScale = 1f / 0x100;

        var tesselator = Tesselator.getInstance();
        var wr = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        var matrix = poseStack.last().pose();
        wr.addVertex(matrix, x, y + height, zLevel).setUv(u * uScale, ((v + height) * vScale));
        wr.addVertex(matrix, x + width, y + height, zLevel).setUv((u + width) * uScale, ((v + height) * vScale));
        wr.addVertex(matrix, x + width, y, zLevel).setUv((u + width) * uScale, (v * vScale));
        wr.addVertex(matrix, x, y, zLevel).setUv(u * uScale, (v * vScale));
        BufferUploader.drawWithShader(wr.buildOrThrow());
    }

    public static void drawLayer(PoseStack poseStack, int x, int y, TexturePos pos) {
        drawTexturedModalRect(poseStack, x, y, pos.getX(), pos.getY(), pos.getWidth(), pos.getHeight(), 0);
    }

    public static void drawLayer(GuiGraphics guiGraphics, int x, int y, ResourceLocation rl, TexturePos pos) {
        guiGraphics.blit(rl, x, y, pos.getX(), pos.getY(), pos.getWidth(), pos.getHeight());
    }

    public static void drawLayerBySize(GuiGraphics guiGraphics, ResourceLocation rl, int x, int y, TexturePos pos, int textureWidth, int textureHeight) {
        guiGraphics.blit(rl, x, y, pos.getWidth(), pos.getHeight(), pos.getX(), pos.getY(), pos.getWidth(), pos.getHeight(), textureWidth, textureHeight);
    }

    public static void renderButton(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY, int z, ResourceLocation texture, Button button, TexturePos normalPos, TexturePos hoveredPos) {
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, texture);

        if (button.isHovered()) {
            GuiHelper.drawLayer(guiGraphics.pose(), button.getX(), button.getY(), hoveredPos);
        } else {
            GuiHelper.drawLayer(guiGraphics.pose(), button.getX(), button.getY(), normalPos);
        }
        RenderSystem.disableBlend();

        button.render(guiGraphics, mouseX, mouseY, partialTicks);
    }


    public static void drawTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY, int x, int y, int weight, int height, List<Component> list) {
        if (x <= mouseX && mouseX <= x + weight && y <= mouseY && mouseY <= y + height) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, list, Optional.empty(), mouseX, mouseY);
        }
    }
}