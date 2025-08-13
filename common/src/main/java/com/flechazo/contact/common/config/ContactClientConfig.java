package com.flechazo.contact.common.config;

import com.flechazo.contact.Contact;
import com.iafenvoy.jupiter.config.container.FileConfigContainer;
import com.iafenvoy.jupiter.config.entry.BooleanEntry;
import com.iafenvoy.jupiter.interfaces.IConfigEntry;
import net.minecraft.resources.ResourceLocation;

public class ContactClientConfig extends FileConfigContainer {
    public static final ContactClientConfig INSTANCE = new ContactClientConfig();

    public final IConfigEntry<Boolean> showNewMailToast = new BooleanEntry("config.contact.client.gui.showNewMailToast", true)
            .json("showNewMailToast");

    public ContactClientConfig() {
        super(ResourceLocation.fromNamespaceAndPath(Contact.MOD_ID, "contact_client_config"), "config.contact.client.title", "./config/contact/contact-client.json");
    }

    @Override
    public void init() {
        this.createTab("gui", "config.contact.client.category.gui")
                .add(this.showNewMailToast);
    }

    public static boolean isShowNewMailToast() {
        return INSTANCE.showNewMailToast.getValue();
    }
}