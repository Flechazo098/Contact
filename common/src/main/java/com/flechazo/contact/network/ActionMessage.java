package com.flechazo.contact.network;

import com.flechazo.contact.Contact;
import com.flechazo.contact.client.ClientProxy;
import com.flechazo.contact.common.screenhandler.PackageScreenHandler;
import com.flechazo.contact.common.screenhandler.PostboxScreenHandler;
import com.flechazo.contact.common.screenhandler.RedPacketEnvelopeScreenHandler;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class ActionMessage {
    private static final ResourceLocation ID = new ResourceLocation(Contact.MOD_ID, "action");

    private final int action;
    private final String extra;

    public ActionMessage(int action, String extra) {
        this.action = action;
        this.extra = extra;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(action);
        buf.writeUtf(extra != null ? extra : "", 32767);
    }

    public static ActionMessage decode(FriendlyByteBuf buf) {
        try {
            int action = buf.readInt();
            String extra = buf.readUtf(32767);
            return new ActionMessage(action, extra.isEmpty() ? null : extra);
        } catch (Exception e) {
            Contact.error("[ActionMessage/decode] Failed to decode, buf readableBytes=" + buf.readableBytes(), e);
            throw e;
        }
    }

    public void handleClient() {
        Minecraft client = Minecraft.getInstance();

        if (action == 0) {
            ClientProxy.notifyNewMail(client);
        } else if (action == 1) {
            if (client.player != null && client.player.containerMenu instanceof PostboxScreenHandler container) {
                container.status = 2;
            }
        }
    }

    public void handleServer(ServerPlayer player) {
        if (player == null) return;
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

    public void sendTo(ServerPlayer player) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        this.encode(buf);
        NetworkManager.sendToPlayer(player, ID, buf);
    }

    public void sendToServer() {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        this.encode(buf);
        NetworkManager.sendToServer(ID, buf);
    }

    public static void registerC2S() {
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, ID, (buf, ctx) -> {
            ActionMessage msg = decode(buf);
            ServerPlayer player = (ServerPlayer) ctx.getPlayer();
            if (player != null) {
                player.server.execute(() -> msg.handleServer(player));
            }
        });
    }

    public static void registerS2C() {
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, ID, (buf, ctx) -> {
            ActionMessage msg = decode(buf);
            Minecraft mc = Minecraft.getInstance();
            mc.execute(msg::handleClient);
        });
    }
}