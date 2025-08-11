package com.flechazo.contact.fabric.network;

import com.flechazo.contact.Contact;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public final class VersionCheckHandler {
    public static final ResourceLocation VERSION_CHECK = new ResourceLocation(Contact.MOD_ID, "version");

    public static void registerServerMessage() {
        ServerLoginConnectionEvents.QUERY_START.register(VersionCheckHandler::onLoginQueryStart);
        ServerLoginNetworking.registerGlobalReceiver(VERSION_CHECK, VersionCheckHandler::onServerLoginCheck);
    }

    public static void registerClientMessage() {
        ClientLoginNetworking.registerGlobalReceiver(VERSION_CHECK, VersionCheckHandler::onClientLoginQuery);
    }

    private static void onLoginQueryStart(ServerLoginPacketListenerImpl serverLoginPacketListener, MinecraftServer server, PacketSender sender, ServerLoginNetworking.LoginSynchronizer loginSynchronizer) {
        var versionCheck = PacketByteBufs.create();
        sender.sendPacket(VERSION_CHECK, versionCheck);
    }

    private static CompletableFuture<FriendlyByteBuf> onClientLoginQuery(Minecraft client, ClientHandshakePacketListenerImpl clientLoginNetworkHandler, FriendlyByteBuf buf, Consumer<GenericFutureListener<? extends Future<? super Void>>> genericFutureListenerConsumer) {
        var response = PacketByteBufs.create();
        response.writeUtf(Contact.NETWORK_VERSION, 32767);
        return CompletableFuture.completedFuture(response);
    }

    private static void onServerLoginCheck(MinecraftServer server, ServerLoginPacketListenerImpl handler, boolean responded, FriendlyByteBuf buf, ServerLoginNetworking.LoginSynchronizer loginSynchronizer, PacketSender packetSender) {
        try {
            String version = buf.readUtf(32767);
            if (!Objects.equals(Contact.NETWORK_VERSION, version)) {
                handler.disconnect(Component.translatable("message.contact.disconnect.mismatch"));
            }
        } catch (Exception ignored) {
            handler.disconnect(Component.literal("Version is out-of-date. Please update your Contact mod."));
        }
    }
}