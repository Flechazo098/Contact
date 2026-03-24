package com.flechazo.contact.network;

import cc.sighs.oelib.network.api.INetworkContext;
import cc.sighs.oelib.network.api.INetworkPacket;
import cc.sighs.oelib.network.api.NetworkPacket;
import cc.sighs.oelib.network.api.Side;
import com.flechazo.contact.Contact;
import com.flechazo.contact.common.screenhandler.PostboxScreenHandler;

import java.util.List;

@NetworkPacket(modId = Contact.MOD_ID, id = "addressee_data_message", side = Side.CLIENT)
public record AddresseeDataMessage(List<String> names,
                                   List<Integer> ticks) implements INetworkPacket<AddresseeDataMessage> {

    @Override
    public void handle(INetworkContext context) {
        var client = context.client();
        if (client.player == null) return;

        if (client.player.containerMenu instanceof PostboxScreenHandler container) {
            container.names = names;
            container.ticks = ticks;
        }
    }
}