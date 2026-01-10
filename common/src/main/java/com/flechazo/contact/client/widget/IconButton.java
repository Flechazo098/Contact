package com.flechazo.contact.client.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class IconButton extends Button {
    protected final OnTooltip onTooltip;
    private boolean isPressed = false;

    public IconButton(int x, int y, int width, int height, Component message, OnPress onPress) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
        this.onTooltip = null;
    }

    public IconButton(int x, int y, int width, int height, Component message, OnPress onPress, OnTooltip tooltip) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
        this.onTooltip = tooltip;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        if (this.isHovered()) {
            this.renderToolTip(guiGraphics, mouseX, mouseY);
        }
    }

    public void renderToolTip(GuiGraphics guiGraphics, int pMouseX, int pMouseY) {
        if (this.onTooltip != null) {
            this.onTooltip.onTooltip(this, guiGraphics, pMouseX, pMouseY);
        }
    }

    @Override
    public void onPress() {
        super.onPress();
        this.isPressed = true;
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        super.onRelease(mouseX, mouseY);
        this.isPressed = false;
    }

    public boolean isPressed() {
        return isPressed;
    }

    public interface OnTooltip {
        void onTooltip(Button button, GuiGraphics guiGraphics, int mouseX, int mouseY);
    }
}