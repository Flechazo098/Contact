package com.flechazo.contact.network;

import cc.sighs.oelib.network.api.INetworkContext;
import cc.sighs.oelib.network.api.INetworkPacket;
import cc.sighs.oelib.network.api.NetworkPacket;
import cc.sighs.oelib.network.api.Side;
import cc.sighs.oelib.network.serialization.NetFieldCodec;
import com.flechazo.contact.Contact;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

@NetworkPacket(modId = Contact.MOD_ID, id = "textbox_edit_message", side = Side.SERVER)
public record TextBoxEditMessage(ItemStack item, int held) implements INetworkPacket<TextBoxEditMessage> {

    @Override
    public void handle(INetworkContext context) {
        var player = context.sender();
        if (player == null) {
            return;
        }

        if (item.hasTag()) {
            if (Inventory.isHotbarSlot(held) || held == 40) {
                var card = player.getInventory().getItem(held);
                card.setTag(item.getTag());
            }
        }
    }
}