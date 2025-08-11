package com.flechazo.contact.network;

import com.flechazo.contact.common.config.ContactCommonConfig;
import com.flechazo.contact.common.handler.AdvancementManager;
import com.flechazo.contact.common.handler.MailboxManager;
import com.flechazo.contact.common.item.IPackageItem;
import com.flechazo.contact.common.item.PostcardItem;
import com.flechazo.contact.common.screenhandler.PostboxScreenHandler;
import com.flechazo.contact.common.storage.MailToBeSent;
import com.flechazo.contact.common.storage.MailboxDataStorage;
import com.mafuyu404.oelib.api.net.INetworkContext;
import com.mafuyu404.oelib.api.net.NetworkPacket;
import com.mafuyu404.oelib.api.net.Side;
import com.mafuyu404.oelib.api.net.SimplePacket;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.*;

@NetworkPacket(side = Side.SERVER)
public class EnquireAddresseeMessage extends SimplePacket<EnquireAddresseeMessage> {
    private final String nameIn;
    private final boolean shouldSend;

    public EnquireAddresseeMessage(String name, boolean shouldSend) {
        this.nameIn = name;
        this.shouldSend = shouldSend;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(nameIn, 32767);
        buf.writeBoolean(shouldSend);
    }

    public static EnquireAddresseeMessage decode(FriendlyByteBuf buf) {
        String name = buf.readUtf(32767);
        boolean shouldSend = buf.readBoolean();
        return new EnquireAddresseeMessage(name, shouldSend);
    }

    @Override
    protected void handleServer(INetworkContext context) {
        ServerPlayer player = getSender(context);
        if (player == null || nameIn.isEmpty()) {
            return;
        }

        MailboxDataStorage data = MailboxDataStorage.getMailboxData(player.server);
        String lowerIn = nameIn.toLowerCase(Locale.ROOT);

        if (lowerIn.equals("@e") && player.server.getProfilePermissions(player.getGameProfile()) >= 2) {
            // 管理员全服寄送
            handleAdminBroadcast(player, data);
            return;
        }

        handleNormalEnquiry(player, data, lowerIn);
    }

    private void handleAdminBroadcast(ServerPlayer player, MailboxDataStorage data) {
        if (shouldSend) {
            if (player.containerMenu instanceof PostboxScreenHandler container) {
                ItemStack parcel = container.parcel.getItem(0).copy();
                parcel.getOrCreateTag().putString("Sender", player.getName().getString());

                for (UUID uuid : data.getData().nameToUUID.values()) {
                    data.getData().mailList.add(new MailToBeSent(uuid, parcel.copy(), 0));
                    data.setDirty();
                }

                ActionMessage.create(1).sendTo(player);
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

    private void handleNormalEnquiry(ServerPlayer player, MailboxDataStorage data, String lowerIn) {
        List<String> names = new ArrayList<>();
        for (String name : data.getData().nameToUUID.keySet()) {
            if (name.toLowerCase(Locale.ROOT).startsWith(lowerIn)) {
                names.add(name);
            }
            if (names.size() == 4) {
                break;
            }
        }

        List<Integer> ticks = new ArrayList<>();
        for (String name : names) {
            UUID uuid = data.getData().nameToUUID.get(name);
            if (data.getData().isMailboxFull(uuid)) {
                ticks.add(-1);
                continue;
            }

            GlobalPos mailboxPos = data.getData().getMailboxPos(uuid);
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
            if (shouldSend && !names.isEmpty() && Objects.equals(names.get(0), nameIn) && ticks.get(0) >= 0) {
                handleSendMail(player, data, names.get(0), ticks.get(0));
            } else {
                AddresseeDataMessage.create(names, ticks).sendTo(player);
            }
        }
    }

    private void handleSendMail(ServerPlayer player, MailboxDataStorage data, String recipientName, int deliveryTicks) {
        PostboxScreenHandler container = (PostboxScreenHandler) player.containerMenu;
        ItemStack parcel = container.parcel.getItem(0);
        parcel.getOrCreateTag().putString("Sender", player.getName().getString());

        if (IPackageItem.checkAndPostmarkPostcard(parcel, player.getName().getString()) ||
                parcel.getItem() instanceof PostcardItem) {
            AdvancementManager.givePlayerAdvancement(
                    player.server,
                    player,
                    new ResourceLocation("contact:send_postcard")
            );
        }

        UUID uuid = data.getData().nameToUUID.get(recipientName);
        GlobalPos mailboxPos = data.getData().getMailboxPos(uuid);

        if (mailboxPos != null) {
            if (mailboxPos.dimension() != player.level().dimension()) {
                parcel.getOrCreateTag().putBoolean("AnotherWorld", true);
            }
        } else {
            if (Level.OVERWORLD != player.level().dimension()) {
                parcel.getOrCreateTag().putBoolean("AnotherWorld", true);
            }
        }

        data.getData().mailList.add(new MailToBeSent(uuid, parcel, deliveryTicks));
        ActionMessage.create(1).sendTo(player);
        container.parcel.setItem(0, ItemStack.EMPTY);
    }

    public static EnquireAddresseeMessage create(String name, boolean shouldSend) {
        return new EnquireAddresseeMessage(name, shouldSend);
    }
}