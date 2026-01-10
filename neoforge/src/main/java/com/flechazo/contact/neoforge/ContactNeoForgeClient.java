package com.flechazo.contact.neoforge;

import cc.sighs.oelib.config.ui.screen.ConfigScreen;
import com.flechazo.contact.Contact;
import com.flechazo.contact.ContactClient;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = Contact.MOD_ID, dist = Dist.CLIENT)
public class ContactNeoForgeClient {
    public ContactNeoForgeClient(IEventBus eventBus, ModContainer container) {
        eventBus.addListener(this::onRegisterAdditionalModels);
        ContactClient.onInitializeClient();
        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> (minecraft, parent) -> new ConfigScreen(parent, Contact.MOD_ID));
    }

    @SubscribeEvent
    public void onRegisterAdditionalModels(ModelEvent.RegisterAdditional event) {
        event.register(new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(Contact.MOD_ID, "block/postcard_pin"), ModelResourceLocation.STANDALONE_VARIANT));
        event.register(new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(Contact.MOD_ID, "block/postcard"), ModelResourceLocation.STANDALONE_VARIANT));
    }
}