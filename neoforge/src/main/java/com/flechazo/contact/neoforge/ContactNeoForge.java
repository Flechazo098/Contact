package com.flechazo.contact.neoforge;

import com.flechazo.contact.Contact;
import com.flechazo.contact.neoforge.attachment.ContactAttachments;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Contact.MOD_ID)
public final class ContactNeoForge {
    public ContactNeoForge(IEventBus modBus) {
        ContactAttachments.ATTACHMENT_TYPES.register(modBus);
        Contact.init();
    }
}