package com.flechazo.contact;

import com.flechazo.contact.common.block.BlockRegistry;
import com.flechazo.contact.common.config.ContactClientConfig;
import com.flechazo.contact.common.screenhandler.ScreenHandlerTypeRegistry;
import com.iafenvoy.jupiter.ConfigManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ContactClient {
    public static void onInitializeClient() {
        ConfigManager.getInstance().registerConfigHandler(ContactClientConfig.INSTANCE);
        ScreenHandlerTypeRegistry.registerContainers();
        BlockRegistry.registerRenderLayer();
    }
}
