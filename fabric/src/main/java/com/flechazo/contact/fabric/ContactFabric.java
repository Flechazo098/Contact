package com.flechazo.contact.fabric;

import com.flechazo.contact.Contact;
import com.flechazo.contact.common.config.ContactCommonConfig;
import com.flechazo.contact.fabric.network.VersionCheckHandler;
import com.flechazo.contact.resourse.PostcardDataManager;
import com.flechazo.contact.resourse.PostcardStyle;
import com.iafenvoy.jupiter.ConfigManager;
import com.iafenvoy.jupiter.ServerConfigManager;
import com.mafuyu404.oelib.fabric.data.DataRegistry;
import net.fabricmc.api.ModInitializer;

public final class ContactFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ConfigManager.getInstance().registerConfigHandler(ContactCommonConfig.INSTANCE);
        ConfigManager.getInstance().registerServerConfig(ContactCommonConfig.INSTANCE, ServerConfigManager.PermissionChecker.IS_OPERATOR);
        VersionCheckHandler.registerServerMessage();
        Contact.init();
        DataRegistry.register(PostcardStyle.class);
        PostcardDataManager.initialize();
    }
}
