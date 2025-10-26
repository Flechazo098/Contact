package com.flechazo.contact.network;

import com.flechazo.contact.Contact;
import com.flechazo.contact.common.screenhandler.PostboxScreenHandler;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public class AddresseeDataMessage {
    private static final ResourceLocation ID = new ResourceLocation(Contact.MOD_ID, "addressee_data");

    private final List<String> names;
    private final List<Integer> ticks; // -1 means mailbox is full, -2 means no mailbox

    public AddresseeDataMessage(List<String> names, List<Integer> ticks) {
        this.names = names;
        this.ticks = ticks;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(names.size());
        for (String name : names) {
            buf.writeUtf(name, 32767);
        }
        for (int tick : ticks) {
            buf.writeInt(tick);
        }
    }

    public static AddresseeDataMessage decode(FriendlyByteBuf buf) {
        int size = buf.readInt();
        List<String> names = new ArrayList<>();
        List<Integer> ticks = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            names.add(buf.readUtf(32767));
        }
        for (int i = 0; i < size; i++) {
            ticks.add(buf.readInt());
        }

        return new AddresseeDataMessage(names, ticks);
    }

    public void handleClient() {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null) return;

        if (client.player.containerMenu instanceof PostboxScreenHandler container) {
            container.names = names;
            container.ticks = ticks;
        }
    }

    public static AddresseeDataMessage create(List<String> names, List<Integer> ticks) {
        return new AddresseeDataMessage(names, ticks);
    }

    public void sendTo(ServerPlayer player) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        this.encode(buf);
        NetworkManager.sendToPlayer(player, ID, buf);
    }

    public static void registerS2C() {
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, ID, (buf, ctx) -> {
            AddresseeDataMessage msg = decode(buf);
            Minecraft mc = Minecraft.getInstance();
            mc.execute(msg::handleClient);
        });
    }
}