package com.flechazo.contact.common.entity;

import cc.sighs.oelib.registry.DeferredRegister;
import cc.sighs.oelib.registry.RegisterSupplier;
import com.flechazo.contact.Contact;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Supplier;

public final class EntityTypeRegistry {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, Contact.MOD_ID);
    public static final RegisterSupplier<EntityType<PostcardEntity>> POSTCARD = register("postcard", () -> {
        EntityType.Builder<PostcardEntity> builder = EntityType.Builder.of(PostcardEntity::new, MobCategory.MISC);
        builder.sized(0.5f, 0.5f)
                .clientTrackingRange(10)
                .eyeHeight(0.0F)
                .updateInterval(Integer.MAX_VALUE);
        return builder.build("postcard");
    });

    private static <T extends Entity> RegisterSupplier<EntityType<T>> register(String name, Supplier<EntityType<T>> entity) {
        return ENTITY_TYPES.register(name, entity);
    }
}