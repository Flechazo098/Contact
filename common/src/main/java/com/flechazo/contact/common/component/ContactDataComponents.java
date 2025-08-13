package com.flechazo.contact.common.component;

import com.flechazo.contact.Contact;
import com.mojang.serialization.Codec;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;

public class ContactDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS =
            DeferredRegister.create(Contact.MOD_ID, Registries.DATA_COMPONENT_TYPE);

    public static final RegistrySupplier<DataComponentType<String>> POSTCARD_TEXT = DATA_COMPONENTS.register(
            "postcard_text",
            () -> DataComponentType.<String>builder()
                    .persistent(Codec.STRING)
                    .networkSynchronized(ByteBufCodecs.STRING_UTF8)
                    .build()
    );

    public static final RegistrySupplier<DataComponentType<ResourceLocation>> POSTCARD_STYLE_ID = DATA_COMPONENTS.register(
            "postcard_style_id",
            () -> DataComponentType.<ResourceLocation>builder()
                    .persistent(ResourceLocation.CODEC)
                    .networkSynchronized(ResourceLocation.STREAM_CODEC)
                    .build()
    );

    public static final RegistrySupplier<DataComponentType<String>> POSTCARD_SENDER = DATA_COMPONENTS.register(
            "postcard_sender",
            () -> DataComponentType.<String>builder()
                    .persistent(Codec.STRING)
                    .networkSynchronized(ByteBufCodecs.STRING_UTF8)
                    .build()
    );

    public static final RegistrySupplier<DataComponentType<String>> TEXT_BOX_CONTENT = DATA_COMPONENTS.register(
            "text_box_content",
            () -> DataComponentType.<String>builder()
                    .persistent(Codec.STRING)
                    .networkSynchronized(ByteBufCodecs.STRING_UTF8)
                    .build()
    );

    public static final RegistrySupplier<DataComponentType<String>> RED_PACKET_BLESSING = DATA_COMPONENTS.register(
            "red_packet_blessing",
            () -> DataComponentType.<String>builder()
                    .persistent(Codec.STRING)
                    .networkSynchronized(ByteBufCodecs.STRING_UTF8)
                    .build()
    );

    public static final RegistrySupplier<DataComponentType<Boolean>> ANOTHER_WORLD = DATA_COMPONENTS.register(
            "another_world",
            () -> DataComponentType.<Boolean>builder()
                    .persistent(Codec.BOOL)
                    .networkSynchronized(ByteBufCodecs.BOOL)
                    .build()
    );
}