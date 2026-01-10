package com.flechazo.contact.client.gui.screen;

import com.flechazo.contact.Contact;
import com.flechazo.contact.client.gui.hud.TexturePos;
import com.flechazo.contact.client.widget.IconButton;
import com.flechazo.contact.common.screenhandler.RedPacketEnvelopeScreenHandler;
import com.flechazo.contact.helper.GuiHelper;
import com.flechazo.contact.network.ActionMessage;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class RedPacketEnvelopeScreen extends AbstractContainerScreen<RedPacketEnvelopeScreenHandler> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Contact.MOD_ID, "textures/gui/red_packet.png");
    private int offsetX;
    private int offsetY;
    private IconButton buttonPack;
    private EditBox blessings;

    public RedPacketEnvelopeScreen(RedPacketEnvelopeScreenHandler screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void init() {
        super.init();
        this.offsetX = (this.width - 176) / 2;
        this.offsetY = (this.height - 166) / 2 + 16;

        this.buttonPack = addRenderableWidget(new IconButton(offsetX + 130, offsetY + 18, 8, 19, Component.translatable("tooltip.contact.envelope.seal"), button -> seal(), this::buttonTooltip));

        this.blessings = this.addRenderableWidget(new EditBox(this.font, offsetX + 66, offsetY + 26, 52, 9, Component.translatable("info.contact.envelope.blessings")));
        this.blessings.setTextColor(-1);
        this.blessings.setTextColorUneditable(-1);
        this.blessings.setValue(menu.blessings);
        this.blessings.setResponder(this::whileTyping);
        this.blessings.setBordered(false);
        this.blessings.setMaxLength(64);
        this.setInitialFocus(this.blessings);
    }

    private void buttonTooltip(Button button, GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (button.isHovered()) {
            GuiHelper.drawTooltip(guiGraphics, mouseX, mouseY, button.getX(), button.getY(), button.getWidth(), button.getHeight(), Lists.newArrayList(button.getMessage()));
        }
    }

    private void seal() {
        var blessings = menu.blessings == null ? "" : menu.blessings;
        ActionMessage packet = new ActionMessage(0, blessings);
        packet.sendToServer();
    }

    private void whileTyping(String blessings) {
        if (!menu.blessings.equals(blessings)) {
            menu.blessings = blessings;
        }
    }

    @Override
    protected void containerTick() {
        super.containerTick();
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, Component.translatable("info.contact.red_packet.blessings"), 64, 30, 0xE6E6E6, false);
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

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) {
            this.minecraft.player.closeContainer();
        }

        return this.blessings.keyPressed(keyCode, scanCode, modifiers) || this.blessings.isFocused() || super.keyPressed(keyCode, scanCode, modifiers);
    }
}
