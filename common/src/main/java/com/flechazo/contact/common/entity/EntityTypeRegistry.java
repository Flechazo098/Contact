package com.flechazo.contact.common.entity;

import com.flechazo.contact.common.registry.RegistryManager;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public final class EntityTypeRegistry {
    public static final RegistrySupplier<EntityType<PostcardEntity>> POSTCARD = RegistryManager.ENTITY_TYPES.register("postcard", () -> {
        EntityType.Builder<PostcardEntity> builder = EntityType.Builder.of(PostcardEntity::new, MobCategory.MISC);
        builder.sized(0.5f, 0.5f).clientTrackingRange(10).updateInterval(Integer.MAX_VALUE);
        return builder.build("postcard");
    });

    public static void init() {
    }
}