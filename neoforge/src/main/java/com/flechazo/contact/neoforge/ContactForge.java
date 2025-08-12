package com.flechazo.contact.neoforge;

import com.flechazo.contact.Contact;
import com.flechazo.contact.neoforge.network.VersionCheckHandler;
import com.flechazo.contact.network.*;
import com.flechazo.contact.resourse.PostcardStyle;
import com.mafuyu404.oelib.api.net.NetworkManager;
import com.mafuyu404.oelib.forge.data.DataRegistry;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Contact.MOD_ID)
public final class ContactForge {
    public ContactForge() {
        EventBuses.registerModEventBus(Contact.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        DataRegistry.register(PostcardStyle.class);
        Contact.init();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
    }

    public void commonSetup(FMLCommonSetupEvent event) {
        NetworkManager.registerPackets(ActionMessage.class, AddresseeDataMessage.class, EnquireAddresseeMessage.class, PostcardEditMessage.class, TextBoxEditMessage.class);
        VersionCheckHandler.register();
    }
}