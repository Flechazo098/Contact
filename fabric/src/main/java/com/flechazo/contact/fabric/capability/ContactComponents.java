package com.flechazo.contact.fabric.capability;

import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentInitializer;

public class ContactComponents implements WorldComponentInitializer {

    @Override
    public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
        registry.register(MailboxDataComponent.KEY, world -> new MailboxDataComponent());
    }
}
