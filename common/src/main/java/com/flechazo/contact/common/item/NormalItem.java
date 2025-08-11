package com.flechazo.contact.common.item;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

public class NormalItem extends Item implements ISilveroakItem {
    private final ResourceKey<CreativeModeTab> itemGroup;
    private final ResourceLocation registryID;

    public NormalItem(ResourceLocation rl, Properties properties, ResourceKey<CreativeModeTab> itemGroup) {
        super(properties);
        this.itemGroup = itemGroup;
        this.registryID = rl;
    }

    public NormalItem(ResourceLocation id, ResourceKey<CreativeModeTab> itemGroup) {
        super(new Item.Properties());
        this.itemGroup = itemGroup;
        this.registryID = id;
    }

    @Nullable
    @Override
    public ResourceKey<CreativeModeTab> getItemGroup() {
        return itemGroup;
    }

    @Override
    public ResourceLocation getRegistryID() {
        return registryID;
    }
}