package com.flechazo.contact.network;

import cc.sighs.oelib.network.api.INetworkContext;
import cc.sighs.oelib.network.api.INetworkPacket;
import cc.sighs.oelib.network.api.NetworkPacket;
import cc.sighs.oelib.network.api.Side;
import com.flechazo.contact.Contact;
import com.flechazo.contact.helper.NetworkHelper;

import java.util.Locale;

@NetworkPacket(modId = Contact.MOD_ID, id = "enquire_addressee_message", side = Side.SERVER)
public record EnquireAddresseeMessage(String nameIn,
                                      boolean shouldSend) implements INetworkPacket<EnquireAddresseeMessage> {

    @Override
    public void handle(INetworkContext context) {
        var player = context.sender();
        if (player == null || nameIn.isEmpty()) {
            return;
        }

        String lowerIn = nameIn.toLowerCase(Locale.ROOT);

        if (lowerIn.equals("@e") && player.server.getProfilePermissions(player.getGameProfile()) >= 2) {
            // 管理员全服寄送
            NetworkHelper.handleAdminBroadcast(player, shouldSend);
            return;
        }

        NetworkHelper.handleNormalEnquiry(player, lowerIn, nameIn, shouldSend);
    }
}