package com.flechazo.contact.platform.forge;

import com.flechazo.contact.common.storage.IMailboxDataProvider;
import com.flechazo.contact.forge.storage.ForgeMailboxDataProvider;
import com.flechazo.contact.forge.storage.MailboxDataCapability;
import com.flechazo.contact.platform.IDataManagerWrapper;
import com.flechazo.contact.platform.IPlatformService;
import com.mafuyu404.oelib.forge.data.DataManager;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.function.Supplier;

public class ForgePlatformService implements IPlatformService {

    @Override
    public <T> IDataManagerWrapper<T> getData(Class<T> dataClass) {
        DataManager<T> manager = DataManager.get(dataClass);
        return new ForgeDataManagerWrapper<>(manager);
    }

    @Override
    public MinecraftServer getCurrentServer() {
        return ServerLifecycleHooks.getCurrentServer();
    }

    @Override
    public void setRenderLayer(Supplier<Block> block) {
        ItemBlockRenderTypes.setRenderLayer(block.get(), RenderType.cutout());
    }

    @Override
    public IMailboxDataProvider getMailboxDataProviderImpl(MinecraftServer server) {
        Level overworld = server.getLevel(Level.OVERWORLD);
        return overworld.getCapability(MailboxDataCapability.MAILBOX_DATA)
                .map(ForgeMailboxDataProvider::new)
                .orElseThrow(() -> new RuntimeException("Mailbox data capability not found"));
    }
}