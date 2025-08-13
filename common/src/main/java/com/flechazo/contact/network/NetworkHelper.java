package com.flechazo.contact.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.server.level.ServerPlayer;

public class NetworkHelper {

    public static void registerServerReceivers() {
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, EnquireAddresseeMessage.TYPE, EnquireAddresseeMessage.STREAM_CODEC,
                (payload, ctx) -> {
                    if (ctx.getPlayer() instanceof ServerPlayer player) {
                        ctx.queue(() -> payload.handleServer(player));
                    }
                });

        NetworkManager.registerReceiver(NetworkManager.Side.C2S, PostcardEditMessage.TYPE, PostcardEditMessage.STREAM_CODEC,
                (payload, ctx) -> {
                    if (ctx.getPlayer() instanceof ServerPlayer player) {
                        ctx.queue(() -> payload.handleServer(player));
                    }
                });

        NetworkManager.registerReceiver(NetworkManager.Side.C2S, TextBoxEditMessage.TYPE, TextBoxEditMessage.STREAM_CODEC,
                (payload, ctx) -> {
                    if (ctx.getPlayer() instanceof ServerPlayer player) {
                        ctx.queue(() -> payload.handleServer(player));
                    }
                });

        NetworkManager.registerReceiver(NetworkManager.Side.C2S, ActionC2SMessage.TYPE, ActionC2SMessage.STREAM_CODEC,
                (payload, ctx) -> {
                    if (ctx.getPlayer() instanceof ServerPlayer player) {
                        ctx.queue(() -> payload.handleServer(player));
                    }
                });
    }

    public static void registerClientReceivers() {
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, ActionS2CMessage.TYPE, ActionS2CMessage.STREAM_CODEC,
                (payload, ctx) -> ctx.queue(payload::handleClient));

        NetworkManager.registerReceiver(NetworkManager.Side.S2C, AddresseeDataMessage.TYPE, AddresseeDataMessage.STREAM_CODEC,
                (payload, ctx) -> ctx.queue(payload::handleClient));
    }

    public static void registerS2CPayloadTypes() {
        if (Platform.getEnvironment() == Env.SERVER) {
            NetworkManager.registerS2CPayloadType(ActionS2CMessage.TYPE, ActionS2CMessage.STREAM_CODEC);
            NetworkManager.registerS2CPayloadType(AddresseeDataMessage.TYPE, AddresseeDataMessage.STREAM_CODEC);
        }
    }

    public static void initializeServer() {
        registerServerReceivers();
        registerS2CPayloadTypes();
    }

    public static void initializeClient() {
        registerClientReceivers();
    }
}
