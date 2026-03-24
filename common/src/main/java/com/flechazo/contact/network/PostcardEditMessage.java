package com.flechazo.contact.network;

import cc.sighs.oelib.network.api.INetworkContext;
import cc.sighs.oelib.network.api.INetworkPacket;
import cc.sighs.oelib.network.api.NetworkPacket;
import cc.sighs.oelib.network.api.Side;
import cc.sighs.oelib.network.serialization.NetFieldCodec;
import com.flechazo.contact.Contact;
import com.flechazo.contact.common.item.PostcardItem;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

@NetworkPacket(modId = Contact.MOD_ID, id = "postcard_edit_message", side = Side.SERVER)
public record PostcardEditMessage(
        ItemStack postcard,
        int held
) implements INetworkPacket<PostcardEditMessage> {

    @Override
    public void handle(INetworkContext context) {
        var player = context.sender();
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
}