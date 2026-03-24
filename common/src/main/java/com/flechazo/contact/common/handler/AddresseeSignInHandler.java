package com.flechazo.contact.common.handler;

import com.flechazo.contact.network.ActionMessage;
import com.flechazo.contact.platform.PlatformHelper;
import net.minecraft.server.level.ServerPlayer;

import static com.flechazo.contact.common.handler.MailboxManager.updateState;

public final class AddresseeSignInHandler {
    public static void onPlayerLoggedIn(ServerPlayer player) {

        var uuid = player.getUUID();
        PlatformHelper.getNameToUUID().put(player.getName().getString(), uuid);
        if (PlatformHelper.getUuidToContents().get(uuid) == null) {
            PlatformHelper.resetMailboxContents(uuid);
        } else {
            if (!PlatformHelper.isMailboxEmpty(uuid)) {
                ActionMessage packet = new ActionMessage(0, "");
                packet.sendTo(player);
            }
            updateState(uuid, PlatformHelper.data());
        }
    }
}