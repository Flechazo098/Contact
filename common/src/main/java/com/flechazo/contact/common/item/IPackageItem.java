package com.flechazo.contact.common.item;

import com.flechazo.contact.client.item.PackageTooltipData;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IPackageItem {
    int getCapacity();

    static boolean checkAndPostmarkPostcard(ItemStack parcel, String sender) {
        if (parcel.getItem() instanceof IPackageItem packageItem) {
            SimpleContainer contents = new SimpleContainer(packageItem.getCapacity());
            ListTag list = parcel.getOrCreateTag().getList("parcel", Tag.TAG_COMPOUND);
            contents.fromTag(list);
            boolean postcard = false;
            for (int i = 0; i < packageItem.getCapacity(); ++i) {
                ItemStack item = contents.getItem(i);
                if (item.getItem() instanceof IMailItem) {
                    if (item.getItem() instanceof PostcardItem) {
                        postcard = true;
                    }
                    item.getOrCreateTag().putString("Sender", sender);
                }
            }
            parcel.getOrCreateTag().put("parcel", contents.createTag());
            return postcard;
        } else {
            return false;
        }
    }

    static void openPackage(IPackageItem item, Player user, InteractionHand hand) {
        SimpleContainer contents = new SimpleContainer(item.getCapacity());
        ItemStack parcel = user.getItemInHand(hand);
        ListTag list = parcel.getOrCreateTag().getList("parcel", Tag.TAG_COMPOUND);
        contents.fromTag(list);
        for (int i = 0; i < item.getCapacity(); ++i) {
            user.getInventory().placeItemBackInInventory(contents.getItem(i));
        }
    }

    static PackageTooltipData getTooltipData(IPackageItem item, ItemStack stack) {
        SimpleContainer contents = new SimpleContainer(item.getCapacity());
        ListTag list = stack.getOrCreateTag().getList("parcel", Tag.TAG_COMPOUND);
        contents.fromTag(list);
        return new PackageTooltipData(contents.items);
    }
}
