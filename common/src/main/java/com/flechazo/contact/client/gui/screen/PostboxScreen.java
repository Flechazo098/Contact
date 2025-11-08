package com.flechazo.contact.client.gui.screen;

import com.flechazo.contact.Contact;
import com.flechazo.contact.client.gui.hud.TexturePos;
import com.flechazo.contact.client.widget.IconButton;
import com.flechazo.contact.common.screenhandler.PostboxScreenHandler;
import com.flechazo.contact.helper.GuiHelper;
import com.flechazo.contact.network.EnquireAddresseeMessage;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;
import java.util.Objects;

public class PostboxScreen extends AbstractContainerScreen<PostboxScreenHandler> {
    private static final ResourceLocation RED_TEXTURE = new ResourceLocation(Contact.MOD_ID, "textures/gui/red_postbox.png");
    private static final ResourceLocation GREEN_TEXTURE = new ResourceLocation(Contact.MOD_ID, "textures/gui/green_postbox.png");
    private final boolean isRed;
    private int offsetX;
    private int offsetY;
    private IconButton buttonSend;
    private EditBox nameField;
    private int selected = 0;

    public PostboxScreen(PostboxScreenHandler screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.isRed = screenContainer.isRed();
    }

    @Override
    protected void init() {
        super.init();
        this.offsetX = (this.width - 176) / 2;
        this.offsetY = (this.height - 166) / 2 + 16;

        this.buttonSend = this.addRenderableWidget(new IconButton(offsetX + 97, offsetY + 26, 10, 9, Component.translatable("tooltip.contact.postbox.send"), button -> send(), this::buttonTooltip));

        this.nameField = this.addRenderableWidget(new EditBox(this.font, offsetX + 42, offsetY + 26, 44, 9, Component.translatable("info.contact.postbox.addressee")));
        this.nameField.setTextColor(-1);
        this.nameField.setTextColorUneditable(-1);
        this.nameField.setValue(menu.playerName);
        this.nameField.setResponder(this::whileTyping);
        this.nameField.setBordered(false);
        this.nameField.setMaxLength(64);
        this.setInitialFocus(this.nameField);
    }

    private void buttonTooltip(Button button, GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (button.isHovered()) {
            GuiHelper.drawTooltip(guiGraphics, mouseX, mouseY, button.getX(), button.getY(), button.getWidth(), button.getHeight(), Lists.newArrayList(button.getMessage()));
        }
    }

    private void whileTyping(String name) {
        if (!menu.playerName.equals(name)) {
            menu.playerName = name;
            if (menu.status == 2) {
                menu.status = 1;
            }
            if (menu.status == 1) {
                EnquireAddresseeMessage packet = EnquireAddresseeMessage.create(menu.playerName, false);
                packet.sendToServer();
            }
        }
    }

    private void send() {
        if (menu.status == 1) {
            if (isAddresseeValid() && menu.ticks.get(0) >= 0) {
                EnquireAddresseeMessage packet = EnquireAddresseeMessage.create(menu.playerName, true);
                packet.sendToServer();
            }
        } else if (menu.status == 2) {
            menu.status = 0;
        }
        this.nameField.setFocused(false);
    }

