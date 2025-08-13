package com.flechazo.contact.fabric.client;

import net.fabricmc.fabric.impl.client.rendering.ColorProviderRegistryImpl;

import static com.flechazo.contact.ContactClient.ITEM_MAILBOX_COLOR;
import static com.flechazo.contact.common.item.ItemRegistry.*;

public final class ItemColorsRegistry {

    public static void init() {
        ColorProviderRegistryImpl.ITEM.register(ITEM_MAILBOX_COLOR,
                ORANGE_MAILBOX_ITEM.get(), MAGENTA_MAILBOX_ITEM.get(), LIGHT_BLUE_MAILBOX_ITEM.get(), YELLOW_MAILBOX_ITEM.get(),
                LIME_MAILBOX_ITEM.get(), PINK_MAILBOX_ITEM.get(), GRAY_MAILBOX_ITEM.get(), LIGHT_GRAY_MAILBOX_ITEM.get(),
                CYAN_MAILBOX_ITEM.get(), PURPLE_MAILBOX_ITEM.get(), BLUE_MAILBOX_ITEM.get(), BROWN_MAILBOX_ITEM.get(),
                GREEN_MAILBOX_ITEM.get(), RED_MAILBOX_ITEM.get(), BLACK_MAILBOX_ITEM.get(), WHITE_MAILBOX_ITEM.get());
    }
}