package com.flechazo.contact.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IMailItem {
    boolean isEnderType();

    default void addSenderInfoTooltip(ItemStack stack, @Nullable Level levelIn, List<Component> tooltip, TooltipFlag flag) {
        if (stack.getOrCreateTag().contains("Sender")) {
            tooltip.add(Component.translatable("tooltip.contact.mail.sender", stack.getOrCreateTag().getString("Sender")).withStyle(ChatFormatting.GRAY));
        }
    }
}
