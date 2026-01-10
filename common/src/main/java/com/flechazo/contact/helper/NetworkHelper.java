package com.flechazo.contact.helper;

import cc.sighs.oelib.network.api.INetworkContext;
import com.flechazo.contact.common.component.ContactDataComponents;
import com.flechazo.contact.common.config.ContactCommonConfig;
import com.flechazo.contact.common.handler.AdvancementManager;
import com.flechazo.contact.common.handler.MailboxManager;
import com.flechazo.contact.common.item.IPackageItem;
import com.flechazo.contact.common.item.PostcardItem;
import com.flechazo.contact.common.screenhandler.PackageScreenHandler;
import com.flechazo.contact.common.screenhandler.PostboxScreenHandler;
import com.flechazo.contact.common.screenhandler.RedPacketEnvelopeScreenHandler;
import com.flechazo.contact.common.storage.MailToBeSent;
import com.flechazo.contact.network.ActionMessage;
import com.flechazo.contact.network.AddresseeDataMessage;
import com.flechazo.contact.platform.PlatformHelper;
import com.flechazo.contact.util.ClientUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.*;

public final class NetworkHelper {

    private static void packParcel(ServerPlayer player, String extra) {
        if (player.containerMenu instanceof PackageScreenHandler screenHandler) {
            screenHandler.isPacked = true;
            if (screenHandler instanceof RedPacketEnvelopeScreenHandler redPacket) {
                redPacket.blessings = extra;
            }
            player.closeContainer();
        }
    }

    public static void handleActionServer(INetworkContext context, int action, String extra) {
        if (action == 0) {
            packParcel(context.sender(), extra);
        }
    }

    public static void handleActionClient(INetworkContext context, int action) {
        switch (action) {
            case 0 -> ClientUtil.notifyNewMail(context.client());
            case 1 -> {
                var client = context.client();
                if (client.player != null && client.player.containerMenu instanceof PostboxScreenHandler handler) {
                    handler.status = 2;
                }
            }
        }
    }

    public static void handleAdminBroadcast(ServerPlayer player, boolean shouldSend) {
        if (shouldSend) {
            if (player.containerMenu instanceof PostboxScreenHandler container) {
                var parcel = container.parcel.getItem(0).copy();
                parcel.set(ContactDataComponents.POSTCARD_SENDER.get(), player.getName().getString());

                for (UUID uuid : PlatformHelper.getNameToUUID().values()) {
                    PlatformHelper.getMailList().add(new MailToBeSent(uuid, parcel.copy(), 0));
                }
                ActionMessage msg = new ActionMessage(1, "");
                msg.sendTo(player);
                container.parcel.setItem(0, ItemStack.EMPTY);
            }
        } else {
            List<String> names = new ArrayList<>();
            names.add("@e");
            List<Integer> ticks = new ArrayList<>();
            ticks.add(0);
            AddresseeDataMessage msg = new AddresseeDataMessage(names, ticks);
            msg.sendTo(player);
        }
    }

    public static void handleNormalEnquiry(ServerPlayer player, String lowerIn, String nameIn, boolean shouldSend) {
        List<String> names = new ArrayList<>();
        for (String name : PlatformHelper.getNameToUUID().keySet()) {
            if (name.toLowerCase(Locale.ROOT).startsWith(lowerIn)) {
                names.add(name);
            }
            if (names.size() == 4) {
                break;
            }
        }

        List<Integer> ticks = new ArrayList<>();
        for (String name : names) {
            var uuid = PlatformHelper.getNameToUUID().get(name);
            if (PlatformHelper.isMailboxFull(uuid)) {
                ticks.add(-1);
                continue;
            }

            var mailboxPos = PlatformHelper.getMailboxPos(uuid);
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
                handleSendMail(player, names.getFirst(), ticks.getFirst());
            } else {
                AddresseeDataMessage msg = new AddresseeDataMessage(names, ticks);
                msg.sendTo(player);
            }
        }
    }

    public static void handleSendMail(ServerPlayer player, String recipientName, int deliveryTicks) {
        PostboxScreenHandler container = (PostboxScreenHandler) player.containerMenu;
        var parcel = container.parcel.getItem(0);
        parcel.set(ContactDataComponents.POSTCARD_SENDER.get(), player.getName().getString());

        if (IPackageItem.checkAndPostmarkPostcard(parcel, player.getName().getString()) ||
                parcel.getItem() instanceof PostcardItem) {
            AdvancementManager.givePlayerAdvancement(
                    player.server,
                    player,
                    ResourceLocation.parse("contact:send_postcard")
            );
        }

        var uuid = PlatformHelper.getNameToUUID().get(recipientName);
        var mailboxPos = PlatformHelper.getMailboxPos(uuid);

        if (mailboxPos != null) {
            if (mailboxPos.dimension() != player.level().dimension()) {
                parcel.set(ContactDataComponents.ANOTHER_WORLD.get(), true);
            }
        } else {
            if (Level.OVERWORLD != player.level().dimension()) {
                parcel.set(ContactDataComponents.ANOTHER_WORLD.get(), true);
            }
        }

        PlatformHelper.getMailList().add(new MailToBeSent(uuid, parcel, deliveryTicks));
        ActionMessage msg = new ActionMessage(1, "");
        msg.sendTo(player);
        container.parcel.setItem(0, ItemStack.EMPTY);
    }
}
