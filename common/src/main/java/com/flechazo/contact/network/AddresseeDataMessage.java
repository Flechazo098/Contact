package com.flechazo.contact.network;

import com.flechazo.contact.Contact;
import com.flechazo.contact.common.screenhandler.PostboxScreenHandler;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public record AddresseeDataMessage(List<String> names, List<Integer> ticks) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<AddresseeDataMessage> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Contact.MOD_ID, "addressee_data_message"));

    public static final StreamCodec<FriendlyByteBuf, AddresseeDataMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), AddresseeDataMessage::names,
            ByteBufCodecs.VAR_INT.apply(ByteBufCodecs.list()), AddresseeDataMessage::ticks,
            AddresseeDataMessage::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handleClient() {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null) return;

        client.execute(() -> {
            if (client.player.containerMenu instanceof PostboxScreenHandler container) {
                container.names = names;
                container.ticks = ticks;
            }
        });
    }

    public static AddresseeDataMessage create(List<String> names, List<Integer> ticks) {
        return new AddresseeDataMessage(names, ticks);
    }

    public void sendTo(ServerPlayer player) {
        NetworkManager.sendToPlayer(player, this);
    }
}