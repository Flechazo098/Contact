package com.flechazo.contact.network;

import cc.sighs.oelib.network.api.INetworkContext;
import cc.sighs.oelib.network.api.INetworkPacket;
import cc.sighs.oelib.network.api.NetworkPacket;
import cc.sighs.oelib.network.api.Side;
import cc.sighs.oelib.network.serialization.NetFieldCodec;
import com.flechazo.contact.Contact;
import com.flechazo.contact.common.component.ContactDataComponents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

@NetworkPacket(modId = Contact.MOD_ID, id = "textbox_edit_message", side = Side.SERVER)
public record TextBoxEditMessage(@NetFieldCodec(holder = ItemStack.class) ItemStack item, int held) implements INetworkPacket<TextBoxEditMessage> {

    @Override
    public void handle(INetworkContext context) {
        var player = context.sender();
        if (player == null) {
            return;
        }

        if (Inventory.isHotbarSlot(held) || held == 40) {
            var card = player.getInventory().getItem(held);
            var content = item.get(ContactDataComponents.TEXT_BOX_CONTENT.get());
            if (content != null) {
                card.set(ContactDataComponents.TEXT_BOX_CONTENT.get(), content);
            }
        }
    }
}