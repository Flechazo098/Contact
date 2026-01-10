package com.flechazo.contact.network;

import cc.sighs.oelib.network.api.INetworkContext;
import cc.sighs.oelib.network.api.INetworkPacket;
import cc.sighs.oelib.network.api.NetworkPacket;
import cc.sighs.oelib.network.api.Side;
import cc.sighs.oelib.network.serialization.NetFieldCodec;
import com.flechazo.contact.Contact;
import com.flechazo.contact.common.component.ContactDataComponents;
import com.flechazo.contact.common.item.PostcardItem;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

@NetworkPacket(modId = Contact.MOD_ID, id = "postcard_edit_message", side = Side.SERVER)
public record PostcardEditMessage(
        @NetFieldCodec(holder = ItemStack.class)
        ItemStack postcard,
        int held
) implements INetworkPacket<PostcardEditMessage> {

    @Override
    public void handle(INetworkContext context) {
        var player = context.sender();
        if (player == null) {
            return;
        }

        if (postcard.getItem() instanceof PostcardItem) {
            if (Inventory.isHotbarSlot(held) || held == 40) {
                var card = player.getInventory().getItem(held);
                if (card.getItem() instanceof PostcardItem) {
                    var text = postcard.get(ContactDataComponents.POSTCARD_TEXT.get());
                    var styleId = postcard.get(ContactDataComponents.POSTCARD_STYLE_ID.get());
                    var sender = postcard.get(ContactDataComponents.POSTCARD_SENDER.get());

                    if (text != null) {
                        card.set(ContactDataComponents.POSTCARD_TEXT.get(), text);
                    }
                    if (styleId != null) card.set(ContactDataComponents.POSTCARD_STYLE_ID.get(), styleId);
                    if (sender != null) card.set(ContactDataComponents.POSTCARD_SENDER.get(), sender);
                }
            }
        }
    }
}