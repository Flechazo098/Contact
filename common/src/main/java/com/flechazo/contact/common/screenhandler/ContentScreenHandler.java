package com.flechazo.contact.common.screenhandler;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public abstract class ContentScreenHandler extends AbstractContainerMenu {
    public ContentScreenHandler(@Nullable MenuType<?> type, int syncId) {
        super(type, syncId);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);

        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack newStack = slot.getItem(), oldStack = newStack.copy();

        boolean isMerged;
        if (index < getContainerCount()) {
            isMerged = moveItemStackTo(newStack, getContainerCount() + 27, getContainerCount() + 36, true)
                    || moveItemStackTo(newStack, getContainerCount(), getContainerCount() + 27, false);
        } else if (index < getContainerCount() + 27) {
            isMerged = moveItemStackTo(newStack, 0, getContainerCount(), false)
                    || moveItemStackTo(newStack, getContainerCount() + 27, getContainerCount() + 36, true);
        } else {
            isMerged = moveItemStackTo(newStack, 0, getContainerCount() + 27, false);
        }

        if (!isMerged) {
            return ItemStack.EMPTY;
        }

        if (newStack.getCount() == 0) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        slot.onTake(player, newStack);

        return oldStack;
    }

    public abstract int getContainerCount();
}
