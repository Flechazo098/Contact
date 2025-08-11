package com.flechazo.contact.fabric.client;

import com.flechazo.contact.client.color.block.MailboxBlockColor;
import com.flechazo.contact.platform.PlatformHelper;
import net.fabricmc.fabric.impl.client.rendering.ColorProviderRegistryImpl;
import net.minecraft.client.color.block.BlockColor;

import static com.flechazo.contact.common.block.BlockRegistry.*;

public final class BlockColorsRegistry {
    public static final BlockColor MAILBOX_COLOR = new MailboxBlockColor();

    public static void init() {
        ColorProviderRegistryImpl.BLOCK.register(MAILBOX_COLOR,
                ORANGE_MAILBOX.get(), MAGENTA_MAILBOX.get(), LIGHT_BLUE_MAILBOX.get(), YELLOW_MAILBOX.get(),
                LIME_MAILBOX.get(), PINK_MAILBOX.get(), GRAY_MAILBOX.get(), LIGHT_GRAY_MAILBOX.get(),
                CYAN_MAILBOX.get(), PURPLE_MAILBOX.get(), BLUE_MAILBOX.get(), BROWN_MAILBOX.get(),
                GREEN_MAILBOX.get(), RED_MAILBOX.get(), BLACK_MAILBOX.get(), WHITE_MAILBOX.get());
    }
}
