package com.flechazo.contact.client.gui.screen;

import com.flechazo.contact.client.gui.hud.TexturePos;
import com.flechazo.contact.client.widget.EditableTextBox;
import com.flechazo.contact.helper.GuiHelper;
import com.flechazo.contact.resourse.PostcardStyle;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PostcardEditScreen extends Screen {
    private final PostcardStyle style;
    private final EditableTextBox textBox;
    private Button buttonDone;
    private final ItemStack postcard;
    private final Player editingPlayer;
    private final InteractionHand hand;

    public PostcardEditScreen(ItemStack postcardIn, Player playerIn, InteractionHand handIn) {
        super(Component.empty());
        this.postcard = postcardIn;
        this.editingPlayer = playerIn;
        this.hand = handIn;
        CompoundTag tag = postcardIn.getTag();
        if (tag != null) {
            if (tag.contains("Info")) {
                style = PostcardStyle.fromNBT(tag);
            } else if (tag.contains("CardID")) {
                style = PostcardStyle.fromNBT(tag);
            } else style = PostcardStyle.DEFAULT;
        } else style = PostcardStyle.DEFAULT;
        this.textBox = this.addRenderableOnly(new EditableTextBox(postcard, editingPlayer, hand,
                (this.width - style.cardWidth()) / 2 + style.textPosX(), (this.height - style.cardHeight() - 30) / 2 + style.textPosY(), style.textWidth(), style.textHeight(),
                12, style.textColor(), Component.literal("Postcard")));
    }

    @Override
    protected void init() {
        this.buttonDone = this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (button) ->
                {
                    this.minecraft.setScreen(null);
                    textBox.sendTextToServer();
                })
                .pos(this.width / 2 - 48, (this.height + style.cardHeight()) / 2 - 5)
                .size(98, 20)
                .build());

        this.textBox.setX((this.width - style.cardWidth()) / 2 + style.textPosX());
        this.textBox.setY((this.height - style.cardHeight() - 30) / 2 + style.textPosY());
        this.textBox.shouldRefresh();

        this.setInitialFocus(textBox);
    }

    @Override
    public void tick() {
        super.tick();
        textBox.tick();
    }

    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (!super.mouseClicked(pMouseX, pMouseY, pButton)) {
            textBox.mouseClicked(pMouseX, pMouseY, pButton);
        }
        return true;
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if (!super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY)) {
            textBox.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
        }
        return true;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(guiGraphics);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        GuiHelper.drawLayerBySize(guiGraphics, style.getCardTexture(),
                (this.width - style.cardWidth()) / 2, (this.height - style.cardHeight() - 30) / 2,
                new TexturePos(0, 0, style.cardWidth(), style.cardHeight()), style.cardWidth(), style.cardHeight());

        textBox.render(guiGraphics, mouseX, mouseY, delta);

        super.render(guiGraphics, mouseX, mouseY, delta);
    }
}
