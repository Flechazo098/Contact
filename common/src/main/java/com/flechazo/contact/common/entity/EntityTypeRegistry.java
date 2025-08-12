package com.flechazo.contact.common.entity;

import com.flechazo.contact.Contact;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Supplier;

public final class EntityTypeRegistry {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Contact.MOD_ID, Registries.ENTITY_TYPE);
    public static final RegistrySupplier<EntityType<PostcardEntity>> POSTCARD = register("postcard", () -> {
        EntityType.Builder<PostcardEntity> builder = EntityType.Builder.of(PostcardEntity::new, MobCategory.MISC);
        builder.sized(0.5f, 0.5f)
                .clientTrackingRange(10)
                .updateInterval(Integer.MAX_VALUE);
        return builder.build("postcard");
    });

    private static <T extends Entity> RegistrySupplier<EntityType<T>> register(String name, Supplier<EntityType<T>> entity) {
        return ENTITY_TYPES.register(name, entity);
    }

    public static void init() {
    }
}