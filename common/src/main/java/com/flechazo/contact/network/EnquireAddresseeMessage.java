package com.flechazo.contact.network;

import com.flechazo.contact.Contact;
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
import io.netty.buffer.Unpooled;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.*;

public class EnquireAddresseeMessage {
    private static final ResourceLocation ID = new ResourceLocation(Contact.MOD_ID, "enquire_addressee");

    private final String nameIn;
    private final boolean shouldSend;

    public EnquireAddresseeMessage(String name, boolean shouldSend) {
        this.nameIn = name;
        this.shouldSend = shouldSend;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(nameIn, 32767);
        buf.writeBoolean(shouldSend);
    }

    public static EnquireAddresseeMessage decode(FriendlyByteBuf buf) {
        String name = buf.readUtf(32767);
        boolean shouldSend = buf.readBoolean();
        return new EnquireAddresseeMessage(name, shouldSend);
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
                parcel.getOrCreateTag().putString("Sender", player.getName().getString());

                for (UUID uuid : data.getNameToUUID().values()) {
                    data.getMailList().add(new MailToBeSent(uuid, parcel.copy(), 0));
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
            if (shouldSend && !names.isEmpty() && Objects.equals(names.get(0), nameIn) && ticks.get(0) >= 0) {
                handleSendMail(player, data, names.get(0), ticks.get(0));
            } else {
                AddresseeDataMessage.create(names, ticks).sendTo(player);
            }
        }
    }

    private void handleSendMail(ServerPlayer player, IMailboxDataProvider data, String recipientName, int deliveryTicks) {
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

        UUID uuid = data.getNameToUUID().get(recipientName);
        GlobalPos mailboxPos = data.getMailboxPos(uuid);

        if (mailboxPos != null) {
            if (mailboxPos.dimension() != player.level().dimension()) {
                parcel.getOrCreateTag().putBoolean("AnotherWorld", true);
            }
        } else {
            if (Level.OVERWORLD != player.level().dimension()) {
                parcel.getOrCreateTag().putBoolean("AnotherWorld", true);
            }
        }

        data.getMailList().add(new MailToBeSent(uuid, parcel, deliveryTicks));
        ActionMessage.create(1).sendTo(player);
        container.parcel.setItem(0, ItemStack.EMPTY);
    }

    public static EnquireAddresseeMessage create(String name, boolean shouldSend) {
        return new EnquireAddresseeMessage(name, shouldSend);
    }

    public void sendToServer() {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        this.encode(buf);
        NetworkManager.sendToServer(ID, buf);
    }

    public static void registerC2S() {
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, ID, (buf, ctx) -> {
            EnquireAddresseeMessage msg = decode(buf);
            ServerPlayer player = (ServerPlayer) ctx.getPlayer();
            if (player != null) {
                player.server.execute(() -> msg.handleServer(player));
            }
        });
    }
}