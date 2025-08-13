package com.flechazo.contact.network;

import com.flechazo.contact.Contact;
import com.flechazo.contact.common.component.ContactDataComponents;
import com.flechazo.contact.common.config.ContactCommonConfig;
import com.flechazo.contact.common.handler.AdvancementManager;
import com.flechazo.contact.common.handler.MailboxManager;
import com.flechazo.contact.common.item.IPackageItem;
import com.flechazo.contact.common.item.PostcardItem;
import com.flechazo.contact.common.screenhandler.PostboxScreenHandler;
import com.flechazo.contact.common.storage.IMailboxDataProvider;
import com.flechazo.contact.common.storage.MailToBeSent;
import com.flechazo.contact.common.storage.MailboxDataManager;
import dev.architectury.networking.NetworkManager;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.*;

public record EnquireAddresseeMessage(String nameIn, boolean shouldSend) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<EnquireAddresseeMessage> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Contact.MOD_ID, "enquire_addressee_message"));

    public static final StreamCodec<FriendlyByteBuf, EnquireAddresseeMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, EnquireAddresseeMessage::nameIn,
            ByteBufCodecs.BOOL, EnquireAddresseeMessage::shouldSend,
            EnquireAddresseeMessage::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handleServer(ServerPlayer player) {
        if (player == null || nameIn.isEmpty()) {
            return;
        }

        IMailboxDataProvider data = MailboxDataManager.getData(player.server);
        String lowerIn = nameIn.toLowerCase(Locale.ROOT);

        if (lowerIn.equals("@e") && player.server.getProfilePermissions(player.getGameProfile()) >= 2) {
            // 管理员全服寄送
            handleAdminBroadcast(player, data);
            return;
        }

        handleNormalEnquiry(player, data, lowerIn);
    }

    private void handleAdminBroadcast(ServerPlayer player, IMailboxDataProvider data) {
        if (shouldSend) {
            if (player.containerMenu instanceof PostboxScreenHandler container) {
                ItemStack parcel = container.parcel.getItem(0).copy();
                parcel.set(ContactDataComponents.POSTCARD_SENDER.get(), player.getName().getString());

                for (UUID uuid : data.getNameToUUID().values()) {
                    data.getMailList().add(new MailToBeSent(uuid, parcel.copy(), 0));
                }

                ActionS2CMessage.create(1).sendTo(player);
                container.parcel.setItem(0, ItemStack.EMPTY);
            }
        } else {
            List<String> names = new ArrayList<>();
            names.add("@e");
            List<Integer> ticks = new ArrayList<>();
            ticks.add(0);
            AddresseeDataMessage.create(names, ticks).sendTo(player);
        }
    }

    private void handleNormalEnquiry(ServerPlayer player, IMailboxDataProvider data, String lowerIn) {
        List<String> names = new ArrayList<>();
        for (String name : data.getNameToUUID().keySet()) {
            if (name.toLowerCase(Locale.ROOT).startsWith(lowerIn)) {
                names.add(name);
            }
            if (names.size() == 4) {
                break;
            }
        }

        List<Integer> ticks = new ArrayList<>();
        for (String name : names) {
            UUID uuid = data.getNameToUUID().get(name);
            if (data.isMailboxFull(uuid)) {
                ticks.add(-1);
                continue;
            }

            GlobalPos mailboxPos = data.getMailboxPos(uuid);
            if (player.containerMenu instanceof PostboxScreenHandler) {
                int tick = 0;
                if (!((PostboxScreenHandler) player.containerMenu).isEnderMail()) {
                    if (mailboxPos != null) {
                        tick = MailboxManager.getDeliveryTicks(
                                player.level().dimension(),
                                player.blockPosition(),
                                mailboxPos.dimension(),
                                mailboxPos.pos()
                        );
                    } else {
                        tick = ContactCommonConfig.isEnableCenterMailbox() ?
                                MailboxManager.getDeliveryTicks(
                                        player.level().dimension(),
                                        player.blockPosition(),
                                        Level.OVERWORLD,
                                        player.level().getSharedSpawnPos()
                                ) : -2;
                    }
                }
                ticks.add(tick);
            }
        }

        if (player.containerMenu instanceof PostboxScreenHandler) {
            if (shouldSend && !names.isEmpty() && Objects.equals(names.getFirst(), nameIn) && ticks.getFirst() >= 0) {
                handleSendMail(player, data, names.getFirst(), ticks.getFirst());
            } else {
                AddresseeDataMessage.create(names, ticks).sendTo(player);
            }
        }
    }

    private void handleSendMail(ServerPlayer player, IMailboxDataProvider data, String recipientName, int deliveryTicks) {
        PostboxScreenHandler container = (PostboxScreenHandler) player.containerMenu;
        ItemStack parcel = container.parcel.getItem(0);
        parcel.set(ContactDataComponents.POSTCARD_SENDER.get(), player.getName().getString());

        if (IPackageItem.checkAndPostmarkPostcard(parcel, player.getName().getString()) ||
                parcel.getItem() instanceof PostcardItem) {
            AdvancementManager.givePlayerAdvancement(
                    player.server,
                    player,
                    ResourceLocation.parse("contact:send_postcard")
            );
        }

        UUID uuid = data.getNameToUUID().get(recipientName);
        GlobalPos mailboxPos = data.getMailboxPos(uuid);

        if (mailboxPos != null) {
            if (mailboxPos.dimension() != player.level().dimension()) {
                parcel.set(ContactDataComponents.ANOTHER_WORLD.get(), true);
            }
        } else {
            if (Level.OVERWORLD != player.level().dimension()) {
                parcel.set(ContactDataComponents.ANOTHER_WORLD.get(), true);
            }
        }

        data.getMailList().add(new MailToBeSent(uuid, parcel, deliveryTicks));
        ActionS2CMessage.create(1).sendTo(player);
        container.parcel.setItem(0, ItemStack.EMPTY);
    }

    public static EnquireAddresseeMessage create(String name, boolean shouldSend) {
        return new EnquireAddresseeMessage(name, shouldSend);
    }

    public void sendToServer() {
        NetworkManager.sendToServer(this);
    }

}