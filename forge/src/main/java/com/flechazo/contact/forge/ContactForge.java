package com.flechazo.contact.forge;

import com.flechazo.contact.Contact;
import com.flechazo.contact.resourse.PostcardDataManager;
import com.flechazo.contact.resourse.PostcardStyle;
import com.mafuyu404.oelib.forge.data.DataRegistry;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Contact.MOD_ID)
public final class ContactForge {
    public ContactForge() {
        EventBuses.registerModEventBus(Contact.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        DataRegistry.register(PostcardStyle.class);
        PostcardDataManager.initialize();
        Contact.init();
    }
}