package com.flechazo.contact.common.handler;

import com.flechazo.contact.common.storage.MailboxDataStorage;
import com.flechazo.contact.network.ActionMessage;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

import static com.flechazo.contact.common.handler.MailboxManager.updateState;

public final class AddresseeSignInHandler {
    public static void onPlayerLoggedIn(ServerPlayer player) {
        MailboxDataStorage data = MailboxDataStorage.getMailboxData(player.getServer());

        UUID uuid = player.getUUID();
        data.getData().nameToUUID.put(player.getName().getString(), uuid);
        if (data.getData().uuidToContents.get(uuid) == null) {
            data.getData().resetMailboxContents(uuid);
            data.setDirty();
        } else {
            if (!data.getData().isMailboxEmpty(uuid)) {
                ActionMessage packet = ActionMessage.create(0);
                packet.sendTo(player);
            }
            updateState(uuid, data.getData());
        }
    }
}