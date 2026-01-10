package com.flechazo.contact.common.screenhandler;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import org.jetbrains.annotations.Nullable;

public abstract class PackageScreenHandler extends ContentScreenHandler {
    public boolean isPacked = false;
    public boolean droppedPaper = false;

    public PackageScreenHandler(@Nullable MenuType<?> type, int syncId) {
        super(type, syncId);
    }

    public abstract SimpleContainer getContainer();

    public abstract ItemStack getUnpackedItem();

    public abstract ItemStack getPackedItem();


    @Override
    public void removed(Player player) {
        if (player instanceof ServerPlayer) {
            if (!isPacked) {
                if (!player.isAlive() || ((ServerPlayer) player).hasDisconnected()) {
                    for (int j = 0; j < getContainerCount(); ++j) {
                        player.drop(getContainer().getItem(j), false);
                        getContainer().setItem(j, ItemStack.EMPTY);
                    }

                    var cursor = this.getCarried();
                    if (!cursor.isEmpty()) {
                        player.drop(cursor, false);
                    }

                    if (!player.getAbilities().instabuild && !droppedPaper) {
                        player.drop(getUnpackedItem(), false);
                        droppedPaper = true;
                    }
                } else {
                    for (int i = 0; i < getContainerCount(); ++i) {
                        player.getInventory().placeItemBackInInventory(getContainer().getItem(i));
                        getContainer().setItem(i, ItemStack.EMPTY);
                    }

                    var cursor = this.getCarried();
                    if (!cursor.isEmpty()) {
                        player.getInventory().placeItemBackInInventory(cursor);
                    }

                    if (!player.getAbilities().instabuild && !droppedPaper) {
                        player.getInventory().placeItemBackInInventory(getUnpackedItem());
                        droppedPaper = true;
                    }
                }
            } else {
                var parcel = getPackedItem();
                parcel.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(getContainer().getItems()));
                if (!player.isAlive() || ((ServerPlayer) player).hasDisconnected()) {
                    player.drop(parcel, false);
                } else {
                    player.getInventory().placeItemBackInInventory(parcel);
                }
            }
        }
    }
}
