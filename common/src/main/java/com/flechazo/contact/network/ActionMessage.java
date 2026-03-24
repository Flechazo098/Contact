package com.flechazo.contact.network;

import cc.sighs.oelib.network.api.INetworkContext;
import cc.sighs.oelib.network.api.INetworkPacket;
import cc.sighs.oelib.network.api.NetworkPacket;
import cc.sighs.oelib.network.api.Side;
import com.flechazo.contact.Contact;
import com.flechazo.contact.helper.NetworkHelper;

@NetworkPacket(modId = Contact.MOD_ID, id = "action_message", side = Side.BOTH)
public record ActionMessage(int action, String extra) implements INetworkPacket<ActionMessage> {
    @Override
    public void handle(INetworkContext context) {
        if (context.isServerSide()) {
            NetworkHelper.handleActionServer(context, action, extra);
        } else {
            NetworkHelper.handleActionClient(context, action);
        }
    }
}