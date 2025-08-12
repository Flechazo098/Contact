package com.flechazo.contact.common.handler;

import com.flechazo.contact.common.storage.IMailboxDataProvider;
import com.flechazo.contact.common.storage.MailboxDataManager;
import com.flechazo.contact.network.ActionMessage;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

import static com.flechazo.contact.common.handler.MailboxManager.updateState;

public final class AddresseeSignInHandler {
    public static void onPlayerLoggedIn(ServerPlayer player) {
        IMailboxDataProvider data = MailboxDataManager.getData(player.getServer());

        UUID uuid = player.getUUID();
        data.getNameToUUID().put(player.getName().getString(), uuid);
        if (data.getUuidToContents().get(uuid) == null) {
            data.resetMailboxContents(uuid);
        } else {
            if (!data.isMailboxEmpty(uuid)) {
                ActionMessage packet = ActionMessage.create(0);
                packet.sendTo(player);
            }
            updateState(uuid, data.data());
        }
    }
}