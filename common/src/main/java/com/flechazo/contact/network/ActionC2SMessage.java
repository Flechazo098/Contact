package com.flechazo.contact.network;

import com.flechazo.contact.Contact;
import com.flechazo.contact.common.screenhandler.PackageScreenHandler;
import com.flechazo.contact.common.screenhandler.RedPacketEnvelopeScreenHandler;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public record ActionC2SMessage(int action, String extra) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ActionC2SMessage> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Contact.MOD_ID, "action_c2s_message"));

    public static final StreamCodec<FriendlyByteBuf, ActionC2SMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ActionC2SMessage::action,
            ByteBufCodecs.STRING_UTF8, ActionC2SMessage::extra,
            ActionC2SMessage::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handleServer(ServerPlayer player) {
        if (player == null) {
            return;
        }

        if (action == 0) {
            packParcel(player, extra);
        }
    }

    public void sendToServer() {
        NetworkManager.sendToServer(this);
    }

    public static ActionC2SMessage create(int action) {
        return new ActionC2SMessage(action, "");
    }

    public static ActionC2SMessage create(int action, String extra) {
        return new ActionC2SMessage(action, extra != null ? extra : "");
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