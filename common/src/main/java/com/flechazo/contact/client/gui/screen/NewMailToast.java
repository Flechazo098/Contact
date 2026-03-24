package com.flechazo.contact.client.gui.screen;

import com.flechazo.contact.common.registry.ItemRegistry;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import java.util.List;

@Environment(EnvType.CLIENT)
public class NewMailToast implements Toast {
    @Override
    public Visibility render(GuiGraphics guiGraphics, ToastComponent component, long ticks) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.blit(TEXTURE, 0, 0, 0, 0, this.width(), this.height());

        List<FormattedCharSequence> list = component.getMinecraft().font.split(Component.translatable("info.contact.new_mail.desc"), 125);
        int i = 16776960;
        if (list.size() == 1) {
            guiGraphics.drawString(component.getMinecraft().font, Component.translatable("info.contact.new_mail.title"), 30, 7, i | -16777216, false);
            guiGraphics.drawString(component.getMinecraft().font, list.get(0), 30, 18, -1, false);
        } else {
            if (ticks < 1500L) {
                int k = Mth.floor(Mth.clamp((float) (1500L - ticks) / 300.0F, 0.0F, 1.0F) * 255.0F) << 24 | 67108864;
                guiGraphics.drawString(component.getMinecraft().font, Component.translatable("info.contact.new_mail.desc"), 30, 11, i | k, false);
            } else {
                int i1 = Mth.floor(Mth.clamp((float) (ticks - 1500L) / 300.0F, 0.0F, 1.0F) * 252.0F) << 24 | 67108864;
                int l = this.height() / 2 - list.size() * 9 / 2;

                for (FormattedCharSequence orderedText : list) {
                    guiGraphics.drawString(component.getMinecraft().font, orderedText, 30, l, 16777215 | i1, false);
                    l += 9;
                }
            }
        }

        guiGraphics.renderItem(new ItemStack(ItemRegistry.LETTER.get()), 8, 8);
        return ticks >= 5000L ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
    }
}
