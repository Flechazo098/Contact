package com.flechazo.contact.common.item;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;

public class NormalBlockItem extends BlockItem implements ISilveroakItem {
    private final ResourceKey<CreativeModeTab> itemGroup;
    private final ResourceLocation registryID;

    public NormalBlockItem(Block block, ResourceLocation id, Properties properties, ResourceKey<CreativeModeTab> itemGroup) {
        super(block, properties);
        this.itemGroup = itemGroup;
        this.registryID = id;
    }

    public NormalBlockItem(Block block, ResourceLocation id, ResourceKey<CreativeModeTab> itemGroup) {
        super(block, new Properties());
        this.itemGroup = itemGroup;
        this.registryID = id;
    }

    @Override
    public ResourceKey<CreativeModeTab> getItemGroup() {
        return itemGroup;
    }

    @Override
    public ResourceLocation getRegistryID() {
        return registryID;
    }
}