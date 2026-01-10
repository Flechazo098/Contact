package com.flechazo.contact.client.gui.screen;

import com.flechazo.contact.common.registry.ItemRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public class NewMailToast implements Toast {
    private static final ResourceLocation BACKGROUND_SPRITE = ResourceLocation.withDefaultNamespace("toast/advancement");

    @Override
    public Visibility render(GuiGraphics guiGraphics, ToastComponent component, long ticks) {
        guiGraphics.blitSprite(BACKGROUND_SPRITE, 0, 0, this.width(), this.height());

        var list = component.getMinecraft().font.split(
                Component.translatable("info.contact.new_mail.desc"), 125
        );
        int titleColor = 0xFFFF00;
        int xTitle = 30;
        int xDesc = 30;

        if (list.size() == 1) {
            guiGraphics.drawString(component.getMinecraft().font,
                    Component.translatable("info.contact.new_mail.title"),
                    xTitle, 7, titleColor | 0xFF000000, false);
            guiGraphics.drawString(component.getMinecraft().font, list.getFirst(),
                    xDesc, 18, 0xFFFFFFFF, false);
        } else {
            if (ticks < 1500L) {
                int fade = Mth.floor(Mth.clamp((1500L - ticks) / 300.0F, 0.0F, 1.0F) * 255.0F) << 24 | 0x4000000;
                guiGraphics.drawString(component.getMinecraft().font,
                        Component.translatable("info.contact.new_mail.title"),
                        xTitle, 11, titleColor | fade, false);
            } else {
                int fade = Mth.floor(Mth.clamp((ticks - 1500L) / 300.0F, 0.0F, 1.0F) * 252.0F) << 24 | 0x4000000;
                int y = this.height() / 2 - list.size() * 9 / 2;
                for (FormattedCharSequence line : list) {
                    guiGraphics.drawString(component.getMinecraft().font, line,
                            xDesc, y, 0xFFFFFF | fade, false);
                    y += 9;
                }
            }
        }

        guiGraphics.renderFakeItem(new ItemStack(ItemRegistry.LETTER.get()), 8, 8);

        return ticks >= 5000L ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
    }
}
