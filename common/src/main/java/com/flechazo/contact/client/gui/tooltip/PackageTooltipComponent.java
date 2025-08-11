package com.flechazo.contact.client.gui.tooltip;

import com.flechazo.contact.client.gui.hud.TexturePos;
import com.flechazo.contact.client.item.PackageTooltipData;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientBundleTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class PackageTooltipComponent implements ClientTooltipComponent {
    public static final ResourceLocation TEXTURE = ClientBundleTooltip.TEXTURE_LOCATION;
    private final NonNullList<ItemStack> contents;

    private static final TexturePos SLOT = TexturePos.create(0, 0, 18, 20);
    private static final TexturePos BORDER_VERTICAL = TexturePos.create(0, 18, 1, 20);
    private static final TexturePos BORDER_HORIZONTAL_TOP = TexturePos.create(0, 20, 18, 1);
    private static final TexturePos BORDER_HORIZONTAL_BOTTOM = TexturePos.create(0, 60, 18, 1);
    private static final TexturePos BORDER_CORNER_TOP = TexturePos.create(0, 20, 1, 1);
    private static final TexturePos BORDER_CORNER_BOTTOM = TexturePos.create(0, 60, 1, 1);

    public PackageTooltipComponent(PackageTooltipData data) {
        contents = data.contents();
    }

    @Override
    public int getHeight() {
        return 26;
    }

    @Override
    public int getWidth(Font font) {
        return this.contents.size() * 18 + 2;
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
        int i = this.contents.size();
        for (int m = 0; m < i; ++m) {
            int n = x + m * 18 + 1;
            int o = y + 1;
            this.drawSlot(n, o, m, guiGraphics, font);
        }
        this.drawOutline(x, y, i, 1, guiGraphics);
    }

    private void drawSlot(int x, int y, int index, GuiGraphics guiGraphics, Font font) {
        if (index >= 4) {
            return;
        }
        ItemStack itemStack = this.contents.get(index);
        this.draw(guiGraphics, x, y, SLOT);
        guiGraphics.renderItem(itemStack, x + 1, y + 1, index);
        guiGraphics.renderItemDecorations(font, itemStack, x + 1, y + 1);
    }

    private void drawOutline(int x, int y, int columns, int rows, GuiGraphics guiGraphics) {
        int i;
        this.draw(guiGraphics, x, y, BORDER_CORNER_TOP);
        this.draw(guiGraphics, x + columns * 18 + 1, y, BORDER_CORNER_TOP);
        for (i = 0; i < columns; ++i) {
            this.draw(guiGraphics, x + 1 + i * 18, y, BORDER_HORIZONTAL_TOP);
            this.draw(guiGraphics, x + 1 + i * 18, y + rows * 20, BORDER_HORIZONTAL_BOTTOM);
        }
        for (i = 0; i < rows; ++i) {
            this.draw(guiGraphics, x, y + i * 20 + 1, BORDER_VERTICAL);
            this.draw(guiGraphics, x + columns * 18 + 1, y + i * 20 + 1, BORDER_VERTICAL);
        }
        this.draw(guiGraphics, x, y + rows * 20, BORDER_CORNER_BOTTOM);
        this.draw(guiGraphics, x + columns * 18 + 1, y + rows * 20, BORDER_CORNER_BOTTOM);
    }

    private void draw(GuiGraphics guiGraphics, int x, int y, TexturePos pos) {
        guiGraphics.blit(TEXTURE, x, y, 0, pos.getX(), pos.getY(), pos.getWidth(), pos.getHeight(), 128, 128);
    }
}
