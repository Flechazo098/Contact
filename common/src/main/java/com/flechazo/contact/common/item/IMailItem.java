package com.flechazo.contact.common.item;

import com.flechazo.contact.common.component.ContactDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IMailItem {
    boolean isEnderType();

    default void addSenderInfoTooltip(ItemStack stack, @Nullable Item.TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flag) {
        var sender = stack.get(ContactDataComponents.POSTCARD_SENDER.get());
        if (sender != null && !sender.isEmpty()) {
            tooltip.add(Component.translatable("tooltip.contact.mail.sender", sender).withStyle(ChatFormatting.GRAY));
        }
    }
}