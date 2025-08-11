package com.flechazo.contact.network;

import com.flechazo.contact.common.screenhandler.PostboxScreenHandler;
import com.mafuyu404.oelib.api.net.INetworkContext;
import com.mafuyu404.oelib.api.net.NetworkPacket;
import com.mafuyu404.oelib.api.net.Side;
import com.mafuyu404.oelib.api.net.SimplePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

import java.util.ArrayList;
import java.util.List;

@NetworkPacket(side = Side.CLIENT)
public class AddresseeDataMessage extends SimplePacket<AddresseeDataMessage> {
    private final List<String> names;
    private final List<Integer> ticks; // -1 means mailbox is full, -2 means no mailbox

    public AddresseeDataMessage(List<String> names, List<Integer> ticks) {
        this.names = names;
        this.ticks = ticks;
    }

    @Override
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

    @Override
    protected void handleClient(INetworkContext context) {
        Minecraft client = getClient(context);
        if (client == null || client.player == null) return;

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
}