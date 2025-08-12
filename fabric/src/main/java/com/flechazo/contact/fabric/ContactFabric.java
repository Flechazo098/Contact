package com.flechazo.contact.fabric;

import com.flechazo.contact.Contact;
import com.flechazo.contact.common.block.BlockRegistry;
import com.flechazo.contact.common.config.ContactCommonConfig;
import com.flechazo.contact.common.entity.EntityTypeRegistry;
import com.flechazo.contact.common.item.ItemRegistry;
import com.flechazo.contact.common.registry.ModCreativeTabRegistry;
import com.flechazo.contact.common.registry.RegistryManager;
import com.flechazo.contact.common.screenhandler.ScreenHandlerTypeRegistry;
import com.flechazo.contact.common.tileentity.BlockEntityTypeRegistry;
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
        BlockRegistry.initBlocks();
        ItemRegistry.initItems();
        BlockEntityTypeRegistry.init();
        EntityTypeRegistry.init();
        NetworkManager.registerPackets(ActionMessage.class, EnquireAddresseeMessage.class, PostcardEditMessage.class, TextBoxEditMessage.class);
        VersionCheckHandler.registerServerMessage();
        ScreenHandlerTypeRegistry.init();
        RegistryManager.initialize();
        Contact.initFabric();
        DataRegistry.register(PostcardStyle.class);
        PostcardDataManager.initialize();
        ModCreativeTabRegistry.initialize();
    }
}
