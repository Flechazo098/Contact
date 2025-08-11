package com.flechazo.contact.fabric;

import com.flechazo.contact.Contact;
import com.flechazo.contact.fabric.network.VersionCheckHandler;
import com.flechazo.contact.resourse.PostcardStyle;
import com.mafuyu404.oelib.fabric.data.DataRegistry;
import net.fabricmc.api.ModInitializer;

public final class ContactFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        DataRegistry.register(PostcardStyle.class);
        Contact.initFabric();
        VersionCheckHandler.registerServerMessage();
    }
}
