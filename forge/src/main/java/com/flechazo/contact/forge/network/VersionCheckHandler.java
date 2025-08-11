package com.flechazo.contact.forge.network;

import com.flechazo.contact.Contact;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Objects;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public final class VersionCheckHandler {
    public static final ResourceLocation VERSION_CHECK = new ResourceLocation(Contact.MOD_ID, "version");
    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            VERSION_CHECK,
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        INSTANCE.messageBuilder(VersionCheckPacket.class, 0)
                .loginIndex(VersionCheckPacket::getLoginIndex, VersionCheckPacket::setLoginIndex)
                .encoder(VersionCheckPacket::encode)
                .decoder(VersionCheckPacket::decode)
                .consumerNetworkThread(VersionCheckPacket::handle)
                .add();
    }

    public static class VersionCheckPacket implements IntSupplier {
        private final String version;
        private int loginIndex;

        public VersionCheckPacket() {
            this.version = Contact.NETWORK_VERSION;
            this.loginIndex = 0;
        }

        public VersionCheckPacket(String version) {
            this.version = version;
            this.loginIndex = 0;
        }

        public int getLoginIndex() {
            return loginIndex;
        }

        public void setLoginIndex(int loginIndex) {
            this.loginIndex = loginIndex;
        }

        @Override
        public int getAsInt() {
            return this.loginIndex;
        }

        public static void encode(VersionCheckPacket packet, FriendlyByteBuf buf) {
            buf.writeUtf(packet.version, 32767);
            // 登录索引由框架通过 setter 注入，不需要写入缓冲
        }

        public static VersionCheckPacket decode(FriendlyByteBuf buf) {
            String version = buf.readUtf(32767);
            return new VersionCheckPacket(version);
        }

        public static void handle(VersionCheckPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                if (context.getDirection() == NetworkDirection.LOGIN_TO_CLIENT) {
                    // 客户端接收到服务端的版本检查请求，回复版本信息
                    INSTANCE.reply(new VersionCheckPacket(), context);
                } else if (context.getDirection() == NetworkDirection.LOGIN_TO_SERVER) {
                    // 服务端接收到客户端的版本信息，进行验证
                    if (!Objects.equals(Contact.NETWORK_VERSION, packet.version)) {
                        context.getNetworkManager().disconnect(Component.translatable("message.contact.disconnect.mismatch"));
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }
}