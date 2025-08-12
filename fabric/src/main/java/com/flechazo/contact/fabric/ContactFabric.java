package com.flechazo.contact.fabric;

import com.flechazo.contact.Contact;
import com.flechazo.contact.common.config.ContactCommonConfig;
import com.flechazo.contact.fabric.network.VersionCheckHandler;
import com.flechazo.contact.network.ActionMessage;
import com.flechazo.contact.network.EnquireAddresseeMessage;
import com.flechazo.contact.network.PostcardEditMessage;
import com.flechazo.contact.network.TextBoxEditMessage;
import com.flechazo.contact.resourse.PostcardDataManager;
import com.flechazo.contact.resourse.PostcardStyle;
import com.iafenvoy.jupiter.ConfigManager;
import com.iafenvoy.jupiter.ServerConfigManager;
import com.mafuyu404.oelib.api.net.NetworkManager;
import com.mafuyu404.oelib.fabric.data.DataRegistry;
import net.fabricmc.api.ModInitializer;

public final class ContactFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ConfigManager.getInstance().registerConfigHandler(ContactCommonConfig.INSTANCE);
        ConfigManager.getInstance().registerServerConfig(ContactCommonConfig.INSTANCE, ServerConfigManager.PermissionChecker.IS_OPERATOR);
        NetworkManager.registerPackets(ActionMessage.class, EnquireAddresseeMessage.class, PostcardEditMessage.class, TextBoxEditMessage.class);
        VersionCheckHandler.registerServerMessage();
        Contact.init();
        DataRegistry.register(PostcardStyle.class);
        PostcardDataManager.initialize();
    }
}
