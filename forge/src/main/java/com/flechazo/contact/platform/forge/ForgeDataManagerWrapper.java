package com.flechazo.contact.platform.forge;

import com.flechazo.contact.platform.IDataManagerWrapper;
import com.mafuyu404.oelib.forge.data.DataManager;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Map;

public class ForgeDataManagerWrapper<T> implements IDataManagerWrapper<T> {

    private final DataManager<T> manager;

    public ForgeDataManagerWrapper(DataManager<T> manager) {
        this.manager = manager;
    }

    @Override
    public Map<ResourceLocation, T> getAllData() {
        return manager.getAllData();
    }

    @Override
    public T getData(ResourceLocation location) {
        return manager.getData(location);
    }

    @Override
    public List<T> getDataList() {
        return manager.getDataList();
    }
}