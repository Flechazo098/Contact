package com.flechazo.contact.common.tileentity;

import com.flechazo.contact.common.registry.RegistryManager;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.world.level.block.entity.BlockEntityType;

import static com.flechazo.contact.common.block.BlockRegistry.*;

public final class BlockEntityTypeRegistry {
    public static final RegistrySupplier<BlockEntityType<MailboxBlockEntity>> MAILBOX_BLOCK_ENTITY =
            RegistryManager.BLOCK_ENTITY_TYPES.register("mailbox", () ->
                    BlockEntityType.Builder.of(MailboxBlockEntity::new,
                                    ORANGE_MAILBOX.get(), MAGENTA_MAILBOX.get(), LIGHT_BLUE_MAILBOX.get(), YELLOW_MAILBOX.get(),
                                    LIME_MAILBOX.get(), PINK_MAILBOX.get(), GRAY_MAILBOX.get(), LIGHT_GRAY_MAILBOX.get(),
                                    CYAN_MAILBOX.get(), PURPLE_MAILBOX.get(), BLUE_MAILBOX.get(), BROWN_MAILBOX.get(),
                                    GREEN_MAILBOX.get(), RED_MAILBOX.get(), BLACK_MAILBOX.get(), WHITE_MAILBOX.get())
                            .build(null));

    public static void init() {
    }
}