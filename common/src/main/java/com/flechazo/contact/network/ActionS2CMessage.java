package com.flechazo.contact.network;

import com.flechazo.contact.Contact;
import com.flechazo.contact.client.ClientProxy;
import com.flechazo.contact.common.screenhandler.PostboxScreenHandler;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public record ActionS2CMessage(int action, String extra) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ActionS2CMessage> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Contact.MOD_ID, "action_s2c_message"));

    public static final StreamCodec<FriendlyByteBuf, ActionS2CMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ActionS2CMessage::action,
            ByteBufCodecs.STRING_UTF8, ActionS2CMessage::extra,
            ActionS2CMessage::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
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

    public void sendTo(ServerPlayer player) {
        NetworkManager.sendToPlayer(player, this);
    }

    public static ActionS2CMessage create(int action) {
        return new ActionS2CMessage(action, "");
    }

    public static ActionS2CMessage create(int action, String extra) {
        return new ActionS2CMessage(action, extra != null ? extra : "");
    }
}