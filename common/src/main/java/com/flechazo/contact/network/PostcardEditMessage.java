package com.flechazo.contact.network;

import com.flechazo.contact.common.item.PostcardItem;
import com.mafuyu404.oelib.api.net.INetworkContext;
import com.mafuyu404.oelib.api.net.NetworkPacket;
import com.mafuyu404.oelib.api.net.Side;
import com.mafuyu404.oelib.api.net.SimplePacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

@NetworkPacket(side = Side.SERVER)
public class PostcardEditMessage extends SimplePacket<PostcardEditMessage> {
    private final ItemStack postcard;
    private final int held;

    public PostcardEditMessage(ItemStack postcard, int held) {
        this.postcard = postcard;
        this.held = held;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeItem(postcard);
        buf.writeInt(held);
    }

    public static PostcardEditMessage decode(FriendlyByteBuf buf) {
        ItemStack postcard = buf.readItem();
        int held = buf.readInt();
        return new PostcardEditMessage(postcard, held);
    }

    @Override
    protected void handleServer(INetworkContext context) {
        ServerPlayer player = getSender(context);
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
}