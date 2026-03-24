package com.flechazo.contact.common.screenhandler;

import com.flechazo.contact.common.item.IMailItem;
import com.google.common.collect.Lists;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import static com.flechazo.contact.common.registry.ScreenHandlerTypeRegistry.GREEN_POSTBOX_CONTAINER;
import static com.flechazo.contact.common.registry.ScreenHandlerTypeRegistry.RED_POSTBOX_CONTAINER;

public class PostboxScreenHandler extends ContentScreenHandler {
    public final SimpleContainer parcel = new SimpleContainer(1);
    // old: 0 for parcel-waiting, 1 for addressee-waiting, 2 for send-ready, 3 for not-found, 4 for full-mailbox, 5 for successful
    // new: 0: waiting mail; 1: writing addressee; 2: successfully sent; 3: cannot send
    public byte status = 0;
    public String playerName = "";
    public List<String> names = Lists.newArrayList();
    public List<Integer> ticks = Lists.newArrayList();
    private final boolean isRed;

    public PostboxScreenHandler(int id, Inventory inv, boolean isRed) {
        super(isRed ? RED_POSTBOX_CONTAINER.get() : GREEN_POSTBOX_CONTAINER.get(), id);
        this.isRed = isRed;
        parcel.addListener(inventory ->
        {
            if (parcel.getItem(0).getItem() instanceof IMailItem) {
                if (!parcel.getItem(0).getOrCreateTag().contains("Sender")) {
                    status = 1;
                } else {
                    status = 3;
                }
            } else if (status != 2) {
                status = 0;
            }
        });
        addSlot(new Slot(parcel, 0, 16, 33) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof IMailItem;
            }
        });
        for (int i = 0; i < 3; ++i) {

            for (int j = 0; j < 9; ++j) {
                addSlot(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 67 + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            addSlot(new Slot(inv, i, 8 + i * 18, 125));
        }
    }

    @Override
    public void removed(Player player) {
        if (player instanceof ServerPlayer) {
            if (!player.isAlive() || ((ServerPlayer) player).hasDisconnected()) {
                player.drop(parcel.getItem(0), false);
                ItemStack cursor = this.getCarried();
                if (!cursor.isEmpty()) {
                    player.drop(cursor, false);
                }
            } else {
                player.getInventory().placeItemBackInInventory(parcel.getItem(0));
                ItemStack cursor = this.getCarried();
                if (!cursor.isEmpty()) {
                    player.getInventory().placeItemBackInInventory(cursor);
                }
            }
            parcel.setItem(0, ItemStack.EMPTY);
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    public boolean isEnderMail() {
        Item mail = parcel.getItem(0).getItem();
        return mail instanceof IMailItem && ((IMailItem) mail).isEnderType();
    }

    @Override
    public int getContainerCount() {
        return 1;
    }

    public boolean isRed() {
        return this.isRed;
    }
}
