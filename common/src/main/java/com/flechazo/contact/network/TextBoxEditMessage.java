package com.flechazo.contact.network;

import com.mafuyu404.oelib.api.net.INetworkContext;
import com.mafuyu404.oelib.api.net.NetworkPacket;
import com.mafuyu404.oelib.api.net.Side;
import com.mafuyu404.oelib.api.net.SimplePacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

@NetworkPacket(side = Side.SERVER)
public class TextBoxEditMessage extends SimplePacket<TextBoxEditMessage> {
    private final ItemStack item;
    private final int held;

    public TextBoxEditMessage(ItemStack item, int held) {
        this.item = item;
        this.held = held;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeItem(item);
        buf.writeInt(held);
    }

    public static TextBoxEditMessage decode(FriendlyByteBuf buf) {
        ItemStack item = buf.readItem();
        int held = buf.readInt();
        return new TextBoxEditMessage(item, held);
    }

    @Override
    protected void handleServer(INetworkContext context) {
        ServerPlayer player = getSender(context);
        if (player == null) {
            return;
        }

        if (item.hasTag()) {
            if (Inventory.isHotbarSlot(held) || held == 40) {
                ItemStack card = player.getInventory().getItem(held);
                card.setTag(item.getTag());
            }
        }
    }

    public static TextBoxEditMessage create(ItemStack item, int held) {
        return new TextBoxEditMessage(item, held);
    }
}