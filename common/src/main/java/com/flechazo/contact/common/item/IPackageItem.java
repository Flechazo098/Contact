package com.flechazo.contact.common.item;

import com.flechazo.contact.client.item.PackageTooltipData;
import com.flechazo.contact.common.component.ContactDataComponents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

public interface IPackageItem {
    static boolean checkAndPostmarkPostcard(ItemStack parcel, String sender) {
        if (parcel.getItem() instanceof IPackageItem packageItem) {
            var contents = parcel.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
            SimpleContainer container = new SimpleContainer(packageItem.getCapacity());
            contents.copyInto(container.getItems());

            boolean postcard = false;
            for (int i = 0; i < packageItem.getCapacity(); ++i) {
                ItemStack item = container.getItem(i);
                if (item.getItem() instanceof IMailItem) {
                    if (item.getItem() instanceof PostcardItem) {
                        postcard = true;
                    }
                    item.set(ContactDataComponents.POSTCARD_SENDER.get(), sender);
                }
            }

            parcel.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(container.getItems()));
            return postcard;
        } else {
            return false;
        }
    }

    static void openPackage(IPackageItem item, Player user, InteractionHand hand) {
        var parcel = user.getItemInHand(hand);
        var contents = parcel.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
        SimpleContainer container = new SimpleContainer(item.getCapacity());
        contents.copyInto(container.getItems());

        for (int i = 0; i < item.getCapacity(); ++i) {
            user.getInventory().placeItemBackInInventory(container.getItem(i));
        }
    }

    static PackageTooltipData getTooltipData(IPackageItem item, ItemStack stack) {
        var contents = stack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
        SimpleContainer container = new SimpleContainer(item.getCapacity());
        contents.copyInto(container.getItems());
        return new PackageTooltipData(container.getItems());
    }

    int getCapacity();
}