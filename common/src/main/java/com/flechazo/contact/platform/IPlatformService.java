package com.flechazo.contact.platform;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public interface IPlatformService {

    <T> IDataManagerWrapper<T> getData(Class<T> dataClass);

    MinecraftServer getCurrentServer();

    void setRenderLayer(Supplier<Block> block);
}