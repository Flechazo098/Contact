package com.flechazo.contact.client.gui.screen;

import com.flechazo.contact.client.gui.hud.TexturePos;
import com.flechazo.contact.client.widget.ReadOnlyTextBox;
import com.flechazo.contact.common.component.ContactDataComponents;
import com.flechazo.contact.data.PostcardDataManager;
import com.flechazo.contact.helper.ColorHelper;
import com.flechazo.contact.helper.GuiHelper;
import com.flechazo.contact.data.PostcardStyle;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class PostcardReadScreen extends Screen {
    private final PostcardStyle style;
    private final ItemStack postcard;
    private ReadOnlyTextBox textBox;
    private Button buttonDone;

    public PostcardReadScreen(ItemStack postcardIn) {
        super(Component.empty());
        this.postcard = postcardIn;

        ResourceLocation styleId = postcardIn.get(ContactDataComponents.POSTCARD_STYLE_ID.get());
            style = PostcardDataManager.getPostcards().getOrDefault(styleId, PostcardStyle.DEFAULT);
    }

    @Override
    protected void init() {
        this.textBox = new ReadOnlyTextBox(postcard,
                (this.width - style.cardWidth()) / 2 + style.textPosX(), style.textPosY() + (this.height - style.cardHeight() - 30) / 2, style.textWidth(), style.textHeight(),
                12, style.textColor(), Component.literal("Postcard"));
        this.buttonDone = this.addRenderableWidget(Button
                .builder(CommonComponents.GUI_DONE, (button) -> this.minecraft.setScreen(null))
                .pos(this.width / 2 - 48, (this.height + style.cardHeight()) / 2 - 5)
                .size(98, 20)
                .build());
    }


    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderMenuBackground(guiGraphics);
        this.setFocused(null);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        GuiHelper.drawLayerBySize(guiGraphics, style.getCardTexture(), (this.width - style.cardWidth()) / 2, (this.height - style.cardHeight() - 30) / 2, new TexturePos(0, 0, style.cardWidth(), style.cardHeight()), style.cardWidth(), style.cardHeight());

        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(ColorHelper.getRedF(style.postmarkColor()), ColorHelper.getGreenF(style.postmarkColor()), ColorHelper.getBlueF(style.postmarkColor()), ColorHelper.getAlphaF(style.postmarkColor()));

        GuiHelper.drawLayerBySize(guiGraphics, style.getPostmarkTexture(), (this.width - style.cardWidth()) / 2 + style.postmarkPosX(), (this.height - style.cardHeight() - 30) / 2 + style.postmarkPosY(), new TexturePos(0, 0, style.postmarkWidth(), style.postmarkHeight()), style.postmarkWidth(), style.postmarkHeight());
        RenderSystem.disableBlend();

        textBox.render(guiGraphics, mouseX, mouseY, partialTicks);
        for (Renderable renderable : this.renderables) {
            renderable.render(guiGraphics, mouseX, mouseY, partialTicks);
        }
    }
}