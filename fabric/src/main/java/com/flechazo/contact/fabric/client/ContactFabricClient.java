package com.flechazo.contact.fabric.client;

import com.flechazo.contact.ContactClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.resources.model.ModelResourceLocation;

public final class ContactFabricClient implements ClientModInitializer, ModelLoadingPlugin {
    @Override
    public void onInitializeClient() {
        ContactClient.onInitializeClient();
    }

    @Override
    public void onInitializeModelLoader(Context pluginContext) {
        pluginContext.addModels(
                new ModelResourceLocation("contact", "postcard_pin", ""),
                new ModelResourceLocation("contact", "postcard", "")
        );
    }
}