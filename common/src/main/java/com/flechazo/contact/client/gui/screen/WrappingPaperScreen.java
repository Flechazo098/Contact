package com.flechazo.contact.client.gui.screen;

import com.flechazo.contact.Contact;
import com.flechazo.contact.client.gui.hud.TexturePos;
import com.flechazo.contact.client.widget.IconButton;
import com.flechazo.contact.common.screenhandler.WrappingPaperScreenHandler;
import com.flechazo.contact.helper.GuiHelper;
import com.flechazo.contact.network.ActionC2SMessage;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class WrappingPaperScreen extends AbstractContainerScreen<WrappingPaperScreenHandler> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Contact.MOD_ID, "textures/gui/wrapping_paper.png");
    private int offsetX;
    private int offsetY;
    private IconButton buttonPack;

    public WrappingPaperScreen(WrappingPaperScreenHandler screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void init() {
        super.init();
        this.offsetX = (this.width - 176) / 2;
        this.offsetY = (this.height - 166) / 2 + 16;

        this.buttonPack = addRenderableWidget(new IconButton(offsetX + 124, offsetY + 15, 18, 19, Component.translatable("tooltip.contact.wrapping_paper.pack"), button -> pack(), this::buttonTooltip));
    }

    private void buttonTooltip(Button button, GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (button.isHovered()) {
            GuiHelper.drawTooltip(guiGraphics, mouseX, mouseY, button.getX(), button.getY(), button.getWidth(), button.getHeight(), Lists.newArrayList(button.getMessage()));
        }
    }

    private void pack() {
        ActionC2SMessage packet = ActionC2SMessage.create(0);
        packet.sendToServer();
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {

    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(guiGraphics, mouseX, mouseY, delta);
        super.render(guiGraphics, mouseX, mouseY, delta);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        RenderSystem.setShaderTexture(0, TEXTURE);
        GuiHelper.drawLayer(guiGraphics.pose(), offsetX, offsetY, new TexturePos(0, 0, 176, 133));

        GuiHelper.renderButton(guiGraphics, delta, mouseX, mouseY, 0, TEXTURE, buttonPack,
                new TexturePos(176, 0, 18, 19),
                new TexturePos(176, 19, 18, 19));
    }
}
