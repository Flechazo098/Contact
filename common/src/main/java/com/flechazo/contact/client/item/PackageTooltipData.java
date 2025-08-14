package com.flechazo.contact.client.item;

import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public record PackageTooltipData(NonNullList<ItemStack> contents) implements TooltipComponent {
    public PackageTooltipData(NonNullList<ItemStack> contents) {
        NonNullList<ItemStack> list = NonNullList.create();

        if (contents != null) {
            for (ItemStack content : contents) {
                if (content != null && !content.isEmpty()) {
                    list.add(content);
                }
            }
        }

        this.contents = list;
    }
}
