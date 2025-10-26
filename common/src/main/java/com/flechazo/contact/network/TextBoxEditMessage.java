package com.flechazo.contact.network;

import com.flechazo.contact.Contact;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class TextBoxEditMessage {
    private static final ResourceLocation ID = new ResourceLocation(Contact.MOD_ID, "textbox_edit");

    private final ItemStack item;
    private final int held;

    public TextBoxEditMessage(ItemStack item, int held) {
        this.item = item;
        this.held = held;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeItem(item);
        buf.writeInt(held);
    }

    public static TextBoxEditMessage decode(FriendlyByteBuf buf) {
        ItemStack item = buf.readItem();
        int held = buf.readInt();
        return new TextBoxEditMessage(item, held);
    }

    public void handleServer(ServerPlayer player) {
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

    public void sendToServer() {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        this.encode(buf);
        NetworkManager.sendToServer(ID, buf);
    }

    public static void registerC2S() {
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, ID, (buf, ctx) -> {
            TextBoxEditMessage msg = decode(buf);
            ServerPlayer player = (ServerPlayer) ctx.getPlayer();
            if (player != null) {
                player.server.execute(() -> msg.handleServer(player));
            }
        });
    }
}