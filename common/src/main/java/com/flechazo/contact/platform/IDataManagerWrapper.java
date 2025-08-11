package com.flechazo.contact.platform;

import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Map;

public interface IDataManagerWrapper<T> {

    Map<ResourceLocation, T> getAllData();

    T getData(ResourceLocation location);

    List<T> getDataList();
}