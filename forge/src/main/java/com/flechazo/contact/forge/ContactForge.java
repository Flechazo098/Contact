package com.flechazo.contact.forge;

import cc.sighs.oelib.network.api.NetworkManager;
import com.flechazo.contact.Contact;
import com.flechazo.contact.ContactClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(Contact.MOD_ID)
public final class ContactForge {
    public ContactForge() {
        NetworkManager.registerPacketScanPackage("com.flechazo.contact.network");
        Contact.init();
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ContactClient::onInitializeClient);
    }
}