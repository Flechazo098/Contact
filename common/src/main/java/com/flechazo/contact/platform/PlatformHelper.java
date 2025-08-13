package com.flechazo.contact.platform;

import com.flechazo.contact.common.storage.IMailboxDataProvider;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;


public class PlatformHelper {

    @ExpectPlatform
    public static IPlatformService getPlatformService() {
        throw new AssertionError("This method should be replaced by Architectury");
    }

    public static MinecraftServer getCurrentServer() {
        return getPlatformService().getCurrentServer();
    }

    public static void setRenderLayer(Supplier<Block> block) {
        getPlatformService().setRenderLayer(block);
    }

    public static IMailboxDataProvider getMailboxDataProvider(MinecraftServer server) {
        return getPlatformService().getMailboxDataProviderImpl(server);
    }
}