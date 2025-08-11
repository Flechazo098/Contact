package com.flechazo.contact.platform.fabric;

import com.flechazo.contact.platform.IDataManagerWrapper;
import com.mafuyu404.oelib.fabric.data.DataManager;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Map;

/**
 * Fabric 数据管理器包装器。
 *
 * @param <T> 数据类型
 */
public class FabricDataManagerWrapper<T> implements IDataManagerWrapper<T> {

    private final DataManager<T> manager;

    public FabricDataManagerWrapper(DataManager<T> manager) {
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