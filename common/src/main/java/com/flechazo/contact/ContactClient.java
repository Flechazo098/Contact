package com.flechazo.contact;

import com.flechazo.contact.client.ClientProxy;
import com.flechazo.contact.client.color.block.MailboxBlockColor;
import com.flechazo.contact.client.color.item.MailboxItemColor;
import com.flechazo.contact.common.block.BlockRegistry;
import com.flechazo.contact.common.config.ContactClientConfig;
import com.flechazo.contact.network.NetworkHelper;
import com.iafenvoy.jupiter.ConfigManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;

@Environment(EnvType.CLIENT)
public class ContactClient {
    public static void onInitializeClient() {
        ConfigManager.getInstance().registerConfigHandler(ContactClientConfig.INSTANCE);
        NetworkHelper.initializeClient();
        ClientProxy.bindEntityRenderer();
        BlockRegistry.registerRenderLayer();
    }

    public static final BlockColor BLOCK_MAILBOX_COLOR = new MailboxBlockColor();
    public static final ItemColor ITEM_MAILBOX_COLOR = new MailboxItemColor();
}
