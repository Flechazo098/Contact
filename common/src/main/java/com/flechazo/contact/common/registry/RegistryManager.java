package com.flechazo.contact.common.registry;

import com.flechazo.contact.Contact;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class RegistryManager {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Contact.MOD_ID, Registries.BLOCK);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Contact.MOD_ID, Registries.ITEM);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Contact.MOD_ID, Registries.ENTITY_TYPE);
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Contact.MOD_ID, Registries.MENU);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Contact.MOD_ID, Registries.BLOCK_ENTITY_TYPE);

    public static void initialize() {
        BLOCKS.register();
        ITEMS.register();
        ENTITY_TYPES.register();
        MENU_TYPES.register();
        BLOCK_ENTITY_TYPES.register();
    }
}