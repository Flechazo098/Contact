package com.flechazo.contact.platform.neoforge;

import com.flechazo.contact.common.storage.IMailboxDataProvider;
import com.flechazo.contact.neoforge.attachment.NeoForgeMailboxDataProvider;
import com.flechazo.contact.platform.IDataManagerWrapper;
import com.flechazo.contact.platform.IPlatformService;
import com.mafuyu404.oelib.neoforge.data.DataManager;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class NeoForgePlatformService implements IPlatformService {

    @Override
    public <T> IDataManagerWrapper<T> getData(Class<T> dataClass) {
        DataManager<T> manager = DataManager.get(dataClass);
        return new NeoforgeForgeDataManagerWrapper<>(manager);
    }

    @Override
    public MinecraftServer getCurrentServer() {
        return DataManager.getCurrentServer();
    }

    @Override
    public void setRenderLayer(Supplier<Block> block) {
        ItemBlockRenderTypes.setRenderLayer(block.get(), RenderType.cutout());
    }

    @Override
    public IMailboxDataProvider getMailboxDataProviderImpl(MinecraftServer server) {
        ServerLevel overworld = server.getLevel(Level.OVERWORLD);
        return new NeoForgeMailboxDataProvider(overworld);
    }
}