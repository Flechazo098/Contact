package com.flechazo.contact.neoforge;

import com.flechazo.contact.Contact;
import com.flechazo.contact.data.PostcardDataManager;
import com.flechazo.contact.neoforge.attachment.ContactAttachments;
import com.flechazo.contact.data.PostcardStyle;
import com.mafuyu404.oelib.neoforge.data.DataRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Contact.MOD_ID)
public final class ContactNeoForge {
    public ContactNeoForge(IEventBus modBus) {
        ContactAttachments.ATTACHMENT_TYPES.register(modBus);
        DataRegistry.register(PostcardStyle.class);
        PostcardDataManager.initialize();
        Contact.init();
    }
}