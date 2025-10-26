package com.flechazo.contact.network;

import com.flechazo.contact.Contact;
import com.flechazo.contact.common.item.PostcardItem;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class PostcardEditMessage {
    private static final ResourceLocation ID = new ResourceLocation(Contact.MOD_ID, "postcard_edit");

    private final ItemStack postcard;
    private final int held;

    public PostcardEditMessage(ItemStack postcard, int held) {
        this.postcard = postcard;
        this.held = held;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeItem(postcard);
        buf.writeInt(held);
    }

    public static PostcardEditMessage decode(FriendlyByteBuf buf) {
        ItemStack postcard = buf.readItem();
        int held = buf.readInt();
        return new PostcardEditMessage(postcard, held);
    }

    public void handleServer(ServerPlayer player) {
        if (player == null) {
            return;
        }

        if (postcard.getItem() instanceof PostcardItem && postcard.hasTag()) {
            if (Inventory.isHotbarSlot(held) || held == 40) {
                ItemStack card = player.getInventory().getItem(held);
                if (card.getItem() instanceof PostcardItem) {
                    card.setTag(postcard.getTag());
                }
            }
        }
    }

    public static PostcardEditMessage create(ItemStack postcard, int held) {
        return new PostcardEditMessage(postcard, held);
    }

    public void sendToServer() {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        this.encode(buf);
        NetworkManager.sendToServer(ID, buf);
    }

    public static void registerC2S() {
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, ID, (buf, ctx) -> {
            PostcardEditMessage msg = decode(buf);
            ServerPlayer player = (ServerPlayer) ctx.getPlayer();
            if (player != null) {
                player.server.execute(() -> msg.handleServer(player));
            }
        });
    }
}