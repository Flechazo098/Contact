package com.flechazo.contact.network;

import com.flechazo.contact.Contact;
import com.flechazo.contact.common.component.ContactDataComponents;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public record TextBoxEditMessage(ItemStack item, int held) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<TextBoxEditMessage> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Contact.MOD_ID, "textbox_edit_message"));

    public static final StreamCodec<RegistryFriendlyByteBuf, TextBoxEditMessage> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC, TextBoxEditMessage::item,
            ByteBufCodecs.VAR_INT, TextBoxEditMessage::held,
            TextBoxEditMessage::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handleServer(ServerPlayer player) {
        if (player == null) {
            return;
        }

        if (Inventory.isHotbarSlot(held) || held == 40) {
            ItemStack card = player.getInventory().getItem(held);
            String content = item.get(ContactDataComponents.TEXT_BOX_CONTENT.get());
            if (content != null) {
                card.set(ContactDataComponents.TEXT_BOX_CONTENT.get(), content);
            }
        }
    }

    public static TextBoxEditMessage create(ItemStack item, int held) {
        return new TextBoxEditMessage(item, held);
    }

    public void sendToServer() {
        NetworkManager.sendToServer(this);
    }

}