package com.flechazo.contact.common.storage;

import com.flechazo.contact.platform.PlatformHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class MailboxDataManager {
    public static IMailboxDataProvider getData(MinecraftServer server) {
        return PlatformHelper.getMailboxDataProvider(server);
    }
    
    public static IMailboxDataProvider getData(Level level) {
        return getData(level.getServer());
    }
    
    public static IMailboxDataProvider getData(ServerPlayer player) {
        return getData(player.getServer());
    }
}