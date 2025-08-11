package com.flechazo.contact.network;

import com.flechazo.contact.client.ClientProxy;
import com.flechazo.contact.common.screenhandler.PackageScreenHandler;
import com.flechazo.contact.common.screenhandler.PostboxScreenHandler;
import com.flechazo.contact.common.screenhandler.RedPacketEnvelopeScreenHandler;
import com.mafuyu404.oelib.api.net.INetworkContext;
import com.mafuyu404.oelib.api.net.NetworkPacket;
import com.mafuyu404.oelib.api.net.Side;
import com.mafuyu404.oelib.api.net.SimplePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

@NetworkPacket(side = Side.BOTH)
public class ActionMessage extends SimplePacket<ActionMessage> {
    private final int action;
    private final String extra;

    public ActionMessage(int action, String extra) {
        this.action = action;
        this.extra = extra;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(action);
        buf.writeUtf(extra != null ? extra : "", 32767);
    }

    public static ActionMessage decode(FriendlyByteBuf buf) {
        int action = buf.readInt();
        String extra = buf.readUtf(32767);
        return new ActionMessage(action, extra.isEmpty() ? null : extra);
    }

    @Override
    protected void handleClient(INetworkContext context) {
        Minecraft client = getClient(context);
        if (client == null) return;

        if (action == 0) {
            ClientProxy.notifyNewMail(client);
        } else if (action == 1) {
            if (client.player != null && client.player.containerMenu instanceof PostboxScreenHandler container) {
                container.status = 2;
            }
        }
    }

    @Override
    protected void handleServer(INetworkContext context) {
        ServerPlayer player = getSender(context);
        if (player == null) {
            return;
        }

        if (action == 0) {
            packParcel(player, extra);
        }
    }

    public static ActionMessage create(int action) {
        return new ActionMessage(action, null);
    }

    public static ActionMessage create(int action, String extra) {
        return new ActionMessage(action, extra);
    }

    private static void packParcel(ServerPlayer player, String extra) {
        if (player.containerMenu instanceof PackageScreenHandler screenHandler) {
            screenHandler.isPacked = true;
            if (screenHandler instanceof RedPacketEnvelopeScreenHandler redPacket) {
                redPacket.blessings = extra;
            }
            player.closeContainer();
        }
    }
}