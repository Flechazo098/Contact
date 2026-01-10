package com.flechazo.contact.fabric;

import com.flechazo.contact.Contact;
import com.flechazo.contact.ContactClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.resources.ResourceLocation;

public final class ContactFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModelLoadingPlugin.register(out -> {
            out.addModels(
                    ResourceLocation.fromNamespaceAndPath(Contact.MOD_ID, "block/postcard_pin"),
                    ResourceLocation.fromNamespaceAndPath(Contact.MOD_ID, "block/postcard")
            );
        });
        ContactClient.onInitializeClient();
    }
}