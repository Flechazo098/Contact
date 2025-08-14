package com.flechazo.contact.network;

import com.flechazo.contact.Contact;
import com.flechazo.contact.common.component.ContactDataComponents;
import com.flechazo.contact.common.item.PostcardItem;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public record PostcardEditMessage(ItemStack postcard, int held) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<PostcardEditMessage> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Contact.MOD_ID, "postcard_edit_message"));

    public static final StreamCodec<RegistryFriendlyByteBuf, PostcardEditMessage> STREAM_CODEC =
            StreamCodec.composite(
                    ItemStack.STREAM_CODEC, PostcardEditMessage::postcard,
                    ByteBufCodecs.VAR_INT, PostcardEditMessage::held,
                    PostcardEditMessage::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handleServer(ServerPlayer player) {
        if (player == null) {
            return;
        }

        if (postcard.getItem() instanceof PostcardItem) {
            if (Inventory.isHotbarSlot(held) || held == 40) {
                ItemStack card = player.getInventory().getItem(held);
                if (card.getItem() instanceof PostcardItem) {
                    String text = postcard.get(ContactDataComponents.POSTCARD_TEXT.get());
                    ResourceLocation styleId = postcard.get(ContactDataComponents.POSTCARD_STYLE_ID.get());
                    String sender = postcard.get(ContactDataComponents.POSTCARD_SENDER.get());

                    if (text != null) {
                        card.set(ContactDataComponents.POSTCARD_TEXT.get(), text);
                    }
                    if (styleId != null) card.set(ContactDataComponents.POSTCARD_STYLE_ID.get(), styleId);
                    if (sender != null) card.set(ContactDataComponents.POSTCARD_SENDER.get(), sender);
                }
            }
        }
    }

    public static PostcardEditMessage create(ItemStack postcard, int held) {
        return new PostcardEditMessage(postcard, held);
    }

    public void sendToServer() {
        NetworkManager.sendToServer(this);
    }
}