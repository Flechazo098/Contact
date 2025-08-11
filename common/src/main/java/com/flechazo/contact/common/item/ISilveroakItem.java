package com.flechazo.contact.common.item;


import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;

public interface ISilveroakItem {
    ResourceKey<CreativeModeTab> getItemGroup();

    ResourceLocation getRegistryID();
}