    private boolean isAddresseeValid() {
        return !menu.names.isEmpty() && Objects.equals(nameField.getValue(), menu.names.get(0));
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        this.nameField.tick();
        if (menu.status == 1 && !nameField.getValue().equals(menu.playerName)) {
            nameField.setValue(menu.playerName);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (menu.status == 1 && nameField.isFocused()) {
            int size = menu.names.size();

            int maxWidth = 55;
            for (int i = 0; i < size; i++) {
                maxWidth = Math.max(this.font.width(menu.names.get(i)) + 8, maxWidth);
            }

            if (size != 0) {
                if (offsetX + 42 <= mouseX && mouseX < offsetX + 42 + maxWidth && offsetY + 40 + selected * 11 <= mouseY && mouseY < offsetY + 51 + selected * 11) {
                    this.nameField.setValue(menu.names.get(selected));
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);

        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0f, 0.0f, 400.0f);
        boolean flag = false;
        if (menu.status == 1 && nameField.isFocused()) {
            int size = menu.names.size();

            int maxWidth = 55;
            for (int i = 0; i < size; i++) {
                maxWidth = Math.max(this.font.width(menu.names.get(i)) + 8, maxWidth);
            }

            int z = 5000;
            if (size != 0) {
                ResourceLocation texture = isRed ? RED_TEXTURE : GREEN_TEXTURE;
//                RenderSystem.setShaderTexture(0, texture);
                int renderWidth = maxWidth;
                if (renderWidth == 55) {
                    GuiHelper.drawLayer(guiGraphics, offsetX + 38, offsetY + 36, texture, new TexturePos(176, 18, 55, 3 + 11 * size));
                    GuiHelper.drawLayer(guiGraphics, offsetX + 38, offsetY + 39 + 11 * size, texture, new TexturePos(176, 65, 55, 2));
                } else {
                    GuiHelper.drawLayer(guiGraphics, offsetX + 38, offsetY + 36, texture, new TexturePos(176, 18, 15, 3 + 11 * size));
                    GuiHelper.drawLayer(guiGraphics, offsetX + 38, offsetY + 39 + 11 * size, texture, new TexturePos(176, 65, 15, 2));

                    GuiHelper.drawLayer(guiGraphics, offsetX + 23 + renderWidth, offsetY + 36, texture, new TexturePos(216, 18, 15, 3 + 11 * size));
                    GuiHelper.drawLayer(guiGraphics, offsetX + 23 + renderWidth, offsetY + 39 + 11 * size, texture, new TexturePos(216, 65, 15, 2));

                    renderWidth -= 30;
                    int pos = 0;

                    while (renderWidth > 15) {
                        GuiHelper.drawLayer(guiGraphics, offsetX + 53 + pos, offsetY + 36, texture, new TexturePos(191, 18, 15, 3 + 11 * size));
                        GuiHelper.drawLayer(guiGraphics, offsetX + 53 + pos, offsetY + 39 + 11 * size, texture, new TexturePos(191, 65, 15, 2));

                        renderWidth -= 15;
                        pos += 15;
                    }
                    GuiHelper.drawLayer(guiGraphics, offsetX + 53 + pos, offsetY + 36, texture, new TexturePos(191, 18, renderWidth, 3 + 11 * size));
                    GuiHelper.drawLayer(guiGraphics, offsetX + 53 + pos, offsetY + 39 + 11 * size, texture, new TexturePos(191, 65, renderWidth, 2));
                }
            }

            for (int i = 0; i < size; i++) {
                if (offsetX + 42 <= mouseX && mouseX < offsetX + 42 + maxWidth && offsetY + 40 + i * 11 <= mouseY && mouseY < offsetY + 51 + i * 11) {
                    selected = i;
                    flag = true;
                }
                guiGraphics.drawString(this.font, menu.names.get(i), offsetX + 42, offsetY + 40 + i * 11, selected == i ? ChatFormatting.YELLOW.getColor() : ChatFormatting.WHITE.getColor(), false);
            }
        }
        guiGraphics.pose().popPose();

        if (!flag) {
            this.renderTooltip(guiGraphics, mouseX, mouseY);
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int x, int y) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        ResourceLocation texture = isRed ? RED_TEXTURE : GREEN_TEXTURE;
        RenderSystem.setShaderTexture(0, texture);

        GuiHelper.drawLayer(guiGraphics.pose(), offsetX, offsetY, new TexturePos(0, 0, 176, 133));

        GuiHelper.renderButton(guiGraphics, partialTicks, x, y, 0, texture, buttonSend,
                new TexturePos(176, 0, 10, 9),
                new TexturePos(176, 9, 10, 9));
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, Component.translatable("info.contact.postbox.addressee"), 40, 30, 0xE6E6E6, false);
        switch (menu.status) {
            case 0 -> {
                MutableComponent text = Component.translatable("info.contact.postbox.need_mail");
                renderTips(guiGraphics, text);
            }
            case 1 -> {
                if (isAddresseeValid()) {
                    int tick = menu.ticks.get(0);
                    if (tick < 0) {
                        MutableComponent text = Component.translatable("info.contact.postbox.no_mailbox");
                        renderTips(guiGraphics, text);
                    } else {
                        MutableComponent text = Component.translatable("info.contact.postbox.estimated");
                        int width = this.font.width(text.getString());
                        int min = tick / 1200;
                        int sec = tick % 1200 / 20;
                        if (width > 38) {
                            guiGraphics.drawString(this.font, text, 141 - width / 2, 26, 0x1A1A1A, false);
                            if (tick < 20) {
                                guiGraphics.drawString(this.font, Component.translatable("info.contact.postbox.instant"), 141 - width / 2, 38, 0x1A1A1A, false);
                            } else {
                                guiGraphics.drawString(this.font, Component.translatable("info.contact.postbox.time", min, sec), 141 - width / 2, 38, 0x1A1A1A, false);
                            }
                        } else {
                            guiGraphics.drawString(this.font, text, 122, 26, 0x1A1A1A, false);
                            if (tick < 20) {
                                guiGraphics.drawString(this.font, Component.translatable("info.contact.postbox.instant"), 122, 38, 0x1A1A1A, false);
                            } else {
                                guiGraphics.drawString(this.font, Component.translatable("info.contact.postbox.time", min, sec), 122, 38, 0x1A1A1A, false);
                            }
                        }
                    }
                } else {
                    MutableComponent text = Component.translatable("info.contact.postbox.need_addressee");
                    renderTips(guiGraphics, text);
//                    MutableText text = Text.translatable("info.contact.postbox.need_addressee");
//                    int width = this.textRenderer.getWidth(text.getString());
//                    if (width > 38)
//                    {
//                        this.textRenderer.draw(matrixStack, text, 141 - width / 2, 32, 0x1A1A1A);
//                    }
//                    else
//                    {
//                        this.textRenderer.draw(matrixStack, text, 122, 32, 0x1A1A1A);
//                    }
                }
            }
            case 2 -> {
                MutableComponent text = Component.translatable("info.contact.postbox.success");
                renderTips(guiGraphics, text);
            }
            case 3 -> {
                MutableComponent text = Component.translatable("info.contact.postbox.cannot_send");
                renderTips(guiGraphics, text);
            }
        }
    }

    private void renderTips(GuiGraphics guiGraphics, MutableComponent text) {
        int width = this.font.width(text.getString());
        if (width > 38) {
            List<FormattedCharSequence> list = this.font.split(text, 50);
            for (int i = 0; i < list.size(); i++) {
                guiGraphics.drawString(this.font, list.get(i), 118, 38 - list.size() * 6 + i * 12, 0x1A1A1A, false);
            }
        } else {
            guiGraphics.drawString(this.font, text, 122, 32, 0x1A1A1A, false);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) {
            this.minecraft.player.closeContainer();
        }

        return this.nameField.keyPressed(keyCode, scanCode, modifiers) || this.nameField.isFocused() || super.keyPressed(keyCode, scanCode, modifiers);
    }
}
