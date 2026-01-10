package com.flechazo.contact.fabric.compat;

import cc.sighs.oelib.config.ui.screen.ConfigScreen;
import com.flechazo.contact.Contact;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screens.Screen;

public class ModMenuCompat implements ModMenuApi {
    @Override
    public ConfigScreenFactory<? extends Screen> getModConfigScreenFactory() {
        return parent -> new ConfigScreen(parent, Contact.MOD_ID);
    }
}
